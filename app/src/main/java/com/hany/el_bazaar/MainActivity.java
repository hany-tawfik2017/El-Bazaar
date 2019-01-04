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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hany.el_bazaar.Model.User;
import com.hany.el_bazaar.NavigationFragments.HomeFragment;
import com.hany.el_bazaar.activities.FavoritesActivity;
import com.hany.el_bazaar.activities.JoinActivity;
import com.hany.el_bazaar.activities.LoginActivity;
import com.hany.el_bazaar.activities.PostProductActivity;
import com.hany.el_bazaar.activities.ProfileActivity;

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
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private User user;

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
        auth = FirebaseAuth.getInstance();
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        header = navigationView.getHeaderView(0);
        relativeLayout = (RelativeLayout) header.findViewById(R.id.rl_header);
        navigationEmail = (TextView) relativeLayout.findViewById(R.id.email_nav);
        authLayout = (LinearLayout) relativeLayout.findViewById(R.id.auth_layout);
        fragment = new HomeFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.commit();

        if (Defaults.getDefaults("userId", this) != null && auth.getCurrentUser() != null && Defaults.getDefaults("auth", this) != null) {
            authLayout.setVisibility(View.INVISIBLE);
            navigationEmail.setVisibility(View.VISIBLE);
            navigationEmail.setText(auth.getCurrentUser().getEmail());
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
            reference.child(Defaults.getDefaults("userId", this)).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
                    checkUserType(user);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(MainActivity.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void checkUserType(User user) {
        MenuItem item = null;
        Menu menu = navigationView.getMenu();
        MenuItem item1 = menu.add(R.id.two, Menu.NONE, Menu.NONE, "Log out");
        item1.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                auth.signOut();
                Defaults.removeDefaults("userId", MainActivity.this);
                Defaults.removeDefaults("auth", MainActivity.this);
                Toast.makeText(MainActivity.this, "you are logged out", Toast.LENGTH_LONG).show();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
                return true;
            }
        });
        boolean isOrganizer = false;
        if (user.userType != null)
            if (user.userType.equals("Organizer")) {
                item = menu.add(R.id.auth_group, Menu.NONE, Menu.NONE, "Post Your Bazaar");
                isOrganizer = true;

            } else if (user.userType.equals("Vendor")) {
                item = menu.add(R.id.auth_group, Menu.NONE, Menu.NONE, "Post Your Product");
                isOrganizer = false;
            }
        if (item != null) {
            item.setIcon(R.drawable.add_post_icon);
            final boolean finalIsOrganizer = isOrganizer;
            item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    Intent intent = new Intent(MainActivity.this, PostProductActivity.class);
                    intent.putExtra("isOrganizer", finalIsOrganizer);
                    startActivity(intent);
                    finish();
                    return true;
                }
            });
        }

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
        Menu menu = null;
        if (navigationView != null) {
            menu = navigationView.getMenu();
            navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    int id = item.getItemId();

                    switch (id) {
                        case R.id.home:
                            selectNavigationFragment(new HomeFragment(), drawerLayout, "Home");
                            break;
                        case R.id.favorites:
                            startActivity(new Intent(MainActivity.this, FavoritesActivity.class));
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
        if (Defaults.getDefaults("userId", this) != null && auth.getCurrentUser() != null &&
                Defaults.getDefaults("auth", this) != null && Defaults.getDefaults("userType", this) != null)
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(intent);
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

    private void getUser(DataSnapshot dataSnapshot, Menu menu) {
        user = dataSnapshot.getValue(User.class);
        MenuItem item = null;
        if (user != null && auth.getCurrentUser() != null && Defaults.getDefaults("auth", this) != null)
            if (user.userType.equals("Vendor"))
                item = menu.add(R.id.auth_group, Menu.NONE, Menu.NONE, "Post your product");
            else if (user.userType.equals("Organizer"))
                item = menu.add(R.id.auth_group, Menu.NONE, Menu.NONE, "Post your Bazaar");
        if (item != null)
            item.setIcon(R.drawable.add_post_icon);

        Toast.makeText(MainActivity.this, user != null ? user.email : "not user", Toast.LENGTH_LONG).show();
    }

    public void selectNavigationFragment(Fragment replacedFragment, DrawerLayout drawerLayout, String tag) {
        drawerLayout.closeDrawers();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, replacedFragment).commit();
    }

}
