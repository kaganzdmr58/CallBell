package com.callbellapp.callbell.KampanyaGoster;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

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
import com.callbellapp.callbell.Isletme.MasalarActivity;
import com.callbellapp.callbell.MainActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.ScannerActivity;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Sınıflar.WebResimUrl;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KampanyaGosterActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ViewPager viewPager;
    private FloatingActionButton fabMenuSiparis;
    private List<WebResimUrl> imageUrls = new ArrayList<>();
    private ViewPagerAdapterKampanyalar adapter;
    private String isletmeScanID;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampanya_goster);

        viewPager = findViewById(R.id.ViewPagerKampanya);

        MenuResimleriAlma();


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





    public void MenuResimleriAlma(){

        isletmeScanID = (String) getIntent().getSerializableExtra("gecisID");

        /*
        // qr bilgileri düzenle if ile doluluk şartı koy.
        isletmeScanID = StartActivity.spScan.getString("isletmeScanID","gecici");
        Boolean ISLETME_MODU = StartActivity.spLogin.getBoolean("ISLETME_MODU",false);
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        if (ISLETME_MODU == true){
            isletmeScanID = isletmeID;
        }
        String gecisID = (String) getIntent().getSerializableExtra("gecisID");
        if (!(gecisID.isEmpty())){
            isletmeScanID = gecisID;
        }*/

        String url = "https://callbellapp.xyz/project/table5/table5_url_find.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.e("response",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray qrCode = jsonObject.getJSONArray("tablo5");

                    for (int i = 0; i<qrCode.length(); i++ ) {

                        JSONObject q = qrCode.getJSONObject(i);
                        String res_url = q.getString("res_url");
                        int res_tur = q.getInt("res_tur");
                        int res_id = q.getInt("res_id");
                        int res_isletme = q.getInt("res_isletme");

                        WebResimUrl webResimUrl = new WebResimUrl(res_id,res_isletme,res_tur,res_url);

                        if ((res_tur == 2) && (!(res_url.isEmpty()))){
                            imageUrls.add(webResimUrl);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (imageUrls.size()==0){
                    Resources res = getResources();
                    String toastMesaj = res.getString(R.string.KampanyaGosterIsletmeHenüzResimEklemedi);
                    Toast.makeText(getApplicationContext(), toastMesaj, Toast.LENGTH_LONG).show();
                }

                adapter = new ViewPagerAdapterKampanyalar(KampanyaGosterActivity.this,imageUrls);
                viewPager.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("res_isletme",isletmeScanID);
                return params;
            }
        };
        Volley.newRequestQueue(KampanyaGosterActivity.this).add(stringRequest);


    }

}
