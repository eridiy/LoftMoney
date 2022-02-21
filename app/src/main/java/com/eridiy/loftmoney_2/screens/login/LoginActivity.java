package com.eridiy.loftmoney_2.screens.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.eridiy.loftmoney_2.LoftApp;
import com.eridiy.loftmoney_2.MainActivity;
import com.eridiy.loftmoney_2.R;
import com.eridiy.loftmoney_2.screens.BudgetFragment;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        configureViews();
        configureViewModel();
    }

    private void configureViews() {
        AppCompatButton loginEnterView = findViewById(R.id.loginEnterView);

        loginEnterView.setOnClickListener(v -> {
            loginViewModel.makeLogin(((LoftApp) getApplication()).authAPI);
        });
    }

    private void configureViewModel() {
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.messageString.observe(this, error -> {
            if (!TextUtils.isEmpty(error)) {
                Toast.makeText(this, "error", Toast.LENGTH_LONG).show();  //возможно нужен будет GetApplicationContext
            }
        });

        loginViewModel.authToken.observe(this, authToken -> {
            if (!TextUtils.isEmpty(authToken)) {
                SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.app_name), 0);
                sharedPreferences.edit().putString(LoftApp.AUTH_KEY, authToken).apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }

        });
    }
}
