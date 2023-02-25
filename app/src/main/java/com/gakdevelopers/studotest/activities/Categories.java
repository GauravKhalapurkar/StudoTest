package com.gakdevelopers.studotest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gakdevelopers.studotest.R;

import org.w3c.dom.Text;

public class Categories extends AppCompatActivity {

    TextView txtCategoryName;

    String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);

        Intent intent = getIntent();
        categoryName = intent.getStringExtra("categoryName");

        txtCategoryName.setText(categoryName);

    }

    public void moveToPrevious(View view) {
        Intent intent = new Intent(Categories.this, Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
//        startActivity(new Intent(Categories.this, Main.class));
//        Categories.this.finish();
    }
}