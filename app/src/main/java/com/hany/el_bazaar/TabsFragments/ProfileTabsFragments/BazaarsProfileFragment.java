package com.hany.el_bazaar.TabsFragments.ProfileTabsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hany.el_bazaar.R;

/**
 * Created by Hany on 12/8/2018.
 */

public class BazaarsProfileFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bazaars_profile_tab,container,false);
    }
}
