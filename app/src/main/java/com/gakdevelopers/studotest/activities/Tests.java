package com.gakdevelopers.studotest.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ProgressBar;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.TestAdapter;
import com.gakdevelopers.studotest.models.TestModel;

import java.util.ArrayList;
import java.util.List;

public class Tests extends AppCompatActivity {

    private Toolbar toolbar;

    private RecyclerView recyclerView;

    private List<TestModel> testList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tests);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        int categoryIndex = getIntent().getIntExtra("categoryIndex", 0);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(Categories.categoryModelList.get(categoryIndex).getName());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        loadTestData();

        TestAdapter adapter = new TestAdapter(testList);
        recyclerView.setAdapter(adapter);

    }

    private void loadTestData() {
        testList = new ArrayList<>();
        testList.add(new TestModel("1", 20, 90));
        testList.add(new TestModel("2", 30, 90));
        testList.add(new TestModel("3", 15, 60));
        testList.add(new TestModel("4", 5, 90));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            Tests.this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
}