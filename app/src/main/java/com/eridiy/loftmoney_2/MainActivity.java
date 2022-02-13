package com.eridiy.loftmoney_2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    TabLayout tabs;
    ViewPager2 pages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabs = findViewById(R.id.tabs);
        pages = findViewById(R.id.pages);
        pages.setAdapter(new MainPagerAdapter(this));

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabs, pages, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(getResources().getStringArray(R.array.main_pager_titles)[position]);
            }
        });
tabLayoutMediator.attach();
    }

    private class MainPagerAdapter extends FragmentStateAdapter {

        public MainPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {

            return BudgetFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }
}