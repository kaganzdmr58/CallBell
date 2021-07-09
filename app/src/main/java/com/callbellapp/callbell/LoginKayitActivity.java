package com.callbellapp.callbell;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginKayitActivity extends AppCompatActivity {
    private Button buttonLogin;
    private EditText editTextName,editTextSurName,editTextEmail,editTextPassword;
    private FirebaseAuth mAuth;
    private String name,email,password, kullaniciUID,email2;
    private String kayitBasarili,kayitHatali,kayitKullanıcıAdı,LoginEmailGiriniz
            ,LoginParolaGiriniz,LoginParola6RakamdanAz,LoginParolaEmail,LoginParolaEmailSonu;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(LoginKayitActivity.this, LoginActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_kayit);



        buttonLogin = findViewById(R.id.buttonGuncelle);
        editTextName = findViewById(R.id.editTextKayitAdi);
        editTextEmail = findViewById(R.id.editTextKayitEmail);
        editTextPassword = findViewById(R.id.editTextKayitSifre);


        StartActivity.editorLogin.putBoolean("ISLETME_MODU",false);
        StartActivity.editorLogin.putBoolean("YONETICI_MODU",false);
        StartActivity.editorLogin.commit();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            editTextEmail.setText(user.getEmail());
/*            editTextName.setText(MainActivity.spLogin.getString("name", "kullanıcı adı yok"));
              editTextPassword.setText(MainActivity.spLogin.getString("password", "şifre yok"));*/
        }

        Resources res = getResources();
        kayitBasarili = res.getString(R.string.LoginKayitBasarili);
        kayitHatali = res.getString(R.string.LoginKayitHatali);
        kayitKullanıcıAdı = res.getString(R.string.LoginKayitKullanıcıAdı);
        LoginEmailGiriniz = res.getString(R.string.LoginEmailGiriniz);
        LoginParolaGiriniz = res.getString(R.string.LoginParolaGiriniz);
        LoginParola6RakamdanAz = res.getString(R.string.LoginParola6RakamdanAz);
        LoginParolaEmail = res.getString(R.string.LoginParolaEmail);
        LoginParolaEmailSonu = res.getString(R.string.LoginParolaEmailSonu);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // personel bilgileri burada
                email = editTextEmail.getText().toString().trim();
                name = editTextName.getText().toString().trim();
                password = editTextPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(getApplicationContext(),LoginEmailGiriniz,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(getApplicationContext(),LoginParolaGiriniz,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length()<6){
                    Toast.makeText(getApplicationContext(),LoginParola6RakamdanAz,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.indexOf("@") == -1){
                    Toast.makeText(getApplicationContext(),LoginParolaEmail,Toast.LENGTH_SHORT).show();
                    return;
                }
                if (email.indexOf(".",email.indexOf("@")) == -1){
                    Toast.makeText(getApplicationContext(),LoginParolaEmailSonu,Toast.LENGTH_SHORT).show();
                    return;
                }


                kullaniciFirebaseEkle(email,password);
            }
        });


    }

    public void kullaniciFirebaseEkle (String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginKayitActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            // updateUI(user);
                            email2 = user.getEmail();
                            kullaniciUID = user.getUid();
                            firebaseKullaniciBilgileriGuncelle();
                            //personelOlusturmaWeb(name,kullaniciUID);
                            // yonetici ve işletme modları kapalı başlangıç.

                            /*startActivity(new Intent(getApplicationContext(),MainActivity.class));
                            finish();*/
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), kayitHatali +"1",Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

//        personelOlusturmaWeb(name);
    }

    public void firebaseKullaniciBilgileriGuncelle(){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            editTextName.setText(user.getDisplayName());
                            //Toast.makeText(LoginKayitActivity.this, "kullanıcı adı güncellendi", Toast.LENGTH_LONG).show();
                            personelOlusturmaWeb(name,kullaniciUID);
                        }
                    }
                });


    }

    public void personelOlusturmaWeb(final String adi,final String UID){
        String url = "https://callbellapp.xyz/project/table4/insert_table4_personnel.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                Log.e("response", response);
                String[] cevap = response.split(":");
                //Toast.makeText(getApplicationContext(), "PersonelID : "+cevap[1]+" - "+kayitBasarili +" web "+ email2,Toast.LENGTH_SHORT).show();
                StartActivity.editorLogin.putString("PersonelID",cevap[1]);
                StartActivity.editorLogin.putBoolean("ISLETME_MODU",false);
                StartActivity.editorLogin.putBoolean("YONETICI_MODU",false);
                StartActivity.editorLogin.commit();
                //Toast.makeText(LoginKayitActivity.this, response, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), kayitHatali/*+"-2"+error.toString()*/,Toast.LENGTH_SHORT).show();
                Log.e("response", String.valueOf(error));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("per_adi",adi);
                params.put("per_uid",UID);
                params.put("per_isletme_id","0");
                return params;
            }
        };
        Volley.newRequestQueue(LoginKayitActivity.this).add(stringRequest);
    }

}
