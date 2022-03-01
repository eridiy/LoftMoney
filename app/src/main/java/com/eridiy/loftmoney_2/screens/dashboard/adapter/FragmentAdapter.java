package com.eridiy.loftmoney_2.screens.dashboard.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;
public class FragmentAdapter extends FragmentPagerAdapter {

    private List<FragmentItem> mFragmentList;

    public FragmentAdapter(List<FragmentItem> fragments,@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mFragmentList = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position).getFragment();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentList.get(position).getTitle();
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
