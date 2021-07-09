package com.callbellapp.callbell;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.callbellapp.callbell.KampanyaGoster.KampanyaGosterActivity;
import com.callbellapp.callbell.MenuGoster.MenuGosterActivity;
import com.callbellapp.callbell.Sınıflar.IsletmeBilgi;
import com.callbellapp.callbell.Sınıflar.Mesajlar;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class IsletmeMesajlariActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ImageView imageViewIconIsletmeMesaj;
    private TextView textViewIsletmeAdi,textViewIsletmeSlogani;
    private Button buttonBilgi,buttonMenu,buttonKampanyya;
    private FloatingActionButton fabMesajEkle,fabInfo;

    private RecyclerView rvIsletmeMesajlari;
    private ArrayList<Mesajlar> isletmeMesajlariArrayList;
    private IsletmeMesajlariRVAdapter adapter;

    private IsletmeBilgi isletme;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));
        finish();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isletme_mesajlari);

        imageViewIconIsletmeMesaj = findViewById(R.id.imageViewIconIsletmeMesaj);
        textViewIsletmeAdi = findViewById(R.id.textViewTarihSaat);
        textViewIsletmeSlogani = findViewById(R.id.textViewIsletmeSlogani);
        buttonBilgi = findViewById(R.id.buttonBilgi);
        buttonMenu = findViewById(R.id.buttonMenu);
        rvIsletmeMesajlari = findViewById(R.id.rvIsletmeMesajlari);
        fabMesajEkle = findViewById(R.id.fabMesajEkle);
        buttonKampanyya = findViewById(R.id.buttonKampanyya);
        fabInfo = findViewById(R.id.fabInfo);

        isletme = (IsletmeBilgi) getIntent().getSerializableExtra("nesne");

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


        // fab görünürlüğü
        Boolean YONETICI_MODU = StartActivity.spLogin.getBoolean("YONETICI_MODU",false);
        String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        if (YONETICI_MODU){
            if (isletmeID.equals(String.valueOf(isletme.getIsId()))){
                fabMesajEkle.setVisibility(View.VISIBLE);
                fabInfo.setVisibility(View.VISIBLE);
            }else{
                fabMesajEkle.setVisibility(View.INVISIBLE);
                fabInfo.setVisibility(View.INVISIBLE);
            }
        }else {
            fabMesajEkle.setVisibility(View.INVISIBLE);
            fabInfo.setVisibility(View.INVISIBLE);
        }



        if (!((isletme.getIsIconUrl()).isEmpty())) {
            Picasso.get()
                    .load(isletme.getIsIconUrl())
                    .fit()
                    .centerInside()
                    .into(imageViewIconIsletmeMesaj);
        }

        if (!((isletme.getIsName()).isEmpty())) {
            textViewIsletmeAdi.setText(isletme.getIsName());
        }

        if (!((isletme.getIsSlogan()).isEmpty())) {
            textViewIsletmeSlogani.setText(isletme.getIsSlogan());
        }

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //işletme menüsüne gönderilecek
                Intent intent = new Intent(getApplicationContext(), MenuGosterActivity.class);
                intent.putExtra("gecisID",String.valueOf(isletme.getIsId()));
                startActivity(intent);
            }
        });

        rvIsletmeMesajlari.setHasFixedSize(true);
        rvIsletmeMesajlari.setLayoutManager(new LinearLayoutManager(this));
        IsletmeBilgileriAlma();

        /*GelenKutusuMesajlarArrayList= new ArrayList<>();
        IsletmeBilgi isletme = new IsletmeBilgi(1,"isName","isYonetciID","isBilgi","is_kampanya","isIcon");
        Mesajlar mesaj1 = new Mesajlar(15,isletme,"mes_mesaj","mes_tarih","mes_url");
        GelenKutusuMesajlarArrayList.add(mesaj1);
        adapter = new GelenKutusuRVAdapter(GelenKutusuActivity.this,GelenKutusuMesajlarArrayList);
        rvGelenKutusu.setAdapter(adapter);*/


        fabMesajEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                String IsletmeMesajlariKampanyaVeDuyuruMesajlari = res.getString(R.string.IsletmeMesajlariKampanyaVeDuyuruMesajlari);
                String IsletmeMesajlariKampanyaVeDuyuruMesajlarıGiriniz = res.getString(R.string.IsletmeMesajlariKampanyaVeDuyuruMesajlarıGiriniz);


                LayoutInflater inflater = LayoutInflater.from(IsletmeMesajlariActivity.this);
                View alert_tasarim = inflater.inflate(R.layout.gelen_mesaj_yaz_alertview_tasarim, null);

                final EditText alert_edittext = (EditText) alert_tasarim.findViewById(R.id.editTextGelenMesajGiris);

                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(IsletmeMesajlariActivity.this);
                alertDialogOlusturucu.setMessage(IsletmeMesajlariKampanyaVeDuyuruMesajlari);
                alertDialogOlusturucu.setView(alert_tasarim);
                //alert_edittext.setText(IsletmeMesajlariKampanyaVeDuyuruMesajlari);
                alert_edittext.setHint(IsletmeMesajlariKampanyaVeDuyuruMesajlarıGiriniz);

                alertDialogOlusturucu.setPositiveButton(R.string.Kaydet, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IsletmeMesajEkleme(alert_edittext.getText().toString().trim());
                    }
                });
                alertDialogOlusturucu.setNegativeButton(R.string.Iptal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(IsletmeMesajlariActivity.this,R.string.ProfilKayitIptalEdildi,Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogOlusturucu.create().show();
            }
        });

        fabInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                String IsletmeMesajlariBilgilendirme = res.getString(R.string.IsletmeMesajlariBilgilendirme);
                String IsletmeMesajlariBilgilendirmeMesajlari = res.getString(R.string.IsletmeMesajlariBilgilendirmeMesajlari);

                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(IsletmeMesajlariActivity.this);
                alertDialogOlusturucu.setTitle(IsletmeMesajlariBilgilendirme);
                alertDialogOlusturucu.setMessage(IsletmeMesajlariBilgilendirmeMesajlari);
                alertDialogOlusturucu.setPositiveButton(R.string.Tamam, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialogOlusturucu.create().show();
            }
        });

        buttonBilgi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                String IsletmeMesajlariTelefon2 = res.getString(R.string.IsletmeMesajlariTelefon2);
                String IsletmeMesajlariemail2 = res.getString(R.string.IsletmeMesajlariemail2);
                String IsletmeMesajlariWebAdres2 = res.getString(R.string.IsletmeMesajlariWebAdres2);
                String IsletmeMesajlariAdress2 = res.getString(R.string.IsletmeMesajlariAdress2);
                String IsletmeMesajlariIsletmeMesajlariGirilmemis = res.getString(R.string.IsletmeMesajlariIsletmeMesajlariGirilmemis);
                String IsletmeMesajlariIsletmeBilgileri = res.getString(R.string.IsletmeMesajlariIsletmeBilgileri);


                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(IsletmeMesajlariActivity.this);
                if (!((isletme.getIsTel()).isEmpty())) {
                    alertDialogOlusturucu.setMessage(
                            IsletmeMesajlariTelefon2+isletme.getIsTel()+
                            IsletmeMesajlariemail2+isletme.getIsEmail()+
                            IsletmeMesajlariWebAdres2+isletme.getIsWeb()+
                            IsletmeMesajlariAdress2+isletme.getIsAdres()
                    );

                }else {
                    alertDialogOlusturucu.setMessage(IsletmeMesajlariIsletmeMesajlariGirilmemis);
                }
                alertDialogOlusturucu.setTitle(IsletmeMesajlariIsletmeBilgileri);
                alertDialogOlusturucu.setIcon(R.drawable.store_black_24dp);

                alertDialogOlusturucu.setPositiveButton(R.string.Tamam, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialogOlusturucu.create().show();
            }
        });


        buttonKampanyya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //işletme menüsüne gönderilecek
                Intent intent = new Intent(getApplicationContext(), KampanyaGosterActivity.class);
                intent.putExtra("gecisID",String.valueOf(isletme.getIsId()));
                startActivity(intent);
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

        String url = "https://callbellapp.xyz/project/table8/all_table8_2.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(getApplicationContext(),"response_"+response,Toast.LENGTH_LONG).show();
                isletmeMesajlariArrayList= new ArrayList<>();

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


                        isletme = new IsletmeBilgi(isId,isName,isYonetciID,isSlogan,is_tel,is_email,is_web,is_adres,isIcon);

                        Mesajlar mesaj1 = new Mesajlar(mes_id,isletme,mes_mesaj,mes_tarih,mes_url);

                        //Toast.makeText(IsletmeMesajlariActivity.this, "mesaj id : "+ mes_mesaj, Toast.LENGTH_SHORT).show();
                        isletmeMesajlariArrayList.add(mesaj1);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new IsletmeMesajlariRVAdapter(IsletmeMesajlariActivity.this,isletmeMesajlariArrayList);
                rvIsletmeMesajlari.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mes_isletme_id",String.valueOf(isletme.getIsId()));
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeMesajlariActivity.this).add(stringRequest);
    }


    public void IsletmeMesajEkleme(final String gidenMesaj) {

        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if ((hour.length())==1){hour = "0" + hour; }
        if ((minute.length())==1){minute = "0" + minute; }
        final String bildirimSaati = hour +" : "+ minute;
        String gun = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String ay = String.valueOf((calendar.get(Calendar.MONTH))+1);
        String yil = String.valueOf(calendar.get(Calendar.YEAR));
        if ((gun.length())==1){gun = "0" + gun; }
        if ((ay.length())==1){ay = "0" + ay; }
        if ((yil.length())==1){yil = "0" + yil; }
        final String tarih = gun+"."+ay+"."+yil;

        String url = "https://callbellapp.xyz/project/table8/insert_table8_1.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(getApplicationContext(),"response_"+response,Toast.LENGTH_LONG).show();
                IsletmeBilgileriAlma();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mes_isletme_id",isletmeID);
                params.put("mes_mesaj",gidenMesaj);
                params.put("mes_tarih",bildirimSaati+" - "+tarih);
                params.put("mes_url","1");
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeMesajlariActivity.this).add(stringRequest);
    }




}
