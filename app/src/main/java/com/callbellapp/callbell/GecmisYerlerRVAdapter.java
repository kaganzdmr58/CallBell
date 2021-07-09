package com.callbellapp.callbell;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.callbellapp.callbell.KampanyaGoster.KampanyaGosterActivity;
import com.callbellapp.callbell.MenuGoster.MenuGosterActivity;
import com.callbellapp.callbell.Sınıflar.IsletmeBilgi;
import com.callbellapp.callbell.Sınıflar.Ziyaretciler;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GecmisYerlerRVAdapter extends RecyclerView.Adapter<GecmisYerlerRVAdapter.CardViewTasarimNesneleriTutucu> {


    //https://developer.android.com/training/basics/intents/sending?hl=TR


    private Context mContext;
    private List<Ziyaretciler> isletmeBilgiListListe;

    public GecmisYerlerRVAdapter(Context mContext, List<Ziyaretciler> isletmeBilgiListListe) {
        this.mContext = mContext;
        this.isletmeBilgiListListe = isletmeBilgiListListe;
    }


    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public TextView textViewIsletmeAdi, textViewIsletmeSlogani, textViewTel, textViewEmail, textViewWeb, textViewAdres;
        public Button buttonKampanyalar, buttonMesajlar, buttonMenu;
        public ImageView imageViewIcon, imageViewSil,imageViewGecmisTel,imageViewGecmisEmail,imageViewGecmisWeb,imageViewGecmisAdres;

        public CardViewTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.imageViewIcon = itemView.findViewById(R.id.imageViewIcon);
            this.imageViewSil = itemView.findViewById(R.id.imageViewSil);
            this.textViewIsletmeAdi = itemView.findViewById(R.id.textViewIsletmeAdi22);
            this.textViewIsletmeSlogani = itemView.findViewById(R.id.textViewIsletmeSloganGecmisYerler);
            this.textViewTel = itemView.findViewById(R.id.textViewTel);
            this.textViewEmail = itemView.findViewById(R.id.textViewEmail);
            this.textViewWeb = itemView.findViewById(R.id.textViewWeb);
            this.textViewAdres = itemView.findViewById(R.id.textViewAdres);
            this.buttonKampanyalar = itemView.findViewById(R.id.buttonKampanyalar);
            this.buttonMesajlar = itemView.findViewById(R.id.buttonMesajlar);
            this.buttonMenu = itemView.findViewById(R.id.buttonMenu);
            this.imageViewGecmisTel = itemView.findViewById(R.id.imageViewGecmisTel);
            this.imageViewGecmisEmail = itemView.findViewById(R.id.imageViewGecmisEmail);
            this.imageViewGecmisWeb = itemView.findViewById(R.id.imageViewGecmisWeb);
            this.imageViewGecmisAdres = itemView.findViewById(R.id.imageViewGecmisAdres);
        }
    }


    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gecmis_yerler_card_tasarim, parent, false);

        return new GecmisYerlerRVAdapter.CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        final Ziyaretciler ziyaretci = isletmeBilgiListListe.get(position);

        if (!((ziyaretci.getZi_isletme().getIsName()).isEmpty())) {
            holder.textViewIsletmeAdi.setText(ziyaretci.getZi_isletme().getIsName());
        }

        if (!((ziyaretci.getZi_isletme().getIsSlogan()).isEmpty())) {
            holder.textViewIsletmeSlogani.setText(ziyaretci.getZi_isletme().getIsSlogan());
        }
        //
        if (!((ziyaretci.getZi_isletme().getIsTel()).isEmpty())) {
            holder.textViewTel.setText(ziyaretci.getZi_isletme().getIsTel());
        }
        if (!((ziyaretci.getZi_isletme().getIsEmail()).isEmpty())) {
            holder.textViewEmail.setText(ziyaretci.getZi_isletme().getIsEmail());
        }
        if (!((ziyaretci.getZi_isletme().getIsWeb()).isEmpty())) {
            holder.textViewWeb.setText(ziyaretci.getZi_isletme().getIsWeb());
        }
        if (!((ziyaretci.getZi_isletme().getIsAdres()).isEmpty())) {
            holder.textViewAdres.setText(ziyaretci.getZi_isletme().getIsAdres());
        }

        if (!((ziyaretci.getZi_isletme().getIsIconUrl()).isEmpty())) {
            Picasso.get()
                    .load(ziyaretci.getZi_isletme().getIsIconUrl())
                    .fit()
                    .centerInside()
                    .into(holder.imageViewIcon);
        }

        holder.imageViewSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GecmişYerleriSilme(ziyaretci);
            }
        });

        holder.buttonMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // masalar detay a gidecek
                Intent intent = new Intent(mContext, MenuGosterActivity.class);
                intent.putExtra("gecisID", String.valueOf(ziyaretci.getZi_isletme().getIsId()));
                mContext.startActivity(intent);
            }
        });

        holder.buttonMesajlar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IsletmeBilgi isletme = new IsletmeBilgi(
                        ziyaretci.getZi_isletme().getIsId()
                        , ziyaretci.getZi_isletme().getIsName()
                        , ziyaretci.getZi_isletme().getIsYonetciID()
                        , ziyaretci.getZi_isletme().getIsSlogan()
                        , ziyaretci.getZi_isletme().getIsTel()
                        , ziyaretci.getZi_isletme().getIsEmail()
                        , ziyaretci.getZi_isletme().getIsWeb()
                        , ziyaretci.getZi_isletme().getIsWeb()
                        , ziyaretci.getZi_isletme().getIsIconUrl());

                // Isletme Mesajları Acktivity'e gidecek
                Intent intent = new Intent(mContext, IsletmeMesajlariActivity.class);
                intent.putExtra("nesne", isletme);
                mContext.startActivity(intent);
            }
        });

        holder.buttonKampanyalar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // masalar detay a gidecek
                Intent intent = new Intent(mContext, KampanyaGosterActivity.class);
                intent.putExtra("gecisID", String.valueOf(ziyaretci.getZi_isletme().getIsId()));
                mContext.startActivity(intent);
            }
        });

        holder.textViewTel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!((ziyaretci.getZi_isletme().getIsTel()).isEmpty())) {
                    Uri number = Uri.parse("tel:"+ziyaretci.getZi_isletme().getIsTel());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    mContext.startActivity(callIntent);
                    /*try{
                        Intent myIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ziyaretci.getZi_isletme().getIsTel()));
                        mContext.startActivity(myIntent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }*/

                }
            }
        });
        holder.imageViewGecmisTel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!((ziyaretci.getZi_isletme().getIsTel()).isEmpty())) {
                    Uri number = Uri.parse("tel:"+ziyaretci.getZi_isletme().getIsTel());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    mContext.startActivity(callIntent);
                    /*try{
                        Intent myIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+ziyaretci.getZi_isletme().getIsTel()));
                        mContext.startActivity(myIntent);
                    }catch(Exception e){
                        e.printStackTrace();
                    }*/

                }
            }
        });
        holder.textViewEmail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //https://developer.android.com/training/basics/intents/sending?hl=TR
                if (!((ziyaretci.getZi_isletme().getIsEmail()).isEmpty())) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    // The intent does not have a URI, so declare the "text/plain" MIME type
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {ziyaretci.getZi_isletme().getIsEmail()}); // recipients
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\nFrom CallBell");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                    // You can also attach multiple items by passing an ArrayList of Uris
                    mContext.startActivity(emailIntent);

                }
            }
        });
        holder.imageViewGecmisEmail.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //https://developer.android.com/training/basics/intents/sending?hl=TR
                if (!((ziyaretci.getZi_isletme().getIsEmail()).isEmpty())) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND);
                    // The intent does not have a URI, so declare the "text/plain" MIME type
                    emailIntent.setType("plain/text");
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {ziyaretci.getZi_isletme().getIsEmail()}); // recipients
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "\n\n\nFrom CallBell");
                    emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"));
                    // You can also attach multiple items by passing an ArrayList of Uris
                    mContext.startActivity(emailIntent);

                }
            }
        });

        holder.textViewWeb.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //https://developer.android.com/training/basics/intents/sending?hl=TR
                if (!((ziyaretci.getZi_isletme().getIsWeb()).isEmpty())) {
                    Uri webpage;
                    String web = ziyaretci.getZi_isletme().getIsWeb();
                    if (web.indexOf("https://")==-1){
                        webpage = Uri.parse("https://"+ziyaretci.getZi_isletme().getIsWeb()+"/");
                    }else {
                        webpage = Uri.parse(ziyaretci.getZi_isletme().getIsWeb());
                    }
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    mContext.startActivity(webIntent);
                }
            }
        });
        holder.imageViewGecmisWeb.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //https://developer.android.com/training/basics/intents/sending?hl=TR
                if (!((ziyaretci.getZi_isletme().getIsWeb()).isEmpty())) {
                    Uri webpage;
                    String web = ziyaretci.getZi_isletme().getIsWeb();
                    if (web.indexOf("https://")==-1){
                        webpage = Uri.parse("https://"+ziyaretci.getZi_isletme().getIsWeb()+"/");
                    }else {
                        webpage = Uri.parse(ziyaretci.getZi_isletme().getIsWeb());
                    }
                    Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
                    mContext.startActivity(webIntent);
                }
            }
        });

        holder.textViewAdres.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //https://developer.android.com/training/basics/intents/sending?hl=TR
                if (!((ziyaretci.getZi_isletme().getIsAdres()).isEmpty())) {
                    // Map point based on address
                    Uri location = Uri.parse("geo:0,0?q="+ziyaretci.getZi_isletme().getIsAdres());
                    // Or map point based on latitude/longitude
                    // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    mContext.startActivity(mapIntent);
                }
            }
        });
        holder.imageViewGecmisAdres.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //https://developer.android.com/training/basics/intents/sending?hl=TR
                if (!((ziyaretci.getZi_isletme().getIsAdres()).isEmpty())) {
                    // Map point based on address
                    Uri location = Uri.parse("geo:0,0?q="+ziyaretci.getZi_isletme().getIsAdres());
                    // Or map point based on latitude/longitude
                    // Uri location = Uri.parse("geo:37.422219,-122.08364?z=14"); // z param is zoom level
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                    mContext.startActivity(mapIntent);
                }
            }
        });




//https://developer.android.com/training/basics/intents/sending#java
    }

    @Override
    public int getItemCount() {
        return isletmeBilgiListListe.size();
    }


    public void GecmişYerleriSilme(final Ziyaretciler ziyaretci){

        String url = "https://callbellapp.xyz/project/table7/delete_table7_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(mContext, response, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(mContext,GecmisYerlerActivity.class);
                mContext.startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("zi_id",String.valueOf(ziyaretci.getZi_id()));
                return params;
            }
        };
        Volley.newRequestQueue(mContext).add(stringRequest);
    }

}
