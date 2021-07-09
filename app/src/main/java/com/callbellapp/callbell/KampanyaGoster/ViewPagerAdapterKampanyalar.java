package com.callbellapp.callbell.KampanyaGoster;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;


import com.callbellapp.callbell.Sınıflar.WebResimUrl;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapterKampanyalar extends PagerAdapter {
    private Context context;
    private List<WebResimUrl> imageUrls;

    public ViewPagerAdapterKampanyalar(Context context, List<WebResimUrl> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
    }

    @Override
    public int getCount() {
        return imageUrls.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //ImageView imageView = new ImageView(context);
        PhotoView photoView = new PhotoView(context);
        Picasso.get()
                .load(imageUrls.get(position).getRes_url())
                .fit()
                .centerInside()
                .into(photoView);
        container.addView(photoView);
        return photoView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
