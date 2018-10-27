package com.hany.el_bazaar.NavigationFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hany.el_bazaar.Adapters.TabsPagerAdapter;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.TabsFragments.BazaarsTabFragment;
import com.hany.el_bazaar.TabsFragments.ProductsTabFragment;
import com.hany.el_bazaar.TabsFragments.ServicesTabFragment;
import com.hany.el_bazaar.TabsFragments.VendorsTabFragment;

/**
 * Created by Hany on 10/19/2018.
 */

public class HomeFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        TabsPagerAdapter tabsAdapter = new TabsPagerAdapter(getChildFragmentManager());
        tabsAdapter.addFragment(new ProductsTabFragment(), "Products");
        tabsAdapter.addFragment(new BazaarsTabFragment(), "Bazaars");
        tabsAdapter.addFragment(new VendorsTabFragment(), "Vendors");
        tabsAdapter.addFragment(new ServicesTabFragment(), "Services");
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));


        return view;
    }
}
