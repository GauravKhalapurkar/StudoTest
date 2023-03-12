package com.gakdevelopers.studotest.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.Categories;
import com.gakdevelopers.studotest.activities.Main;
import com.gakdevelopers.studotest.activities.Score;
import com.gakdevelopers.studotest.activities.SignIn;
import com.gakdevelopers.studotest.activities.Splash;
import com.gakdevelopers.studotest.adapters.CategoryAdapter;
import com.gakdevelopers.studotest.database.DbQuery;
import com.gakdevelopers.studotest.interfaces.MyCompleteListener;

public class MyCourses extends Fragment {

    GridView gridView;

    ProgressDialog loading;

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

        gridView = (GridView) view.findViewById(R.id.gridView);

        loading =  ProgressDialog.show(getActivity(),"Loading","Please Wait",false,false);

        DbQuery.loadData("PAID_TESTS", new MyCompleteListener() {
            @Override
            public void onSuccess() {
                DbQuery.checkMyCourses(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {

                        DbQuery.loadMyCourses(new MyCompleteListener() {
                            @Override
                            public void onSuccess() {
                                CategoryAdapter adapter = new CategoryAdapter("PAID_TESTS", DbQuery.g_catList);
                                gridView.setAdapter(adapter);

                                loading.dismiss();
                            }

                            @Override
                            public void onFailure() {
                                Toast.makeText(getActivity(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

                                loading.dismiss();
                            }
                        });
                    }

                    @Override
                    public void onFailure() {
                        Toast.makeText(getActivity(), "Something went wrong. Please try again!", Toast.LENGTH_SHORT).show();

                        loading.dismiss();
                    }
                });
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