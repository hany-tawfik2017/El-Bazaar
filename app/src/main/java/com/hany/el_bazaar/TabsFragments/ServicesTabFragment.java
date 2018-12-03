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

import com.hany.el_bazaar.Adapters.ServicesTabAdapter;
import com.hany.el_bazaar.Model.Service;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 10/19/2018.
 */

public class ServicesTabFragment extends Fragment {

    RecyclerView servicesListView;
    ServicesTabAdapter servicesAdapter;
    ArrayList<Service> services;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.services_tab_fragment, container, false);
        servicesListView = view.findViewById(R.id.services_view);
        services = new ArrayList<>();
        setupDefaultServices();
        servicesAdapter = new ServicesTabAdapter(services, getContext());
        servicesListView.setLayoutManager(new LinearLayoutManager(getContext()));
        servicesListView.setAdapter(servicesAdapter);

        return view;
    }

    private void setupDefaultServices() {
        services.add(new Service("Bazaars photographer"));
        services.add(new Service("Videos making"));
        services.add(new Service("Manage Facebook page"));
        services.add(new Service("Instagram business account"));
        services.add(new Service("Sponsor ads"));
        services.add(new Service("WhatsApp sponsor ads"));
        services.add(new Service("Graphic design"));
        services.add(new Service("Event management"));
        services.add(new Service("Outdoor ads"));
        services.add(new Service("Facebook ads organizing"));
        services.add(new Service("Create an online event"));
    }
}
