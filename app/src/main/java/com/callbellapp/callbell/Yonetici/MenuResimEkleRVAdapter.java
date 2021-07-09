package com.callbellapp.callbell.Yonetici;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import com.callbellapp.callbell.R;
import com.callbellapp.callbell.Sınıflar.WebResimUrl;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MenuResimEkleRVAdapter extends RecyclerView.Adapter<MenuResimEkleRVAdapter.CardViewTasarimNesneleriTutucu> implements Serializable {
    private Context mContext;
    private List<WebResimUrl> UrlList;

    public MenuResimEkleRVAdapter(Context mContext, List<WebResimUrl> urlList) {
        this.mContext = mContext;
        UrlList = urlList;
    }


    public class CardViewTasarimNesneleriTutucu extends RecyclerView.ViewHolder{
        public CardView CardViewResim;
        public ImageView imageViewResim;
        public ImageView imageViewSil;

        public CardViewTasarimNesneleriTutucu(@NonNull View itemView) {
            super(itemView);
            this.CardViewResim = itemView.findViewById(R.id.CardViewResim);
            this.imageViewResim = itemView.findViewById(R.id.imageViewResim);
            this.imageViewSil = itemView.findViewById(R.id.imageViewSil);
        }
    }


    @NonNull
    @Override
    public MenuResimEkleRVAdapter.CardViewTasarimNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_resim_goster_card_tasarim,parent,false);

        return new MenuResimEkleRVAdapter.CardViewTasarimNesneleriTutucu(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MenuResimEkleRVAdapter.CardViewTasarimNesneleriTutucu holder, int position) {
        final WebResimUrl webResimUrl = UrlList.get(position);

        Picasso.get()
                .load(webResimUrl.getRes_url())
                .fit()
                .centerInside()
                .into(holder.imageViewResim);

        holder.imageViewSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MenuResimleriSilme(webResimUrl);


            }
        });

    }

    @Override
    public int getItemCount() {
        return UrlList.size();
    }



    public void MenuResimleriSilme(final WebResimUrl webResimUrl){

        final String dosya_adi = webResimUrl.getRes_url().substring(47);

        String url = "https://callbellapp.xyz/project/table5/table5_delete_photo_and_sql.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Log.e("response",response);

                Intent intent = new Intent(mContext, MenuResimEkleActivity.class);
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
                params.put("dosya",dosya_adi);
                params.put("res_id",String.valueOf(webResimUrl.getRes_id()));
                return params;
            }
        };
        Volley.newRequestQueue(mContext).add(stringRequest);


    }
}
