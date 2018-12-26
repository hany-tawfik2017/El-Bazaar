package com.hany.el_bazaar.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.hany.el_bazaar.Fragments.EditAboutContactsFragment;
import com.hany.el_bazaar.Fragments.EditUserProfileFragment;
import com.hany.el_bazaar.R;

/**
 * Created by Hany on 12/9/2018.
 */

public class EditProfileActivity extends AppCompatActivity {

    private Fragment fragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private ImageView backImage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        backImage = (ImageView) findViewById(R.id.back_img);
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if ((Boolean) getIntent().getExtras().get("infoFlag")){
            fragment = new EditUserProfileFragment();
            Bundle bundle = new Bundle();
            bundle.putString("userName",getIntent().getExtras().getString("userName"));
            bundle.putString("userEmail",getIntent().getExtras().getString("userEmail"));
            bundle.putString("userAddress",getIntent().getExtras().getString("userAddress"));
            bundle.putString("userMob",getIntent().getExtras().getString("userMob"));
            bundle.putString("userPass",getIntent().getExtras().getString("userPass"));
            fragment.setArguments(bundle);
        }
        else
            fragment = new EditAboutContactsFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
