package com.gakdevelopers.studotest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.models.Question;

import java.util.List;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.ViewHolder> {

    private List<Question> questionsList;

    public QuestionsAdapter(List<Question> questionsList) {
        this.questionsList = questionsList;
    }

    @NonNull
    @Override
    public QuestionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.ViewHolder holder, int position) {
        holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtQuestion;

        Button btnOptionA, btnOptionB, btnOptionC, btnOptionD;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);

            btnOptionA = (Button) itemView.findViewById(R.id.btnOptionA);
            btnOptionB = (Button) itemView.findViewById(R.id.btnOptionB);
            btnOptionC = (Button) itemView.findViewById(R.id.btnOptionC);
            btnOptionD = (Button) itemView.findViewById(R.id.btnOptionD);

        }

        private void setData(final int position) {
            txtQuestion.setText(questionsList.get(position).getQuestion());

            btnOptionA.setText(questionsList.get(position).getOptionA());
            btnOptionB.setText(questionsList.get(position).getOptionB());
            btnOptionC.setText(questionsList.get(position).getOptionC());
            btnOptionD.setText(questionsList.get(position).getOptionD());


        }

    }
}
