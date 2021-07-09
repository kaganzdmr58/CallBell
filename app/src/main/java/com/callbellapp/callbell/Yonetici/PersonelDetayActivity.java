package com.callbellapp.callbell.Yonetici;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import com.callbellapp.callbell.AyarlarActivity;
import com.callbellapp.callbell.GecmisYerlerActivity;
import com.callbellapp.callbell.GelenKutusuActivity;
import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.IsletmeSecimActivity;
import com.callbellapp.callbell.MainActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.ScannerActivity;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Sınıflar.PersonelFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class PersonelDetayActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private Button buttonKayit;
    private EditText editTextPersonelNot,editTextPersonelAdi,editTextPersonelTelefon,editTextPersonelGorev;
    private PersonelFirebase personel;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),PersonelActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel_detay);


        buttonKayit = findViewById(R.id.buttonKayit);
        editTextPersonelNot = findViewById(R.id.editTextPersonelNot);
        editTextPersonelTelefon = findViewById(R.id.editTextPersonelTelefon);
        editTextPersonelGorev = findViewById(R.id.editTextPersonelGorev);
        editTextPersonelAdi = findViewById(R.id.editTextPersonelAdi);


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


        personel =(PersonelFirebase) getIntent().getSerializableExtra("nesne");
        // Personel activity üzeirnden gelen personel nesnesi.

        editTextPersonelAdi.setText(personel.getPerfAdi() );
        editTextPersonelGorev.setText( personel.getPerfGorev());
        editTextPersonelTelefon.setText( personel.getPerfTel());
        editTextPersonelNot.setText(personel.getPerfNot());

        buttonKayit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                String PersonelBilgileriKayitEdildi  = res.getString(R.string.PersonelBilgileriKayitEdildi);

                final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRefIsletme = database.getReference("PersonelListesi").child(isletmeID);

                Map<String, Object> zilCalBilgi = new HashMap<>();
                zilCalBilgi.put("perfAdi", editTextPersonelAdi.getText().toString().trim());
                zilCalBilgi.put("perfGorev", editTextPersonelGorev.getText().toString().trim());
                zilCalBilgi.put("perfTel", editTextPersonelTelefon.getText().toString().trim());
                zilCalBilgi.put("perfNot", editTextPersonelNot.getText().toString().trim());
                myRefIsletme.child(personel.getPerfKey()).updateChildren(zilCalBilgi);
                Toast.makeText(getApplicationContext(),PersonelBilgileriKayitEdildi,Toast.LENGTH_LONG).show();
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

}
