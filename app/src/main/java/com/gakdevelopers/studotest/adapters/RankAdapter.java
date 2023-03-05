package com.gakdevelopers.studotest.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.models.Rank;

import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.ViewHolder> {

    private List<Rank> userList;

    public RankAdapter(List<Rank> userList) {
        this.userList = userList;
    }

    @NonNull
    @Override
    public RankAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankAdapter.ViewHolder holder, int position) {
        String name = userList.get(position).getName();
        int score = userList.get(position).getScore();
        int rank = userList.get(position).getRank();

        holder.setData(name, score, rank);
    }

    @Override
    public int getItemCount() {
        if (userList.size() > 25) {
            return 25;
        } else {
            return userList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtScore, txtRank;

        private CardView cardRankItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.txtName);
            txtScore = (TextView) itemView.findViewById(R.id.txtScore);
            txtRank = (TextView) itemView.findViewById(R.id.txtRank);

            cardRankItem = (CardView) itemView.findViewById(R.id.cardRankItem);
        }

        private void setData(String name, int score, int rank) {
            txtName.setText(name);
            txtScore.setText("Score: " + score);
            txtRank.setText("Rank: " + rank);
        }

    }
}
