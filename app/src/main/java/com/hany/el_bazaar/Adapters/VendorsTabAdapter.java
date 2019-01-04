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

import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.VendorInfoActivity;

import java.util.ArrayList;

/**
 * Created by Hany on 11/24/2018.
 */

public class VendorsTabAdapter extends RecyclerView.Adapter<VendorsTabAdapter.ViewHolder> {

    Context context;
    ArrayList<Vendor> vendors;
    boolean isBazaarVendor;

    public VendorsTabAdapter(Context context, ArrayList<Vendor> vendors) {
        this.context = context;
        this.vendors = vendors;
    }

    @NonNull
    @Override
    public VendorsTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(isBazaarVendor ? R.layout.vendor_bazaar_list_item : R.layout.vendor_item_list, parent, false);
        return new VendorsTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VendorsTabAdapter.ViewHolder holder, final int position) {

        if (vendors.get(position).getBrandName() != null)
            holder.vendorName.setText(vendors.get(position).getVendorName() + " - " + vendors.get(position).getBrandName());
        else
            holder.vendorName.setText(vendors.get(position).getVendorName());
        holder.vendorRate.setRating(vendors.get(position).getVendorRate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VendorInfoActivity.class);
                intent.putExtra("vendorId", vendors.get(position).getVendorId());
                intent.putExtra("vendorName", vendors.get(position).getVendorName());
                intent.putExtra("brandName", vendors.get(position).getBrandName());
                context.startActivity(intent);
            }
        });

    }

    public void setBazaarVendor(boolean bazaarVendor) {
        isBazaarVendor = bazaarVendor;
    }

    @Override
    public int getItemCount() {
        if (vendors != null && vendors.size() > 0)
            return vendors.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView vendorName;
        RatingBar vendorRate;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.brand_img);
            vendorName = itemView.findViewById(R.id.vendor_name);
            vendorRate = itemView.findViewById(R.id.vendor_rate);
        }
    }
}
