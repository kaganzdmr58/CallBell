package com.callbellapp.callbell.Yonetici;

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
import com.callbellapp.callbell.Sınıflar.PersonelFirebase;

import java.io.Serializable;
import java.util.List;

public class PersonelRVAdapter extends RecyclerView.Adapter<PersonelRVAdapter.CardTasarimNesneleriTutucu> implements Serializable {
    private Context mContext;
    private List<PersonelFirebase>personelListesi;

    public PersonelRVAdapter(Context mContext, List<PersonelFirebase> personelListesi) {
        this.mContext = mContext;
        this.personelListesi = personelListesi;
    }



    public class CardTasarimNesneleriTutucu extends RecyclerView.ViewHolder{
        public CardView cardViewPersonel;
        public TextView textViewPersonel;

        public CardTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.cardViewPersonel = itemView.findViewById(R.id.cardViewPersonel);
            this.textViewPersonel = itemView.findViewById(R.id.textViewPersonel);
        }

    }

    @NonNull
    @Override
    public CardTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.personel_card_tasarim_resimsiz,parent,false);

        return new CardTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimNesneleriTutucu holder, int position) {
        final PersonelFirebase personel = personelListesi.get(position);
        holder.textViewPersonel.setText(personel.getPerfGorev()+" - "+personel.getPerfAdi());
        holder.cardViewPersonel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // personel detay actvitiy e gidecek yanında personel nesnesi ile
                Intent intent = new Intent(mContext,PersonelDetayActivity.class);
                intent.putExtra("nesne",personel);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return personelListesi.size();
    }
}
