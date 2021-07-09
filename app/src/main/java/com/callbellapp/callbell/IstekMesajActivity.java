package com.callbellapp.callbell;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
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


import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.IsletmeSecimActivity;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IstekMesajActivity extends AppCompatActivity {

    private TextView textViewIstekBaslik,textViewDilekSikayet;
    private EditText editTextIstekMetniGiris;
    private Button buttonIstekGonder;
    private Boolean sikayetDurum = true;
    public static String cihazMaster, mesaj,qrName;

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private String musteriMail,IstekUygulamaGelistiriciyeMesajGonderildi,IstekIsletmeSahibineMesajIletildi
            ,IstekQrCodeOkutulamadıgıIcınGonderilemedi;
    private RatingBar ratingBar;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(IstekMesajActivity.this,AyarlarActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_istek_mesaj);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbarArkaPlanRengi));
        toolbar.setTitleTextColor(getResources().getColor(R.color.butonTextColor));
        // toolbar.setSubtitleTextColor(getResources().getColor(R.color.butonTextColor));
        toolbar.setLogo(R.drawable.room_service_red_48dp);
        setSupportActionBar(toolbar);


        textViewIstekBaslik = findViewById(R.id.textViewIstekBaslik);
        textViewDilekSikayet = findViewById(R.id.textViewDilekSikayet);
        editTextIstekMetniGiris = findViewById(R.id.editTextIstekMetniGiris);
        buttonIstekGonder = findViewById(R.id.buttonIstekGonder);
        ratingBar = findViewById(R.id.ratingBar);

        Resources res = getResources();
        musteriMail  = res.getString(R.string.IStekMusteriMail);
        IstekUygulamaGelistiriciyeMesajGonderildi  = res.getString(R.string.IstekUygulamaGelistiriciyeMesajGonderildi);
        IstekIsletmeSahibineMesajIletildi  = res.getString(R.string.IstekIsletmeSahibineMesajIletildi);
        IstekQrCodeOkutulamadıgıIcınGonderilemedi  = res.getString(R.string.IstekQrCodeOkutulamadıgıIcınGonderilemedi);





        // burası açılması için yani butonun aktif olabilmesi için önce qr code taratılmalı.
        // qr coddan gelen bilgide cihaz master yazan cihaza dilek şikayet gönderilecek.
        // siparişler ise bütün cihazlara gönderilecek gelenIstekMesaj ile gönderim sağlanacak.
        // textViewDilekSikayet ile swich butonu gibi çalıştırılıp dilek ve şikayette bulunulacak ise
        // textViewIstekBaslik, textViewDilekSikayet ve editTextIstekMetniGiris(hint'i) değiştirilecek


        textViewDilekSikayet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sikayetDurum){
                    sikayetDurum = false;
                    gelistiriciyeMesaj();
                }else {
                    sikayetDurum = true;
                    Sikayet();
                }
            }
        });
        // toolbara tıklamada main activity açılışı
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
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

        // masa scan code ile masa ismi webden alınabilir.


        buttonIstekGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                final FirebaseUser user = mAuth.getCurrentUser();
                // musteri adı görünecek mi sorgusu yap.

                mesaj = editTextIstekMetniGiris.getText().toString().trim();
                final String isletmeScanID = StartActivity.spScan.getString("isletmeScanID","gecici");
                final String qrName = StartActivity.spScan.getString("qrName","gecici");

                //Toast.makeText(IstekMesajActivity.this, "Rating bar : " + ratingBar.getRating(), Toast.LENGTH_SHORT).show();
                String musteriAdi = ""; // boolean türünde sorgusu yapılacak.
                Boolean KULLANICI_ADI_GIZLE = StartActivity.spBilgiler.getBoolean("KULLANICI_ADI_GIZLE",true);
                if (!KULLANICI_ADI_GIZLE){
                    musteriAdi = user.getEmail() +" ";
                }

                float rating = ratingBar.getRating();

                if (sikayetDurum == false){
                    // uygulama geliştiriciye mesaj gönderme.
                    //masa adı geleiştiriciye mesajda e mail olarak gideccek.
                    sikayetGonderWeb(user.getEmail()+" - "+musteriAdi,mesaj,user.getDisplayName(),"uygulamaGelistiricisine",user.getUid(),rating);

                    Toast.makeText(getApplicationContext(),IstekUygulamaGelistiriciyeMesajGonderildi,Toast.LENGTH_SHORT).show();
                }else {
                    if (!isletmeScanID.equals("gecici")){
                        // cihaz master yani işletme qr codu okutuldu mu?
                        sikayetGonderWeb(user.getEmail(),mesaj,musteriAdi,isletmeScanID,user.getUid(),rating);

                        Toast.makeText(getApplicationContext(),IstekIsletmeSahibineMesajIletildi,Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(getApplicationContext(),IstekQrCodeOkutulamadıgıIcınGonderilemedi,Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    private void gelistiriciyeMesaj (){
        textViewIstekBaslik.setText(R.string.IstekUygulamaGeriBildirim);
        textViewDilekSikayet.setText(R.string.IstekIsletmeYonetımıneGorusOnerideBulun);
        editTextIstekMetniGiris.setHint(R.string.IstekUygulamaGelistiricisineIstekMesajıGiriniz);
    }

    private void Sikayet(){
        textViewIstekBaslik.setText(R.string.IstekGorusOneri);
        textViewDilekSikayet.setText(R.string.IstekUygulamaGelsitircisineGorusOnerideBulun);
        editTextIstekMetniGiris.setHint(R.string.IstekKurumYonetimineGorusOnerideBulun);
    }

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

    public void sikayetGonderWeb(final String email,final String sikayet,final String musteriAdi,final String isletmeQr,final String musteriUID,final float rating){

        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if ((hour.length())==1){hour = "0" + hour; }
        if ((minute.length())==1){minute = "0" + minute; }
        String gun = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String ay = String.valueOf((calendar.get(Calendar.MONTH))+1);
        String yil = String.valueOf(calendar.get(Calendar.YEAR));
        if ((gun.length())==1){gun = "0" + gun; }
        if ((ay.length())==1){ay = "0" + ay; }
        if ((yil.length())==1){yil = "0" + yil; }
        final String bildirimSaati = hour +":"+ minute +"-"+gun+"."+(ay)+"."+yil  ;

        // masa adı geleiştiriciye mesajda e mail olarak gideccek.

        String url = "https://callbellapp.xyz/project/table6/insert_table6_complaint.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("si_masa_tarih",email +" / "+bildirimSaati);
                params.put("si_sikayet",sikayet);
                params.put("si_email_ad",musteriAdi);
                params.put("si_isletme_qr",isletmeQr);
                params.put("si_gonderen_uid",musteriUID);
                params.put("si_rating",String.valueOf(rating));
                return params;
            }
        };
        Volley.newRequestQueue(IstekMesajActivity.this).add(stringRequest);

    }





}
