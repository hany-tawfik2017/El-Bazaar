package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 12/25/2018.
 */

public class PostItemAdapter extends RecyclerView.Adapter<PostItemAdapter.ViewHolder> {

    Context context;
    ArrayList<Object> listItems;
    ArrayList<String> spinnerItems;

    public PostItemAdapter(Context context, ArrayList<Object> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @NonNull
    @Override
    public PostItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_list_item, parent, false);
        return new PostItemAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostItemAdapter.ViewHolder holder, final int position) {
        holder.removeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listItems.remove(position);
                notifyItemRemoved(position);
            }
        });


    }

    public void setSpinnerItems(ArrayList<String> spinnerItems) {
        this.spinnerItems = spinnerItems;
    }

    @Override
    public int getItemCount() {
        if (listItems != null && listItems.size() > 0)
            return listItems.size();

        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView removeItem;
        Spinner bazaarVendorSelection;


        public ViewHolder(View itemView) {
            super(itemView);
            bazaarVendorSelection = itemView.findViewById(R.id.vendor_bazaar_selection);
            removeItem = itemView.findViewById(R.id.remove);
            if (spinnerItems != null && spinnerItems.size() > 0) {
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, spinnerItems);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                bazaarVendorSelection.setAdapter(spinnerAdapter);
            }
        }
    }
}
