package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.TestAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class Tests extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private TestAdapter adapter;

    ProgressDialog loading;

    private TextView txtFreeTrial;

    private Button btnBuyNow;

    private String categoryName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        txtFreeTrial = (TextView) findViewById(R.id.txtFreeTrial);

        btnBuyNow = (Button) findViewById(R.id.btnBuyNow);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        String testType = intent.getStringExtra("testType");
        categoryName = intent.getStringExtra("categoryName");

        if (testType.equals("PAID_TESTS")) {
            txtFreeTrial.setVisibility(View.VISIBLE);
            btnBuyNow.setVisibility(View.VISIBLE);
        }

        loading =  ProgressDialog.show(Tests.this,"Loading","Please Wait",false,false);

        DbQuery.checkUsersCourses(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Log.d("DOC_EXIST", "yes");
            }

            @Override
            public void onFailure() {
                Log.d("DOC_EXIST", "no");
            }
        });

        DbQuery.loadTests(testType, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                DbQuery.loadMyScores(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        adapter = new TestAdapter(DbQuery.g_testList);
                        recyclerView.setAdapter(adapter);

                        loading.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(Tests.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                        loading.dismiss();
                    }
                });
            }

            @Override
            public void onFailure() {
                Toast.makeText(Tests.this, "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Tests.this, BuyTest.class);
                i.putExtra("categoryName", categoryName);
                startActivity(i);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Tests.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}