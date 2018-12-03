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
import android.widget.Toast;

import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 11/24/2018.
 */

public class VendorsTabAdapter extends RecyclerView.Adapter<VendorsTabAdapter.ViewHolder> {

    Context context;
    ArrayList<Vendor> vendors;

    public VendorsTabAdapter(Context context, ArrayList<Vendor> vendors) {
        this.context = context;
        this.vendors = vendors;
    }

    @NonNull
    @Override
    public VendorsTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vendor_item_list, parent, false);
        return new VendorsTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorsTabAdapter.ViewHolder holder, int position) {
        holder.brandName.setText(vendors.get(position).getBrandName());
        holder.vendorName.setText(vendors.get(position).getVendorName());
        holder.vendorRate.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(context, "the rating is " + rating, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (vendors != null && vendors.size() > 0)
            return vendors.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView vendorName, brandName;
        RatingBar vendorRate;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.brand_img);
            vendorName = itemView.findViewById(R.id.vendor_name);
            brandName = itemView.findViewById(R.id.brand_name);
            vendorRate = itemView.findViewById(R.id.vendor_rate);
        }
    }
}
