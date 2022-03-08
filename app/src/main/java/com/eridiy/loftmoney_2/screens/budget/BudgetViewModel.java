package com.eridiy.loftmoney_2.screens.budget;

import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eridiy.loftmoney_2.LoftApp;
import com.eridiy.loftmoney_2.R;
import com.eridiy.loftmoney_2.api.LoftAPI;
import com.eridiy.loftmoney_2.api.RemoteItem;
import com.eridiy.loftmoney_2.items.Item;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class BudgetViewModel extends ViewModel {

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<List<Item>> itemsData = new MutableLiveData<>();
    public MutableLiveData<String> error = new MutableLiveData<>();
    public MutableLiveData<Integer> selectedCounter = new MutableLiveData<>(-1);

    public void getData(SharedPreferences sharedPreferences, int currentPosition, LoftAPI loftApi) {
        String position = "";
        if (currentPosition == 0) {
            position = "expense";
        } else {
            position = "income";
        }

        String authToken = sharedPreferences.getString(LoftApp.AUTH_KEY, "");
        Disposable disposable = loftApi.getItems(position, authToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(remoteItems -> {
                    List<Item> Items = new ArrayList<>();
                    for (RemoteItem remoteItem : remoteItems) {
                        Items.add(Item.getInstance(remoteItem));
                    }
                    itemsData.postValue(Items);
                }, throwable -> {
                    error.postValue(throwable.getLocalizedMessage());
                });

        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }
}
