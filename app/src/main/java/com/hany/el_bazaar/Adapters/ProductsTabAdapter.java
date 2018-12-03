package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 11/30/2018.
 */

public class ProductsTabAdapter extends RecyclerView.Adapter<ProductsTabAdapter.ViewHolder> {

    Context context;
    ArrayList<Product> products;

    public ProductsTabAdapter(Context context, ArrayList<Product> products) {
        this.context = context;
        this.products = products;
    }

    @NonNull
    @Override
    public ProductsTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_grid_item, parent, false);
        return new ProductsTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsTabAdapter.ViewHolder holder, int position) {
        holder.productName.setText(products.get(position).getProductName());
        holder.productPrice.setText(products.get(position).getProductPrice());
        holder.bazaarAddress.setText("LONGCHAMP, City Star Mall");
        if (products.get(position).getProductName().contains("Necklace"))
            holder.productImage.setImageResource(R.drawable.necklace_img);
        else
            holder.productImage.setImageResource(R.drawable.perfume_img);

        if (!products.get(position).isFavorite())
            holder.favImage.setImageResource(R.drawable.unselected_fav_product);
        else
            holder.favImage.setImageResource(R.drawable.selected_fav_product);

    }

    @Override
    public int getItemCount() {
        if (products != null && products.size() > 0)
            return products.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage, favImage;
        TextView productName, productPrice,bazaarAddress;
        RatingBar productRate;

        public ViewHolder(View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_img);
            favImage = itemView.findViewById(R.id.select_favourite);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productRate = itemView.findViewById(R.id.product_rate);
            bazaarAddress = itemView.findViewById(R.id.bazaar_address);
        }
    }
}

