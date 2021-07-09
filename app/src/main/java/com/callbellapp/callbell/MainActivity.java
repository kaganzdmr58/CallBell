package com.callbellapp.callbell;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.IsletmeSecimActivity;
import com.callbellapp.callbell.MainMenuAuto.SliderAdapter;
import com.callbellapp.callbell.MainMenuAuto.SliderItem;

import com.callbellapp.callbell.MenuGoster.MenuGosterActivity;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button buttonZil,buttonMenu,buttonSparisGonder;
    private TextView textViewShow;
    public static EditText editTextSparisGiris;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    public static Resources res;
    static FirebaseDatabase database;
    static DatabaseReference myRefIsletme;
    private  String kullaniciAdi,MainCallBellHosgeldiniz,MainQrTaratilmadigiIcin,MainBilgiler
            ,isletmeID,MainGarsonCagirAlertMesaj,MainScanr,MainQrKoduTara,isletmeScanID,resimAlIsletmeID;
    private boolean zilCalmaDurum=true, scanSayacDurum;
    final List<SliderItem> sliderItems = new ArrayList<>();

    private ViewPager2 viewPager2;
    private Handler sliderHandler= new Handler();
    private Integer scannerSayac = 0,zilSayac;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        moveTaskToBack(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!internetKontrol()){ //internet kontrol methodu çağırılıyor
            //Toast.makeText(getApplicationContext(), "İnternet Bağlı!", Toast.LENGTH_LONG).show();
            textViewShow.setVisibility(View.VISIBLE);
            viewPager2.setVisibility(View.INVISIBLE);
        }/*else{
            //Toast.makeText(getApplicationContext(), "İnternet Yok!", Toast.LENGTH_LONG).show();

        }*/
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editTextSparisGiris = findViewById(R.id.editTextOrderEntery);
        textViewShow = findViewById(R.id.textViewShow);
        buttonZil = findViewById(R.id.buttonBell);
        buttonMenu = findViewById(R.id.buttonMenuAndAccount);
        buttonSparisGonder = findViewById(R.id.buttonOrderSend);
        viewPager2 = findViewById(R.id.viewPagerImageSlider);
        //textViewIsletmeAdi = findViewById(R.id.textViewIsletmeAdi);


        // textview scroll ederek kaydırma ekliyoruz
        textViewShow.setMovementMethod(new ScrollingMovementMethod());

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setBackgroundColor(getResources().getColor(R.color.toolbarArkaPlanRengi));
        toolbar.setTitleTextColor(getResources().getColor(R.color.butonTextColor));
        // toolbar.setSubtitleTextColor(getResources().getColor(R.color.butonTextColor));
        toolbar.setLogo(R.drawable.room_service_red_48dp);
        setSupportActionBar(toolbar);

        //Navigasyonlar
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    /*case R.id.action_birinci:
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                        finish();
                        break;*/
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

        isletmeID = StartActivity.spYonetici.getString("isletmeID","null");


        // ADMOB banner
        MobileAds.initialize(this,"ca-app-pub-2183039164562504~2945725091");
        //cihaz admob ID
        final AdView banner = findViewById(R.id.banner);
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



        //ResimTextViewDonusumu ();

        res = getResources();
        MainCallBellHosgeldiniz = res.getString(R.string.MainCallBellHosgeldiniz);
        MainQrTaratilmadigiIcin = res.getString(R.string.MainQrTaratilmadigiIcin);
        MainBilgiler = res.getString(R.string.MainBilgiler);
        MainGarsonCagirAlertMesaj = res.getString(R.string.MainGarsonCagirAlertMesaj);
        MainScanr = res.getString(R.string.MainScanr);
        MainQrKoduTara = res.getString(R.string.MainQrKoduTara);



        textViewShow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(textViewShow,R.string.MainIcerikSilinsinMi,Snackbar.LENGTH_SHORT).setAction(R.string.Evet, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        StartActivity.editorBilgiler.putString("MainEkranaYazılan","");
                        StartActivity.editorBilgiler.commit();
                        textViewShow.setText("");
                        Snackbar.make(textViewShow,R.string.MainIcerikSilindi,Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
                return false;
            }
        });

        textViewShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // textview scroll ederek kaydırma ekliyoruz
                textViewShow.setMovementMethod(new ScrollingMovementMethod());
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

        buttonZil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity.editorScan.putInt("zilSayac",(zilSayac+1));
                StartActivity.editorScan.commit();
                zilSayac += 1;


                // butonun rengini normale dönderiyoruz.
                //buttonSparisGonder.setTextColor(getResources().getColor(R.color.butonTextColor));
                buttonSparisGonder.setBackground(getResources().getDrawable(R.drawable.buttonshape_3));

                Resources res = getResources();
                String MainZilCaldiYazisi  = res.getString(R.string.MainZilCaldiYazisi);
                //ZIL_CALDI_KOD,MENU_ISTEDI_KOD,HESAP_ISTEDI_KOD;
                if (zilCalmaDurum){
                    gidecekMesaj("ZIL_CALDI_KOD_TRUE");
                    zilCalmaDurum =false;
                }else {
                    gidecekMesaj("ZIL_CALDI_KOD_FALSE");
                    zilCalmaDurum = true;
                }

                //  Ekran Klavyesi kapatma komutu
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSparisGiris.getWindowToken(), 0);
                // editText içeriğini temizliyoruz.
                editTextSparisGiris.setText("");

                mainResimAc();


            }
        });

        buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // qr bilgileri düzenle if ile doluluk şartı koy.
                isletmeScanID = StartActivity.spScan.getString("isletmeScanID","gecici");
                Boolean ISLETME_MODU = StartActivity.spLogin.getBoolean("ISLETME_MODU",false);
                if (ISLETME_MODU == true){
                    isletmeScanID = isletmeID;
                }

                //  Ekran Klavyesi kapatma komutu
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSparisGiris.getWindowToken(), 0);
                // editText içeriğini temizliyoruz.
                editTextSparisGiris.setText("");

                //butonun rengini normale dönderiyoruz.
                //buttonSparisGonder.setTextColor(getResources().getColor(R.color.butonTextColor));
                buttonSparisGonder.setBackground(getResources().getDrawable(R.drawable.buttonshape_3));


                StartActivity.editorScan.putInt("zilSayac",(zilSayac+1));
                StartActivity.editorScan.commit();
                zilSayac += 1;

                if (isletmeScanID.equals("gecici")){
                    Toast.makeText(getApplicationContext(),MainQrTaratilmadigiIcin,Toast.LENGTH_SHORT).show();
                    mainResimAc();
                    return;
                }else {
                    Intent intent = new Intent(getApplicationContext(), MenuGosterActivity.class);
                    intent.putExtra("gecisID",isletmeScanID);
                    startActivity(intent);
                }

            }
        });

        buttonSparisGonder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // gidecek mesaj metodunu çalıştırıyıruz.
                gidecekMesaj(editTextSparisGiris.getText().toString().trim());
                // butonun rengini normale dönderiyoruz.
                //buttonSparisGonder.setTextColor(getResources().getColor(R.color.butonTextColor));
                buttonSparisGonder.setBackground(getResources().getDrawable(R.drawable.buttonshape_3));
                //  Ekran Klavyesi kapatma komutu
                InputMethodManager imm = (InputMethodManager) getSystemService(getApplicationContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editTextSparisGiris.getWindowToken(), 0);
                // editText içeriğini temizliyoruz.
                editTextSparisGiris.setText("");

                StartActivity.editorScan.putInt("zilSayac",(zilSayac+1));
                StartActivity.editorScan.commit();
                zilSayac += 1;

                mainResimAc();
            }
        });


        editTextSparisGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // buraya gönder butonunun yazını kırmızı yapma.
//                buttonSparisGonder.setTextColor(getResources().getColor(R.color.colorAccent));
                buttonSparisGonder.setBackground(getResources().getDrawable(R.drawable.buttonshape_main));
                textViewShow.setVisibility(View.INVISIBLE);
                viewPager2.setVisibility(View.INVISIBLE);

                mainResimAc();
            }
        });




        String mainYaz = StartActivity.spScan.getString("mainYaz","yaz");
        if (mainYaz.equals("yaz")){
            MasaBilgileriAlma();
        }



        // kayan resimler

        // qr bilgileri düzenle if ile doluluk şartı koy.
        resimAlIsletmeID = res.getString(R.string.resimAlIsletmeID);

        isletmeScanID = StartActivity.spScan.getString("isletmeScanID","gecici");
        Boolean ISLETME_MODU = StartActivity.spLogin.getBoolean("ISLETME_MODU",false);


        if (!(isletmeScanID.equals("gecici"))){
            resimAlIsletmeID = isletmeScanID;
        }

        if (ISLETME_MODU == true){
            resimAlIsletmeID = isletmeID;
        }

        mainResimAc();



        zilSayac = StartActivity.spScan.getInt("zilSayac",0);
        if ((zilSayac >= 5) && (zilSayac <= 10)){
            GorusOneriSorgusuAlert();
        }



        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1-Math.abs(position);
                page.setScaleY(0.85f + r*0.15f);
            }
        });

        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable,5000);
            }
        });

        Resources res = getResources();
        String MainSparisMerhaba = res.getString(R.string.MainSparisMerhaba);
        String MainSparisVermekIcin = res.getString(R.string.MainSparisVermekIcin);
        String MainSparisVermekIcin2 = res.getString(R.string.MainSparisVermekIcin2);

        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();
        if (isletmeScanID.equals("gecici")){
            editTextSparisGiris.setHint(MainSparisMerhaba+user.getDisplayName()+MainSparisVermekIcin);
        }else {
            editTextSparisGiris.setHint(MainSparisMerhaba+user.getDisplayName()+MainSparisVermekIcin2);
        }





    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable,5000);
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



    public void mainResimAc(){
        // görselleri göster
        textViewShow.setVisibility(View.INVISIBLE);
        viewPager2.setVisibility(View.VISIBLE);
        //Toast.makeText(MainActivity.this, "Resim modu açıldı.", Toast.LENGTH_SHORT).show();
        //imageViewIcon.setVisibility(View.VISIBLE);
        MenuResimleriAlma();
    }


    public void gidecekMesaj(final String mesaj){

        // qr bilgileri düzenle if ile doluluk şartı koy.
        final String isletmeScanID = StartActivity.spScan.getString("isletmeScanID","gecici");
        final String masaScanID = StartActivity.spScan.getString("masaScanID","gecici");
        final String PerseonelID =StartActivity.spLogin.getString("PersonelID","8");

        if (isletmeScanID.equals("gecici")){
            Toast.makeText(getApplicationContext(),MainQrTaratilmadigiIcin,Toast.LENGTH_SHORT).show();
            return;
        }
/*
        // Initialize Firebase Auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        Boolean KULLANICI_ADI_GOSTER = StartActivity.spBilgiler.getBoolean("KULLANICI_ADI_GOSTER",true);
        if (KULLANICI_ADI_GOSTER){
            kullaniciAdi = " / "+user.getDisplayName();
        }else {
            kullaniciAdi = "";
        }

        final String mesaj = mesaj1 +kullaniciAdi;
*/

        Calendar calendar = Calendar.getInstance();
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        if ((hour.length())==1){hour = "0" + hour; }
        if ((minute.length())==1){minute = "0" + minute; }
        final String bildirimSaati = hour +" : "+ minute;

        database = FirebaseDatabase.getInstance();
        myRefIsletme = database.getReference("QrCodeListesi").child(isletmeScanID);

        Map<String, Object> zilCalBilgi = new HashMap<>();
        zilCalBilgi.put("qrfMesaj", mesaj);
        zilCalBilgi.put("qrfZaman", bildirimSaati);
        myRefIsletme.child(masaScanID).updateChildren(zilCalBilgi);

        // işletmenin web gecmis cagrilar tablosuna ekleme yapılacak.

        String url = "https://callbellapp.xyz/project/table3/insert_table3_call.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(MainActivity.this, R.string.MainCagrinizIletildi, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("ca_isletme_id",isletmeScanID);
                params.put("ca_masa_id",masaScanID);
                params.put("ca_musteri_id",PerseonelID);
                params.put("ca_mesaj",mesaj);
                params.put("ca_zaman",bildirimSaati);
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);

    }


    public void MasaBilgileriAlma(){
        // qr bilgileri düzenle if ile doluluk şartı koy.
        final String isletmeScanID = StartActivity.spScan.getString("isletmeScanID","gecici");
        final String masaScanID = StartActivity.spScan.getString("masaScanID","gecici");

        if (masaScanID.equals("gecici")){
            //Toast.makeText(getApplicationContext(),"İşletme qr codu taratılmadığı için işlemi gerçekleştiremiyoruz.",Toast.LENGTH_SHORT).show();
            return;
        }
        // işletmenin web gecmis cagrilar tablosuna ekleme yapılacak.

        String url = "https://callbellapp.xyz/project/table1/all_table1_qr_code_find_customer.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                //Log.e("response",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray qrCode = jsonObject.getJSONArray("tablo1");

                    for (int i = 0; i<qrCode.length(); i++ ) {


                        JSONObject q = qrCode.getJSONObject(i);
                        int qrId = q.getInt("qr_id");
                        String qrName = q.getString("qr_name");
                        //String qrIsletme_id = q.getString("qr_isletme_id");

                        JSONObject is = q.getJSONObject("qr_isletme_id");

                        String isName = is.getString("is_name");
                        String isBilgi = is.getString("is_bilgi");
                        String is_kampanya = is.getString("is_kampanya");
                        String isIcon = is.getString("is_icon_url");

                        //IsletmeBilgi qrCodeWeb = new IsletmeBilgi(0,isName,"null",isBilgi,is_kampanya,"null");


                        StartActivity.editorScan.putString("mainYaz","bitti");
                        StartActivity.editorScan.putString("qrName",qrName);
                        StartActivity.editorScan.commit();


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("qr_id",masaScanID);
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);


    }





    private void GorusOneriSorgusuAlert(){

        StartActivity.editorScan.putInt("zilSayac",(zilSayac+10));
        StartActivity.editorScan.commit();
        zilSayac += 10;


        Resources res = getResources();
        String MainGorusOneriMesaj = res.getString(R.string.MainGorusOneriMesaj);
        String MainGorusOneri = res.getString(R.string.MainGorusOneri);
        String Evet = res.getString(R.string.Evet);
        String Hayır = res.getString(R.string.Hayır);

        AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(MainActivity.this);
        alertDialogOlusturucu.setMessage(MainGorusOneriMesaj);
        alertDialogOlusturucu.setIcon(R.drawable.room_service_red_48dp);
        alertDialogOlusturucu.setTitle(MainGorusOneri);

        alertDialogOlusturucu.setPositiveButton(Evet, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                startActivity(new Intent(getApplicationContext(), IstekMesajActivity.class));
                finish();
            }
        });
        alertDialogOlusturucu.setNegativeButton(Hayır, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialogOlusturucu.create().show();

    }


    public void MenuResimleriAlma(){

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

                        if ((res_tur == 2) && (!(res_url.isEmpty()))){
                            sliderItems.add(new SliderItem(res_url));
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                viewPager2.setAdapter(new SliderAdapter(sliderItems,viewPager2));

               if (sliderItems.size()==0){
                   resimAlIsletmeID = res.getString(R.string.resimAlIsletmeID);
                   mainResimAc();
               }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("res_isletme",resimAlIsletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(MainActivity.this).add(stringRequest);


    }






    protected boolean internetKontrol() { //interneti kontrol eden method
        // TODO Auto-generated method stub
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

}




// manifest dosyasına android:windowSoftInputMode="stateAlwaysHidden" ile klavyenin açılması sorununu çözdük.