package com.gakdevelopers.studotest.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.gakdevelopers.studotest.R;

public class MyCourses extends Fragment {

    public MyCourses() {
    }

    public static MyCourses newInstance(String param1, String param2) {
        MyCourses fragment = new MyCourses();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_courses, container, false);

        return view;
    }
}