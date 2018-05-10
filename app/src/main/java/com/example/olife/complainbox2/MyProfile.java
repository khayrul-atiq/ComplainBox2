package com.example.olife.complainbox2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class MyProfile extends Fragment {

    private int previousTab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_profile, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("My Profile");

        replaceFragment(new ProfileInformation(),0);


        //ViewPager viewPager = getActivity().findViewById(R.id.viewPagerContainer);
        //setupViewPager(viewPager);


        TabLayout tabLayout = getActivity().findViewById(R.id.tabLayout);
        //tabLayout.setupWithViewPager(viewPager);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if(tab.getPosition()==0) replaceFragment(new ProfileInformation(),tab.getPosition());
                else if (tab.getPosition()==1) replaceFragment(new ProfileAcivity(),tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    private void replaceFragment(Fragment fragment, int currentTab) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        if(previousTab> currentTab) transaction.setCustomAnimations(R.anim.slide_from_left,R.anim.slide_to_right);
        else transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left);

        transaction.replace(R.id.viewPagerContainer, fragment);
        previousTab = currentTab;
        transaction.commit();
    }

/*
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        adapter.addFragment(new ProfileInformation(), "Profile");
        adapter.addFragment(new ProfileAcivity(),"Activity");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

*/

}
