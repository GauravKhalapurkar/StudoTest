package com.gakdevelopers.studotest.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.database.DbQuery;
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

        private TextView txtQuestion;

        private Button btnOptionA, btnOptionB, btnOptionC, btnOptionD, btnPrevSelected;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);

            btnOptionA = (Button) itemView.findViewById(R.id.btnOptionA);
            btnOptionB = (Button) itemView.findViewById(R.id.btnOptionB);
            btnOptionC = (Button) itemView.findViewById(R.id.btnOptionC);
            btnOptionD = (Button) itemView.findViewById(R.id.btnOptionD);

            btnPrevSelected = null;

        }

        private void setData(final int position) {
            txtQuestion.setText(questionsList.get(position).getQuestion());

            btnOptionA.setText(questionsList.get(position).getOptionA());
            btnOptionB.setText(questionsList.get(position).getOptionB());
            btnOptionC.setText(questionsList.get(position).getOptionC());
            btnOptionD.setText(questionsList.get(position).getOptionD());

            btnOptionA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(btnOptionA, 1, position);
                }
            });

            btnOptionB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(btnOptionB, 2, position);
                }
            });

            btnOptionC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(btnOptionC, 3, position);
                }
            });

            btnOptionD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectedOption(btnOptionD, 4, position);
                }
            });

        }

        private void selectedOption(Button button, int option, int questionId) {
            if (btnPrevSelected == null) {
                button.setBackgroundResource(R.drawable.custom_button);
                button.setTextColor(Color.parseColor("#ffffff"));
                //btnPrevSelected.setTextColor(Color.parseColor("#000000"));
                DbQuery.g_question_list.get(questionId).setSelectedAnswer(option);

                btnPrevSelected = button;
            } else {
                if (btnPrevSelected.getId() == button.getId()) {
                    button.setBackgroundResource(R.drawable.custom_button_unselected);
                    btnPrevSelected.setTextColor(Color.parseColor("#000000"));
                    DbQuery.g_question_list.get(questionId).setSelectedAnswer(-1);

                    btnPrevSelected = null;
                } else {
                    btnPrevSelected.setBackgroundResource(R.drawable.custom_button_unselected);
                    button.setBackgroundResource(R.drawable.custom_button);
                    button.setTextColor(Color.parseColor("#ffffff"));
                    btnPrevSelected.setTextColor(Color.parseColor("#000000"));

                    DbQuery.g_question_list.get(questionId).setSelectedAnswer(option);

                    btnPrevSelected = button;
                }
            }
        }

    }

}
