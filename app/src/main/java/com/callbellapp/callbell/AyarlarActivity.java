package com.callbellapp.callbell;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.callbellapp.callbell.Alarm.AlarmReceiver;
import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.IsletmeSecimActivity;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class AyarlarActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private Button buttonProfilGecis;
    private Switch switchKullaniciAdiniGizle,switchIsletmeModu;
    private TextView textViewIsletmeKur,textViewAd,textViewEmail,textViewGorusOneri
            ,textViewInfo,textViewBildirimSaatiYazi,textViewBildirimSaati;
    private ImageView imageViewProfil;

    private FirebaseAuth mAuth;
    private FirebaseUser user;


    //bildirm saati işlemleri
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ayarlar);

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

        //Görselleri bağlama
        switchKullaniciAdiniGizle = findViewById(R.id.switchKullaniciAdiniGizle);
        switchIsletmeModu = findViewById(R.id.switchIsletmeModu);
        textViewIsletmeKur = findViewById(R.id.textViewIsletmeKur);
        textViewInfo = findViewById(R.id.textViewInfo);
        textViewAd = findViewById(R.id.textViewAd);
        textViewEmail = findViewById(R.id.textViewEmail);
        textViewGorusOneri = findViewById(R.id.textViewGorusOneri);
        imageViewProfil = findViewById(R.id.imageViewProfil);
        textViewBildirimSaatiYazi = findViewById(R.id.textViewBildirimSaatiYazi);
        textViewBildirimSaati = findViewById(R.id.textViewBildirimSaati);
        buttonProfilGecis = findViewById(R.id.buttonProfilGecis);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        if (user != null) {
            textViewEmail.setText(user.getEmail());
            textViewAd.setText(user.getDisplayName());
        }

        textViewAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),ProfilActivity.class));
                finish();
            }
        });
        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),ProfilActivity.class));
                finish();
            }
        });
        imageViewProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),ProfilActivity.class));
                finish();
            }
        });
        buttonProfilGecis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),ProfilActivity.class));
                finish();
            }
        });

        Boolean KULLANICI_ADI_GOSTER = StartActivity.spBilgiler.getBoolean("KULLANICI_ADI_GOSTER",true);
        switchKullaniciAdiniGizle.setChecked(KULLANICI_ADI_GOSTER);
        switchKullaniciAdiniGizle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                StartActivity.editorBilgiler.putBoolean("KULLANICI_ADI_GOSTER",b);
                StartActivity.editorBilgiler.commit();
                startActivity(new Intent(getApplicationContext(),AyarlarActivity.class));
                finish();
            }
        });

        Boolean ISLETME_MODU = StartActivity.spLogin.getBoolean("ISLETME_MODU",false);
        switchIsletmeModu.setChecked(ISLETME_MODU);
        switchIsletmeModu.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
                    if(isletmeID.equals("null")){
                        // herhangi bir işletmeye dahil değilsek.
                        textViewIsletmeKur.setVisibility(View.VISIBLE);
                    }
                }else{
                    textViewIsletmeKur.setVisibility(View.INVISIBLE);
                }
                StartActivity.editorLogin.putBoolean("ISLETME_MODU",b);
                StartActivity.editorLogin.commit();
                StartActivity.editorScan.putBoolean("resimDurum",b);
                StartActivity.editorScan.commit();
                startActivity(new Intent(getApplicationContext(),AyarlarActivity.class));
                finish();
            }
        });
        if (ISLETME_MODU){
            String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
            if(isletmeID.equals("null")){
                // herhangi bir işletmeye dahil değilsek.
                textViewIsletmeKur.setVisibility(View.VISIBLE);
            }
        }else{
            textViewIsletmeKur.setVisibility(View.INVISIBLE);
        }

        textViewIsletmeKur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), IsletmeSecimActivity.class));
                finish();
            }
        });




        textViewGorusOneri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),IstekMesajActivity.class));
                finish();
            }
        });


        // bildirim saati işlemleri.
        textViewBildirimSaati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaatAyarla();
            }
        });

        textViewBildirimSaatiYazi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaatAyarla();
            }
        });


        String bildirimSaati = StartActivity.spLogin.getString("bildirimSaati","12:00");
        String[] saatDakika = bildirimSaati.split(":");
        String saat = saatDakika[0];
        String dakika = saatDakika[1];
        if ((saat.length())==1){
            saat = "0" + saat;
        }
        if ((dakika.length())==1){
            dakika = "0" + dakika;
        }
        textViewBildirimSaati.setText(saat+":"+dakika);



        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        final Intent alarmIntent = new Intent(AyarlarActivity.this, AlarmReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(AyarlarActivity.this
                ,0,alarmIntent,0);


        textViewInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                String AyarlarMerhaba = res.getString(R.string.AyarlarMerhaba);
                String AyarlarBilgilendirmeMetni = res.getString(R.string.AyarlarBilgilendirmeMetni);
                String AyarlarUygulamaHakkındaKısaBilgilendirme = res.getString(R.string.AyarlarUygulamaHakkındaKısaBilgilendirme);
                String Tamam = res.getString(R.string.Tamam);

                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(AyarlarActivity.this);
                alertDialogOlusturucu.setIcon(R.drawable.room_service_red_48dp);
                alertDialogOlusturucu.setTitle(AyarlarUygulamaHakkındaKısaBilgilendirme);
                alertDialogOlusturucu.setMessage(AyarlarMerhaba+user.getDisplayName()+AyarlarBilgilendirmeMetni);

                alertDialogOlusturucu.setPositiveButton(Tamam, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                alertDialogOlusturucu.create().show();
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

    public void SaatAyarla(){
        Calendar calendar = Calendar.getInstance();
        final int saat = calendar.get(Calendar.HOUR_OF_DAY);
        final int dakika = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePicker;

        timePicker = new TimePickerDialog(AyarlarActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String saat = String.valueOf(i),dakika=String.valueOf(i1);
                if ((saat.length())==1){
                    saat = "0" + saat;
                }
                if ((dakika.length())==1){
                    dakika = "0" + dakika;
                }

                textViewBildirimSaati.setText(saat+" : "+dakika);
                StartActivity.editorLogin.putString("bildirimSaati",i+":"+i1);
                StartActivity.editorLogin.commit();

                // Alarm çalışması ayarlanması
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(Calendar.HOUR_OF_DAY,i);
                calendar.set(Calendar.MINUTE,i1);
                calendar.set(Calendar.SECOND,0);

                long neZamanTetiklenecek = calendar.getTimeInMillis();
                long tekrarlamaSuresi = 1000*60*1;  // 1 dk   tekrarlama istemiyorsak sıfır yapıyoruz.

                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP
                        ,neZamanTetiklenecek
                        ,alarmManager.INTERVAL_DAY  // günün aynı saatinde çalışır.
                        ,pendingIntent);

                Resources res = getResources();
                String toastMesaj = res.getString(R.string.AyarlarAlarmAyarlandı);
                Toast.makeText(getApplicationContext(),toastMesaj,Toast.LENGTH_LONG).show();

            }
        },saat,dakika,true);

        Resources res = getResources();
        String AyarlarSaatSeciniz = res.getString(R.string.AyarlarSaatSeciniz);
        String AyarlarAyarla = res.getString(R.string.AyarlarAyarla);
        String Iptal = res.getString(R.string.Iptal);

        timePicker.setTitle(AyarlarSaatSeciniz);
        timePicker.setButton(DialogInterface.BUTTON_POSITIVE,AyarlarAyarla,timePicker);
        timePicker.setButton(DialogInterface.BUTTON_NEGATIVE,Iptal,timePicker);
        timePicker.show();
    }


}
