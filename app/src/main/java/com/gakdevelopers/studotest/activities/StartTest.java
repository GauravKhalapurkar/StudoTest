package com.gakdevelopers.studotest.activities;

import static com.gakdevelopers.studotest.database.DbQuery.g_catList;
import static com.gakdevelopers.studotest.database.DbQuery.loadQuestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class StartTest extends AppCompatActivity {

    private TextView txtCategoryName, txtTestTitle, txtQuestions, txtBestScore, txtTime;

    private Button btnStartTest;

    private ImageView imgBack;

    ProgressDialog loading;

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

        loading =  ProgressDialog.show(StartTest.this,"Loading","Please Wait",false,false);

        loadQuestions(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                setData();
                loading.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(StartTest.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

    }

    private void setData() {
        txtCategoryName.setText(g_catList.get(DbQuery.g_selected_cat_index).getName());
        txtTestTitle.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTestId()));
        txtQuestions.setText(String.valueOf(DbQuery.g_question_list.size()));

        txtBestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()) + "%");

        txtTime.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()) + " m");
    }

}