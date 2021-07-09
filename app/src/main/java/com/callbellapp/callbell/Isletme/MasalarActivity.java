package com.callbellapp.callbell.Isletme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.callbellapp.callbell.Sınıflar.QrCode;
import com.callbellapp.callbell.Sınıflar.QrCodeFirebase;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import java.util.HashMap;
import java.util.Map;

public class MasalarActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private TextView textViewCafeAdi,textViewIsletmeQRKodu;
    private RecyclerView rvMasalar;
    private FloatingActionButton fabMasaEkle;
    private ArrayList<QrCode> masalarArrayList;
    private MasalarRVAdapter adapter;
    private ImageView imageViewQrGoster;
    private Animation animationBuyut,animationKucult;
    private Boolean animasyonDurum = false;
    private Button buttonAnimasyonuKapat;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
         isletmeMasalarıAlma();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isletmeMasalarıAlma();
    }

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masalar);

        textViewCafeAdi = findViewById(R.id.textViewCafeAdi);
        rvMasalar = findViewById(R.id.rvMasalar);
        fabMasaEkle = findViewById(R.id.fabMasaEkle);
        imageViewQrGoster = findViewById(R.id.imageViewQrGoster);
        buttonAnimasyonuKapat = findViewById(R.id.buttonAnimasyonuKapat);
        textViewIsletmeQRKodu = findViewById(R.id.textViewIsletmeQRKodu);


        rvMasalar.setHasFixedSize(true);
        rvMasalar.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));


/*        QrCode masa1 = new QrCode(1,"Masa 1","key1",StartActivity.isletmeQrCode);
        QrCode masa2 = new QrCode(2,"Masa 2","key2",StartActivity.isletmeQrCode);
        QrCode masa3 = new QrCode(3,"Masa 3","key3",StartActivity.isletmeQrCode);

        masalarArrayList.add(masa1);
        masalarArrayList.add(masa2);
        masalarArrayList.add(masa3);
        adapter = new MasalarRVAdapter(MasalarActivity.this,masalarArrayList);
        rvMasalar.setAdapter(adapter);*/

        //isletmeMasalarıAlma();

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


        Boolean YONETICI_MODU = StartActivity.spLogin.getBoolean("YONETICI_MODU",false);
        if (YONETICI_MODU){
            fabMasaEkle.setVisibility(View.VISIBLE);

        }else{
            fabMasaEkle.setVisibility(View.INVISIBLE);
        }
        fabMasaEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MasaEklemeWeb();
                // web masa listesine masa bilgileri eklemesi yapılacak
            }
        });

        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        String isletmeQRCode = StartActivity.spYonetici.getString("isletmeQRCode","null");
        qrCodeUret(isletmeQRCode);
        imageViewQrGoster.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(toolbar,R.string.ProfilQrKoduKaydet,Snackbar.LENGTH_LONG)
                        .setAction(R.string.Evet, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                QrCodeKaydet(R.string.IsletmeQrKodu+isletmeID);
                            }
                        }).show();
                return false;
            }
        });

        animationBuyut = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.isletme_qr_kod_buyutme);
        animationKucult = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.isletme_qr_kod_kucultme);

        imageViewQrGoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.startAnimation(animationBuyut);
                // kapat butonu göster
                // işletme QR kodu yazısı göster
                textViewIsletmeQRKodu.setVisibility(View.INVISIBLE);
                buttonAnimasyonuKapat.setVisibility(View.VISIBLE);

                Resources res = getResources();
                String toastMesaj = res.getString(R.string.MasalarIsletmeQRKodu);
                Toast.makeText(MasalarActivity.this, toastMesaj, Toast.LENGTH_LONG).show();

            }
        });

        buttonAnimasyonuKapat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageViewQrGoster.startAnimation(animationKucult);
                // kapat butonu göster
                // işletme QR kodu yazısı göster
                textViewIsletmeQRKodu.setVisibility(View.INVISIBLE);
                buttonAnimasyonuKapat.setVisibility(View.INVISIBLE);
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


    public void MasaEklemeWeb(){
        Resources res = getResources();
        final String MasalarDetayMasa  = res.getString(R.string.MasalarDetayMasa);

        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        int masaList = 0;
        if(masalarArrayList == null){
            masaList =0;
        }else {
            masaList = masalarArrayList.size();
        }

        final int yeniMasaNu = masaList+ 1;
        String url = "https://callbellapp.xyz/project/table1/insert_table1_qr_code.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);

                String[] cevap = response.split(":");

                StartActivity.editorYonetici.putString("masa "+yeniMasaNu+"ID",cevap[1]);
                StartActivity.editorYonetici.commit();

                MasaEkleme(cevap[1],"masa "+yeniMasaNu);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("qr_name",""+MasalarDetayMasa + yeniMasaNu );
                params.put("qr_isletme_id",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(MasalarActivity.this).add(stringRequest);
    }

    public void MasaEkleme(String masaID,String masaAdi){
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefIsletme = database.getReference("QrCodeListesi").child(isletmeID);

        QrCodeFirebase MasaMesaj = new QrCodeFirebase(masaID,"Mesaj","00:00",masaAdi);

        myRefIsletme.child(masaID).setValue(MasaMesaj);

        isletmeMasalarıAlma();
    }

    public void qrCodeUret(String qr){
        MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
        try {
            BitMatrix bitMatrix =multiFormatWriter.encode(qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewQrGoster.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
    private void QrCodeKaydet(String kayitAdi){
        OutputStream outputStream;
        try {
            BitmapDrawable drawable1 = (BitmapDrawable) imageViewQrGoster.getDrawable();
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



    public void isletmeMasalarıAlma() {
        // burada masada taratılan qr koda göre web den veri çekilecek.
        // çekilen veriler spBilgilere kaydedilecek destroy da null yapılacak.
        // önce masa qr bilgi, sonra işletme qr bilgileri alınacak.
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        if (isletmeID.equals("null")){
            Resources res = getResources();
            String toastMesaj = res.getString(R.string.MasalarIsletmeIDHatasi);
            Toast.makeText(getApplicationContext(), toastMesaj, Toast.LENGTH_LONG).show();
            return;
        }


        String url = "https://callbellapp.xyz/project/table1/all_table1_qr_code_find.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                masalarArrayList = new ArrayList<>();
                //Log.e("response",response);
                //Toast.makeText(getApplicationContext(),isletmeID,Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray qrCode = jsonObject.getJSONArray("tablo1");

                    for (int i = 0; i<qrCode.length(); i++ ) {

                        JSONObject q = qrCode.getJSONObject(i);

                        int qrId = q.getInt("qr_id");
                        String qrName = q.getString("qr_name");
                        String qrIsletme_id = q.getString("qr_isletme_id");

                        QrCode qrCodeWeb = new QrCode(qrId,qrName,qrIsletme_id);

                        masalarArrayList.add(qrCodeWeb);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter = new MasalarRVAdapter(MasalarActivity.this,masalarArrayList);
                rvMasalar.setAdapter(adapter);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("qr_isletme_id",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(MasalarActivity.this).add(stringRequest);


    }






}
