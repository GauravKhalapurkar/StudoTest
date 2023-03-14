package com.gakdevelopers.studotest.fragments;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

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

        DbQuery.g_fireStore.collection("NOTIFICATIONS")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot snapshots, FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }
                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
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
                                break;
                            }
                        }
                    }
                });

        return view;
    }
}