package com.example.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;



import com.example.yunmusic.R;

import java.util.ArrayList;
import java.util.List;

public class TabNetPagerFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyPagerAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_net_pager,container,false);
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        setUpViewPager();

        return view;
    }

    private void setUpViewPager() {
        adapter = new MyPagerAdapter(getChildFragmentManager());
        Fragment recommendFragment = new RecommendFragment();
        Fragment friendsFragment = new FriendsFragment();
        Fragment radioStationFragment = new RadioStationFragment();
        adapter.addFragment(recommendFragment,"推荐");
        adapter.addFragment(friendsFragment,"朋友");
        adapter.addFragment(radioStationFragment,"电台");
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
        tabLayout.setupWithViewPager(viewPager);
    }

    class MyPagerAdapter extends FragmentStatePagerAdapter {
        private List<Fragment> fragments = new ArrayList<>();
        private List<String> titles = new ArrayList<>();

        public void addFragment(Fragment fragment,String title) {
            fragments.add(fragment);
            titles.add(title);
        }


        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments.get(i);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }
}
