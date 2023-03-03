package com.gakdevelopers.studotest.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.fragments.Home;
import com.gakdevelopers.studotest.fragments.Leaderboard;
import com.gakdevelopers.studotest.fragments.MyCourses;
import com.gakdevelopers.studotest.fragments.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    BottomNavigationView bottomNavigationView;

    Home home = new Home();
    MyCourses myCourses  = new MyCourses();
    Leaderboard leaderboard = new Leaderboard();
    Profile profile = new Profile();

    TextView txtFirstLetter, txtEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        txtFirstLetter = navigationView.getHeaderView(0).findViewById(R.id.txtFirstLetter);
        txtEmail = navigationView.getHeaderView(0).findViewById(R.id.txtEmail);

        String name = DbQuery.myProfile.getName();
        txtEmail.setText(name);
        txtFirstLetter.setText(name.toUpperCase().substring(0, 1));

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.nav_open, R.string.nav_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                        return true;
                    case R.id.item_my_courses:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, myCourses).commit();
                        return true;
                    case R.id.item_leaderboard:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, leaderboard).commit();
                        return true;
                    case R.id.item_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                        return true;

                }
                return false;
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home: getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                break;
            case R.id.item_my_courses: getSupportFragmentManager().beginTransaction().replace(R.id.container, myCourses).commit();
                break;
            case R.id.item_leaderboard: getSupportFragmentManager().beginTransaction().replace(R.id.container, leaderboard).commit();
                break;
            case R.id.item_profile: getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}