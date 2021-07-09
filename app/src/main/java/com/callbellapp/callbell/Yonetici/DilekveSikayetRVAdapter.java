package com.callbellapp.callbell.Yonetici;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.callbellapp.callbell.R;
import com.callbellapp.callbell.Sınıflar.DilekveSikayeteler;

import java.util.List;

public class DilekveSikayetRVAdapter extends RecyclerView.Adapter<DilekveSikayetRVAdapter.CardViewTasarimNesneleriTutucu>{
    private Context mContext;
    private List<DilekveSikayeteler> dilekveSikayetelerListe;

    public DilekveSikayetRVAdapter(Context mContext, List<DilekveSikayeteler> dilekveSikayetelerListe) {
        this.mContext = mContext;
        this.dilekveSikayetelerListe = dilekveSikayetelerListe;
    }



    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public CardView cardViewSikayet;
        public TextView textViewBaslik,textViewSikayet,textViewAdEmail;
        public RatingBar ratingBarSikayet;

        public CardViewTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.cardViewSikayet = itemView.findViewById(R.id.cardViewSikayet);
            this.textViewBaslik = itemView.findViewById(R.id.textViewBaslik);
            this.textViewSikayet = itemView.findViewById(R.id.textViewSikayet);
            this.textViewAdEmail = itemView.findViewById(R.id.textViewAdEmail);
            this.ratingBarSikayet = itemView.findViewById(R.id.ratingBarSikayet);
        }
    }

    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sikayetler_card_tasarim,parent,false);

        return new DilekveSikayetRVAdapter.CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        DilekveSikayeteler sikayet = dilekveSikayetelerListe.get(position);

        holder.textViewBaslik.setText(sikayet.getSi_masa_tarih());
        holder.textViewSikayet.setText(sikayet.getSi_sikayet());
        holder.textViewAdEmail.setText(sikayet.getSi_email_ad());
        holder.ratingBarSikayet.setRating(sikayet.getSi_rating());


    }

    @Override
    public int getItemCount() {
        return dilekveSikayetelerListe.size();
    }
}
