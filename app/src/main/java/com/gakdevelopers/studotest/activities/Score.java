package com.gakdevelopers.studotest.activities;

import static com.gakdevelopers.studotest.database.DbQuery.g_freeTrialTestList;
import static com.gakdevelopers.studotest.database.DbQuery.g_selected_test_index;
import static com.gakdevelopers.studotest.database.DbQuery.g_testList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.gakdevelopers.studotest.internet.NetworkChangeListener;
import com.gakdevelopers.studotest.models.TestsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Score extends AppCompatActivity {

    private TextView txtScore, txtTimeTaken, txtTotalQuestions, txtCorrect, txtWrong, txtUnattempted, txtViewAnswers, txtHome, txtPositiveMarks, txtNegativeMarks, txtYourRank;

    private CardView cardShowRank, cardShareScore;

    private long timeTaken;

    private ProgressDialog loading;

    private Toolbar toolbar;

    private int finalScore, marksObtained;

    private String displayScore;

    private List<TestsModel> myList = new ArrayList<>();

    private static boolean isFree;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_score);

        txtScore = (TextView) findViewById(R.id.txtScore);
        txtTimeTaken = (TextView) findViewById(R.id.txtTimeTaken);
        txtTotalQuestions = (TextView) findViewById(R.id.txtTotalQuestions);
        txtCorrect = (TextView) findViewById(R.id.txtCorrect);
        txtWrong = (TextView) findViewById(R.id.txtWrong);
        txtUnattempted = (TextView) findViewById(R.id.txtUnattempted);
        txtViewAnswers = (TextView) findViewById(R.id.txtViewAnswers);
        txtHome = (TextView) findViewById(R.id.txtHome);
        txtPositiveMarks = (TextView) findViewById(R.id.txtPositiveMarks);
        txtNegativeMarks = (TextView) findViewById(R.id.txtNegativeMarks);
        txtYourRank = (TextView) findViewById(R.id.txtYourRank);

        cardShowRank = (CardView) findViewById(R.id.cardShowRank);
        cardShareScore = (CardView) findViewById(R.id.cardShareScore);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        isFree = intent.getBooleanExtra("isFree", false);

        Log.d("isFree_IN_SCORE", String.valueOf(isFree));

        try {
            if (isFree) {
                myList = g_freeTrialTestList;
            } else {
                myList = g_testList;
            }

            loading =  ProgressDialog.show(Score.this,"Loading","Please Wait",false,false);

            loadData();
        } catch (Exception e) {
            Toast.makeText(Score.this, "Error Code: 707. Please restart app and try again!", Toast.LENGTH_SHORT).show();
            Log.d("ERROR_CODE", e.getMessage());
        }

        txtViewAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Score.this, Answers.class);
                startActivity(intent);
            }
        });

        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Score.this, Main.class));
                Score.this.finish();
            }
        });

        // here was saveResult();

        cardShareScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Hi, " +
                        "\n\n" +
                        "I scored " + displayScore + " in " + myList.get(g_selected_test_index).getTestId() + " test in the StudoTest App." +
                        "\n\n" +
                        "Let's come together to compete. Download StudoTest now from Google Play Store: \n\n" +
                        "https://play.google.com/store/apps/details?id=" + getApplicationContext().getPackageName();
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Share App");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

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

        txtPositiveMarks.setText(String.valueOf(DbQuery.g_positive_marks));
        if (DbQuery.g_negative_marks == 0) {
            txtNegativeMarks.setText("" + DbQuery.g_negative_marks);
        } else {
            txtNegativeMarks.setText("-" + DbQuery.g_negative_marks);
        }

        marksObtained = (correct * DbQuery.g_positive_marks) - (wrong * DbQuery.g_negative_marks);

        //finalScore = correct * 100 / DbQuery.g_question_list.size();

        finalScore = marksObtained;

        displayScore = String.valueOf(marksObtained) + "/" + String.valueOf(DbQuery.g_question_list.size() * DbQuery.g_positive_marks);

        txtScore.setText("" + displayScore);

        //txtScore.setText(String.valueOf(marksObtained) + "/" + String.valueOf(DbQuery.g_question_list.size() * DbQuery.g_positive_marks));

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

        saveResult();

        DbQuery.loadLeaderboard(isFree, finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                txtYourRank.setText("Your Rank: " + DbQuery.g_rank + " / " + DbQuery.g_all_leader_scores.size());
                loading.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(Score.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });
    }

    private void saveResult() {
        DbQuery.saveResult(isFree, finalScore, new MyCompleteListener() {
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
            startActivity(new Intent(Score.this, Main.class));
            Score.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        startActivity(new Intent(Score.this, Main.class));
        Score.this.finish();
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