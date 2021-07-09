package com.callbellapp.callbell;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
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


import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.IsletmeSecimActivity;
import com.callbellapp.callbell.Yonetici.YoneticiMainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class ProfilActivity extends AppCompatActivity {
    private ImageView imageViewResim,imageViewQrCode;
    private TextView textViewName,textViewEmail,textViewQrCode,textViewCikis;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private String name,email,password,ProfilKullaniciAdi,ProfilQRCodunuz;

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        startActivity(new Intent(ProfilActivity.this,AyarlarActivity.class));
        finish();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        // navigasyonlar
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


        // Görselleri bağlama
        imageViewResim = findViewById(R.id.imageViewResim);
        imageViewQrCode = findViewById(R.id.imageViewQrCode);
        textViewName = findViewById(R.id.textViewName);
        textViewEmail = findViewById(R.id.textViewEmail);

        textViewQrCode = findViewById(R.id.textViewQrCode);
        textViewCikis = findViewById(R.id.textViewCikis);


        Resources res = getResources();
        name  = res.getString(R.string.ProfilKullaniciAdi);
        email = res.getString(R.string.ProfilEmail);
        password = res.getString(R.string.ProfilPassword);
        ProfilKullaniciAdi = res.getString(R.string.ProfilKullaniciAdi);
        ProfilQRCodunuz = res.getString(R.string.ProfilQRCodunuz);


        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        user = mAuth.getCurrentUser();
        if (user != null) {
            textViewEmail.setText(email+user.getEmail());
            textViewName.setText(name+user.getDisplayName());
            textViewQrCode.setText(ProfilQRCodunuz+user.getUid());
        }

        final String PersonelID = StartActivity.spLogin.getString("PersonelID","null");
        qrCodeUret("callbell:"+"p:"+user.getUid()+":"+PersonelID);
        imageViewQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(toolbar,R.string.ProfilQrKoduKaydet,Snackbar.LENGTH_SHORT)
                        .setAction(R.string.Evet, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                QrCodeKaydet(R.string.ProfilOturumQrKodu+PersonelID);
                            }
                        }).show();
                return true;
            }
        });





        textViewName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                LayoutInflater inflater = LayoutInflater.from(ProfilActivity.this);
                View alert_tasarim = inflater.inflate(R.layout.masa_adi_alertview_tasarim, null);

                final EditText alert_edittext = (EditText) alert_tasarim.findViewById(R.id.alert_edittext);

                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(ProfilActivity.this);
                alertDialogOlusturucu.setMessage(R.string.ProfilKullanıcıAdi);
                alertDialogOlusturucu.setView(alert_tasarim);
                alert_edittext.setText(user.getDisplayName());
                alert_edittext.setHint(user.getDisplayName());

                alertDialogOlusturucu.setPositiveButton(R.string.Kaydet, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ProfilActivity.this,R.string.ProfilKullaniciAdiDegistirildi,Toast.LENGTH_SHORT).show();
                        firebaseKullaniciBilgileriGuncelle(String.valueOf(alert_edittext.getText()).trim());
                    }
                });
                alertDialogOlusturucu.setNegativeButton(R.string.Iptal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(ProfilActivity.this,R.string.ProfilKayitIptalEdildi,Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogOlusturucu.create().show();
            }
        });

        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),R.string.ProfilEMailGuncellemeYapılamaz,Toast.LENGTH_SHORT).show();
            }
        });



        textViewCikis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(ProfilActivity.this);
                alertDialogOlusturucu.setTitle(R.string.ProfilCikisYapAlertBaslik);
                alertDialogOlusturucu.setMessage(R.string.ProfilCikisYapmakIstiyormusunuz/*StartActivity.spLogin.getString("password", "şifre yok")*/);

                alertDialogOlusturucu.setPositiveButton(R.string.Evet, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                       cikisIslemleri();
                    }
                });
                alertDialogOlusturucu.setNegativeButton(R.string.Iptal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alertDialogOlusturucu.create().show();

            }
        });


    }

    public void cikisIslemleri(){

        mAuth.signOut();

        StartActivity.editorYonetici.putString("isletmeID","null");
        StartActivity.editorYonetici.commit();

        StartActivity.editorLogin.putBoolean("YONETICI_MODU",false);
        StartActivity.editorLogin.putBoolean("ISLETME_MODU",false);
        StartActivity.editorLogin.commit();

        StartActivity.editorScan.putBoolean("scanSayacDurum",false);
        StartActivity.editorScan.putInt("scanSayac",0);
        StartActivity.editorScan.commit();

        startActivity(new Intent(ProfilActivity.this, StartActivity.class));
        finish();
    }

    public void firebaseKullaniciBilgileriGuncelle(String name){
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            textViewName.setText(ProfilKullaniciAdi+user.getDisplayName());
                            Toast.makeText(ProfilActivity.this, R.string.ProfilKullaniciAdiDegistirildi, Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }
    public void qrCodeUret(String qr){
        MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
        try {
            BitMatrix bitMatrix =multiFormatWriter.encode(qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewQrCode.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }

    }
    private void QrCodeKaydet(String kayitAdi){
        OutputStream outputStream;
        try {
            BitmapDrawable drawable1 = (BitmapDrawable) imageViewQrCode.getDrawable();
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
