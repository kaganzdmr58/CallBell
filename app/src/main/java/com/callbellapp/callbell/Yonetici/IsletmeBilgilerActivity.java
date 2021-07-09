package com.callbellapp.callbell.Yonetici;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.callbellapp.callbell.Isletme.MasalarActivity;
import com.callbellapp.callbell.MainActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.ScannerActivity;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Sınıflar.IsletmeBilgi;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IsletmeBilgilerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private ImageView imageViewIcon;
    private EditText editTextIsletmeName,editTextWeb,editTextSlogan,editTextTel,editTextEmail,editTextAdres;
    private Button buttonIsletmeBilgiKaydet;
    private String isletmeID;

    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    private String UploadUrl = "https://callbellapp.xyz/project/table5/";
    private int resimYukleID=0;
    private IsletmeBilgi isletmeBilgi;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),YoneticiMainActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_isletme_bilgiler);


        imageViewIcon = findViewById(R.id.imageViewIcon);
        editTextIsletmeName = findViewById(R.id.editTextIsletmeName);
        editTextSlogan = findViewById(R.id.editTextSlogan);
        editTextTel = findViewById(R.id.editTextTel);
        editTextEmail = findViewById(R.id.editTextPassword);
        editTextWeb = findViewById(R.id.editTextWeb);
        editTextAdres = findViewById(R.id.editTextAdres);
        buttonIsletmeBilgiKaydet = findViewById(R.id.buttonIsletmeBilgiKaydet);

        isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        IsletmeBilgileriAl();

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.IsletmeBilgilerTitle);
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


        buttonIsletmeBilgiKaydet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsletmeBilgileriDegistirme();

            }
        });

        imageViewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(toolbar,R.string.IsletmeBilgilerResimDegistirilsinMi,Snackbar.LENGTH_SHORT)
                        .setAction(R.string.Evet, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // eğer resim mevcutsa resim silinecek sonra resim seçime geçilecek.
                                if (!((isletmeBilgi.getIsIconUrl()).isEmpty())) {
                                    //Toast.makeText(IsletmeBilgilerActivity.this, "durum1", Toast.LENGTH_SHORT).show();
                                    IconResimiSilme(isletmeBilgi.getIsIconUrl());
                                }else {
                                    // eğer resim mevcutt değilse direk olarak resim seçmeye geçiyoruz
                                    selectImage();
                                    //Toast.makeText(IsletmeBilgilerActivity.this, "durum2", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }).show();
            }
        });



    }


    public void IconResimiSilme(final String webUrl){

        final String dosya_adi = webUrl.substring(47);
        String[] idAl = dosya_adi.split(":");
        final String ID = idAl[3].substring(0,(idAl[3].length()-4));


        //Toast.makeText(this, dosya_adi+"--"+resId[0], Toast.LENGTH_SHORT).show();
/*
        editTextIsletmeBilgi.setText(idAl[3]);
        editTextKampanya.setText(ID);*/

        String url = "https://callbellapp.xyz/project/table5/table5_delete_photo_and_sql.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(IsletmeBilgilerActivity.this, response, Toast.LENGTH_SHORT).show();

                selectImage();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("dosya",dosya_adi);
                params.put("res_id",ID);
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeBilgilerActivity.this).add(stringRequest);

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


    private void IsletmeBilgileriDegistirme(){
        String url = "https://callbellapp.xyz/project/table2/update_table2_business_name_info_campaign_change.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);

                Resources res = getResources();
                String YoneticiMainMusteriMainEkranGuncellendi2  = res.getString(R.string.YoneticiMainMusteriMainEkranGuncellendi2);
                Toast.makeText(IsletmeBilgilerActivity.this,YoneticiMainMusteriMainEkranGuncellendi2,Toast.LENGTH_SHORT).show();

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
                params.put("is_name",editTextIsletmeName.getText().toString());
                params.put("is_slogan",editTextSlogan.getText().toString());
                params.put("is_tel",editTextTel.getText().toString());
                params.put("is_email",editTextEmail.getText().toString());
                params.put("is_web",editTextWeb.getText().toString());
                params.put("is_adres",editTextAdres.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeBilgilerActivity.this).add(stringRequest);
    }




    private void selectImage(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,IMG_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Toast.makeText(getApplicationContext(),"resultCode :"+ resultCode+"/RESULT_OK :"+ RESULT_OK,Toast.LENGTH_LONG).show();
        if ((requestCode == IMG_REQUEST)/* && (requestCode == RESULT_OK)*/ && (data != null)){
            Uri path = data.getData();
            //Toast.makeText(getApplicationContext(),"image yukle çalıştı-2",Toast.LENGTH_LONG).show();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),path);

                imageViewIcon.setImageBitmap(bitmap);

                /*Snackbar.make(toolbar,"Resmi değiştirilsin mi?",Snackbar.LENGTH_LONG)
                        .setAction(R.string.Evet, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).show();
*/
                ResimYukleIDAlmaIslemleri(3);

                //Toast.makeText(this, "11", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(final int res_tur){
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        String url = "https://callbellapp.xyz/project/table5/upload_table5.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String Response = jsonObject.getString("response");
                    //Toast.makeText(getApplicationContext(),Response,Toast.LENGTH_LONG).show();
                    //imageViewYukle.setImageResource(0);
                    ResimYukleIslemleri();
                    //Toast.makeText(IsletmeBilgilerActivity.this, "333", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("response",response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response",error.toString());
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("name","CallBell:"+isletmeID+":"+res_tur+":"+resimYukleID);
                params.put("image",imagageToString(bitmap));
                params.put("res_id",String.valueOf(resimYukleID));
                params.put("res_url",UploadUrl+"uploads/"+"CallBell:"+isletmeID+":"+res_tur+":"+resimYukleID+".jpg");
                return params;
            }
        };
        MySingleton.getInstance(IsletmeBilgilerActivity.this).addToRequestQue(stringRequest);
    }

    private  String imagageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,25,byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes,Base64.DEFAULT);
    }


    // resim dosyası silme
    // https://stackoverflow.com/questions/2371408/how-to-delete-a-file-via-php


    public void ResimYukleIDAlmaIslemleri(final int res_tur){
        // ilk olarak ID alacak yer burasıdır.
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

        String url = "https://callbellapp.xyz/project/table5/insert_table5.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                // yeni işletme oluşturma cevabı burası işletme ID si buradan alıncak.
                String[] cevap = response.split(":");
                //Toast.makeText(getApplicationContext(),cevap[1],Toast.LENGTH_LONG).show();
                if (Integer.parseInt(cevap[1]) == 0){
                    Resources res = getResources();
                    String toastMesaj = res.getString(R.string.IsletmeBilgilerResimYuklemedeHataOlustu);
                    Toast.makeText(getApplicationContext(),toastMesaj,Toast.LENGTH_LONG).show();
                    return;
                }

                resimYukleID = Integer.parseInt(cevap[1]);
                //Toast.makeText(getApplicationContext(),"ID Nu " +resimYukleID,Toast.LENGTH_LONG).show();
                uploadImage(3);

                //Toast.makeText(IsletmeBilgilerActivity.this, "22", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("response", String.valueOf(error));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("res_isletme",isletmeID);
                params.put("res_tur",String.valueOf(res_tur));
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeBilgilerActivity.this).add(stringRequest);
    }



    private void IsletmeBilgileriAl(){


        String url = "https://callbellapp.xyz/project/table2/all_table2_business_info_find.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);

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


                        isletmeBilgi = new IsletmeBilgi(isId,isName,isYonetciID,isSlogan,is_tel,is_email,is_web,is_adres,isIcon);


                        editTextWeb.setText(is_web);
                        editTextIsletmeName.setText(isName);
                        editTextSlogan.setText(isSlogan);
                        editTextTel.setText(is_tel);
                        editTextEmail.setText(is_email);
                        editTextAdres.setText(is_adres);

                        if (!(isIcon.isEmpty())) {
                            Picasso.get()
                                    .load(isIcon)
                                    .fit()
                                    .centerInside()
                                    .into(imageViewIcon);
                        }


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
        Volley.newRequestQueue(IsletmeBilgilerActivity.this).add(stringRequest);
    }

    public void ResimYukleIslemleri(){

        String url = "https://callbellapp.xyz/project/table2/update_table2_icon_url.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                Resources res = getResources();
                String toastMesaj = res.getString(R.string.IsletmeBilgilerResimYuklemeTamamlandı);
                Toast.makeText(getApplicationContext(), toastMesaj, Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("response", String.valueOf(error));
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("is_icon_url",UploadUrl+"uploads/"+"CallBell:"+isletmeID+":3:"+resimYukleID+".jpg");
                params.put("is_id",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(IsletmeBilgilerActivity.this).add(stringRequest);
    }
}
/*


    LayoutInflater inflater = LayoutInflater.from(YoneticiMainActivity.this);
    View alert_tasarim = inflater.inflate(R.layout.isletme_bilgi_ve_kampanyalar_alert_tasarim, null);

    final EditText alert_edittext_kampanya = (EditText) alert_tasarim.findViewById(R.id.editTextKampanya);
    final EditText alert_edittext_isletme_bilgi = (EditText) alert_tasarim.findViewById(R.id.editTextIsletmeBilgi);

    AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(YoneticiMainActivity.this);
                alertDialogOlusturucu.setMessage(R.string.YoneticiMainİsletmeBilgileri);
                        alertDialogOlusturucu.setView(alert_tasarim);
                        alert_edittext_isletme_bilgi.setHint(R.string.YoneticiMainİsletmeBilgileriniGiriniz);
                        alert_edittext_isletme_bilgi.setText(isletmeBilgi);
                        alert_edittext_kampanya.setHint(R.string.YoneticiMainGosterilecekIndirimveKampanyalar);
                        alert_edittext_kampanya.setText(kampanyaYazisi);

                        alertDialogOlusturucu.setPositiveButton(R.string.Kaydet, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {
        IsletmeBilgisiDegistirme(alert_edittext_isletme_bilgi.getText().toString().trim());
        IsletmeMainYaziDegistirme(alert_edittext_kampanya.getText().toString().trim());
        }
        });
        alertDialogOlusturucu.setNegativeButton(R.string.Iptal, new DialogInterface.OnClickListener() {
@Override
public void onClick(DialogInterface dialogInterface, int i) {
        Toast.makeText(YoneticiMainActivity.this,R.string.ProfilKayitIptalEdildi,Toast.LENGTH_SHORT).show();
        }
        });
        alertDialogOlusturucu.create().show();*/




//https://www.mehmetkirazli.com/android-dersleri-22-telefon-numarasini-aramak/