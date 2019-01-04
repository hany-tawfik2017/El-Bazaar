package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hany.el_bazaar.GlideApp;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.ProductDetailsActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hany on 12/27/2018.
 */

public class ProductVendorAdapter extends RecyclerView.Adapter<ProductVendorAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;
    StorageReference storageReference;

    public ProductVendorAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductVendorAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_vendor_info_item, parent, false);
        return new ProductVendorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductVendorAdapter.ViewHolder holder, final int position) {
        holder.productName.setText(products.get(position).getProductName());
        holder.bazaarAddress.setText("City Star Mall");
        if (products.get(position).getImages()!=null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("product/" + products.get(position).getImages().get(0));
            GlideApp.with(context).load(storageReference).into(holder.productImage);
        }
        else
            holder.productImage.setImageResource(R.drawable.logo);

    }

    @Override
    public int getItemCount() {
        if (products != null && products.size() > 0)
            return products.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName,bazaarAddress;
        RatingBar productRate;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_img);
            productName = itemView.findViewById(R.id.product_name);
            productRate = itemView.findViewById(R.id.product_rate);
            bazaarAddress = itemView.findViewById(R.id.bazaar_address);
        }
    }
}
