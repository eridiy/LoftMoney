package com.eridiy.loftmoney_2;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.eridiy.loftmoney_2.api.RemoteItem;
import com.eridiy.loftmoney_2.api.Response;
import com.eridiy.loftmoney_2.items.Item;
import com.eridiy.loftmoney_2.items.ItemsAdapter;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BudgetFragment extends Fragment {

    private static final String ARG_CURRENT_POSITION = "current_position";

    private RecyclerView itemsView;
    private int currentPosition;
    private ItemsAdapter itemsAdapter = new ItemsAdapter();
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        configureRecyclerView();
    }

    @Override
    public void onResume() {
        super.onResume();

        generateData();
    }

    @Override
    public void onDestroy() {//Здесь по умолчанию в видео выпадал "protected", но Алексей делал в MainActivity
        compositeDisposable.dispose();
        super.onDestroy();
    }

    private void configureRecyclerView() {
        itemsView = getView().findViewById(R.id.rv_items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        itemsView.setLayoutManager(layoutManager);
        itemsView.setAdapter(itemsAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL);
        itemsView.addItemDecoration(dividerItemDecoration);
    }

    private void generateData() {
        Disposable disposable = ((LoftApp) getApplication()).loftAPI.getItems("income")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(Response -> {
                    if (Response.getStatus().equals("success")) {
                        List<Item> Items = new ArrayList<>();

                        for (RemoteItem remoteItem : Response.getItemsList()) {
                            Items.add(Item.getInstance(remoteItem));
                        }

                        itemsAdapter.setData(Items);
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), getString(R.string.connection_lost), Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Toast.makeText(getActivity().getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                });

        compositeDisposable.add(disposable);

    }

    private Object getApplication() {
        return null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_budget, container, false);
    }

    public static BudgetFragment newInstance(int position) {
        BudgetFragment budgetFragment = new BudgetFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_CURRENT_POSITION, position);
        budgetFragment.setArguments(args);
        return budgetFragment;
    }
}