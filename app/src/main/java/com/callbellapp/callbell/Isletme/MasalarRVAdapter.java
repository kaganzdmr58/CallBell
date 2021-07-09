package com.callbellapp.callbell.Isletme;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.callbellapp.callbell.R;
import com.callbellapp.callbell.Sınıflar.QrCode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MasalarRVAdapter extends RecyclerView.Adapter<MasalarRVAdapter.CardViewTasarimNesneleriTutucu> implements Serializable {
    private Context mContext;
    private List<QrCode> masalarList;

    public MasalarRVAdapter(Context mContext, ArrayList<QrCode> masalarList) {
        this.mContext = mContext;
        this.masalarList = masalarList;
    }

    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder{
        public CardView cardViewMasa;
        public TextView textViewMasa;

        public CardViewTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.cardViewMasa = itemView.findViewById(R.id.cardViewMasa);
            this.textViewMasa = itemView.findViewById(R.id.textViewMasa);
        }
    }


    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.masalar_card_tasarim_resimsiz,parent,false);

        return new CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {

            final QrCode masa = masalarList.get(position);

            holder.textViewMasa.setText(masa.getQrName());
            holder.cardViewMasa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // masalar detay a gidecek
                    Intent intent = new Intent(mContext, MasalarDetayActivity.class);
                    intent.putExtra("nesne", masa);
                    mContext.startActivity(intent);
                }
            });



    }

    @Override
    public int getItemCount() {
           return masalarList.size();
    }

}
