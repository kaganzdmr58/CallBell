package com.callbellapp.callbell;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.callbellapp.callbell.Sınıflar.PersonelLogin;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
    private EditText editTextEmail,editTextPassword;
    private Button buttonLogin;
    private TextView textViewKayit,textViewForgottPassword;
    private String email,password;
    private FirebaseAuth mAuth;
    private String kullaniciAdiHatali,oturumAcildi,LoginEmailGiriniz,LoginParolaGiriniz;
    public static SharedPreferences spLogin;
    public static SharedPreferences.Editor editorLogin;


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonGuncelle);
        textViewKayit =findViewById(R.id.textViewKayit);
        textViewForgottPassword = findViewById(R.id.textViewForgottPassword);

        spLogin = getSharedPreferences("LoginBilgileri3",MODE_PRIVATE);
        editorLogin = spLogin.edit();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        Resources res = getResources();
        kullaniciAdiHatali = res.getString(R.string.LoginKullaniciAdiVeParolaHatali);
        oturumAcildi = res.getString(R.string.LoginOturumAcildiYazi);
        LoginEmailGiriniz = res.getString(R.string.LoginEmailGiriniz);
        LoginParolaGiriniz = res.getString(R.string.LoginParolaGiriniz);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = editTextEmail.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),LoginEmailGiriniz,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),LoginParolaGiriniz,Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseOturumAc(email,password);
            }
        });

        textViewKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginKayitActivity.class));
            }
        });

        textViewForgottPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ForgottPassword();
            }
        });



    }

    public void firebaseOturumAc(final String email0, String password0){
        mAuth.signInWithEmailAndPassword(email0, password0)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email2 = user.getEmail();
                            String kullaniciUID = user.getUid();
                            Toast.makeText(LoginActivity.this, oturumAcildi + email2,
                                    Toast.LENGTH_SHORT).show();

                            StartActivity.editorLogin.putString("email",email2);
                            StartActivity.editorLogin.putString("UIDQrCode",kullaniciUID);
                            StartActivity.editorLogin.commit();

                            /*startActivity(new Intent(LoginActivity.this, StartActivity.class));
                            finish();*/
                            PersonelIsletmeKontrol(user.getUid());
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(LoginActivity.this, kullaniciAdiHatali, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            String name = user.getDisplayName();
            String email1 = user.getEmail();

        }
    }


    public void PersonelIsletmeKontrol(final String per_uid) {
        String url = "https://callbellapp.xyz/project/table4/all_table4_personnel_login_key_find.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.e("response",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray personeller = jsonObject.getJSONArray("tablo4");

                    for (int i = 0; i<personeller.length(); i++ ) {

                        JSONObject c = personeller.getJSONObject(i);

                        int per_id = c.getInt("per_id");
                        String per_adi = c.getString("per_adi");
                        String per_uid = c.getString("per_uid");
                        int per_isletme_id = c.getInt("per_isletme_id");
                        String per_islt_yon_uid = c.getString("per_islt_yon_uid");

                        PersonelLogin personelLogin = new PersonelLogin(per_id,per_adi, per_uid,per_isletme_id);

                        //Toast.makeText(LoginActivity.this, "işletme ID : "+per_isletme_id, Toast.LENGTH_SHORT).show();

                        StartActivity.editorLogin.putString("PersonelID",String.valueOf(per_id));
                        StartActivity.editorLogin.commit();

                        if (per_isletme_id != 0){
                            StartActivity.editorYonetici.putString("isletmeID", String.valueOf(per_isletme_id));
                            StartActivity.editorYonetici.commit();

                            StartActivity.editorLogin.putBoolean("ISLETME_MODU",true);
                            StartActivity.editorLogin.commit();

                            // yonetici uid ile personel uid aynı ise yönetici modunu aç
                        }else {
                            startActivity(new Intent(LoginActivity.this, StartActivity.class));
                            finish();
                        }
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        if(per_islt_yon_uid.equals(user.getUid())){
                            StartActivity.editorLogin.putBoolean("ISLETME_MODU",true);
                            StartActivity.editorLogin.putBoolean("YONETICI_MODU",true);
                            StartActivity.editorLogin.commit();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                startActivity(new Intent(LoginActivity.this, StartActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("per_uid",per_uid);
                return params;
            }
        };
        Volley.newRequestQueue(LoginActivity.this).add(stringRequest);


    }


    public void ForgottPassword(){

        if (TextUtils.isEmpty(editTextEmail.getText().toString().trim())){
            Toast.makeText(getApplicationContext(),LoginEmailGiriniz,Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.sendPasswordResetEmail(editTextEmail.getText().toString())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, editTextEmail.getText().toString().trim()+R.string.LoginAdresineGonderildi, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this, R.string.LoginSifreGuncellemedeHataOlustu, Toast.LENGTH_LONG).show();
                }
            }
        });
        //https://www.youtube.com/watch?v=t8vUdt1eEzE
    }


}
