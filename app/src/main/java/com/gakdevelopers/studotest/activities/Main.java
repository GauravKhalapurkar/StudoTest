package com.gakdevelopers.studotest.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.fragments.Home;
import com.gakdevelopers.studotest.fragments.Notifications;
import com.gakdevelopers.studotest.fragments.MyCourses;
import com.gakdevelopers.studotest.fragments.Profile;
import com.gakdevelopers.studotest.internet.NetworkChangeListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class Main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;

    BottomNavigationView bottomNavigationView;

    Home home = new Home();
    MyCourses myCourses = new MyCourses();
    Notifications notifications = new Notifications();
    Profile profile = new Profile();

    TextView txtFirstLetter, txtEmail, txtTelegramGroup;

    BottomSheetDialog dialogTerms, dialogRefund, dialogPrivacy, dialogContact, dialogAbout;

    ImageView imgClose;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

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
                    case R.id.item_notifications:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, notifications).commit();
                        return true;
                    case R.id.item_profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                        return true;

                }
                return false;
            }
        });

        dialogTerms = new BottomSheetDialog(this);
        dialogRefund = new BottomSheetDialog(this);
        dialogPrivacy = new BottomSheetDialog(this);
        dialogContact = new BottomSheetDialog(this);
        dialogAbout = new BottomSheetDialog(this);

        dialogTerms.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialogRefund.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialogPrivacy.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialogContact.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialogAbout.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, home).commit();
                break;
            case R.id.item_my_courses:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, myCourses).commit();
                break;
            case R.id.item_notifications:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, notifications).commit();
                break;
            case R.id.item_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.container, profile).commit();
                break;
            case R.id.item_about: {
                createAboutDialog();
                dialogAbout.show();
            }
            break;
            case R.id.item_contact: {
                createContactDialog();
                dialogContact.show();
            }
            break;
            case R.id.item_privacy_policy: {
                createPrivacyDialog();
                dialogPrivacy.show();
            }
            break;
            case R.id.item_terms_and_conditions: {
                createTermsDialog();
                dialogTerms.show();
            }
            break;
            case R.id.item_refund_policy: {
                createRefundDialog();
                dialogRefund.show();
            }
            break;
            case R.id.item_share:
                shareApp();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void createAboutDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_about_us, null, false);
        imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAbout.dismiss();
            }
        });
        dialogAbout.setContentView(view);
    }

    private void createContactDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_contact, null, false);
        txtTelegramGroup = view.findViewById(R.id.txtTelegramGroup);
        txtTelegramGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/mppscmcqgroup")));
            }
        });
        imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogContact.dismiss();
            }
        });
        dialogContact.setContentView(view);
    }

    private void createPrivacyDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_privacy_policy, null, false);
        imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogPrivacy.dismiss();
            }
        });
        dialogPrivacy.setContentView(view);
    }

    private void createTermsDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_terms_and_conditions, null, false);
        imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogTerms.dismiss();
            }
        });
        dialogTerms.setContentView(view);
    }

    private void createRefundDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_refund_policy, null, false);
        imgClose = view.findViewById(R.id.imgClose);
        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogRefund.dismiss();
            }
        });
        dialogRefund.setContentView(view);
    }

    private void shareApp() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Hey! Install this Awesome App for Practicing Exam oriented MCQ's. " + "\n\n" +
                "Download Now from Google Play Store: https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share App");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }
}