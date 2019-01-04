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
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.VendorsTabAdapter;
import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Hany on 10/19/2018.
 */

public class VendorsTabFragment extends Fragment {
    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
    RecyclerView vendorsList;
    TextView vendorsCount;
    ArrayList<Vendor> vendors;
    SpinKitView loading;
    VendorsTabAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vendors_tab_fragment, container, false);
        vendorsList = view.findViewById(R.id.vendors);
        vendorsCount = view.findViewById(R.id.item_count);
        vendors = new ArrayList<>();
        loading = view.findViewById(R.id.loading);
        setupDefaultVendors();
        adapter = new VendorsTabAdapter(getActivity(), vendors);
        vendorsList.setLayoutManager(new LinearLayoutManager(getActivity()));
        vendorsList.setAdapter(adapter);

        return view;
    }

    private void setupDefaultVendors() {
        Query query = reference.orderByChild("userType").equalTo("Vendor");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (loading.getVisibility() == View.VISIBLE)
                    loading.setVisibility(View.GONE);
                vendors.clear();
                if (dataSnapshot.exists()) {
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setupVendors(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupVendors(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Vendor vendor = new Vendor();
                vendor.setVendorId(entry.getKey());
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    vendor.setVendorName((String) mapObj.get("name"));
                    if (mapObj.get("brandName") != null)
                        vendor.setBrandName((String) mapObj.get("brandName"));
                    if(mapObj.get("userRate")!=null)
                        vendor.setVendorRate((long)mapObj.get("userRate"));
                    vendors.add(vendor);
                }
            }
            adapter.notifyDataSetChanged();
            vendorsCount.setText("" + adapter.getItemCount() + " items");
        }
    }
}
