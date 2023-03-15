package com.gakdevelopers.studotest.activities;

import static com.gakdevelopers.studotest.database.DbQuery.g_catList;
import static com.gakdevelopers.studotest.database.DbQuery.loadQuestions;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class StartTest extends AppCompatActivity {

    private TextView txtCategoryName, txtTestTitle, txtQuestions, txtBestScore, txtTime, txtPositiveMarks, txtNegativeMarks;

    private Button btnStartTest;

    private ImageView imgBack;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_start_test);

        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);
        txtTestTitle = (TextView) findViewById(R.id.txtTestTitle);
        txtQuestions = (TextView) findViewById(R.id.txtQuestions);
        txtBestScore = (TextView) findViewById(R.id.txtBestScore);
        txtTime = (TextView) findViewById(R.id.txtTime);
        txtPositiveMarks = (TextView) findViewById(R.id.txtPositiveMarks);
        txtNegativeMarks = (TextView) findViewById(R.id.txtNegativeMarks);

        btnStartTest = (Button) findViewById(R.id.btnStartTest);

        imgBack = (ImageView) findViewById(R.id.imgBack);

        Intent intent = getIntent();
        boolean isFree = intent.getBooleanExtra("isFree", false);

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
                intent.putExtra("isFree", isFree);
                startActivity(intent);
                finish();
            }
        });

        //loading =  ProgressDialog.show(StartTest.this,"Loading","Please Wait",false,false);

        //Toast.makeText(this, "" + DbQuery.g_positive_marks + " - " + DbQuery.g_negative_marks, Toast.LENGTH_SHORT).show();

        txtPositiveMarks.setText("" + DbQuery.g_positive_marks);

        if (DbQuery.g_negative_marks == 0) {
            txtNegativeMarks.setText("" + DbQuery.g_negative_marks);
        } else {
            txtNegativeMarks.setText("-" + DbQuery.g_negative_marks);
        }

        setData(isFree);

        /*loadQuestions(new MyCompleteListener() {
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
        });*/

    }

    private void setData(boolean isFree) {
        txtCategoryName.setText(g_catList.get(DbQuery.g_selected_cat_index).getName());

        if (isFree) {
            txtTestTitle.setText(String.valueOf(DbQuery.g_freeTrialTestList.get(DbQuery.g_selected_test_index).getTestId()));
            txtQuestions.setText(String.valueOf(DbQuery.g_question_list.size()));

            txtBestScore.setText(String.valueOf(DbQuery.g_freeTrialTestList.get(DbQuery.g_selected_test_index).getTopScore()));

            txtTime.setText(String.valueOf(DbQuery.g_freeTrialTestList.get(DbQuery.g_selected_test_index).getTime()) + " m");
        } else {
            txtTestTitle.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTestId()));
            txtQuestions.setText(String.valueOf(DbQuery.g_question_list.size()));

            txtBestScore.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTopScore()));

            txtTime.setText(String.valueOf(DbQuery.g_testList.get(DbQuery.g_selected_test_index).getTime()) + " m");
        }

    }

}