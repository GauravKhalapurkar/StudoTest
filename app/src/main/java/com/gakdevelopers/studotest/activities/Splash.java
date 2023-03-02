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

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mAuth = FirebaseAuth.getInstance();
        DbQuery.g_fireStore = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            DbQuery.loadData("FREE_TESTS", new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    intent = new Intent(Splash.this, Main.class);
                }
                @Override
                public void onFailure() {
                    Toast.makeText(Splash.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            intent = new Intent(Splash.this, SignIn.class);
        }

        Handler h = new Handler();

        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(intent);
                finish();
            }
        },2000);

    }
}