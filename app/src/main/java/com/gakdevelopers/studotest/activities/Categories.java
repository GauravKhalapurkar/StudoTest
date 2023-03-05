package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.TextView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.CategoryAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.models.CategoryModel;

import java.util.ArrayList;
import java.util.List;

public class Categories extends AppCompatActivity {

    private Toolbar toolbar;

    String categoryName;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        gridView = (GridView) findViewById(R.id.gridView);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("" + categoryName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catList);
        gridView.setAdapter(adapter);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Categories.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}