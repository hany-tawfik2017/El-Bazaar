package com.hany.el_bazaar.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Adapters.TabsPagerAdapter;
import com.hany.el_bazaar.R;
import com.hany.el_bazaar.TabsFragments.ProfileTabsFragments.AboutTabFragment;
import com.hany.el_bazaar.TabsFragments.ProfileTabsFragments.BazaarsProfileFragment;

import java.util.Map;

/**
 * Created by Hany on 12/8/2018.
 */

public class ProfileActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ImageView editProfile,backImage,editUserInfo;
    DatabaseReference reference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.profile_viewpager);
        editProfile = (ImageView) findViewById(R.id.edit_profile_img);
        backImage = findViewById(R.id.back_img);
        editUserInfo = findViewById(R.id.edit_user_image);
        TabsPagerAdapter tabsAdapter = new TabsPagerAdapter(getSupportFragmentManager());
        tabsAdapter.addFragment(new AboutTabFragment(), "About");
        tabsAdapter.addFragment(new BazaarsProfileFragment(), "Bazaars");
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        reference = FirebaseDatabase.getInstance().getReference("users");
        reference.child("-LRnJ8eIJAP_pRsNZ7PB").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
                Toast.makeText(ProfileActivity.this,data.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        reference.child("-LRnJ8eIJAP_pRsNZ7PB").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String,String> data = (Map<String, String>) dataSnapshot.getValue();
                Toast.makeText(ProfileActivity.this,data.toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ProfileActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        editUserInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("infoFlag",true);
                startActivity(intent);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,EditProfileActivity.class);
                intent.putExtra("infoFlag",false);
                startActivity(intent);
            }
        });
    }
}
