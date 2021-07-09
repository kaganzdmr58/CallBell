package com.callbellapp.callbell.Isletme;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class MasalarDetayActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView rvMasalarDetay;
    private ImageView imageViewMasaQrCode,imageViewSessiz,imageViewSparis;
    private TextView textViewMasaAdi;
    private ArrayList<CagrilarDetay> cagrilarMasalarArrayList;
    private MasalarDetayRVAdapter adapter;
    private QrCode masa;
    private Boolean zilEtkin;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MasalarActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masalar_detay);

        masa = (QrCode) getIntent().getSerializableExtra("nesne");

        rvMasalarDetay = findViewById(R.id.rvMasalarDetay);
        rvMasalarDetay.setHasFixedSize(true);
        rvMasalarDetay.setLayoutManager(new LinearLayoutManager(this));

        gelenCagrilariAlma();



        /*cagrilarMasalarArrayList = new ArrayList<>();

        CagrilarDetay c1 = new CagrilarDetay(1,14,null,8,"Mesaj","Zaman");
        cagrilarMasalarArrayList.add(c1);

        adapter = new MasalarRVAdapter(MasalarDetayActivity.this,cagrilarMasalarArrayList);
        rvMasalarDetay.setAdapter(adapter);*/



        imageViewMasaQrCode = findViewById(R.id.imageViewMasaQrCode);
        imageViewSessiz = findViewById(R.id.imageViewSessiz);
        imageViewSparis = findViewById(R.id.imageViewSparis);
        textViewMasaAdi = findViewById(R.id.textViewMasaAdi);

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


        textViewMasaAdi.setText(masa.getQrName());
        textViewMasaAdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean YONETICI_MODU = StartActivity.spLogin.getBoolean("YONETICI_MODU",false);
                if (YONETICI_MODU ){
                    LayoutInflater inflater = LayoutInflater.from(MasalarDetayActivity.this);
                    View alert_tasarim = inflater.inflate(R.layout.masa_adi_alertview_tasarim, null);

                    final EditText alert_edittext = (EditText) alert_tasarim.findViewById(R.id.alert_edittext);

                    AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(MasalarDetayActivity.this);
                    alertDialogOlusturucu.setMessage(R.string.ProfilKullanıcıAdi);
                    alertDialogOlusturucu.setView(alert_tasarim);
                    alert_edittext.setHint(masa.getQrName());
                    alert_edittext.setText(masa.getQrName());

                    alertDialogOlusturucu.setPositiveButton(R.string.Gonder, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // update metodu
                            MasaAdiDegistir(alert_edittext.getText().toString().trim());
                        }
                    });
                    alertDialogOlusturucu.setNegativeButton(R.string.Iptal, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            Resources res = getResources();
                            String toastMesaj = res.getString(R.string.KayıtİptalEdildi);
                            Toast.makeText(getApplicationContext(), toastMesaj, Toast.LENGTH_LONG).show();
                        }
                    });
                    alertDialogOlusturucu.create().show();

                }else{
                    Resources res = getResources();
                    String toastMesaj = res.getString(R.string.MasalarMasaIsmıDegistirme);
                    Toast.makeText(getApplicationContext(), toastMesaj, Toast.LENGTH_LONG).show();
                }

            }
        });

        String isletmeID =StartActivity.spYonetici.getString("isletmeID","null");
        String PersonelID =StartActivity.spLogin.getString("PersonelID","null");

        qrCodeUret("callbell:"+"m:"+isletmeID+":"+masa.getQrId());//işletme yöneticisi id si alınacak.
        imageViewMasaQrCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(toolbar,R.string.ProfilQrKoduKaydet,Snackbar.LENGTH_LONG)
                        .setAction(R.string.Evet, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                QrCodeKaydet(masa.getQrName());
                            }
                        }).show();
            }
        });

        imageViewSparis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Resources res = getResources();
                String MasalarSiparislerBuraya  = res.getString(R.string.MasalarSiparislerBuraya);
                String gonder  = res.getString(R.string.Gonder);
                final String MasalarSiparislerGonderildi  = res.getString(R.string.MasalarSiparislerGonderildi);
                final String MasalarSiparislerIptalEdildi  = res.getString(R.string.MasalarSiparislerIptalEdildi);

                LayoutInflater inflater = LayoutInflater.from(MasalarDetayActivity.this);
                View alert_tasarim = inflater.inflate(R.layout.sparis_alertview_tasarim, null);

                final EditText alert_edittext = (EditText) alert_tasarim.findViewById(R.id.editTextWeb);

                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(MasalarDetayActivity.this);
                alertDialogOlusturucu.setMessage(R.string.ProfilKullanıcıAdi);
                alertDialogOlusturucu.setView(alert_tasarim);
                alert_edittext.setHint(MasalarSiparislerBuraya);

                alertDialogOlusturucu.setPositiveButton(gonder, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gidecekMesaj(alert_edittext.getText().toString().trim());
                        Toast.makeText(MasalarDetayActivity.this,MasalarSiparislerGonderildi,Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogOlusturucu.setNegativeButton(R.string.Iptal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(MasalarDetayActivity.this,MasalarSiparislerIptalEdildi,Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogOlusturucu.create().show();
            }
        });

            zilEtkin = StartActivity.spZilEtkin.getBoolean(String.valueOf(masa.getQrId()),true);
        if(zilEtkin){
            imageViewSessiz.setImageResource(R.drawable.ic_notifications_active_black_24dp);
        }else {
            imageViewSessiz.setImageResource(R.drawable.ic_notifications_off_black_24dp);
        }

        imageViewSessiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zilEtkin = StartActivity.spZilEtkin.getBoolean(String.valueOf(masa.getQrId()),true);
                if(zilEtkin){
                    StartActivity.editorZilEtkin.putBoolean(String.valueOf(masa.getQrId()),false);
                    StartActivity.editorZilEtkin.commit();
                    imageViewSessiz.setImageResource(R.drawable.ic_notifications_off_black_24dp);
                }else {
                    StartActivity.editorZilEtkin.putBoolean(String.valueOf(masa.getQrId()),true);
                    StartActivity.editorZilEtkin.commit();
                    imageViewSessiz.setImageResource(R.drawable.ic_notifications_active_black_24dp);
                }
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

    public void qrCodeUret(String qr){
        MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
        try {
            BitMatrix bitMatrix =multiFormatWriter.encode(qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewMasaQrCode.setImageBitmap(bitmap);

        }catch (WriterException e){
            e.printStackTrace();
        }

    }


    private void QrCodeKaydet(String kayitAdi){
        OutputStream outputStream;
        try {
            BitmapDrawable drawable1 = (BitmapDrawable) imageViewMasaQrCode.getDrawable();
            Bitmap bitmap1 = drawable1.getBitmap();

            File filePath = Environment.getExternalStorageDirectory();
            File dir = new File(filePath.getAbsolutePath()+"/CallBell/");
            dir.mkdir();
            File file = new File(dir,kayitAdi+/*"_"+System.currentTimeMillis()+*/".jpg");
            outputStream = new FileOutputStream(file);

            bitmap1.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
            outputStream.flush();
            outputStream.close();

            Resources res = getResources();
            String YonetDetayResimKaydedildi  = res.getString(R.string.YonetDetayResimKaydedildi);
            Toast.makeText(getApplicationContext(),YonetDetayResimKaydedildi+file,Toast.LENGTH_LONG).show();

        }catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void MasaAdiDegistir(final String masaYeniAd){
        String url = "https://callbellapp.xyz/project/table1/update_table1_qr_code.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);
                Resources res = getResources();
                String MasalarMasanınAdi  = res.getString(R.string.MasalarMasanınAdi);
                String MasalarDegisti  = res.getString(R.string.MasalarDegisti);
                Toast.makeText(getApplicationContext(),MasalarMasanınAdi+masaYeniAd+MasalarDegisti,Toast.LENGTH_SHORT).show();
                textViewMasaAdi.setText(masaYeniAd);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("qr_name",masaYeniAd);
                params.put("qr_id",String.valueOf(masa.getQrId()));
                return params;
            }
        };

        Volley.newRequestQueue(MasalarDetayActivity.this).add(stringRequest);

    }


    public void gelenCagrilariAlma() {
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        if (isletmeID.equals("null")){
            return;
        }


        String url = "https://callbellapp.xyz/project/table3/all_table3_call_find_tab_key.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                cagrilarMasalarArrayList = new ArrayList<>();
                //Log.e("response",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray cagrilar = jsonObject.getJSONArray("tablo3");

                    for (int i = 0; i<cagrilar.length(); i++ ) {
                        // önce cağrilar sınıfı oluşturuluyor daha sonra çağrilar sınıfı ile qr_code sınıfına ulaşılıyor adımlara dikkat.
                        JSONObject c = cagrilar.getJSONObject(i);
                        int ca_id = c.getInt("ca_id");
                        int ca_isletme_id = c.getInt("ca_isletme_id");
                        //String ca_masa_id = c.getString("ca_masa_id");
                        int ca_musteri_id = c.getInt("ca_musteri_id");
                        String ca_mesaj = c.getString("ca_mesaj");
                        String ca_zaman = c.getString("ca_zaman");

                        JSONObject q = c.getJSONObject("ca_masa_id");
                        int qrId = q.getInt("qr_id");
                        String qrName = q.getString("qr_name");
                        String qrIsletme_id = q.getString("qr_isletme_id");

                        // gelen mesajı filtreleme
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



                        // Listeye ekleme
                        QrCode qrCodeWeb = new QrCode(qrId,qrName,qrIsletme_id);

                        CagrilarDetay cagri = new CagrilarDetay(ca_id,ca_isletme_id,qrCodeWeb,ca_musteri_id,gelenIstekMesaj,ca_zaman);

                        cagrilarMasalarArrayList.add(cagri);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new MasalarDetayRVAdapter(MasalarDetayActivity.this,cagrilarMasalarArrayList);
                rvMasalarDetay.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ca_masa_id",String.valueOf(masa.getQrId()));
                return params;
            }
        };
        Volley.newRequestQueue(MasalarDetayActivity.this).add(stringRequest);


    }


    public void gidecekMesaj(final String mesaj){
        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if ((hour.length())==1){hour = "0" + hour; }
        if ((minute.length())==1){minute = "0" + minute; }
        final String bildirimSaati = hour +" : "+ minute;

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefIsletme = database.getReference("QrCodeListesi").
                child(String.valueOf(masa.getQrIsletmeID()));

            Map<String, Object> zilCalBilgi = new HashMap<>();
            zilCalBilgi.put("qrfMesaj", mesaj);
            zilCalBilgi.put("qrfZaman", bildirimSaati);
            myRefIsletme.child(String.valueOf(masa.getQrId())).updateChildren(zilCalBilgi);

            // işletmenin web gecmis cagrilar tablosuna ekleme yapılacak.

        String url = "https://callbellapp.xyz/project/table3/insert_table3_call.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                gelenCagrilariAlma();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ca_isletme_id",String.valueOf(masa.getQrIsletmeID()));
                params.put("ca_masa_id",String.valueOf(masa.getQrId()));
                params.put("ca_musteri_id","8");
                params.put("ca_mesaj",mesaj);
                params.put("ca_zaman",bildirimSaati);
                return params;
            }
        };
        Volley.newRequestQueue(MasalarDetayActivity.this).add(stringRequest);

    }
}
