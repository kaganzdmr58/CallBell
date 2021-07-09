package com.callbellapp.callbell.Isletme;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.callbellapp.callbell.R;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Sınıflar.PersonelFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerIsletmeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), IsletmeSecimActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        Resources res = getResources();
        String ScannerIsletmeQrTaraIlk  = res.getString(R.string.ScannerIsletmeQrTaraIlk);
        Toast.makeText(getApplicationContext(),ScannerIsletmeQrTaraIlk,Toast.LENGTH_LONG).show();
    }
    @Override
    public void handleResult(Result result) {
        // işletme key shared ile kayıt altına alınacak.
        String [] scanCode = result.getText().split(":");     // işletme ve masa qr codlarını java ile ayırma.

        if(scanCode[0].equals("callbell")){
            if ( scanCode[1].equals("i")){
                StartActivity.editorYonetici.putString("isletmeID",scanCode[2]);
                StartActivity.editorYonetici.commit();

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();

                if(scanCode[3].equals(user.getUid())){
                    // eğer işletme yonetici UID si kullanıcı UID ile aynı ise yonetici modunu aç
                    StartActivity.editorLogin.putBoolean("YONETICI_MODU",true);
                    StartActivity.editorLogin.commit();

                    Resources res = getResources();
                    String ScannerYoneticiModuAktif  = res.getString(R.string.ScannerYoneticiModuAktif);
                    Toast.makeText(getApplicationContext(),ScannerYoneticiModuAktif,Toast.LENGTH_SHORT).show();
                }

                // eğer işletme qr kodu ise hafızaya alıyoruz.
                StartActivity.editorYonetici.putString("isletmeQRCode",result.getText());
                StartActivity.editorYonetici.commit();

                // firebase işletme işlemleri.
                String yeniPersonelKey = user.getUid();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRefIsletme = database.getReference("PersonelListesi").child(scanCode[2]);

                PersonelFirebase personel1 =new PersonelFirebase(yeniPersonelKey,user.getDisplayName(),"null","null","null");

                myRefIsletme.child(yeniPersonelKey).setValue(personel1);


                //Toast.makeText(getApplicationContext(),scanCode[0]+"---"+scanCode[1]+"---"+scanCode[2]+"---"+scanCode[3],Toast.LENGTH_SHORT).show();

                /*startActivity(new Intent(getApplicationContext(),StartActivity.class));
                finish();*/

                // İşletme seçimi yapılan yer.
                PersonelIsletmeBilgisiDegistirme(scanCode[2]);
                //onBackPressed();
            }else {
                Resources res = getResources();
                String ScannerIsletmeQRTara  = res.getString(R.string.ScannerIsletmeQRTara);
                Toast.makeText(getApplicationContext(),ScannerIsletmeQRTara,Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        }else {
            Resources res = getResources();
            String ScannerCallBellQrDegil  = res.getString(R.string.ScannerCallBellQrDegil);
            Toast.makeText(getApplicationContext(),ScannerCallBellQrDegil,Toast.LENGTH_SHORT).show();
            onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        ScannerView.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ScannerView.setResultHandler(this);
        ScannerView.startCamera();
    }


    public void PersonelIsletmeBilgisiDegistirme(final String isID){
        // işletme kurulumunda web işlemleri burada yapılacak.

        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        String url = "https://callbellapp.xyz/project/table4/update_table4_bussiness.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(ScannerIsletmeActivity.this, response, Toast.LENGTH_SHORT).show();

                startActivity(new Intent(getApplicationContext(),StartActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response", String.valueOf(error));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("per_uid",currentUser.getUid());
                params.put("per_isletme_id",isID);
                return params;
            }
        };
        Volley.newRequestQueue(ScannerIsletmeActivity.this).add(stringRequest);
    }
}
