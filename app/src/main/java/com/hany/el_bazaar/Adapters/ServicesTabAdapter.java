package com.hany.el_bazaar.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.Service;
import com.hany.el_bazaar.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hany on 11/24/2018.
 */

public class ServicesTabAdapter extends RecyclerView.Adapter<ServicesTabAdapter.ViewHolder> {

    private ArrayList<Service> servicesList;
    private Context context;
    private List<String> servicesNames;
    private DatabaseReference reference;

    public ServicesTabAdapter(ArrayList<Service> servicesList, Context context, DatabaseReference reference) {
        this.servicesList = servicesList;
        this.context = context;
        servicesNames = new ArrayList<>();
        this.reference = reference;
    }

    @NonNull
    @Override
    public ServicesTabAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.service_tab_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ServicesTabAdapter.ViewHolder holder, final int position) {
        holder.serviceName.setText(servicesList.get(position).getServiceName());
        if (Defaults.getDefaults("userId", context) != null && Defaults.getDefaults("userType", context) != null)
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setCheckedServices(holder, position);
                }
            });
        holder.checkService.setEnabled(false);
        if (Defaults.getDefaults("userId", context) != null && Defaults.getDefaults("userType", context) != null)
            if (servicesList.get(position).isServiceChecked() && servicesList.get(position).getServiceUsers() != null && servicesList.get(position).getServiceUsers().contains(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                holder.checkService.setChecked(true);
            else
                holder.checkService.setChecked(false);
    }

    private void setCheckedServices(ViewHolder holder, final int position) {
        final List<String> serviceUsers;
        if (holder.checkService.isChecked()) {
            if (servicesList.get(position).getServiceUsers() != null) {
                servicesList.get(position).getServiceUsers().remove(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                if (servicesList.get(position).getServiceUsers().size() == 0) {
                    servicesList.get(position).setServiceChecked(false);
                }
            }
            reference.child(servicesList.get(position).getServiceId()).child("serviceChecked").setValue(servicesList.get(position).isServiceChecked())
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            if (!servicesList.get(position).isServiceChecked())
                                servicesList.get(position).getServiceUsers().clear();
                            reference.child(servicesList.get(position).getServiceId()).child("serviceUsers").setValue(servicesList.get(position).getServiceUsers())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Removed from Services needed", Toast.LENGTH_LONG).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
            if (servicesNames.size() > 0)
                servicesNames.remove(servicesList.get(position).getServiceName());
            holder.checkService.setChecked(false);
        } else {
            if (!servicesList.get(position).isServiceChecked()) {
                servicesList.get(position).setServiceChecked(true);
                serviceUsers = new ArrayList<>();
                serviceUsers.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            } else {
                serviceUsers = servicesList.get(position).getServiceUsers();
                if (!serviceUsers.contains(FirebaseAuth.getInstance().getCurrentUser().getEmail()))
                    serviceUsers.add(FirebaseAuth.getInstance().getCurrentUser().getEmail());
            }
            reference.child(servicesList.get(position).getServiceId()).child("serviceChecked").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    reference.child(servicesList.get(position).getServiceId()).child("serviceUsers").setValue(serviceUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(context, "Added to Services needed", Toast.LENGTH_LONG).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
            holder.checkService.setChecked(true);
            if (servicesNames.size() == 0)
                servicesNames.add(servicesList.get(position).getServiceName());
            else {
                if (!servicesNames.contains(servicesList.get(position).getServiceName()))
                    servicesNames.add(servicesList.get(position).getServiceName());
            }
        }
    }

    public List<String> getServicesNames() {
        return servicesNames;
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
