package com.callbellapp.callbell.Yonetici;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


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
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PersonelActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView rvPersonel;
    private FloatingActionButton fabPersonelEkle;
    private ArrayList<PersonelFirebase> personelArrayListe= new ArrayList<>();
    public static PersonelRVAdapter adapter;
    private ImageView imageViewYenile;
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),YoneticiMainActivity.class));
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personel);

        rvPersonel = findViewById(R.id.rvPersonel);
        fabPersonelEkle = findViewById(R.id.fabPersonelEkle);
        imageViewYenile = findViewById(R.id.imageViewYenile);

        rvPersonel.setHasFixedSize(true);
        rvPersonel.setLayoutManager(new LinearLayoutManager(this));

        perosnelListesiAlmaFirebase();


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


        fabPersonelEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Resources res = getResources();
                String PersonelEkleme  = res.getString(R.string.PersonelEkleme);
                String PersonelEklemeAciklama  = res.getString(R.string.PersonelEklemeAciklama);

                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(PersonelActivity.this);
                alertDialogOlusturucu.setTitle(PersonelEkleme);
                alertDialogOlusturucu.setMessage(PersonelEklemeAciklama);

                alertDialogOlusturucu.setPositiveButton(R.string.Tamam, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialogOlusturucu.create().show();
            }
        });

        imageViewYenile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PersonelActivity.class));
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

    public void perosnelListesiAlmaFirebase(){
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        //personelArrayListe = new ArrayList<>();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRefPersonel = database.getReference("PersonelListesi").child(isletmeID);

        myRefPersonel.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                PersonelFirebase personel = dataSnapshot.getValue(PersonelFirebase.class);

                String perfKey = dataSnapshot.getKey();  // kayıtlı personelin UID si
                String perfAdi = personel.getPerfAdi();
                String perfGorev = personel.getPerfGorev();
                String perfTel = personel.getPerfTel();
                String perfNot =personel.getPerfNot();

                PersonelFirebase personel1 =new PersonelFirebase(perfKey,perfAdi,perfGorev,perfTel,perfNot);

                personelArrayListe.add(personel1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

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
        adapter = new PersonelRVAdapter(this,personelArrayListe);
        rvPersonel.setAdapter(adapter);
    }

}
