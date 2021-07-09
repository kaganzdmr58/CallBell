package com.callbellapp.callbell;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;


import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Sınıflar.QrCodeFirebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StartActivity extends AppCompatActivity {
    private Button buttonMain,buttonLogin,buttonKayit;
    public static SharedPreferences spLogin,spZilEtkin,spBilgiler,spYonetici,spScan;
    public static SharedPreferences.Editor editorLogin,editorZilEtkin,editorBilgiler,editorYonetici,editorScan;
    static FirebaseDatabase database;
    static DatabaseReference myRefIsletme;
    //public static String isletmeQrCode;
    public NotificationCompat.Builder builder;
    public static Boolean KULLANICI_ADI_GIZLE;
    private TextView textViewIsletmeQrCode;
    // Standart Yazılar
    static String MainZilCaldiYazisi;
    static String MainbutonHesapOtoYaziHesabiİstedi;
    static String MainButtonHesapOtoYaziMenuIstedi;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        // Initialize Firebase Auth
        FirebaseAuth mAuth =FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){

            Resources res = getResources();
            String MainOturumAcikDegil = res.getString(R.string.MainOturumAcikDegil);

            Toast.makeText(getApplicationContext(), MainOturumAcikDegil,Toast.LENGTH_LONG).show();
            startActivity(new Intent(StartActivity.this,LoginActivity.class));
            finish();
        }else {
            startActivity(new Intent(StartActivity.this,MainActivity.class));
            finish();
            // Toast.makeText(getApplicationContext(), "Oturum açık",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        buttonKayit = findViewById(R.id.buttonKayit);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonMain = findViewById(R.id.buttonMain);
        textViewIsletmeQrCode = findViewById(R.id.textViewIsletmeQrCode);

        // müşterinin bilgileri tuttuğu alan
        spBilgiler = getSharedPreferences("Bilgiler",MODE_PRIVATE);
        editorBilgiler = spBilgiler.edit();

        // işletme yöneticisinin bilgileri tuttuğu alan
        spYonetici = getSharedPreferences("YoneticiBilgiler",MODE_PRIVATE);
        editorYonetici = spYonetici.edit();

        // masa zil durumlarının kaydı
        spZilEtkin = getSharedPreferences("zil_Etkin",MODE_PRIVATE);
        editorZilEtkin = spZilEtkin.edit();

        // müşteri tarama bilgileri
        spScan = getSharedPreferences("scanner",MODE_PRIVATE);
        editorScan = spScan.edit();


        spLogin = getSharedPreferences("LoginBilgileri6",MODE_PRIVATE);
        editorLogin = spLogin.edit();

        //ISLETME_MODU = spLogin.getBoolean("ISLETME_MODU",false);

        KULLANICI_ADI_GIZLE = spBilgiler.getBoolean("KULLANICI_ADI_GIZLE",true);


        buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // yonetici modu aç
                StartActivity.editorLogin.putBoolean("YONETICI_MODU",true);
                StartActivity.editorLogin.commit();

                //startActivity(new Intent(getApplicationContext(), AyarlarActivity.class));
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // yonetici modu kapat
                StartActivity.editorLogin.putBoolean("YONETICI_MODU",false);
                StartActivity.editorLogin.commit();

                //startActivity(new Intent(getApplicationContext(), MasalarActivity.class));
            }
        });

        buttonMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }
        });



        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        //cihaz qr codu kullanıcı UID kodu ile eşleniyor böylece bütün cihazları senkronize etmiş oluyoruz.
        if (!isletmeID.equals("null")){   // işletme qr codu geçici değilse (işletmeye dahil ise)
            IsletmeFirebaseDinleme();
        }



        Resources res = getResources();
        MainZilCaldiYazisi  = res.getString(R.string.MainZilCaldiYazisi);
        MainbutonHesapOtoYaziHesabiİstedi= res.getString(R.string.MainbutonHesapOtoYaziHesabiİstedi);
        MainButtonHesapOtoYaziMenuIstedi = res.getString(R.string.MainButtonHesapOtoYaziMenuIstedi);


        //textViewIsletmeQrCode.setText(String.valueOf(isletmeID));

        // main ekranda gösterilecek alertviewin her açılışta bir defa çalışması için
        StartActivity.editorScan.putBoolean("scanSayacDurum",false);
        // taratılan işletme qr bilgileri sıfırlanıyor.
        StartActivity.editorScan.putString("isletmeScanID","gecici");
        StartActivity.editorScan.putString("masaScanID","gecici");
        // main ekrana yazdırma metodu 1 defa çalışması için
        StartActivity.editorScan.putString("mainYaz","yaz");
        // başlangıç değeri false oluyor
        StartActivity.editorScan.putBoolean("resimDurum",false);
        // zil sayaç zile basılma durmuna göre işletme hakkında oy vermeye yönlendirecek.
        StartActivity.editorScan.putInt("zilSayac",0);

        StartActivity.editorScan.commit();




    }

    public void IsletmeFirebaseDinleme(){
        // sürekli olarak dinlenilen Cihaz dinleme noktası
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        database = FirebaseDatabase.getInstance();
        myRefIsletme = database.getReference("QrCodeListesi").child(isletmeID);

        myRefIsletme.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                QrCodeFirebase masaZil = dataSnapshot.getValue(QrCodeFirebase.class);

                String qrfKey = dataSnapshot.getKey();
                String qrfMesaj = masaZil.getQrfMesaj();
                String qrfZaman = masaZil.getQrfZaman();
                String qrfAdi = masaZil.getQrfAdi();

                gelenCagriSorgula(qrfMesaj,qrfKey,qrfZaman,qrfAdi);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



    public void gelenCagriSorgula(String gelenIstekMesaj1,String qrfKey,String qrfZaman,String qrfAdi){
        String gelenIstekMesaj = "";

        if (gelenIstekMesaj1.equals("ZIL_CALDI_KOD_TRUE")){
            gelenIstekMesaj =MainZilCaldiYazisi;
        }else if (gelenIstekMesaj1.equals("ZIL_CALDI_KOD_FALSE")) {
            gelenIstekMesaj = MainZilCaldiYazisi;
        }else if (gelenIstekMesaj1.equals("MENU_ISTEDI_KOD")){
            gelenIstekMesaj = MainButtonHesapOtoYaziMenuIstedi;
        }else if (gelenIstekMesaj1.equals("HESAP_ISTEDI_KOD")){
            gelenIstekMesaj =  MainbutonHesapOtoYaziHesabiİstedi;
        }else {
            gelenIstekMesaj = gelenIstekMesaj1;
        }

        Boolean zilEtkin = spZilEtkin.getBoolean(qrfKey, true);
        if (zilEtkin) {
            Toast.makeText(getApplicationContext(),qrfAdi+" : "+gelenIstekMesaj,Toast.LENGTH_LONG).show();
            bildirimOlusturma(qrfAdi+" : "+gelenIstekMesaj);
        }


    }






    public void bildirimOlusturma(String gelenMesaj){
        NotificationManager bildirimYoneticisi =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, CagrilarActivity.class);

        PendingIntent gidilecekIntent = PendingIntent.getActivity(this
                ,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        long[] pattern = {500,500,500};

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            String kanalId = "kanalId";
            String kanalAd = "kanalAd";
            String kanalTanım = "kanalTanım";
            int kanalOnceligi = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel kanal = bildirimYoneticisi.getNotificationChannel(kanalId);

            if(kanal==null){
                kanal = new NotificationChannel(kanalId,kanalAd,kanalOnceligi);
                kanal.setDescription(kanalTanım);
                bildirimYoneticisi.createNotificationChannel(kanal);
            }

            builder = new NotificationCompat.Builder(this,kanalId);

            builder.setContentTitle("CallBell")
                    .setContentText(gelenMesaj)
                    .setSmallIcon(R.drawable.room_service_red_48dp)
                    .setAutoCancel(true)
                    .setVibrate(pattern)
                    .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.chime))
                    .setContentIntent(gidilecekIntent);

        }else {

            builder = new NotificationCompat.Builder(this);

            builder.setContentTitle("CallBell")
                    .setContentText(gelenMesaj)
                    .setSmallIcon(R.drawable.room_service_red_48dp)
                    .setAutoCancel(true)
                    .setSound(Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.chime))
                    .setVibrate(pattern)
                    .setContentIntent(gidilecekIntent);

        }

        bildirimYoneticisi.notify(1,builder.build());
    }







}
