package com.callbellapp.callbell.Yonetici;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.IsletmeSecimActivity;
import com.callbellapp.callbell.MainActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.ScannerActivity;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Sınıflar.DilekveSikayeteler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DilekVeSikayetlerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView rvSikayet;
    private ArrayList<DilekveSikayeteler> sikayetArrayListe;
    public static DilekveSikayetRVAdapter adapter;
    private TextView textViewBaslik;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),YoneticiMainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dilek_ve_sikayetler);

        textViewBaslik = findViewById(R.id.textViewBaslik);
        rvSikayet = findViewById(R.id.rvSikayet);

        rvSikayet.setHasFixedSize(true);
        rvSikayet.setLayoutManager(new LinearLayoutManager(this));

        SikayetleriAlma();


        /*sikayetArrayListe= new ArrayList<>();

        DilekveSikayeteler s1 = new DilekveSikayeteler(1,"masa nu ve tarih saat","şikayet","e mail ve ad","","");
        sikayetArrayListe.add(s1);

        adapter = new DilekveSikayetRVAdapter(this,sikayetArrayListe);
        rvSikayet.setAdapter(adapter);*/


        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbarArkaPlanRengi));
        toolbar.setTitleTextColor(getResources().getColor(R.color.butonTextColor));
        // toolbar.setSubtitleTextColor(getResources().getColor(R.color.butonTextColor));
        toolbar.setLogo(R.drawable.room_service_red_48dp);
        setSupportActionBar(toolbar);

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


        textViewBaslik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DilekVeSikayetlerActivity.class));
                finish();
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

    public void SikayetleriAlma() {
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        String url = "https://callbellapp.xyz/project/table6/all_table6_complaint_find_business_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sikayetArrayListe= new ArrayList<>();
                //Log.e("response",response);
                //Toast.makeText(getApplicationContext(),isletmeID,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray qrCode = jsonObject.getJSONArray("tablo6");

                    for (int i = 0; i<qrCode.length(); i++ ) {

                        JSONObject q = qrCode.getJSONObject(i);

                        int si_id = q.getInt("si_id");
                        String si_masa_tarih = q.getString("si_masa_tarih");
                        String si_sikayet = q.getString("si_sikayet");
                        String si_email_ad = q.getString("si_email_ad");
                        String si_isletme_qr = q.getString("si_isletme_qr");
                        String si_gonderen_uid = q.getString("si_gonderen_uid");
                        String si_rating = q.getString("si_rating");

                        DilekveSikayeteler sikayet = new DilekveSikayeteler(si_id,si_masa_tarih
                                ,si_sikayet,si_email_ad,si_isletme_qr,si_gonderen_uid,Float.parseFloat(si_rating));

                        sikayetArrayListe.add(sikayet);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                adapter = new DilekveSikayetRVAdapter(DilekVeSikayetlerActivity.this,sikayetArrayListe);
                rvSikayet.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("si_isletme_qr",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(DilekVeSikayetlerActivity.this).add(stringRequest);


    }



}
