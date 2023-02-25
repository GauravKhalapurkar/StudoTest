package com.gakdevelopers.studotest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gakdevelopers.studotest.R;

public class Leaderboard extends Fragment {

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

        return view;
    }
}