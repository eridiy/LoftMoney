package com.eridiy.loftmoney_2.screens.budget;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eridiy.loftmoney_2.LoftApp;
import com.eridiy.loftmoney_2.R;
import com.eridiy.loftmoney_2.api.RemoteItem;
import com.eridiy.loftmoney_2.databinding.FragmentBudgetBinding;
import com.eridiy.loftmoney_2.items.Item;
import com.eridiy.loftmoney_2.items.ItemsAdapter;
import com.eridiy.loftmoney_2.screens.AddItemActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment {

    private static final String ARG_CURRENT_POSITION = "current_position";
    public static final String ARG_ADD_ITEM_NAME = "arg_add_item_name";
    public static final String ARG_ADD_ITEM_PRICE = "arg_add_item_price";
    public static final int ARG_EXTRA = 500;

    private FragmentBudgetBinding binding;
    private int currentPosition;
    private ItemsAdapter itemsAdapter = new ItemsAdapter();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBudgetBinding.inflate(inflater, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configureRecyclerView();

        binding.addFab.setOnClickListener(view1 -> {
            Intent intent = new Intent(getContext(), AddItemActivity.class);
            intent.putExtra(AddItemActivity.ARG_POSITION, currentPosition);
            startActivity(intent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        configureSwipeRefresh();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            currentPosition = getArguments().getInt(ARG_CURRENT_POSITION);
        }
    }


    @Override
    public void onDestroyView() {
        compositeDisposable.dispose();
        super.onDestroyView();
        binding = null;
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
            getData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void getData() {
        String position = "";
        if (currentPosition == 0) {
            position = "expense";
        } else {
            position = "income";
        }

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");

        Disposable disposable = ((LoftApp) getActivity().getApplication()).loftAPI.getItems(position, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(remoteItems -> {
                    List<Item> Items = new ArrayList<>();

                        for (RemoteItem remoteItem : remoteItems) {
                            Items.add(Item.getInstance(remoteItem));
                        }

                        itemsAdapter.setData(Items, currentPosition);

                }, throwable -> {
                    Toast.makeText(getActivity().getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });

        compositeDisposable.add(disposable);

    }

    private Object getApplication() {
        return null;
    }

    public static BudgetFragment newInstance(int position) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, position);
        budgetFragment.setArguments(args);
        return budgetFragment;
    }
}