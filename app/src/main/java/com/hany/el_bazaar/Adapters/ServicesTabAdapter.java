package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hany.el_bazaar.Model.Service;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 11/24/2018.
 */

public class ServicesTabAdapter extends RecyclerView.Adapter<ServicesTabAdapter.ViewHolder> {

    private ArrayList<Service> servicesList;
    private Context context;

    public ServicesTabAdapter(ArrayList<Service> servicesList, Context context) {
        this.servicesList = servicesList;
        this.context = context;
    }

    @NonNull
    @Override
    public ServicesTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_tab_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServicesTabAdapter.ViewHolder holder, int position) {
        holder.serviceName.setText(servicesList.get(position).getServiceName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.checkService.setChecked(true);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (servicesList != null && servicesList.size() > 0)
            return servicesList.size();
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView serviceName;
        CheckBox checkService;

        public ViewHolder(View itemView) {
            super(itemView);
            serviceName = (TextView) itemView.findViewById(R.id.service_name);
            checkService = (CheckBox) itemView.findViewById(R.id.check_service);
        }
    }
}
