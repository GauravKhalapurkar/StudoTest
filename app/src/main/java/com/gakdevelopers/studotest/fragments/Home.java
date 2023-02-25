package com.gakdevelopers.studotest.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.gakdevelopers.studotest.R;
import com.gakdevelopers.studotest.activities.Categories;
import com.gakdevelopers.studotest.adapters.ViewPagerAdapter;
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.Timer;
import java.util.TimerTask;

public class Home extends Fragment {

    CardView cardFreeTest, cardTestSeries;

    ViewPager mViewPager;

    int[] images = {R.drawable.one, R.drawable.two, R.drawable.three};

    ViewPagerAdapter mViewPagerAdapter;

    SpringDotsIndicator dot1;

    int currentPage = 0;
    Timer timer;

    public Home() {
    }

    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        cardFreeTest = (CardView) view.findViewById(R.id.cardFreeTest);
        cardTestSeries = (CardView) view.findViewById(R.id.cardTestSeries);

        mViewPager = (ViewPager) view.findViewById(R.id.viewPagerIntro);

        dot1 =  view.findViewById(R.id.spring_dots_indicator);

        loadCarouselContent();

        cardFreeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Categories.class);
                intent.putExtra("categoryName", "Free Tests");
                startActivity(intent);
            }
        });

        cardTestSeries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Categories.class);
                intent.putExtra("categoryName", "Test Series");
                startActivity(intent);
            }
        });

        return view;
    }

    private void loadCarouselContent() {
        mViewPagerAdapter = new ViewPagerAdapter(getActivity(), images);

        mViewPager.setAdapter(mViewPagerAdapter);

        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == 5) {
                    currentPage = 0;
                }
                mViewPager.setCurrentItem(currentPage++, true);
            }
        };

        timer = new Timer();
        timer.schedule(new TimerTask() { // task to be scheduled
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);

        dot1.setViewPager(mViewPager);
    }

}