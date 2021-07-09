package com.callbellapp.callbell.Isletme;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.callbellapp.callbell.AyarlarActivity;
import com.callbellapp.callbell.GecmisYerlerActivity;
import com.callbellapp.callbell.GelenKutusuActivity;
import com.callbellapp.callbell.MainActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.ScannerActivity;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class IsletmeSecimActivity extends AppCompatActivity {
    private Button buttonIsletmeyeDahilOl,buttonIsletmeKur;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    @Override
    protected void onStart() {
        super.onStart();
        // eğer işletmeye dahilse masalar activitye geçiş yap.
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        if (!isletmeID.equals("null")){
            startActivity(new Intent(getApplicationContext(),MasalarActivity.class));
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isletme_secim);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbarArkaPlanRengi));
        toolbar.setTitleTextColor(getResources().getColor(R.color.butonTextColor));
        // toolbar.setSubtitleTextColor(getResources().getColor(R.color.butonTextColor));
        toolbar.setLogo(R.drawable.room_service_red_48dp);
        setSupportActionBar(toolbar);
        // toolbara tıklamada main activity açılışı
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        //Navigasyonlar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.action_birinci:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;
                    case R.id.action_ikinci:
                        startActivity(new Intent(getApplicationContext(), GecmisYerlerActivity.class));
                        finish();
                        break;
                    case R.id.action_ucuncu:
                        startActivity(new Intent(getApplicationContext(), ScannerActivity.class));
                        finish();
                        break;
                    case R.id.action_dorduncu:
                        startActivity(new Intent(getApplicationContext(), GelenKutusuActivity.class));
                        finish();
                        break;
                    case R.id.action_besinci:
                        startActivity(new Intent(getApplicationContext(), AyarlarActivity.class));
                        finish();
                        break;
                    default:
                }
                return true;
            }
        });




        buttonIsletmeyeDahilOl = findViewById(R.id.buttonIsletmeyeDahilOl);
        buttonIsletmeKur = findViewById(R.id.buttonIsletmeKur);

        buttonIsletmeKur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // isletmekur firebase işlemleri
                IsletmeKurWebIslemleri();
                // işletme Kur web işlemleri

                //işletme seçim veya kurulum bitince start activity deki dinleme metodunu aktif et.

            }
        });

        buttonIsletmeyeDahilOl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // scanner actvity ile işletme qr codu alınacak ve dinlemeye başlanacak.
                startActivity(new Intent(getApplicationContext(),ScannerIsletmeActivity.class));
                finish();
                //işletme seçim veya kurulum bitince start activity deki dinleme metodunu aktif et.
            }
        });

    }


    public void IsletmeKurWebIslemleri(){
        // işletme kurulumunda web işlemleri burada yapılacak.

        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        String url = "https://callbellapp.xyz/project/table2/insert_table2_business_create.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                // yeni işletme oluşturma cevabı burası işletme ID si buradan alıncak.
                String[] cevap = response.split(":");
                //Toast.makeText(getApplicationContext(),cevap[1],Toast.LENGTH_LONG).show();
                if (Integer.parseInt(cevap[1])==0){
                    Resources res = getResources();
                    String IsletmeSecimIsletmeKurmadaHata = res.getString(R.string.IsletmeSecimIsletmeKurmadaHata);

                    Toast.makeText(getApplicationContext(),IsletmeSecimIsletmeKurmadaHata,Toast.LENGTH_LONG).show();
                    return;
                }

                // işletme QR codu
                StartActivity.editorYonetici.putString("isletmeQRCode","callbell:"+"i:"+cevap[1]+":"+currentUser.getUid());
                StartActivity.editorYonetici.commit();

                IsletmeKurFirebaseIslemleri(cevap[1]);
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
                params.put("is_yonetici_id",currentUser.getUid());
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeSecimActivity.this).add(stringRequest);
    }

    public void IsletmeKurFirebaseIslemleri(String isletmeID){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefIsletme = database.getReference("QrCodeListesi");

        /*      IsletmeBilgi isletmeBilgi = new IsletmeBilgi(1,"A işletmesi",isletmeKey,"UID","null","null","null");    */

        myRefIsletme.child(isletmeID).setValue("İşletme Başarıyla Kuruldu.");

        // işletme key shared ile kayıt altına alınacak.
        StartActivity.editorYonetici.putString("isletmeID",isletmeID);
        StartActivity.editorYonetici.commit();
        StartActivity.editorLogin.putBoolean("YONETICI_MODU",true);
        StartActivity.editorLogin.commit();

        /*StartActivity start = new StartActivity();
        start.IsletmeFirebaseDinleme();*/

        PersonelIsletmeBilgisiDegistirme(isletmeID);


    }

    /*public void IsletmeyeDahilOl(String isletmeQrCode){

        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        String yeniPersonelKey = currentUser.getUid();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefIsletme = database.getReference("PersonelListesi").child(isletmeQrCode);

        PersonelFirebase personel1 =new PersonelFirebase(yeniPersonelKey,"null","null","null","null");

        myRefIsletme.child(yeniPersonelKey).setValue(personel1);

        startActivity(new Intent(getApplicationContext(),StartActivity.class));
        finish();
    }*/



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Boolean ISLETME_MODU = StartActivity.spLogin.getBoolean("ISLETME_MODU",false);
        Boolean YONETICI_MODU = StartActivity.spLogin.getBoolean("YONETICI_MODU",false);
        if (ISLETME_MODU){
            if (YONETICI_MODU){
                getMenuInflater().inflate(R.menu.toolbar_isletme_yonetici_menu,menu);
            }else {
                getMenuInflater().inflate(R.menu.toolbar_isletme_menu,menu);
            }
        }else {
            getMenuInflater().inflate(R.menu.toolbar_ayarlar_menu,menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_yonetici:
                startActivity(new Intent(getApplicationContext(), YoneticiMainActivity.class));
                finish();
                return true;
            case R.id.action_isletme:
                startActivity(new Intent(getApplicationContext(), IsletmeSecimActivity.class));
                finish();
                return true;
            case R.id.action_cagrilar:
                startActivity(new Intent(getApplicationContext(), CagrilarActivity.class));
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void PersonelIsletmeBilgisiDegistirme(final String isID){
        // işletme kurulumunda web işlemleri burada yapılacak.

        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        final FirebaseUser currentUser = mAuth.getCurrentUser();

        String url = "https://callbellapp.xyz/project/table4/update_table4_bussiness_add_boss_uid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                //Toast.makeText(IsletmeSecimActivity.this, response, Toast.LENGTH_SHORT).show();
                Resources res = getResources();
                String IsletmeSecimTebriklerIsletmenizBasariylaKuruldu = res.getString(R.string.IsletmeSecimTebriklerIsletmenizBasariylaKuruldu);
                Toast.makeText(IsletmeSecimActivity.this, IsletmeSecimTebriklerIsletmenizBasariylaKuruldu, Toast.LENGTH_SHORT).show();

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
                params.put("per_islt_yon_uid",currentUser.getUid());
                // burada işletme yonetici ID de ekleniyor. scanner da yok.
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeSecimActivity.this).add(stringRequest);
    }
}
