package com.callbellapp.callbell.Yonetici;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import com.callbellapp.callbell.Isletme.IsletmeSecimActivity;
import com.callbellapp.callbell.IsletmeMesajlariActivity;
import com.callbellapp.callbell.MainActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.ScannerActivity;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Sınıflar.IsletmeBilgi;
import com.callbellapp.callbell.Sınıflar.PersonelFirebase;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
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

public class YoneticiMainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ImageView imageViewIsletmeQrCode;
    private String isletmeAdi,isletmeBilgi,kampanyaYazisi,isletmeID;
    private TextView textViewIsletmeAdi;
    private Button buttonGunlukKampanyalarım,buttonSikayet,buttonIsletme,buttonPersonel,buttonKampanya,buttonMenuResim;
    private ArrayList<PersonelFirebase> personelArrayListe;
    private IsletmeBilgi isletme;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yonetici_main);

        imageViewIsletmeQrCode = findViewById(R.id.imageViewIsletmeQrCode);
        textViewIsletmeAdi = findViewById(R.id.textViewTarihSaat);
        buttonGunlukKampanyalarım = findViewById(R.id.buttonGunlukKampanyalarım);
        buttonSikayet = findViewById(R.id.buttonSikayet);
        buttonIsletme = findViewById(R.id.buttonIsletme);
        buttonPersonel = findViewById(R.id.buttonPersonel);
        buttonKampanya = findViewById(R.id.buttonKampanyaResimler);
        buttonMenuResim = findViewById(R.id.buttonMenuResimler);




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

        // işletme qr code
        isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        String isletmeQRCode = StartActivity.spYonetici.getString("isletmeQRCode","null");
        qrCodeUret(isletmeQRCode);
        imageViewIsletmeQrCode.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Snackbar.make(toolbar,R.string.ProfilQrKoduKaydet,Snackbar.LENGTH_SHORT)
                        .setAction(R.string.Evet, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                QrCodeKaydet(R.string.IsletmeQrKodu+isletmeID);
                            }
                        }).show();
                return false;
            }
        });
        IsletmeBilgileriAl();


        buttonPersonel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //perosnelListesiAlmaFirebase();
                startActivity(new Intent(getApplicationContext(),PersonelActivity.class));
                finish();
            }
        });

        buttonGunlukKampanyalarım.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //işletme menüsüne gönderilecek
                Intent intent = new Intent(getApplicationContext(), IsletmeMesajlariActivity.class);
                intent.putExtra("nesne",isletme);
                startActivity(intent);
                finish();
            }
        });


        buttonSikayet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DilekVeSikayetlerActivity.class));
                finish();
            }
        });



        textViewIsletmeAdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                String ProfilKullanıcıAdi = res.getString(R.string.ProfilKullanıcıAdi);
                String Kaydet = res.getString(R.string.Kaydet);
                String Iptal = res.getString(R.string.Iptal);
                final String ProfilKayitIptalEdildi = res.getString(R.string.ProfilKayitIptalEdildi);

                LayoutInflater inflater = LayoutInflater.from(YoneticiMainActivity.this);
                View alert_tasarim = inflater.inflate(R.layout.masa_adi_alertview_tasarim, null);

                final EditText alert_edittext = (EditText) alert_tasarim.findViewById(R.id.alert_edittext);

                AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(YoneticiMainActivity.this);
                alertDialogOlusturucu.setMessage(ProfilKullanıcıAdi);
                alertDialogOlusturucu.setView(alert_tasarim);
                alert_edittext.setText(isletmeAdi);
                alert_edittext.setHint(isletmeAdi);

                alertDialogOlusturucu.setPositiveButton(Kaydet, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IsletmeAdiDegistirme(alert_edittext.getText().toString().trim());

                    }
                });
                alertDialogOlusturucu.setNegativeButton(Iptal, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(YoneticiMainActivity.this,ProfilKayitIptalEdildi,Toast.LENGTH_SHORT).show();
                    }
                });
                alertDialogOlusturucu.create().show();
            }
        });






        buttonIsletme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), IsletmeBilgilerActivity.class));
                finish();
            }
        });


        buttonMenuResim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MenuResimEkleActivity.class));
                finish();
            }
        });

        buttonKampanya.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), KampanyaResimEkleActivity.class));
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

    public void qrCodeUret(String qr){
        MultiFormatWriter multiFormatWriter =new MultiFormatWriter();
        try {
            BitMatrix bitMatrix =multiFormatWriter.encode(qr, BarcodeFormat.QR_CODE,200,200);
            BarcodeEncoder barcodeEncoder =new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            imageViewIsletmeQrCode.setImageBitmap(bitmap);
        }catch (WriterException e){
            e.printStackTrace();
        }
    }
    private void QrCodeKaydet(String kayitAdi){
        OutputStream outputStream;
        try {
            BitmapDrawable drawable1 = (BitmapDrawable) imageViewIsletmeQrCode.getDrawable();
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


    private void IsletmeAdiDegistirme(final String yazi){
        String url = "https://callbellapp.xyz/project/table2/update_table2_business_name_change.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);

                Resources res = getResources();
                String ProfilKullaniciAdiDegistirildi  = res.getString(R.string.ProfilKullaniciAdiDegistirildi);
                Toast.makeText(YoneticiMainActivity.this,ProfilKullaniciAdiDegistirildi,Toast.LENGTH_SHORT).show();

                startActivity(new Intent(YoneticiMainActivity.this,YoneticiMainActivity.class));
                finish();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("is_name",yazi);
                params.put("is_id",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(YoneticiMainActivity.this).add(stringRequest);
    }


    public void perosnelListesiAlmaFirebase(){
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        personelArrayListe = new ArrayList<>();
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
                /*PersonelFirebase personel = dataSnapshot.getValue(PersonelFirebase.class);

                String perfKey = dataSnapshot.getKey();  // kayıtlı personelin UID si
                String perfAdi = personel.getPerfAdi();
                String perfGorev = personel.getPerfGorev();
                String perfTel = personel.getPerfTel();
                String perfNot =personel.getPerfNot();

                PersonelFirebase personel1 =new PersonelFirebase(perfKey,perfAdi,perfGorev,perfTel,perfNot);

                personelArrayListe.add(personel1);
*/
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
        Intent intent = new Intent(getApplicationContext(),PersonelActivity.class);
        intent.putExtra("array",personelArrayListe);
        startActivity(intent);
        finish();
    }

    private void IsletmeBilgileriAl(){
        Resources res = getResources();
        final String YoneticiIndirimKodu  = res.getString(R.string.YoneticiIndirimKodu);
        final String YoneticiİsletmeBilgiler  = res.getString(R.string.YoneticiİsletmeBilgiler);
        final String YoneticiMainİsletmeAdi  = res.getString(R.string.YoneticiMainİsletmeAdi);


        Log.e("response isletme ID",isletmeID);
        String url = "https://callbellapp.xyz/project/table2/all_table2_business_info_find.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray qrCode = jsonObject.getJSONArray("tablo2");

                    for (int i = 0; i<qrCode.length(); i++ ) {

                        JSONObject q = qrCode.getJSONObject(i);

                        int isId = q.getInt("is_id");
                        String isName = q.getString("is_name");
                        String isYonetciID = q.getString("is_yonetici_id");
                        String isSlogan = q.getString("is_slogan");
                        String is_tel = q.getString("is_tel");
                        String is_email = q.getString("is_email");
                        String is_web = q.getString("is_web");
                        String is_adres = q.getString("is_adres");
                        String isIcon = q.getString("is_icon_url");


                        kampanyaYazisi = isSlogan;
                        isletmeBilgi = is_tel;
                        textViewIsletmeAdi.setText(isName);
                        isletmeAdi = isName;

                        if (isName.isEmpty()){
                            textViewIsletmeAdi.setText(YoneticiMainİsletmeAdi);
                            textViewIsletmeAdi.setTextSize(14.0f);
                        }


                        isletme = new IsletmeBilgi(isId,isName,isYonetciID,isSlogan,is_tel,is_email,is_web,is_adres,isIcon);

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
                params.put("is_id",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(YoneticiMainActivity.this).add(stringRequest);
    }

}
