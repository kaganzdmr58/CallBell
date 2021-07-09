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
import com.callbellapp.callbell.Sınıflar.Ziyaretciler;
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

public class GecmisYerlerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView rvGecmisYerler;
    private ArrayList<Ziyaretciler> GecmisIsletmelerArrayList;
    private GecmisYerlerRVAdapter adapter;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gecmis_yerler);


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

        rvGecmisYerler = findViewById(R.id.rvGecmisYerler);
        rvGecmisYerler.setHasFixedSize(true);
        rvGecmisYerler.setLayoutManager(new LinearLayoutManager(this));
        IsletmeBilgileriAlma();

        /*GecmisIsletmelerArrayList= new ArrayList<>();
        IsletmeBilgi isletmeBilgi = new IsletmeBilgi(
                1,"is_name","is_yonetici_id",
                "is_bilgi","is_kampanya",
                "https://callbellapp.xyz/project/table5/uploads/CallBell%3A41%3A1%3A27.jpg");
        GecmisIsletmelerArrayList.add(isletmeBilgi);
        adapter = new GecmisYerlerRVAdapter(GecmisYerlerActivity.this,GecmisIsletmelerArrayList);
        rvGecmisYerler.setAdapter(adapter);*/

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

        String url = "https://callbellapp.xyz/project/table7/all_table7_find_uid.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                GecmisIsletmelerArrayList= new ArrayList<>();
                //Log.e("response",response);
                //Toast.makeText(getApplicationContext(),"response"+response,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray ziyaret = jsonObject.getJSONArray("tablo7");

                    for (int i = 0; i<ziyaret.length(); i++ ) {

                        JSONObject z = ziyaret.getJSONObject(i);

                        int zi_id = z.getInt("zi_id");
                        String zi_uid = z.getString("zi_uid");
                        //String zi_isletme = z.getString("zi_isletme");

                        JSONObject q = z.getJSONObject("zi_isletme");

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

                        Ziyaretciler ziyaretci = new Ziyaretciler(zi_id,zi_uid,isletmeBilgi);

                        GecmisIsletmelerArrayList.add(ziyaretci);
                        //Toast.makeText(getApplicationContext(),"response"+is_id,Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                StartActivity.editorLogin.putInt("GecmisIsletmelerArrayList",GecmisIsletmelerArrayList.size());
                StartActivity.editorLogin.commit();

                adapter = new GecmisYerlerRVAdapter(GecmisYerlerActivity.this,GecmisIsletmelerArrayList);
                rvGecmisYerler.setAdapter(adapter);

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
        Volley.newRequestQueue(GecmisYerlerActivity.this).add(stringRequest);
    }

}
