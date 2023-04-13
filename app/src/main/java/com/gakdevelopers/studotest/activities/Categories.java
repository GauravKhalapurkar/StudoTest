package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.CategoryAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.internet.NetworkChangeListener;
import com.gakdevelopers.studotest.models.CategoryModel;

import java.security.spec.ECField;
import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    private Toolbar toolbar;

    String categoryName;

    GridView gridView;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_categories);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        gridView = (GridView) findViewById(R.id.gridView);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("" + categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        try {
            CategoryAdapter adapter = new CategoryAdapter(categoryName, DbQuery.g_catList);
            gridView.setAdapter(adapter);
        } catch (Exception e) {
            Toast.makeText(Categories.this, "Error Code: 713. Please restart app and try again!", Toast.LENGTH_SHORT).show();
            Log.d("ERROR_CODE", e.getMessage());
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Categories.this.finish();
        }

        return super.onOptionsItemSelected(item);
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