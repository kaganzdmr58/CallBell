package com.callbellapp.callbell.Isletme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.callbellapp.callbell.R;
import com.callbellapp.callbell.Sınıflar.CagrilarDetay;

import java.util.ArrayList;

public class MasalarDetayRVAdapter extends RecyclerView.Adapter<MasalarDetayRVAdapter.CardViewTasarimNesneleriTutucu> {
    private Context mContext;
    private ArrayList<CagrilarDetay> cagrilarMasalarArrayList;

    public MasalarDetayRVAdapter(Context mContext, ArrayList<CagrilarDetay> cagrilarMasalarArrayList) {
        this.mContext = mContext;
        this.cagrilarMasalarArrayList = cagrilarMasalarArrayList;
    }



    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
        public TextView textViewMesaj,textViewMasaNu,textViewSaat;
        public CardView cardViewCagrilar;

        public CardViewTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.textViewMesaj = itemView.findViewById(R.id.textViewMesaj);
            this.textViewMasaNu = itemView.findViewById(R.id.textViewMasaNu);
            this.textViewSaat = itemView.findViewById(R.id.textViewSaat);
            this.cardViewCagrilar = itemView.findViewById(R.id.cardViewCagrilar);
        }
    }

    @NonNull
    @Override
    public CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.cagrilar_card_tasarim_resimsiz,parent,false);

        return new CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimNesneleriTutucu holder, int position) {
        final CagrilarDetay cagri = cagrilarMasalarArrayList.get(position);
        holder.textViewMasaNu.setText(cagri.getCaMasaID().getQrName());  // burası için foreinkey kullanımı denenecek
        holder.textViewSaat.setText(cagri.getCaZaman());
        holder.textViewMesaj.setText(cagri.getCaMesaj());
        holder.cardViewCagrilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // tılamada yapılacak işlem
                //alertViewGoster(cagri.getCaMasaKey(),cagri.getCaZaman(),cagri.getCaMesaj());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cagrilarMasalarArrayList.size();
    }



}
