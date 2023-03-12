package com.gakdevelopers.studotest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class Splash extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        DbQuery.g_fireStore = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            DbQuery.loadData("PAID_TESTS", new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    Intent intent = new Intent(Splash.this, Main.class);
                    startTimer(intent);
                }
                @Override
                public void onFailure() {
                    Toast.makeText(Splash.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Splash.this, SignIn.class);

                    startTimer(intent);
                }
            });

        } else {
            Intent intent = new Intent(Splash.this, SignIn.class);
            startTimer(intent);
        }
    }

    private void startTimer(Intent intent) {
        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        },3000);
    }
}