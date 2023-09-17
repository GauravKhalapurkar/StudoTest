package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.AnswersAdapter;
import com.gakdevelopers.studotest.adapters.ViewAnswersAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.gakdevelopers.studotest.internet.NetworkChangeListener;
import com.gakdevelopers.studotest.models.Question;
import com.gakdevelopers.studotest.models.ViewAnswer;

import java.util.ArrayList;

public class Answers extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private LinearLayout linearFilters;

    private TextView txtHome, txtViewAll, txtCorrect, txtIncorrect, txtUnattempted;

    boolean fromTestAdapter = false;

    int position = 0;

    Intent intent;

    private EditText editSearch;

    private boolean isFree;

    ViewAnswersAdapter adapter;
    AnswersAdapter answersAdapter;

    ProgressDialog loading;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_answers);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        linearFilters = (LinearLayout) findViewById(R.id.linearFilters);

        txtHome = (TextView) findViewById(R.id.txtHome);
        txtViewAll = (TextView) findViewById(R.id.txtViewAll);
        txtCorrect = (TextView) findViewById(R.id.txtCorrect);
        txtIncorrect = (TextView) findViewById(R.id.txtIncorrect);
        txtUnattempted = (TextView) findViewById(R.id.txtUnattempted);

        editSearch = (EditText) findViewById(R.id.editSearch);

        linearFilters.setVisibility(View.GONE);

        intent = getIntent();

        if (intent != null) {
            fromTestAdapter = intent.getBooleanExtra("fromTestAdapter", false);
            position = intent.getIntExtra("position", 0);
            isFree = intent.getBooleanExtra("isFree", false);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Answers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        txtViewAll.setTextColor(getResources().getColor(R.color.colorSecondary));

        txtViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAllAnswers();

                txtViewAll.setTextColor(getResources().getColor(R.color.colorSecondary));

                txtCorrect.setTextColor(getResources().getColor(R.color.colorBlack));

                txtIncorrect.setTextColor(getResources().getColor(R.color.colorBlack));

                txtUnattempted.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        txtCorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("Correct");

                txtCorrect.setTextColor(getResources().getColor(R.color.colorSecondary));

                txtViewAll.setTextColor(getResources().getColor(R.color.colorBlack));

                txtIncorrect.setTextColor(getResources().getColor(R.color.colorBlack));

                txtUnattempted.setTextColor(getResources().getColor(R.color.colorBlack));
            }
        });

        txtIncorrect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("Incorrect");

                txtIncorrect.setTextColor(getResources().getColor(R.color.colorSecondary));

                txtViewAll.setTextColor(getResources().getColor(R.color.colorBlack));

                txtCorrect.setTextColor(getResources().getColor(R.color.colorBlack));

                txtUnattempted.setTextColor(getResources().getColor(R.color.colorBlack));

            }
        });

        txtUnattempted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editSearch.setText("Unattempted");

                txtUnattempted.setTextColor(getResources().getColor(R.color.colorSecondary));

                txtViewAll.setTextColor(getResources().getColor(R.color.colorBlack));

                txtCorrect.setTextColor(getResources().getColor(R.color.colorBlack));

                txtIncorrect.setTextColor(getResources().getColor(R.color.colorBlack));

            }
        });

        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        try {
            if (fromTestAdapter) {

                linearFilters.setVisibility(View.GONE);

                loading =  ProgressDialog.show(Answers.this,"Loading","Please Wait",false,false);

                DbQuery.g_selected_test_index = position;

                DbQuery.loadViewAnswers(isFree, new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        adapter = new ViewAnswersAdapter(DbQuery.g_view_answers_list);
                        recyclerView.setAdapter(adapter);

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(Answers.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });

            } else {
                linearFilters.setVisibility(View.VISIBLE);

                loadAllAnswers();
            }
        } catch (Exception e) {
            Toast.makeText(Answers.this, "Error Code: 701. Please restart app and try again!", Toast.LENGTH_SHORT).show();
            Log.d("ERROR_CODE", e.getMessage());
        }

        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Answers.this, Main.class));
                Answers.this.finish();
            }
        });
    }

    private void filter(String text) {

        ArrayList<Question> filteredQuestion = new ArrayList<>();

        String correctness = null;

        for (Question question: DbQuery.g_question_list) {

            if (question.getSelectedAnswer() == -1) {
                correctness = "Unattempted";
            } else {
                if (question.getSelectedAnswer() == question.getAnswer()) {
                    correctness = "Correct";
                } else {
                    correctness = "Incorrect";
                }
            }

            if (correctness.equals(text)) {
                filteredQuestion.add(question);
            }
        }

        AnswersAdapter answersAdapter11 = new AnswersAdapter(filteredQuestion);
        recyclerView.setAdapter(answersAdapter11);

        /*ArrayList<Question> filteredList = new ArrayList<>();

        for (Question item: DbQuery.g_question_list) {

            Log.d("test_test_ing_new", AnswersAdapter.correctness);
            Log.d("test_test_ing_q", AnswersAdapter.txtQuestion.getText().toString());
            Log.d("test_test_ing_sele", AnswersAdapter.txtCorrectness.getText().toString());

            Log.d("item.getSelect", "" + item.getSelectedAnswer());

            if (AnswersAdapter.correctness.equals(text)) {
                filteredList.add(item);
            }

            if (item.getSelectedAnswer() text) {
                filteredList.add(item);
            }
        }

        answersAdapter.filterList(filteredList);*/
    }

    private void loadAllAnswers() {
        answersAdapter = new AnswersAdapter(DbQuery.g_question_list);
        recyclerView.setAdapter(answersAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Answers.this.finish();
        }

        return super.onOptionsItemSelected(item);
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