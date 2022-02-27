package com.eridiy.loftmoney_2.screens.login;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.eridiy.loftmoney_2.api.AuthAPI;
import com.eridiy.loftmoney_2.api.AuthResponse;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {

    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public MutableLiveData<String> messageString = new MutableLiveData<>("");
    public MutableLiveData<String> authToken = new MutableLiveData<>("");

    @Override
    protected void onCleared() {
        compositeDisposable.dispose();
        super.onCleared();
    }

    void makeLogin(AuthAPI authAPI, String userId) {
        compositeDisposable.add(authAPI.makeLogin(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    authToken.postValue(authResponse.getAuthToken());
                }, throwable -> {
                    messageString.postValue(throwable.getLocalizedMessage());
                }));
    }
}

