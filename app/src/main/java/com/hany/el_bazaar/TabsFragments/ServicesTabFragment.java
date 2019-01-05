package com.hany.el_bazaar.TabsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.ServicesTabAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.Service;
import com.hany.el_bazaar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hany on 10/19/2018.
 */

public class ServicesTabFragment extends Fragment {

    RecyclerView servicesListView;
    ServicesTabAdapter servicesAdapter;
    ArrayList<Service> services;
    Button sendServices;
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("services");
    SpinKitView loading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.services_tab_fragment, container, false);
        servicesListView = view.findViewById(R.id.services_view);
        sendServices = view.findViewById(R.id.send_services);
        loading = view.findViewById(R.id.loading);
        services = new ArrayList<>();
        loading.setVisibility(View.VISIBLE);
        servicesAdapter = new ServicesTabAdapter(services, getContext(),reference);
        servicesListView.setLayoutManager(new LinearLayoutManager(getContext()));
        servicesListView.setAdapter(servicesAdapter);
        sendServices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Animation alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.button_alpha_anim);
                v.startAnimation(alphaAnimation);
                if (Defaults.getDefaults("userId", getActivity()) != null && Defaults.getDefaults("userType", getActivity()) != null) {
                    Toast.makeText(getActivity(), servicesAdapter.getServicesNames().toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), "You should Sign up as a vendor or organizer to use Services", Toast.LENGTH_LONG).show();
                }

            }
        });
        addServices();
        return view;
    }

    private void addServices() {
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                services.clear();
                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                setupDefaultServices(map);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setupDefaultServices(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Service service = new Service();
                service.setServiceId(entry.getKey());
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    service.setServiceName((String) mapObj.get("serviceName"));
                    service.setServiceChecked((Boolean) mapObj.get("serviceChecked"));
                    service.setServiceUsers((List<String>) mapObj.get("serviceUsers"));
                    services.add(service);
                }
            }
            servicesAdapter.notifyDataSetChanged();
        }
    }
}
