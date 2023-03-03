package com.gakdevelopers.studotest.activities;

import static com.gakdevelopers.studotest.database.DbQuery.g_catList;
import static com.gakdevelopers.studotest.database.DbQuery.g_question_list;
import static com.gakdevelopers.studotest.database.DbQuery.g_selected_cat_index;
import static com.gakdevelopers.studotest.database.DbQuery.g_selected_test_index;
import static com.gakdevelopers.studotest.database.DbQuery.g_testList;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.QuestionsAdapter;
import com.gakdevelopers.studotest.database.DbQuery;

import java.util.concurrent.TimeUnit;

public class Question extends AppCompatActivity {

    private TextView txtQuestionId, txtTimer, txtSubmit, txtCategoryName, txtClearSelection, txtMarkForReview;

    private ImageView imgBookmark, imgQuestionsListGrid, imgPrevQuestion, imgNextQuestion;

    private RecyclerView recyclerQuestion;

    private int questionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        txtQuestionId = (TextView) findViewById(R.id.txtQuestionId);
        txtTimer = (TextView) findViewById(R.id.txtTimer);
        txtSubmit = (TextView) findViewById(R.id.txtSubmit);
        txtCategoryName = (TextView) findViewById(R.id.txtCategoryName);
        txtClearSelection = (TextView) findViewById(R.id.txtClearSelection);
        txtMarkForReview = (TextView) findViewById(R.id.txtMarkForReview);

        imgBookmark = (ImageView) findViewById(R.id.imgBookmark);
        imgQuestionsListGrid = (ImageView) findViewById(R.id.imgQuestionsListGrid);
        imgPrevQuestion = (ImageView) findViewById(R.id.imgPrevQuestion);
        imgNextQuestion = (ImageView) findViewById(R.id.imgNextQuestion);

        recyclerQuestion = (RecyclerView) findViewById(R.id.recyclerQuestion);

        questionId = 0;

        QuestionsAdapter questionsAdapter = new QuestionsAdapter(g_question_list);
        recyclerQuestion.setAdapter(questionsAdapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerQuestion.setLayoutManager(layoutManager);

        txtQuestionId.setText("1/" + String.valueOf(g_question_list.size()));
        txtCategoryName.setText(g_catList.get(g_selected_cat_index).getName());

        setSnapHelper();

        setOnClickListeners();

        startTimer();

    }

    private void setSnapHelper() {
        SnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerQuestion);

        recyclerQuestion.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                View view = snapHelper.findSnapView(recyclerView.getLayoutManager());
                questionId = recyclerView.getLayoutManager().getPosition(view);

                txtQuestionId.setText(String.valueOf(questionId+1) + "/" + String.valueOf(g_question_list.size()));
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void setOnClickListeners() {
        imgPrevQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionId > 0) {
                    recyclerQuestion.smoothScrollToPosition(questionId - 1);
                }
            }
        });

        imgNextQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (questionId < g_question_list.size() - 1) {
                    recyclerQuestion.smoothScrollToPosition(questionId + 1);
                }
            }
        });
    }

    private void startTimer() {
        long totalTime = g_testList.get(g_selected_test_index).getTime() * 60 * 1000;

        CountDownTimer timer = new CountDownTimer(totalTime + 1000, 1000) {
            @Override
            public void onTick(long remainingTime) {
                String time = String.format("%02d:%02d mins",
                        TimeUnit.MILLISECONDS.toMinutes(remainingTime),
                        TimeUnit.MILLISECONDS.toSeconds(remainingTime) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(remainingTime)));

                txtTimer.setText(time);
            }

            @Override
            public void onFinish() {

            }
        };

        timer.start();
    }

}