package com.gakdevelopers.studotest.activities;

import static com.gakdevelopers.studotest.database.DbQuery.g_catList;
import static com.gakdevelopers.studotest.database.DbQuery.g_question_list;
import static com.gakdevelopers.studotest.database.DbQuery.g_selected_cat_index;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.QuestionsAdapter;
import com.gakdevelopers.studotest.database.DbQuery;

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

}