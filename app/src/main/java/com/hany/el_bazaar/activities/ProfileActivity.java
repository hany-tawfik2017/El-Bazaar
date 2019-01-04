package com.hany.el_bazaar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.SpinKitView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.TabsPagerAdapter;
import com.hany.el_bazaar.Defaults;
import com.hany.el_bazaar.Model.User;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.TabsFragments.ProfileTabsFragments.AboutTabFragment;
import com.hany.el_bazaar.TabsFragments.ProfileTabsFragments.BazaarsProfileFragment;

import java.io.Serializable;

/**
 * Created by Hany on 12/8/2018.
 */

public class ProfileActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView editProfile, backImage, editUserInfo;
    DatabaseReference reference;
    TextView userName, userEmail;
    User user;
    SpinKitView loading;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        editProfile = (ImageView) findViewById(R.id.edit_profile_img);
        backImage = findViewById(R.id.back_img);
        userName = findViewById(R.id.user_name);
        userEmail = findViewById(R.id.user_email);
        loading = findViewById(R.id.loading);
        editUserInfo = findViewById(R.id.edit_user_image);
        TabsPagerAdapter tabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new AboutTabFragment(), "About");
        tabsAdapter.addFragment(new BazaarsProfileFragment(), "Bazaars");
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        reference = FirebaseDatabase.getInstance().getReference("users");
        loading.setVisibility(View.VISIBLE);
        reference.child(Defaults.getDefaults("userId", this)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                loading.setVisibility(View.GONE);
                user = dataSnapshot.getValue(User.class);
                if (user != null) {
                    userEmail.setText(user.email);
                    userName.setText(user.name);
                }
                setEditButtons();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                loading.setVisibility(View.GONE);
                Toast.makeText(ProfileActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void setEditButtons() {
        final Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        intent.putExtra("userName", user.name);
        intent.putExtra("userEmail", user.email);
        intent.putExtra("userAddress", user.address);
        intent.putExtra("userPass", user.password);
        intent.putExtra("userMob", user.mobile);
        if (user.aboutMap != null)
            intent.putExtra("about", (Serializable) user.aboutMap);
        if (user.brandName != null)
            intent.putExtra("brandName", user.brandName);
        editUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("infoFlag", true);
                startActivity(intent);
                finish();
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                intent.putExtra("infoFlag", false);
                startActivity(intent);
                finish();
            }
        });
    }
}
