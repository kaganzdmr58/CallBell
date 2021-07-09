package com.callbellapp.callbell;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.callbellapp.callbell.Sınıflar.IsletmeBilgi;
import com.callbellapp.callbell.Sınıflar.Mesajlar;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IsletmeMesajlariRVAdapter extends RecyclerView.Adapter<IsletmeMesajlariRVAdapter.CardViewTasarimNesneleriTutucu>{
    private Context mContext;
    private List<Mesajlar> isletmeuMesajlariListe;

    public IsletmeMesajlariRVAdapter(Context mContext, List<Mesajlar> isletmeuMesajlariListe) {
        this.mContext = mContext;
        this.isletmeuMesajlariListe = isletmeuMesajlariListe;
    }



    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public TextView textViewTarihVeSaat,textViewGunlukMetin;
        public ImageView imageViewSil;
        public CardView CardViewGunlukMesaj;

        public CardViewTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.textViewTarihVeSaat = itemView.findViewById(R.id.textViewTarihVeSaat);
            this.textViewGunlukMetin = itemView.findViewById(R.id.textViewGunlukMetin);
            this.imageViewSil = itemView.findViewById(R.id.imageViewSil);
            this.CardViewGunlukMesaj = itemView.findViewById(R.id.CardViewGunlukMesaj);
        }
    }


    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.isletme_mesajlari_card_view,parent,false);

        return new IsletmeMesajlariRVAdapter.CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CardViewTasarimNesneleriTutucu holder, int position) {
        final Mesajlar mesaj = isletmeuMesajlariListe.get(position);

        if (!((mesaj.getMes_tarih()).isEmpty())) {
            holder.textViewTarihVeSaat.setText(mesaj.getMes_tarih());
        }
        if (!((mesaj.getMes_mesaj()).isEmpty())) {
            holder.textViewGunlukMetin.setText(mesaj.getMes_mesaj());
        }

        holder.imageViewSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // silme web servisine bağlanılacak id nu gönderilecek ve silinecek.
                IsletmeMesajSilme(mesaj);
            }
        });

        // fab görünürlüğü
        Boolean YONETICI_MODU = StartActivity.spLogin.getBoolean("YONETICI_MODU",false);
        String isletmeID = StartActivity.spYonetici.getString("isletmeID","null");
        if (YONETICI_MODU){
            if (isletmeID.equals(String.valueOf(mesaj.getMes_isletme_id().getIsId()))){
                holder.imageViewSil.setVisibility(View.VISIBLE);
            }else{
                holder.imageViewSil.setVisibility(View.INVISIBLE);
            }
        }else {
            holder.imageViewSil.setVisibility(View.INVISIBLE);
        }



    }

    @Override
    public int getItemCount() {
        return isletmeuMesajlariListe.size();
    }


    public void IsletmeMesajSilme(final Mesajlar mesaj) {

        String url = "https://callbellapp.xyz/project/table8/delete_table8_id.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);
                //Toast.makeText(mContext,"response_"+response,Toast.LENGTH_LONG).show();
                IsletmeBilgi isletme = new IsletmeBilgi(
                         mesaj.getMes_isletme_id().getIsId()
                        ,mesaj.getMes_isletme_id().getIsName()
                        ,mesaj.getMes_isletme_id().getIsYonetciID()
                        ,mesaj.getMes_isletme_id().getIsSlogan()
                        ,mesaj.getMes_isletme_id().getIsTel()
                        ,mesaj.getMes_isletme_id().getIsEmail()
                        ,mesaj.getMes_isletme_id().getIsWeb()
                        ,mesaj.getMes_isletme_id().getIsAdres()
                        ,mesaj.getMes_isletme_id().getIsIconUrl());

                Intent intent = new Intent(mContext,IsletmeMesajlariActivity.class);
                intent.putExtra("nesne",isletme);
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
                params.put("mes_id",String.valueOf(mesaj.getMes_id()));
                return params;
            }
        };
        Volley.newRequestQueue(mContext).add(stringRequest);
    }


}
