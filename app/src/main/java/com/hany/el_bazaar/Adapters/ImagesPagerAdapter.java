package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hany.el_bazaar.GlideApp;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 12/26/2018.
 */

public class ImagesPagerAdapter extends PagerAdapter {

    ArrayList<String> images;
    StorageReference reference;
    boolean isProduct;
    private Context context;
    private LayoutInflater layoutInflater;

    public ImagesPagerAdapter(Context context, boolean isProduct, ArrayList<String> images) {
        this.context = context;
        this.isProduct = isProduct;
        this.images = images;
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.from(context).inflate(R.layout.fragment_image_one, container, false);
        ImageView imageView = view.findViewById(R.id.first_image);
        if (images.get(position) != null && images.get(position).length() > 0) {
            if (isProduct)
                reference = FirebaseStorage.getInstance().getReference().child("product/" + images.get(position));
            else
                reference = FirebaseStorage.getInstance().getReference().child("bazaar/" + images.get(position));
            GlideApp.with(context).load(reference).into(imageView);
        } else {
            imageView.setImageResource(R.drawable.logo);
        }

        ViewPager viewPager = (ViewPager) container;
        viewPager.addView(view,0);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ViewPager vp = (ViewPager) container;
        View view = (View) object;
        vp.removeView(view);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }
}
