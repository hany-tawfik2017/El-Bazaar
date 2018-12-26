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
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.BazaarsTabAdapter;
import com.hany.el_bazaar.Adapters.VendorsTabAdapter;
import com.hany.el_bazaar.Model.Bazaar;
import com.hany.el_bazaar.Model.Product;
import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Hany on 10/19/2018.
 */

public class BazaarsTabFragment extends Fragment {

    RecyclerView bazaarList;
    TextView bazaarCount;
    ArrayList<Bazaar> bazaars;
    SpinKitView loading;
    BazaarsTabAdapter adapter;
    DatabaseReference reference;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.bazaars_tab_fragment,container,false);
        bazaarList = view.findViewById(R.id.bazaars);
        bazaarCount = view.findViewById(R.id.item_count);
        loading = view.findViewById(R.id.loading);
        loading.setVisibility(View.VISIBLE);
        bazaars = new ArrayList<>();
        setupDefaultBazaars();
        adapter = new BazaarsTabAdapter(getActivity(),bazaars);
        bazaarList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bazaarList.setAdapter(adapter);

        return view;
    }

    private void setupDefaultBazaars(){
        reference = FirebaseDatabase.getInstance().getReference("bazaars");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                bazaars.clear();
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    setBazaarArray(map);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void setBazaarArray(Map<String, Object> map) {
        if (map != null) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                Bazaar bazaar = new Bazaar();
                bazaar.setBazaarId(entry.getKey());
                if (entry.getValue() instanceof Map) {
                    Map<String, Object> mapObj = (Map<String, Object>) entry.getValue();
                    bazaar.setBazaarName((String) mapObj.get("bazaarName"));
                    bazaar.setBazaarPlace((String) mapObj.get("bazaarPlace"));
                    bazaar.setBazaarDesc((String) mapObj.get("bazaarDesc"));
                    bazaar.setOrganizerName((String) mapObj.get("organizerName"));
                    bazaar.setVendorNumbers((String) mapObj.get("vendorNumbers"));
                    bazaar.setImages((List<String>) mapObj.get("images"));
                    bazaars.add(bazaar);
                }
            }
            adapter.notifyDataSetChanged();
            bazaarCount.setText(""+adapter.getItemCount()+" items");
        }
    }
}
