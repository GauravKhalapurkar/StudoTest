package com.gakdevelopers.studotest.fragments;

import static com.gakdevelopers.studotest.database.DbQuery.g_users_count;
import static com.gakdevelopers.studotest.database.DbQuery.g_users_list;
import static com.gakdevelopers.studotest.database.DbQuery.myPerformance;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.Tests;
import com.gakdevelopers.studotest.adapters.RankAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class Leaderboard extends Fragment {

    private TextView txtTotalUsers, txtScore, txtRank;

    private RecyclerView recyclerView;

    private RankAdapter adapter;

    private ProgressDialog loading;

    public Leaderboard() {
    }

    public static Leaderboard newInstance(String param1, String param2) {
        Leaderboard fragment = new Leaderboard();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        txtTotalUsers = (TextView) view.findViewById(R.id.txtTotalUsers);
        txtScore = (TextView) view.findViewById(R.id.txtScore);
        txtRank = (TextView) view.findViewById(R.id.txtRank);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new RankAdapter(DbQuery.g_users_list);

        recyclerView.setAdapter(adapter);

        loading =  ProgressDialog.show(getContext(),"Loading","Please Wait",false,false);

        DbQuery.getTopUsers(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                adapter.notifyDataSetChanged();

                if (myPerformance.getScore() == 0) {
                    if (!DbQuery.iAmInTopList) {
                        calculateRank();
                    }

                    txtScore.setText("Score: " + myPerformance.getScore());
                    txtRank.setText("Rank: " + myPerformance.getRank());
                }

                loading.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getContext(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
        });

        txtTotalUsers.setText("Total Users: " + DbQuery.g_users_count);

        return view;
    }

    private void calculateRank() {
        int lowTopScore = g_users_list.get(g_users_list.size() - 1).getScore();

        int remainingSlots = g_users_count - 25;

        int mySlot = myPerformance.getScore() * remainingSlots / lowTopScore;

        int rank;

        if (lowTopScore != myPerformance.getScore()) {
            rank = g_users_count - mySlot;
        } else {
            rank = 26;
        }

        myPerformance.setRank(rank);
    }

}