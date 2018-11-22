package com.hany.el_bazaar;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.hany.el_bazaar.NavigationFragments.HomeFragment;
import com.hany.el_bazaar.activities.JoinActivity;
import com.hany.el_bazaar.activities.LoginActivity;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle mDrawerToggle;
    //private TabLayout tabLayout;
    private View header;
    private DrawerLayout drawerLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private RelativeLayout relativeLayout;
    private String title;
    private NavigationView navigationView;
    private LinearLayout authLayout;
    private TextView login, join, navigationEmail;
    private android.support.v4.app.Fragment fragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("El-Bazaar");
        getSupportActionBar().setIcon(R.drawable.logo_toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        fragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

    }

    @Override
    protected void onStart() {
        super.onStart();
        initScreen();
    }

    void initScreen() {
        initNavigationDrawer();
    }

    public void initNavigationDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    switch (id) {
                        case R.id.home:
                            selectNavigationFragment(new HomeFragment(), drawerLayout, "Home");
                            break;
                        case R.id.browse:
                            Toast.makeText(MainActivity.this, "browse", Toast.LENGTH_LONG).show();
                            break;
                    }
                    return true;
                }
            });
        }
        header = navigationView.getHeaderView(0);
        relativeLayout = (RelativeLayout) header.findViewById(R.id.rl_header);
        login = (TextView) relativeLayout.findViewById(R.id.login);
        join = (TextView) relativeLayout.findViewById(R.id.join);
        navigationEmail = (TextView) relativeLayout.findViewById(R.id.email_nav);
        authLayout = (LinearLayout) relativeLayout.findViewById(R.id.auth_layout);
        auth = FirebaseAuth.getInstance();
        boolean authenticated = false;
        if (auth.getCurrentUser() != null) {
            authLayout.setVisibility(View.INVISIBLE);
            navigationEmail.setVisibility(View.VISIBLE);
            navigationEmail.setText(auth.getCurrentUser().getEmail());
            Menu menu = navigationView.getMenu();
            menu.add(R.id.two, Menu.NONE, Menu.NONE, "Log out");

        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
            }
        });
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, JoinActivity.class));
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "profile activity", Toast.LENGTH_LONG).show();
            }
        });
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.close_drawer) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        drawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }

    public void selectNavigationFragment(Fragment replacedFragment, DrawerLayout drawerLayout, String tag) {
        drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, replacedFragment).commit();
    }
}
