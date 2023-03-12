package com.gakdevelopers.studotest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.models.Question;

import java.util.List;

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.ViewHolder> {

    private List<Question> quesList;

    public AnswersAdapter(List<Question> quesList) {
        this.quesList = quesList;
    }

    @NonNull
    @Override
    public AnswersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_item, parent, false);
        return new AnswersAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswersAdapter.ViewHolder holder, int position) {
        String question = quesList.get(position).getQuestion();
        String a = quesList.get(position).getOptionA();
        String b = quesList.get(position).getOptionB();
        String c = quesList.get(position).getOptionC();
        String d = quesList.get(position).getOptionD();
        String explanation = quesList.get(position).getExplanation();
        int selected = quesList.get(position).getSelectedAnswer();
        int answer = quesList.get(position).getAnswer();

        holder.setData(position, question, a, b, c, d, selected, answer, explanation);
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
        }

        private void setData(int position, String question, String a, String b, String c, String d, int selected, int answer, String explanation) {
            txtQuestion.setText(String.valueOf(position+1) + ". " + question);
            txtOptionA.setText("A. " + a);
            txtOptionB.setText("B. " + b);
            txtOptionC.setText("C. " + c);
            txtOptionD.setText("D. " + d);

            if (selected == -1) {
                txtCorrectness.setText("Unattempted");
                txtCorrectness.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreyDark));
                setUnAttemptedOptionColor(answer, R.color.colorGreen, R.color.colorGreyDark);
            } else {
                if (selected == answer) {
                    txtCorrectness.setText("Correct");
                    txtCorrectness.setTextColor(itemView.getContext().getResources().getColor(R.color.colorGreen));
                    setOptionColor(selected, R.color.colorGreen, R.color.colorGreyDark);
                } else {
                    txtCorrectness.setText("Incorrect");
                    txtCorrectness.setTextColor(itemView.getContext().getResources().getColor(R.color.colorRed));
                    setCorrectInCorrectOptions(selected, answer, R.color.colorRed, R.color.colorGreen, R.color.colorGreyDark);
                    //setOptionColor(selected, R.color.colorRed, R.color.colorGreyDark);
                }
            }

            txtExplanation.setText("" + explanation);
        }

        private void setUnAttemptedOptionColor(int answer, int color, int colorSecondary) {
            switch (answer) {
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

        private void setCorrectInCorrectOptions(int selected, int answer, int incorrect, int correct, int colorSecondary) {
            switch (selected) {
                case 1: {
                    switch (answer) {
                        case 2: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                        }
                        break;
                        case 3: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                        }
                        break;
                        case 4: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(correct));
                        }
                        break;
                    }
                }
                break;
                case 2: {
                    switch (answer) {
                        case 1: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                        }
                        break;
                        case 3: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                        }
                        break;
                        case 4: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(correct));
                        }
                        break;
                    }
                }
                break;
                case 3: {
                    switch (answer) {
                        case 1: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                        }
                        break;
                        case 2: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                        }
                        break;
                        case 4: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(correct));
                        }
                        break;
                    }
                }
                break;
                case 4: {
                    switch (answer) {
                        case 1: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                        }
                        break;
                        case 2: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                        }
                        break;
                        case 3: {
                            txtOptionA.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionB.setTextColor(itemView.getContext().getResources().getColor(colorSecondary));
                            txtOptionC.setTextColor(itemView.getContext().getResources().getColor(correct));
                            txtOptionD.setTextColor(itemView.getContext().getResources().getColor(incorrect));
                        }
                        break;
                    }
                }
                break;
                default:
            }
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
