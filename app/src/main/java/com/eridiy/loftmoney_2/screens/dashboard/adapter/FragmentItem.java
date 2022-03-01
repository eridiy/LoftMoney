package com.eridiy.loftmoney_2.screens.dashboard.adapter;
import androidx.fragment.app.Fragment;

public class FragmentItem {

    private Fragment fragment;
    private String title;

    public FragmentItem(Fragment fragment, String title) {
        this.fragment = fragment;
        this.title = title;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public String getTitle() {
        return title;
    }
}
