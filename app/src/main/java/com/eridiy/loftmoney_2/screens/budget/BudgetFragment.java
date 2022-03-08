package com.eridiy.loftmoney_2.screens.budget;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ActionMode;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.eridiy.loftmoney_2.LoftApp;
import com.eridiy.loftmoney_2.R;
import com.eridiy.loftmoney_2.api.LoftAPI;
import com.eridiy.loftmoney_2.databinding.FragmentBudgetBinding;
import com.eridiy.loftmoney_2.items.Item;
import com.eridiy.loftmoney_2.items.ItemAdapterClick;
import com.eridiy.loftmoney_2.items.ItemsAdapter;
import com.eridiy.loftmoney_2.screens.AddItemActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.function.Consumer;

import io.reactivex.disposables.CompositeDisposable;

public class BudgetFragment extends Fragment implements ActionModeListener {


    private BudgetViewModel viewModel;
    SharedPreferences sharedPreferences;
    LoftAPI loftAPI;
    private ActionMode actionMode;
    public TabLayout tabs;
    private ViewPager2 pages;


    private static final String ARG_CURRENT_POSITION = "current_position";

    private FragmentBudgetBinding binding;
    private int currentPosition;
    private ItemsAdapter itemsAdapter = new ItemsAdapter();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        loftAPI = ((LoftApp) getActivity().getApplication()).loftAPI;

        configureViewModel();
        configureRecyclerView();

        binding.addFab.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AddItemActivity.class);
            intent.putExtra(AddItemActivity.ARG_POSITION, currentPosition);
            startActivity(intent);
        });

        binding.addFab.setColorFilter(Color.argb(255, 255, 255, 255));

        //это анимация скрытия ФАБ
        binding.rvItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0 && binding.addFab.getVisibility() == View.VISIBLE) {
                    binding.addFab.hide();
                } else if (dy < 0 && binding.addFab.getVisibility() != View.VISIBLE) {
                    binding.addFab.show();
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        viewModel.getData(sharedPreferences, currentPosition, loftAPI);
        configureSwipeRefresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        itemsAdapter.setItemAdapterClick(new ItemAdapterClick() {   //обратить внимание, ставить кликер после супера или после аргумента?
            @Override
            public void onItemClick(Item item) {
                // сюда поместить что-то чтобы клики по йтемам проходили только из ActionMode
                //но при этом просто анимировались как нажатие без AM
                if (actionMode == null) {
                    Toast.makeText(getContext(), "it's just a click on " + item.getName(), Toast.LENGTH_SHORT).show();
                } else {
                    item.setSelected(!item.isSelected());
                    itemsAdapter.updateItem(item);
                    checkSelectedCount();
                    Toast.makeText(getContext(), "click in ActionMode ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemLongClick(Item item) {
                actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
                Toast.makeText(getContext(), "ActionMode activated", Toast.LENGTH_LONG).show();
                item.setSelected(true);
                itemsAdapter.updateItem(item);
                checkSelectedCount();
            }

        });

        if (getArguments() != null) {
            currentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
        }
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            actionMode = mode;
            actionMode.getMenuInflater().inflate(R.menu.menu_action_mode, menu);
            binding.addFab.hide();
            actionMode.setTitle(getString(R.string.selected) + " " +  "?"); // сюда Count нужен, но как?!?!
            return true;
        }

        //? может изменение цвета тулбара надо сюда лучше сделать без лишних действий с адаптерами и т.п.?

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_menu_delete:
                    Toast.makeText(getContext(), "deleted!", Toast.LENGTH_SHORT).show();
//                    clearSelectedItems();
                    return true;
            }
            return false;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
            Toast.makeText(getContext(), "закрыто", Toast.LENGTH_SHORT).show(); // снять выделение всех айтемов
            itemsAdapter.clearSelections();
        }
    };

    @Override
    public void onDestroyView() {
        compositeDisposable.dispose();
        super.onDestroyView();
        binding = null;
    }

    private void configureViewModel() {
        viewModel = new ViewModelProvider(getActivity()).get(BudgetViewModel.class);

        viewModel.itemsData.observe(getViewLifecycleOwner(), list -> {
            itemsAdapter.setData(list, currentPosition);
        });
        viewModel.error.observe(getViewLifecycleOwner(), error -> {
            Toast.makeText(getActivity().getApplicationContext(), error, Toast.LENGTH_LONG).show();
        });

/*        viewModel.selectedCounter.observe(getViewLifecycleOwner(), newCount -> {

        });*/
    }

    private void configureRecyclerView() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        binding.rvItems.setLayoutManager(layoutManager);
        binding.rvItems.setAdapter(itemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        binding.rvItems.addItemDecoration(dividerItemDecoration);
    }

    private void configureSwipeRefresh() {
        SwipeRefreshLayout swipeRefreshLayout = binding.budgetSwipeRefreshView;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            viewModel.getData(sharedPreferences, currentPosition, loftAPI);
            swipeRefreshLayout.setRefreshing(false);
        });

    }

/*    private void clearSelectedItems() {
        Fragment fragment = getParentFragmentManager().getFragments().get(pages.getCurrentItem());
        if (fragment instanceof ActionModeListener) {
            ((ActionModeListener) fragment).onClearSelectedClick();
        }
    }*/

    public static BudgetFragment newInstance(int position) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, position);
        budgetFragment.setArguments(args);
        return budgetFragment;
    }

    private void checkSelectedCount() {
        int selectedItemsCount = 0;
        for (Item item : itemsAdapter.getItemList()) {
            if (item.isSelected()) {
                    selectedItemsCount++;
            }
        }

        viewModel.selectedCounter.postValue(selectedItemsCount);
    }

    @Override
    public void onClearSelectedClick() {

    }

    @Override
    public void onCounterChanged(int newCount) {

    }
}