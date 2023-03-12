package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.AnswersAdapter;
import com.gakdevelopers.studotest.adapters.ViewAnswersAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class Answers extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private TextView txtHome;

    boolean fromTestAdapter = false;

    int position = 0;

    Intent intent;

    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_answers);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        txtHome = (TextView) findViewById(R.id.txtHome);


        intent = getIntent();

        if (intent != null) {
            fromTestAdapter = intent.getBooleanExtra("fromTestAdapter", false);
            position = intent.getIntExtra("position", 0);
        }

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Answers");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        if (fromTestAdapter) {
            loading =  ProgressDialog.show(Answers.this,"Loading","Please Wait",false,false);

            DbQuery.g_selected_test_index = position;

            DbQuery.loadViewAnswers(new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    ViewAnswersAdapter adapter = new ViewAnswersAdapter(DbQuery.g_view_answers_list);
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
            AnswersAdapter adapter = new AnswersAdapter(DbQuery.g_question_list);
            recyclerView.setAdapter(adapter);
        }

        txtHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Answers.this, Main.class));
                Answers.this.finish();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Answers.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}