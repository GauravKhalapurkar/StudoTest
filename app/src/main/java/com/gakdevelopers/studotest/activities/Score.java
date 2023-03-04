package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

import java.util.concurrent.TimeUnit;

public class Score extends AppCompatActivity {

    private TextView txtScore, txtTimeTaken, txtTotalQuestions, txtCorrect, txtWrong, txtUnattempted, txtViewAnswers;

    private CardView cardShowRank, cardShareScore;

    private long timeTaken;

    private ProgressDialog loading;

    private Toolbar toolbar;

    private int finalScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtTimeTaken = (TextView) findViewById(R.id.txtTimeTaken);
        txtTotalQuestions = (TextView) findViewById(R.id.txtTotalQuestions);
        txtCorrect = (TextView) findViewById(R.id.txtCorrect);
        txtWrong = (TextView) findViewById(R.id.txtWrong);
        txtUnattempted = (TextView) findViewById(R.id.txtUnattempted);
        txtViewAnswers = (TextView) findViewById(R.id.txtViewAnswers);

        cardShowRank = (CardView) findViewById(R.id.cardShowRank);
        cardShareScore = (CardView) findViewById(R.id.cardShareScore);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        loading =  ProgressDialog.show(Score.this,"Loading","Please Wait",false,false);

        loadData();

        txtViewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        saveResult();

    }

    private void loadData() {
        int correct = 0, wrong = 0, unAttempted = 0;

        for (int i = 0; i < DbQuery.g_question_list.size(); i++) {
            if (DbQuery.g_question_list.get(i).getSelectedAnswer() == -1) {
                unAttempted++;
            } else {
                if (DbQuery.g_question_list.get(i).getSelectedAnswer() == DbQuery.g_question_list.get(i).getAnswer()) {
                    correct++;
                } else {
                    wrong++;
                }
            }
        }


        finalScore = correct * 100 / DbQuery.g_question_list.size();
        txtScore.setText(String.valueOf(correct) + "/" + String.valueOf(DbQuery.g_question_list.size()));

        timeTaken = getIntent().getLongExtra("timeTaken", 0);
        String time = String.format("%02d:%02d mins",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken)));
        txtTimeTaken.setText(time);


        txtTotalQuestions.setText(String.valueOf(DbQuery.g_question_list.size()));

        txtCorrect.setText(String.valueOf(correct));
        txtWrong.setText(String.valueOf(wrong));
        txtUnattempted.setText(String.valueOf(unAttempted));
    }

    private void saveResult() {
        DbQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                loading.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(Score.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Score.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

}