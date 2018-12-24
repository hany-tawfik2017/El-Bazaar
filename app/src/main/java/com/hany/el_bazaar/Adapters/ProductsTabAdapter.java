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
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.ProductDetailsActivity;

import java.util.ArrayList;

/**
 * Created by Hany on 11/30/2018.
 */

public class ProductsTabAdapter extends RecyclerView.Adapter<ProductsTabAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;
    boolean isList;
    boolean isFavoriteList;
    DatabaseReference reference;

    public ProductsTabAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(!isList ? R.layout.product_grid_item : R.layout.favorite_list_item, parent, false);
        return new ProductsTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductsTabAdapter.ViewHolder holder, final int position) {
        holder.productName.setText(products.get(position).getProductName());
        holder.productPrice.setText(products.get(position).getProductPrice());
        holder.productCurrency.setText(products.get(position).getProductCurrency());
        holder.bazaarAddress.setText("LONGCHAMP, City Star Mall");
        if (products.get(position).getProductName().contains("Necklace"))
            holder.productImage.setImageResource(R.drawable.necklace_img);
        else
            holder.productImage.setImageResource(R.drawable.perfume_img);

        if (products.get(position).isFavorite()) {
            holder.favImage.setVisibility(View.INVISIBLE);
            holder.unFavImage.setVisibility(View.VISIBLE);
        }

        reference = FirebaseDatabase.getInstance().getReference("products");
        if (!isFavoriteList) {
            holder.favImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.favImage.setVisibility(View.INVISIBLE);
                    holder.unFavImage.setVisibility(View.VISIBLE);
                    reference.child(products.get(position).getProductId()).child("isFavorite").setValue(true);
                }
            });
            holder.unFavImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.favImage.setVisibility(View.VISIBLE);
                    holder.unFavImage.setVisibility(View.INVISIBLE);
                    reference.child(products.get(position).getProductId()).child("isFavorite").setValue(false);
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailsActivity.class);
                intent.putExtra("productName", products.get(position).getProductName());
                intent.putExtra("productPrice", products.get(position).getProductPrice());
                intent.putExtra("productId", products.get(position).getProductId());
                intent.putExtra("address", holder.bazaarAddress.getText().toString());
                context.startActivity(intent);
            }
        });

    }

    public void setFavoriteList(boolean favoriteList) {
        isFavoriteList = favoriteList;
    }

    public void setList(boolean list) {
        isList = list;
    }

    @Override
    public int getItemCount() {
        if (products != null && products.size() > 0)
            return products.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, favImage, unFavImage;
        TextView productName, productPrice, bazaarAddress, productCurrency;
        RatingBar productRate;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_img);
            favImage = itemView.findViewById(R.id.select_favourite);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productRate = itemView.findViewById(R.id.product_rate);
            bazaarAddress = itemView.findViewById(R.id.bazaar_address);
            productCurrency = itemView.findViewById(R.id.product_currency);
            unFavImage = itemView.findViewById(R.id.unselect_favourite);
        }
    }
}

