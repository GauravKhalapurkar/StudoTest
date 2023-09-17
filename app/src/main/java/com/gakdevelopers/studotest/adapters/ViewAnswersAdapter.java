package com.gakdevelopers.studotest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.models.Question;
import com.gakdevelopers.studotest.models.ViewAnswer;

import java.util.ArrayList;
import java.util.List;

public class ViewAnswersAdapter extends RecyclerView.Adapter<ViewAnswersAdapter.ViewHolder> {

    private List<ViewAnswer> quesList;

    public ViewAnswersAdapter(List<ViewAnswer> quesList) {
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public ViewAnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        return new ViewAnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewAnswersAdapter.ViewHolder holder, int position) {
        String question = quesList.get(position).getQuestion();
        String a = quesList.get(position).getOptionA();
        String b = quesList.get(position).getOptionB();
        String c = quesList.get(position).getOptionC();
        String d = quesList.get(position).getOptionD();
        String explanation = quesList.get(position).getExplanation();
        int answer = quesList.get(position).getAnswer();

        holder.setData(position, question, a, b, c, d, answer, explanation);
    }

    @Override
    public int getItemCount() {
        return quesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtQuestion, txtOptionA, txtOptionB, txtOptionC, txtOptionD, txtCorrectness, txtExplanation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtQuestion = (TextView) itemView.findViewById(R.id.txtQuestion);
            txtOptionA = (TextView) itemView.findViewById(R.id.txtOptionA);
            txtOptionB = (TextView) itemView.findViewById(R.id.txtOptionB);
            txtOptionC = (TextView) itemView.findViewById(R.id.txtOptionC);
            txtOptionD = (TextView) itemView.findViewById(R.id.txtOptionD);
            txtCorrectness = (TextView) itemView.findViewById(R.id.txtCorrectness);
            txtExplanation = (TextView) itemView.findViewById(R.id.txtExplanation);

            txtCorrectness.setVisibility(View.GONE);
        }

        private void setData(int position, String question, String a, String b, String c, String d, int answer, String explanation) {
            txtQuestion.setText(String.valueOf(position+1) + ". " + question);
            txtOptionA.setText("A. " + a);
            txtOptionB.setText("B. " + b);
            txtOptionC.setText("C. " + c);
            txtOptionD.setText("D. " + d);

            setOptionColor(answer, R.color.colorGreen, R.color.colorPrimaryDark);

            txtExplanation.setText("" + explanation);
        }

        private void setOptionColor(int selected, int color, int colorSecondary) {
            switch (selected) {
                case 1: {
                    txtOptionA.setTextColor(itemView.getContext().getResources().getColor(color));
                    txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                }
                        break;
                case 2: {
                    txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionB.setTextColor(itemView.getContext().getResources().getColor(color));
                    txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                }
                    break;
                case 3: {
                    txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionC.setTextColor(itemView.getContext().getResources().getColor(color));
                    txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                }
                    break;
                case 4: {
                    txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                    txtOptionD.setTextColor(itemView.getContext().getResources().getColor(color));
                }
                    break;
                default:
            }
        }

    }
}
