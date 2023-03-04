package com.gakdevelopers.studotest.adapters;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.StartTest;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.models.FreeTestsModel;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder> {

    private List<FreeTestsModel> testList;

    public TestAdapter(List<FreeTestsModel> testList) {
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
        holder.setData(position, testTitle, progress);
    }

    @Override
    public int getItemCount() {
        return testList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtTestTitle;
        private TextView txtProgressPercent;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTestTitle = (TextView) itemView.findViewById(R.id.txtTestTitle);
            txtProgressPercent = (TextView) itemView.findViewById(R.id.txtProgressPercent);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
        }

        private void setData(final int position, String testTitle, int progress) {
            txtTestTitle.setText("" + testTitle);
            txtProgressPercent.setText(String.valueOf(progress) + "% Completed");
            progressBar.setProgress(progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DbQuery.g_selected_test_index = position;
                    Intent intent = new Intent(itemView.getContext(), StartTest.class);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
