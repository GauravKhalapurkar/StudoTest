package com.gakdevelopers.studotest.adapters;

import static com.gakdevelopers.studotest.database.DbQuery.ANSWERED;
import static com.gakdevelopers.studotest.database.DbQuery.NOT_VISITED;
import static com.gakdevelopers.studotest.database.DbQuery.UNANSWERED;

import android.content.Context;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.Question;
import com.gakdevelopers.studotest.database.DbQuery;

public class QuestionsGridAdapter extends BaseAdapter {

    private int noOfQuestions;
    private Context context;

    public QuestionsGridAdapter(Context context, int noOfQuestions) {
        this.noOfQuestions = noOfQuestions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return noOfQuestions;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v;

        if (view == null) {
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_grid_item, viewGroup, false);

        } else {
            v = view;
        }

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context instanceof Question) {
                    ((Question) context).goToQuestion(i);
                }
            }
        });

        TextView txtQuestionNo = (TextView) v.findViewById(R.id.txtQuestionNo);

        CardView cardBg = (CardView) v.findViewById(R.id.cardBg);

        txtQuestionNo.setText(String.valueOf(i + 1));

        if (DbQuery.g_question_list.get(i).getStatus() == NOT_VISITED) {
            cardBg.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(), R.color.colorGreyLight)));
        } else if (DbQuery.g_question_list.get(i).getStatus() == UNANSWERED) {
            cardBg.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(), R.color.colorRed)));
        } else if (DbQuery.g_question_list.get(i).getStatus() == ANSWERED) {
            cardBg.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(v.getContext(), R.color.colorGreen)));
        }

        return v;
    }
}
