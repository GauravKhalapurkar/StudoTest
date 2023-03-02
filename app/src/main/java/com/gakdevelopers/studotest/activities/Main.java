package com.gakdevelopers.studotest.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.fragments.Home;
import com.gakdevelopers.studotest.fragments.Leaderboard;
import com.gakdevelopers.studotest.fragments.MyCourses;
import com.gakdevelopers.studotest.fragments.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Main extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Home home = new Home();
    MyCourses myCourses  = new MyCourses();
    Leaderboard leaderboard = new Leaderboard();
    Profile profile = new Profile();

    TextView txtUserName;

    String userName;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();

        txtUserName = (TextView) findViewById(R.id.txtUserName);

        Intent intent = getIntent();
        txtUserName.setText("" + intent.getStringExtra("fullName"));

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
}