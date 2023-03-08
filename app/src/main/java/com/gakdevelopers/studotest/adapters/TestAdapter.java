package com.gakdevelopers.studotest.adapters;

import static com.gakdevelopers.studotest.database.DbQuery.loadQuestions;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.Answers;
import com.gakdevelopers.studotest.activities.Score;
import com.gakdevelopers.studotest.activities.StartTest;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
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
        boolean live = testList.get(position).isLive();
        holder.setData(position, testTitle, progress, attempt, live);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtComingSoon, txtTestTitle, txtAttempt, txtViewAnswers;
        private TextView txtProgressPercent;
        private ProgressBar progressBar;
        private CardView cardTest;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtComingSoon = (TextView) itemView.findViewById(R.id.txtComingSoon);
            txtTestTitle = (TextView) itemView.findViewById(R.id.txtTestTitle);
            txtAttempt = (TextView) itemView.findViewById(R.id.txtAttempt);
            txtViewAnswers = (TextView) itemView.findViewById(R.id.txtViewAnswers);
            txtProgressPercent = (TextView) itemView.findViewById(R.id.txtProgressPercent);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            cardTest = (CardView) itemView.findViewById(R.id.cardTest);

            txtViewAnswers.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Answers.class);
                    intent.putExtra("fromTestAdapter", true);
                    intent.putExtra("position", getAdapterPosition());
                    itemView.getContext().startActivity(intent);
                }
            });
        }

        private void setData(final int position, String testTitle, int progress, int attempt, boolean live) {

            if (!live) {
                cardTest.setAlpha(0.1F);
                txtComingSoon.setVisibility(View.VISIBLE);
            } else {
                cardTest.setAlpha(1);
                txtComingSoon.setVisibility(View.GONE);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (attempt < 3) {
                            DbQuery.g_selected_test_index = position;

                            loadQuestions(new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    Intent intent = new Intent(itemView.getContext(), StartTest.class);
                                    itemView.getContext().startActivity(intent);
                                }

                                @Override
                                public void onFailure() {

                                }
                            });
                        } else {
                            Toast.makeText(itemView.getContext(), "Maximum attempts reached.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            txtTestTitle.setText("" + testTitle);
            txtProgressPercent.setText(String.valueOf(progress) + "% Marks Obtained");
            txtAttempt.setText("ATTEMPT " + String.valueOf(attempt) + " OF 3");
            progressBar.setProgress(progress);

            if (attempt > 0) {
                txtViewAnswers.setVisibility(View.VISIBLE);
            } else {
                txtViewAnswers.setVisibility(View.GONE);
            }

        }
    }
}
