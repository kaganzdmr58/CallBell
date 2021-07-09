package com.callbellapp.callbell.Isletme;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.callbellapp.callbell.R;
import com.callbellapp.callbell.Sınıflar.CagrilarDetay;

import java.io.Serializable;
import java.util.List;

public class CagrilarRVAdapter extends RecyclerView.Adapter<CagrilarRVAdapter.CardTasarimNesneleriTutucu> implements Serializable {
    private Context mContext;
    private List<CagrilarDetay> cagrilarListe;

    public CagrilarRVAdapter(Context mContext, List<CagrilarDetay> cagrilarListe) {
        this.mContext = mContext;
        this.cagrilarListe = cagrilarListe;
    }

    public class CardTasarimNesneleriTutucu extends RecyclerView.ViewHolder {
            public TextView textViewMesaj,textViewMasaNu,textViewSaat;
            public CardView cardViewCagrilar;

            public CardTasarimNesneleriTutucu(@NonNull View itemView) {
                super(itemView);
                this.textViewMesaj =itemView.findViewById(R.id.textViewMesaj);
                this.textViewMasaNu = itemView.findViewById(R.id.textViewMasaNu);
                this.textViewSaat = itemView.findViewById(R.id.textViewSaat);
                this.cardViewCagrilar = itemView.findViewById(R.id.cardViewCagrilar);
            }
    }

    @NonNull
    @Override
    public CardTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cagrilar_card_tasarim_resimsiz,parent,false);

        return new CardTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CardTasarimNesneleriTutucu holder, int position) {
        final CagrilarDetay cagri = cagrilarListe.get(position);

        holder.textViewMasaNu.setText(cagri.getCaMasaID().getQrName());    //forein key ile alınacak. en son bak
        holder.textViewSaat.setText(cagri.getCaZaman());
        holder.textViewMesaj.setText(cagri.getCaMesaj());

        holder.cardViewCagrilar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // AlertView ile mesajın tamamı gösterilecek.
                //alertViewGoster(cagri.getCaMasaKey(),cagri.getCaZaman(),cagri.getCaMesaj());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cagrilarListe.size();
    }


    private void alertViewGoster(String masaAdi,String zaman,String mesaj){
        AlertDialog.Builder alertDialogOlusturucu = new AlertDialog.Builder(mContext);
        alertDialogOlusturucu.setTitle(masaAdi + " - "+ zaman);
        alertDialogOlusturucu.setMessage(mesaj);

        alertDialogOlusturucu.setPositiveButton(R.string.Tamam, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });

        alertDialogOlusturucu.create().show();
    }
}
