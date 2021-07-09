package com.callbellapp.callbell.Yonetici;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.callbellapp.callbell.Isletme.CagrilarActivity;
import com.callbellapp.callbell.Isletme.MasalarActivity;
import com.callbellapp.callbell.MainActivity;
import com.callbellapp.callbell.R;
import com.callbellapp.callbell.ScannerActivity;
import com.callbellapp.callbell.StartActivity;
import com.callbellapp.callbell.Sınıflar.WebResimUrl;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class KampanyaResimEkleActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private RecyclerView rvKampanyaGoster;
    private ArrayList<WebResimUrl> KampanyaResimlerArrayList;
    private KampanyaResimEkleRVAdapter adapter;

    private ImageView imageViewKampanyaSec,imageViewKampanyaYukle;
    private TextView textViewKampanyaYukle,textViewKampanyaBilgi;
    private final int IMG_REQUEST = 1;
    private Bitmap bitmap;
    private String UploadUrl = "https://callbellapp.xyz/project/table5/";
    private int resimYukleID=0;
    private Boolean resimYukleDurum = true;


/*  Resim türleri
*   Menu resimleri      = 1
*   Kampanya resimleri  = 2
*   Icon Resimleri      = 3
*   Bildirim Resimleri  = 4
* */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),YoneticiMainActivity.class));
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // burada alınan ID silinecek
        ResimYukleIDSilmeIslemleri();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kampanya_resim_ekle);

        // https://www.youtube.com/watch?v=Xqizhonc7IE&t=1145s

        rvKampanyaGoster = findViewById(R.id.rvKampanyaGoster);
        imageViewKampanyaSec = findViewById(R.id.imageViewKampanyaSec);
        imageViewKampanyaYukle = findViewById(R.id.imageViewKampanyaYukle);
        textViewKampanyaYukle = findViewById(R.id.textViewKampanyaYukle);
        textViewKampanyaBilgi = findViewById(R.id.textViewKampanyaBilgi);

        // textview scroll ederek kaydırma ekliyoruz
        textViewKampanyaBilgi.setMovementMethod(new ScrollingMovementMethod());

        rvKampanyaGoster.setHasFixedSize(true);
        rvKampanyaGoster.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

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


        KampanyaResimleriAlma();

        imageViewKampanyaSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (resimYukleDurum){
                    selectImage();
                }else {
                    Resources res = getResources();
                    String toastMesaj = res.getString(R.string.KampanyaResimleriResimDoldu);
                    Toast.makeText(getApplicationContext(),toastMesaj,Toast.LENGTH_LONG).show();
                }
                //databaseden ID numarası alınıp resim dosyasının ismine kayıt yapılacak yükle komutu ile database de değişiklik yapılacak.
            }
        });

        imageViewKampanyaYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(2);
            }
        });

        textViewKampanyaYukle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadImage(2);
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



    //resim yukleme: Resim yukleme her imageView e tıklamada galeri seçimi açılacak,
    //aynı anda database de yeni bir satır eklenecek eklenen satırdan ID numarası alıncak resim ismi için
    //seçilen resim ilgili ID numarasına göre verilecek isme göre kaydedilecek ve database güncellenecek.



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

                    imageViewKampanyaSec.setImageBitmap(bitmap);
                    //imageViewKampanyaYukle.setVisibility(View.VISIBLE);
                    //textViewKampanyaYukle.setVisibility(View.VISIBLE);
                    ResimYukleIDAlmaIslemleri(2);


                //Toast.makeText(getApplicationContext(),"resimSecStatu ",Toast.LENGTH_LONG).show();
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
                   // Toast.makeText(getApplicationContext(),Response,Toast.LENGTH_LONG).show();
                    //imageViewYukle.setImageResource(0);
                    resimYukleID = 0;
                    KampanyaResimleriAlma();

                    imageViewKampanyaYukle.setVisibility(View.INVISIBLE);
                    textViewKampanyaYukle.setVisibility(View.INVISIBLE);
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
        MySingleton.getInstance(KampanyaResimEkleActivity.this).addToRequestQue(stringRequest);
    }

    private  String imagageToString(Bitmap bitmap){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,17,byteArrayOutputStream);
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
                Log.e("response",response);
                // yeni işletme oluşturma cevabı burası işletme ID si buradan alıncak.
                String[] cevap = response.split(":");
                //Toast.makeText(getApplicationContext(),cevap[1],Toast.LENGTH_LONG).show();
                if (Integer.parseInt(cevap[1]) == 0){
                    Resources res = getResources();
                    String toastMesaj = res.getString(R.string.KampanyaResmiResimYuklemedeHataOlustu);
                    Toast.makeText(getApplicationContext(),toastMesaj,Toast.LENGTH_LONG).show();
                    return;
                }

                resimYukleID = Integer.parseInt(cevap[1]);

                imageViewKampanyaYukle.setVisibility(View.VISIBLE);
                textViewKampanyaYukle.setVisibility(View.VISIBLE);

                //Toast.makeText(getApplicationContext(),"ID Nu " +resimYukleID,Toast.LENGTH_LONG).show();

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
                params.put("res_isletme",isletmeID);
                params.put("res_tur",String.valueOf(res_tur));
                return params;
            }
        };
        Volley.newRequestQueue(KampanyaResimEkleActivity.this).add(stringRequest);
    }


    public void KampanyaResimleriAlma(){
        // qr bilgileri düzenle if ile doluluk şartı koy.
        final String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");

       /* if (isletmeScanID.equals("gecici")){
            //Toast.makeText(getApplicationContext(),"İşletme qr codu taratılmadığı için işlemi gerçekleştiremiyoruz.",Toast.LENGTH_SHORT).show();
            return;
        }*/
        // işletmenin web gecmis cagrilar tablosuna ekleme yapılacak.

        String url = "https://callbellapp.xyz/project/table5/table5_url_find.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                KampanyaResimlerArrayList = new ArrayList<>();
                //Log.e("response",response);

                try {
                    JSONObject jsonObject = new JSONObject(response);

                    JSONArray qrCode = jsonObject.getJSONArray("tablo5");

                    for (int i = 0; i<qrCode.length(); i++ ) {

                        JSONObject q = qrCode.getJSONObject(i);
                        String res_url = q.getString("res_url");
                        int res_tur = q.getInt("res_tur");
                        int res_id = q.getInt("res_id");
                        int res_isletme = q.getInt("res_isletme");

                        //Toast.makeText(KampanyaResimEkleActivity.this, res_url, Toast.LENGTH_SHORT).show();

                        WebResimUrl webResimUrl = new WebResimUrl(res_id,res_isletme,res_tur,res_url);
                        if ((res_tur == 2) && (!(res_url.isEmpty()))){
                            KampanyaResimlerArrayList.add(webResimUrl);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (KampanyaResimlerArrayList.size() == 7){
                    resimYukleDurum = false;
                }
                adapter = new KampanyaResimEkleRVAdapter(KampanyaResimEkleActivity.this, KampanyaResimlerArrayList);
                rvKampanyaGoster.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("res_isletme",isletmeID);
                return params;
            }
        };
        Volley.newRequestQueue(KampanyaResimEkleActivity.this).add(stringRequest);


    }


    public void ResimYukleIDSilmeIslemleri(){
        // tablo 5 ten ID silinecek ki boşa yer kaplamasın.

        String url = "https://callbellapp.xyz/project/table5/delete_table5_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

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
                params.put("res_id",String.valueOf(resimYukleID));
                return params;
            }
        };
        Volley.newRequestQueue(KampanyaResimEkleActivity.this).add(stringRequest);
    }


}
