package com.gakdevelopers.studotest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.Main;
import com.gakdevelopers.studotest.activities.SignIn;
import com.gakdevelopers.studotest.activities.SignUp;
import com.gakdevelopers.studotest.database.DbQuery;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {

    private TextView txtFirstLetter, txtName, txtRank, txtScore;

    private CardView cardLogout, cardLeaderboard, cardEditProfile;

    private BottomNavigationView bottomNavigationView;

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

        cardEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
}