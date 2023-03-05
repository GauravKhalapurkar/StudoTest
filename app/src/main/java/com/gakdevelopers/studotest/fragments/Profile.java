package com.gakdevelopers.studotest.fragments;

import static com.gakdevelopers.studotest.database.DbQuery.g_users_count;
import static com.gakdevelopers.studotest.database.DbQuery.g_users_list;
import static com.gakdevelopers.studotest.database.DbQuery.myPerformance;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.EditProfile;
import com.gakdevelopers.studotest.activities.Main;
import com.gakdevelopers.studotest.activities.SignIn;
import com.gakdevelopers.studotest.activities.SignUp;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {

    private TextView txtFirstLetter, txtName, txtRank, txtScore;

    private CardView cardLogout, cardLeaderboard, cardEditProfile;

    private BottomNavigationView bottomNavigationView;

    private ProgressDialog loading;

    public Profile() {
    }

    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        txtFirstLetter = (TextView) view.findViewById(R.id.txtFirstLetter);
        txtName = (TextView) view.findViewById(R.id.txtName);
        txtRank = (TextView) view.findViewById(R.id.txtRank);
        txtScore = (TextView) view.findViewById(R.id.txtScore);

        cardLeaderboard = (CardView) view.findViewById(R.id.cardLeaderboard);
        cardLogout = (CardView) view.findViewById(R.id.cardLogout);
        cardEditProfile = (CardView) view.findViewById(R.id.cardEditProfile);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        String username = DbQuery.myProfile.getName();
        txtFirstLetter.setText(username.toUpperCase().substring(0,1));
        txtName.setText(username);

        txtScore.setText(String.valueOf(DbQuery.myPerformance.getScore()));

        if (DbQuery.g_users_list.size() == 0) {
            loading =  ProgressDialog.show(getContext(),"Loading","Please Wait",false,false);

            DbQuery.getTopUsers(new MyCompleteListener() {
                @Override
                public void onSuccess() {

                    if (myPerformance.getScore() != 0) {
                        if (!DbQuery.iAmInTopList) {
                            calculateRank();
                        }

                        txtScore.setText("" + myPerformance.getScore());
                        txtRank.setText("" + myPerformance.getRank());
                    }

                    loading.dismiss();
                }

                @Override
                public void onFailure() {
                    Toast.makeText(getContext(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();
                    loading.dismiss();
                }
            });
        } else {
            txtScore.setText("" + myPerformance.getScore());
            if (myPerformance.getScore() != 0)
                txtRank.setText("" + myPerformance.getRank());
        }

        cardEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                    startActivity(intent);
            }
        });

        cardLeaderboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomNavigationView.setSelectedItemId(R.id.item_leaderboard);
            }
        });

        cardLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(getContext(), SignIn.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

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