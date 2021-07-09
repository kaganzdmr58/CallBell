package com.callbellapp.callbell.Isletme;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.callbellapp.callbell.Sınıflar.CagrilarDetay;
import com.callbellapp.callbell.Sınıflar.QrCode;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CagrilarActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView rvCagrilar;
    private ArrayList<CagrilarDetay>cagrilarArrayList;
    private CagrilarRVAdapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cagrilar);

        rvCagrilar = findViewById(R.id.rvCagrilar);
        rvCagrilar.setHasFixedSize(true);
        rvCagrilar.setLayoutManager(new LinearLayoutManager(this));

        gelenCagrilariAlma();

        /*cagrilarArrayList = new ArrayList<>();

        Cagrilar c1 = new Cagrilar(1,"isletmeKey","masaKey","MusteriUID","Mesaj","Zaman");
        cagrilarArrayList.add(c1);

        adapter = new GecmisCagrilarRVAdapter(this,cagrilarArrayList);
        rvCagrilar.setAdapter(adapter);*/


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

        // ADMOB banner
        MobileAds.initialize(this,"ca-app-pub-2183039164562504~2945725091");
        //cihaz admob ID
        AdView banner = findViewById(R.id.banner);
        AdRequest adRequest = new AdRequest.Builder().build();
        banner.loadAd(adRequest);
        banner.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                // Reklam yüklendiğinde çalışır
                Log.e("Banner"," onAdLoaded yüklendi");
            }
            @Override
            public void onAdFailedToLoad(int i) {
                // Hata oluştuğunda çalışır.
                Log.e("Banner","onAdFailedToLoad yüklendi : " + i);

                // banner.loadAd( new AdRequest.Builder().build());
            }
            @Override
            public void onAdOpened() {
                // Reklam tıklanarak açılma
                Log.e("Banner","onAdOpened yüklendi");
            }

            @Override
            public void onAdLeftApplication() {
                //Uygulamadan ayrılınca
                Log.e("Banner","onAdLeftApplication çalıştı");
            }

            @Override
            public void onAdClosed() {
                //Reklamdan geri dönünce çalışır.
                Log.e("Banner","onAdClosed çalıştı");
            }
        });


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

    public void gelenCagrilariAlma() {
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        if (isletmeID.equals("null")){
            return;
        }

        String url = "https://callbellapp.xyz/project/table3/all_table3_call_find_business_key.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                cagrilarArrayList = new ArrayList<>();
                //Log.e("response",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray cagrilar = jsonObject.getJSONArray("tablo3");

                    for (int i = 0; i<cagrilar.length(); i++ ) {

                        JSONObject c = cagrilar.getJSONObject(i);

                        int ca_id = c.getInt("ca_id");
                        int ca_isletme_id = c.getInt("ca_isletme_id");
                        //int ca_masa_id = c.getInt("ca_masa_id");
                        int ca_musteri_id = c.getInt("ca_musteri_id");
                        String ca_mesaj = c.getString("ca_mesaj");
                        String ca_zaman = c.getString("ca_zaman");

                        JSONObject q = c.getJSONObject("ca_masa_id");
                        int qrId = q.getInt("qr_id");
                        String qrName = q.getString("qr_name");
                        String qrIsletme_id = q.getString("qr_isletme_id");


                        // Gelen mesajı filtreleme
                        Resources res = getResources();
                        String MainZilCaldiYazisi  = res.getString(R.string.MainZilCaldiYazisi);
                        String MainbutonHesapOtoYaziHesabiİstedi= res.getString(R.string.MainbutonHesapOtoYaziHesabiİstedi);
                        String MainButtonHesapOtoYaziMenuIstedi = res.getString(R.string.MainButtonHesapOtoYaziMenuIstedi);

                        String gelenIstekMesaj = "";

                        if (ca_mesaj.equals("ZIL_CALDI_KOD_TRUE")){
                            gelenIstekMesaj =MainZilCaldiYazisi;
                        }else if (ca_mesaj.equals("ZIL_CALDI_KOD_FALSE")) {
                            gelenIstekMesaj = MainZilCaldiYazisi;
                        }else if (ca_mesaj.equals("MENU_ISTEDI_KOD")){
                            gelenIstekMesaj = MainButtonHesapOtoYaziMenuIstedi;
                        }else if (ca_mesaj.equals("HESAP_ISTEDI_KOD")){
                            gelenIstekMesaj =  MainbutonHesapOtoYaziHesabiİstedi;
                        }else {
                            gelenIstekMesaj = ca_mesaj;
                        }


                        // mesajı listeye aktarma
                        QrCode qrCodeWeb = new QrCode(qrId,qrName,qrIsletme_id);

                        CagrilarDetay cagri = new CagrilarDetay(
                                ca_id,ca_isletme_id, qrCodeWeb,ca_musteri_id,gelenIstekMesaj,ca_zaman);

                        cagrilarArrayList.add(cagri);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new CagrilarRVAdapter(CagrilarActivity.this,cagrilarArrayList);
                rvCagrilar.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ca_isletme_id",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(CagrilarActivity.this).add(stringRequest);

    }

}


/* tersten sıralatma
https://www.yazilimekip.com/php-ile-mysql-de-bir-tabloda-bulunan-en-son-kaydin-id-degerini-almak.html
* */