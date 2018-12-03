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

import com.hany.el_bazaar.Model.Bazaar;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 12/1/2018.
 */

public class BazaarsTabAdapter extends RecyclerView.Adapter<BazaarsTabAdapter.ViewHolder> {

    Context context;
    ArrayList<Bazaar> bazaars;

    public BazaarsTabAdapter(Context context, ArrayList<Bazaar> bazaars) {
        this.context = context;
        this.bazaars = bazaars;
    }

    @NonNull
    @Override
    public BazaarsTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bazaar_list_item, parent, false);
        return new BazaarsTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BazaarsTabAdapter.ViewHolder holder, int position) {
        holder.vendorsCount.setText(""+bazaars.get(position).getVendorNumbers()+" Vendors");
        holder.organizerName.setText(bazaars.get(position).getOrganizerName());
        holder.bazaarName.setText(bazaars.get(position).getBazaarName());
        holder.bazaarPlace.setText(bazaars.get(position).getBazaarPlace());

    }

    @Override
    public int getItemCount() {
        if (bazaars != null && bazaars.size() > 0)
            return bazaars.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bazaarImage;
        TextView bazaarName, organizerName, vendorsCount,bazaarPlace;


        public ViewHolder(View itemView) {
            super(itemView);
            bazaarImage = itemView.findViewById(R.id.bazaar_img);
            bazaarName = itemView.findViewById(R.id.bazaar_name);
            organizerName = itemView.findViewById(R.id.organizer_name);
            vendorsCount = itemView.findViewById(R.id.vendors_count);
            bazaarPlace = itemView.findViewById(R.id.bazaar_place);
        }
    }
}