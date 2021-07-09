package com.callbellapp.callbell;

import android.content.Intent;
import android.os.Bundle;
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


import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.MasalarActivity;
import com.callbellapp.callbell.Sınıflar.IsletmeBilgi;
import com.callbellapp.callbell.Sınıflar.Mesajlar;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GelenKutusuActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView rvGelenKutusu;
    private ArrayList<Mesajlar> GelenKutusuMesajlarArrayList;
    private GelenKutusuRVAdapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_kutusu);


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


        rvGelenKutusu = findViewById(R.id.rvGelenKutusu);
        rvGelenKutusu.setHasFixedSize(true);
        rvGelenKutusu.setLayoutManager(new LinearLayoutManager(this));
        IsletmeBilgileriAlma();

        /*GelenKutusuMesajlarArrayList= new ArrayList<>();
        IsletmeBilgi isletme = new IsletmeBilgi(1,"isName","isYonetciID","isBilgi","is_kampanya","isIcon");
        Mesajlar mesaj1 = new Mesajlar(15,isletme,"mes_mesaj","mes_tarih","mes_url");
        GelenKutusuMesajlarArrayList.add(mesaj1);
        adapter = new GelenKutusuRVAdapter(GelenKutusuActivity.this,GelenKutusuMesajlarArrayList);
        rvGelenKutusu.setAdapter(adapter);*/

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
                startActivity(new Intent(getApplicationContext(), MasalarActivity.class));
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


    public void IsletmeBilgileriAlma() {
        // Initialize Firebase Auth
        final FirebaseAuth mAuth  = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        /*Log.e("uid",user.getUid());
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        Log.e("uid_isletmeId",isletmeID);*/

        String url = "https://callbellapp.xyz/project/table8/all_table8_1.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(getApplicationContext(),"response_"+response,Toast.LENGTH_LONG).show();
                GelenKutusuMesajlarArrayList= new ArrayList<>();

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray mesaj = jsonObject.getJSONArray("tablo8");

                    for (int i = 0; i<mesaj.length(); i++ ) {

                        JSONObject m = mesaj.getJSONObject(i);

                        int mes_id = m.getInt("mes_id");
                        //int mes_isletme_id = m.getInt("mes_isletme_id");
                        String mes_mesaj = m.getString("mes_mesaj");
                        String mes_tarih = m.getString("mes_tarih");
                        String mes_url = m.getString("mes_url");


                        JSONObject q = m.getJSONObject("mes_isletme_id");

                        int isId = q.getInt("is_id");
                        String isName = q.getString("is_name");
                        String isYonetciID = q.getString("is_yonetici_id");
                        String isSlogan = q.getString("is_slogan");
                        String is_tel = q.getString("is_tel");
                        String is_email = q.getString("is_email");
                        String is_web = q.getString("is_web");
                        String is_adres = q.getString("is_adres");
                        String isIcon = q.getString("is_icon_url");


                        IsletmeBilgi isletmeBilgi = new IsletmeBilgi(isId,isName,isYonetciID,isSlogan,is_tel,is_email,is_web,is_adres,isIcon);

                        Mesajlar mesaj1 = new Mesajlar(mes_id,isletmeBilgi,mes_mesaj,mes_tarih,mes_url);

                        //Toast.makeText(GelenKutusuActivity.this, "mesaj id : "+ mes_mesaj, Toast.LENGTH_SHORT).show();

                        GelenKutusuMesajlarArrayList.add(mesaj1);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                adapter = new GelenKutusuRVAdapter(GelenKutusuActivity.this,GelenKutusuMesajlarArrayList);
                rvGelenKutusu.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("zi_uid",user.getUid());
                return params;
            }
        };
        Volley.newRequestQueue(GelenKutusuActivity.this).add(stringRequest);
    }



    public void KampanyaResimleriSilme(){

        String url = "https://callbellapp.xyz/project/table5/table5_delete_photo_and_sql.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dosya","CallBell:41:1:28.jpg");
                params.put("res_id","28");
                return params;
            }
        };
        Volley.newRequestQueue(GelenKutusuActivity.this).add(stringRequest);


    }


}



