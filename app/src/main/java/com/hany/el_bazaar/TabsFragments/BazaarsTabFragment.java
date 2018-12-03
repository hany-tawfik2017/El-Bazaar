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

import com.hany.el_bazaar.Adapters.BazaarsTabAdapter;
import com.hany.el_bazaar.Adapters.VendorsTabAdapter;
import com.hany.el_bazaar.Model.Bazaar;
import com.hany.el_bazaar.Model.Vendor;
import com.hany.el_bazaar.R;

import java.util.ArrayList;

/**
 * Created by Hany on 10/19/2018.
 */

public class BazaarsTabFragment extends Fragment {

    RecyclerView bazaarList;
    TextView bazaarCount;
    ArrayList<Bazaar> bazaars;
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
        bazaars = new ArrayList<>();
        setupDefaultBazaars();
        BazaarsTabAdapter adapter = new BazaarsTabAdapter(getActivity(),bazaars);
        bazaarList.setLayoutManager(new LinearLayoutManager(getActivity()));
        bazaarList.setAdapter(adapter);
        bazaarCount.setText(""+adapter.getItemCount()+" items");
        return view;
    }

    private void setupDefaultBazaars(){
        bazaars.add(new Bazaar("LONGCHAMP","Hany","CityStars",4));
        bazaars.add(new Bazaar("Club Aldo","Motaz","CityStars",5));
        bazaars.add(new Bazaar("Concrete","Mohamed","Mall of Egypt",7));
        bazaars.add(new Bazaar("Tie House","Hussien","Mall of Arabia",4));
        bazaars.add(new Bazaar("Bazaar","Mohsen","City Mall",4));
        bazaars.add(new Bazaar("Radio Shack","Ahmed","CityStars",3));

    }
}
