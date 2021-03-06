package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.hany.el_bazaar.GlideApp;
import com.hany.el_bazaar.Model.Bazaar;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.activities.BazaarDetailsActivity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Hany on 12/1/2018.
 */

public class BazaarsTabAdapter extends RecyclerView.Adapter<BazaarsTabAdapter.ViewHolder> {

    Context context;
    ArrayList<Bazaar> bazaars;
    private boolean isProfileBazaar;
    StorageReference storageReference;
    public boolean notClicked;

    public BazaarsTabAdapter(Context context, ArrayList<Bazaar> bazaars) {
        this.context = context;
        this.bazaars = bazaars;
    }

    @NonNull
    @Override
    public BazaarsTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(!isProfileBazaar ? R.layout.bazaar_list_item : R.layout.bazaar_profile_list_item, parent, false);
        return new BazaarsTabAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final BazaarsTabAdapter.ViewHolder holder, final int position) {
        holder.vendorsCount.setText(bazaars.get(position).getVendorNumbers());
        if (holder.organizerName != null)
            holder.organizerName.setText(bazaars.get(position).getOrganizerName());
        holder.bazaarName.setText(bazaars.get(position).getBazaarName());
        holder.bazaarPlace.setText(bazaars.get(position).getBazaarPlace());
        if (bazaars.get(position).getImages()!=null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("bazaar/" + bazaars.get(position).getImages().get(0));
            GlideApp.with(context).load(storageReference).into(holder.bazaarImage);
        }
        else
            holder.bazaarImage.setImageResource(R.drawable.logo);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(notClicked)
                    return;
                Intent intent = new Intent(context, BazaarDetailsActivity.class);
                intent.putExtra("vendorsNumbers", bazaars.get(position).getVendorNumbers());
                intent.putExtra("organizerName", bazaars.get(position).getOrganizerName());
                intent.putExtra("bazaarName", bazaars.get(position).getBazaarName());
                intent.putExtra("bazaarDesc",bazaars.get(position).getBazaarDesc());
                intent.putExtra("bazaarId",bazaars.get(position).getBazaarId());
                intent.putExtra("bazaarPlace", bazaars.get(position).getBazaarPlace());
                intent.putExtra("bazaarImages", (Serializable) bazaars.get(position).getImages());
                context.startActivity(intent);
            }
        });

    }

    public void setProfileBazaar(boolean isProfileBazaar) {
        this.isProfileBazaar = isProfileBazaar;
    }

    @Override
    public int getItemCount() {
        if (bazaars != null && bazaars.size() > 0)
            return bazaars.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView bazaarImage;
        TextView bazaarName, organizerName, vendorsCount, bazaarPlace;


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