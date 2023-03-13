package com.gakdevelopers.studotest.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.adapters.CategoryAdapter;
import com.gakdevelopers.studotest.adapters.NotificationsAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class Notifications extends Fragment {

    GridView gridView;

    ProgressDialog loading;

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

        gridView = (GridView) view.findViewById(R.id.gridView);

        loading =  ProgressDialog.show(getActivity(),"Loading","Please Wait",false,false);

        DbQuery.loadNotifications(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                NotificationsAdapter adapter = new NotificationsAdapter(DbQuery.g_notifications);
                gridView.setAdapter(adapter);

                loading.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(getActivity(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

                loading.dismiss();
            }
        });

        return view;
    }
}