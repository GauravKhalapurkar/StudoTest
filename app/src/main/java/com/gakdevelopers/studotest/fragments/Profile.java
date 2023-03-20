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
import com.gakdevelopers.studotest.activities.EditProfile;
import com.gakdevelopers.studotest.activities.SignIn;
import com.gakdevelopers.studotest.database.DbQuery;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {

    private TextView txtFirstLetter, txtName, txtRank, txtUserId;

    private CardView cardLogout, cardEditProfile;

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

        txtUserId = (TextView) view.findViewById(R.id.txtUserId);
        txtUserId.setSelected(true);

        cardLogout = (CardView) view.findViewById(R.id.cardLogout);
        cardEditProfile = (CardView) view.findViewById(R.id.cardEditProfile);

        bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);

        String username = DbQuery.myProfile.getName();
        String userID = DbQuery.myProfile.getUserID();
        txtFirstLetter.setText(username.toUpperCase().substring(0,1));
        txtName.setText(username);
        txtUserId.setText(userID);

        cardEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), EditProfile.class);
                    startActivity(intent);
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