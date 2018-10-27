package com.hany.el_bazaar.Adapters;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hany.el_bazaar.TabsFragments.BazaarsTabFragment;
import com.hany.el_bazaar.TabsFragments.ProductsTabFragment;
import com.hany.el_bazaar.TabsFragments.ServicesTabFragment;
import com.hany.el_bazaar.TabsFragments.VendorsTabFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hany on 10/19/2018.
 */

public class TabsPagerAdapter extends FragmentStatePagerAdapter {

    private final List<Fragment> fragmentList = new ArrayList<>();
    private final List<String> titleList = new ArrayList<>();

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        titleList.add(title);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }
}
