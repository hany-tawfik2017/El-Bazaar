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
import android.widget.TextView;

import com.hany.el_bazaar.Adapters.VendorsTabAdapter;
import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 10/19/2018.
 */

public class VendorsTabFragment extends Fragment {
    RecyclerView vendorsList;
    TextView vendorsCount;
    ArrayList<Vendor> vendors;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vendors_tab_fragment,container,false);
        vendorsList = view.findViewById(R.id.vendors);
        vendorsCount = view.findViewById(R.id.item_count);
        vendors = new ArrayList<>();
        setupDefaultVendors();
        VendorsTabAdapter adapter = new VendorsTabAdapter(getActivity(),vendors);
        vendorsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        vendorsList.setAdapter(adapter);
        vendorsCount.setText(""+adapter.getItemCount()+" items");
        return view;
    }

    private void setupDefaultVendors(){
        vendors.add(new Vendor("Hany","Lacoste"));
        vendors.add(new Vendor("Hussien","Hugo"));
        vendors.add(new Vendor("Mohsen","Lacoste"));
        vendors.add(new Vendor("Motaz","HP Laptop"));
    }
}
