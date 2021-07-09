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

import com.callbellapp.callbell.Sınıflar.IsletmeBilgi;
import com.callbellapp.callbell.Sınıflar.Mesajlar;


import com.squareup.picasso.Picasso;

import java.util.List;

public class GelenKutusuRVAdapter extends RecyclerView.Adapter<GelenKutusuRVAdapter.CardViewTasarimNesneleriTutucu> {
    private Context mContext;
    private List<Mesajlar> gelenKutusuMesajlarListe;

    public GelenKutusuRVAdapter(Context mContext, List<Mesajlar> gelenKutusuMesajlarListe) {
        this.mContext = mContext;
        this.gelenKutusuMesajlarListe = gelenKutusuMesajlarListe;
    }


    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public TextView textViewIsletmeAdiTarihSaat,textViewGunlukKampanyaMetni;
        public ImageView imageViewMesajIcon;
        public CardView CardViewGelenMesaj;

        public CardViewTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.textViewIsletmeAdiTarihSaat = itemView.findViewById(R.id.textViewTarihSaat);
            this.textViewGunlukKampanyaMetni = itemView.findViewById(R.id.textViewGunlukKampanyaMetni);
            this.imageViewMesajIcon = itemView.findViewById(R.id.imageViewMesajIcon);
            this.CardViewGelenMesaj = itemView.findViewById(R.id.CardViewGelenMesaj);
        }
    }


    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gelen_kutusu_card_tasarim,parent,false);

        return new GelenKutusuRVAdapter.CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        final Mesajlar mesaj = gelenKutusuMesajlarListe.get(position);

        if (!((mesaj.getMes_mesaj()).isEmpty())){
            holder.textViewGunlukKampanyaMetni.setText(mesaj.getMes_mesaj());
        }

        if (!((mesaj.getMes_tarih()).isEmpty())){
            holder.textViewIsletmeAdiTarihSaat.setText(mesaj.getMes_isletme_id().getIsName()+" - "+mesaj.getMes_tarih());
        }

        if (!((mesaj.getMes_isletme_id().getIsIconUrl()).isEmpty())) {
            Picasso.get()
                    .load(mesaj.getMes_isletme_id().getIsIconUrl())
                    .into(holder.imageViewMesajIcon);
        }

        holder.CardViewGelenMesaj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
        });


/*        if (!((ziyaretci.getZi_isletme().getIsBilgi()).isEmpty())) {
            holder.textViewBilgiler.setText(ziyaretci.getZi_isletme().getIsBilgi());
        }
        if (!((ziyaretci.getZi_isletme().getIsName()).isEmpty())) {
            holder.textViewIsletmeAdi.setText(ziyaretci.getZi_isletme().getIsName());
        }

        if (!((ziyaretci.getZi_isletme().getIs_icon_url()).isEmpty())) {
            Picasso.get()
                    .load(ziyaretci.getZi_isletme().getIs_icon_url())
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

        holder.textViewMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // masalar detay a gidecek
                Intent intent = new Intent(mContext, MenuGosterActivity.class);
                intent.putExtra("gecisID", String.valueOf(ziyaretci.getZi_id()));
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return gelenKutusuMesajlarListe.size();
    }
}
