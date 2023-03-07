package com.gakdevelopers.studotest.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.StartTest;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.models.TestsModel;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<TestsModel> testList;

    public TestAdapter(List<TestsModel> testList) {
        this.testList = testList;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        String testTitle = testList.get(position).getTestId();
        int progress = testList.get(position).getTopScore();
        int attempt = testList.get(position).getAttempt();
        holder.setData(position, testTitle, progress, attempt);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTestTitle, txtAttempt, txtViewAnswers;
        private TextView txtProgressPercent;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTestTitle = (TextView) itemView.findViewById(R.id.txtTestTitle);
            txtAttempt = (TextView) itemView.findViewById(R.id.txtAttempt);
            txtViewAnswers = (TextView) itemView.findViewById(R.id.txtViewAnswers);
            txtProgressPercent = (TextView) itemView.findViewById(R.id.txtProgressPercent);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

        private void setData(final int position, String testTitle, int progress, int attempt) {
            txtTestTitle.setText("" + testTitle);
            txtProgressPercent.setText(String.valueOf(progress) + "% Marks Obtained");
            txtAttempt.setText("ATTEMPT " + String.valueOf(attempt) + " OF 3");
            progressBar.setProgress(progress);

            if (attempt > 0) {
                txtViewAnswers.setVisibility(View.VISIBLE);
            } else {
                txtViewAnswers.setVisibility(View.GONE);
            }
            
            txtViewAnswers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(itemView.getContext(), "View answers clicked", Toast.LENGTH_SHORT).show();
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (attempt < 3) {
                        DbQuery.g_selected_test_index = position;
                        Intent intent = new Intent(itemView.getContext(), StartTest.class);
                        itemView.getContext().startActivity(intent);
                    } else {
                        Toast.makeText(itemView.getContext(), "Maximum attempts reached.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
