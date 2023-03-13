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
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class Notifications extends Fragment {

    public Notifications() {
    }

    public static Notifications newInstance(String param1, String param2) {
        Notifications fragment = new Notifications();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        return view;
    }
}