package com.hany.el_bazaar;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.hany.el_bazaar.activities.JoinActivity;

public class MainActivity extends AppCompatActivity {

    ActionBarDrawerToggle mDrawerToggle;
    //private TabLayout tabLayout;
    private View header;
    private DrawerLayout drawerLayout;
    private android.support.v7.widget.Toolbar toolbar;
    private RelativeLayout relativeLayout;
    private Fragment fragment = null;
    private String title;
    private NavigationView navigationView;
    private TextView login , join;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Legal matters");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    @Override
    protected void onStart() {
        super.onStart();
        initScreen();
    }
    void initScreen(){
        initNavigationDrawer();
    }
    public void initNavigationDrawer() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();
                    switch (id){
                        case R.id.home:
                            Toast.makeText(MainActivity.this,"home",Toast.LENGTH_LONG).show();
                            break;
                        case R.id.browse:
                            Toast.makeText(MainActivity.this,"browse",Toast.LENGTH_LONG).show();
                            break;
                    }
                    return true;
                }
            });
        }
        header = navigationView.getHeaderView(0);
        relativeLayout = (RelativeLayout)header.findViewById(R.id.rl_header);
        login = (TextView) relativeLayout.findViewById(R.id.login);
        join = (TextView) relativeLayout.findViewById(R.id.join);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this,"login activity",Toast.LENGTH_LONG).show();
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
                Toast.makeText(MainActivity.this,"profile activity",Toast.LENGTH_LONG).show();
            }
        });
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        mDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.drawer_open,R.string.close_drawer){
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
}
