package com.callbellapp.callbell;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.zxing.Result;

import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView ScannerView;
    String ScannerMasaQrTaratın,ScannerBulundugunuzMasaQrTaratın,ScannerCallBellQrDegil;


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScannerView = new ZXingScannerView(this);
        setContentView(ScannerView);

        Resources res = getResources();
        ScannerMasaQrTaratın = res.getString(R.string.ScannerMasaQrTaratın);
        ScannerBulundugunuzMasaQrTaratın = res.getString(R.string.ScannerBulundugunuzMasaQrTaratın);
        ScannerCallBellQrDegil = res.getString(R.string.ScannerCallBellQrDegil);

        Toast.makeText(getApplicationContext(),ScannerMasaQrTaratın,Toast.LENGTH_LONG).show();
    }
    @Override
    public void handleResult(Result result) {
        // işletme key shared ile kayıt altına alınacak.
        String [] scanCode = result.getText().split(":");     // işletme ve masa qr codlarını java ile ayırma.

        // bu bölüme taratılan kodu sorgulama algoritması gelecek
        if(scanCode[0].equals("callbell")){
           if ( scanCode[1].equals("m")){

               StartActivity.editorScan.putString("isletmeScanID",scanCode[2]);
               StartActivity.editorScan.putString("masaScanID",scanCode[3]);

               //Toast.makeText(getApplicationContext(),scanCode[0]+"---"+scanCode[1]+"---"+scanCode[2]+"---"+scanCode[3],Toast.LENGTH_SHORT).show();
               StartActivity.editorScan.putBoolean("resimDurum",false);
               StartActivity.editorScan.commit();

               FirebaseAuth mAuth = FirebaseAuth.getInstance();
               final FirebaseUser user = mAuth.getCurrentUser();

               Resources res = getResources();
               String MainSparisMerhaba = res.getString(R.string.MainSparisMerhaba);
               String MainSparisVermekIcin2 = res.getString(R.string.MainSparisVermekIcin2);
               MainActivity.editTextSparisGiris.setHint(MainSparisMerhaba+user.getDisplayName()+MainSparisVermekIcin2);

               ZiyaretEdilenYerEkleMetod(scanCode[2]);
               //onBackPressed();
           }else {

               Toast.makeText(getApplicationContext(),ScannerBulundugunuzMasaQrTaratın,Toast.LENGTH_SHORT).show();
               onBackPressed();
           }
        }else {
            Toast.makeText(getApplicationContext(),ScannerCallBellQrDegil,Toast.LENGTH_LONG).show();
            if ( result.getText().contains("www") ) {
                Toast.makeText(getApplicationContext(),R.string.ScannerInternetSitesineYonlendiriliyorsunuz,Toast.LENGTH_LONG).show();
                Uri webpage;
                String web = result.getText().trim();
                if (web.indexOf("https://") == -1){
                    webpage = Uri.parse("https://"+result.getText().trim()+"/");
                }else {
                    webpage = Uri.parse(result.getText().trim());
                }
                Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                startActivity(webIntent);
            }else {
                onBackPressed();
            }
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

    public void ZiyaretEdilenYerEkleMetod(final String isletmeID){

        // 1- user ile ID alınacak,  2- işletme ID metod ile alınacak.
        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        String url = "https://callbellapp.xyz/project/table7/insert_table7_visitor_not_repeated.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.e("response",response);
                onBackPressed();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("zi_isletme",isletmeID);
                params.put("zi_uid",user.getUid());
                return params;
            }
        };
        Volley.newRequestQueue(ScannerActivity.this).add(stringRequest);
    }



}

