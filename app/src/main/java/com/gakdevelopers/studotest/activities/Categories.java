package com.gakdevelopers.studotest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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

    TextView txtCategoryName;

    String categoryName;

    GridView gridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);

        gridView = (GridView) findViewById(R.id.gridView);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");

        txtCategoryName.setText("" + categoryName);

        //loadCategories();

        CategoryAdapter adapter = new CategoryAdapter(DbQuery.g_catList);
        gridView.setAdapter(adapter);

    }

    public void moveToPrevious(View view) {
        Intent intent = new Intent(Categories.this, Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        startActivity(new Intent(Categories.this, Main.class));
//        Categories.this.finish();
    }
}