package com.gakdevelopers.studotest.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.gakdevelopers.studotest.R;

public class StartTest extends AppCompatActivity {

    private TextView txtCategoryName, txtTestTitle, txtQuestions, txtBestScore, txtTime;

    private Button btnStartTest;

    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);

        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);
        txtTestTitle = (TextView) findViewById(R.id.txtTestTitle);
        txtQuestions = (TextView) findViewById(R.id.txtQuestions);
        txtBestScore = (TextView) findViewById(R.id.txtBestScore);
        txtTime = (TextView) findViewById(R.id.txtTime);

        btnStartTest = (Button) findViewById(R.id.btnStartTest);

        imgBack = (ImageView) findViewById(R.id.imgBack);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartTest.this.finish();
            }
        });

        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartTest.this, Question.class);
                startActivity(intent);
                finish();
            }
        });

    }
}