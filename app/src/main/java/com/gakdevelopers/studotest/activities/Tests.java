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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.FreeTrialTestAdapter;
import com.gakdevelopers.studotest.adapters.TestAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.gakdevelopers.studotest.internet.NetworkChangeListener;

public class Tests extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView, recyclerViewFreeTrial;

    private TestAdapter adapter;

    private FreeTrialTestAdapter freeTrialTestAdapter;

    ProgressDialog loading;

    private Button btnBuyNow;

    private String categoryName;

    //private TextView txtFreeTrial;

    public static boolean isCourseBought = false;

    NetworkChangeListener networkChangeListener = new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_tests);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerViewFreeTrial = (RecyclerView) findViewById(R.id.recyclerViewFreeTrial);
        recyclerViewFreeTrial.setVisibility(View.GONE);

        btnBuyNow = (Button) findViewById(R.id.btnBuyNow);

        //txtFreeTrial = (TextView) findViewById(R.id.txtFreeTrial);
        //txtFreeTrial.setVisibility(View.GONE);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(DbQuery.g_catList.get(DbQuery.g_selected_cat_index).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        LinearLayoutManager layoutManagerFreeTrial = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManagerFreeTrial.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewFreeTrial.setLayoutManager(layoutManagerFreeTrial);

        Intent intent = getIntent();
        String testType = intent.getStringExtra("testType");
        categoryName = intent.getStringExtra("categoryName");

        loading =  ProgressDialog.show(Tests.this,"Loading","Please Wait",false,false);

        try {
            if (!testType.equals("FREE_TESTS")) {
                //txtFreeTrial.setVisibility(View.VISIBLE);

                recyclerViewFreeTrial.setVisibility(View.VISIBLE);

                DbQuery.checkMyCourses(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {

                        isCourseBought = DbQuery.g_my_courses_list.contains(categoryName);

                        if (isCourseBought) {
                            btnBuyNow.setVisibility(View.GONE);
                        } else {
                            btnBuyNow.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onFailure() {
                        Log.d("DOC_EXIST", "no");
                    }
                });
            }

            DbQuery.loadTests(testType, new MyCompleteListener() {
                @Override
                public void onSuccess() {
                    DbQuery.loadMyScores(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {

                            freeTrialTestAdapter = new FreeTrialTestAdapter(testType, DbQuery.g_freeTrialTestList);
                            recyclerViewFreeTrial.setAdapter(freeTrialTestAdapter);

                            adapter = new TestAdapter(testType, DbQuery.g_testList);
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
        } catch (Exception e) {
            Toast.makeText(Tests.this, "Error Code: 712. Please restart app and try again!", Toast.LENGTH_SHORT).show();
            Log.d("ERROR_CODE", e.getMessage());
        }

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