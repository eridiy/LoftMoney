package com.eridiy.loftmoney_2.screens.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telecom.Call;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.lifecycle.ViewModelProvider;

import com.eridiy.loftmoney_2.LoftApp;
import com.eridiy.loftmoney_2.MainActivity;
import com.eridiy.loftmoney_2.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private GoogleSignInClient mGoogleSignInClient;
    private int RC_SIGN_IN = 666;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();

        configureViews();
        configureViewModel();
        configureGoogleAuth();
    }

    private void configureGoogleAuth() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }


    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            // Signed in successfully, show authenticated UI.
            if (account != null) {
                loginViewModel.makeLogin(((LoftApp) getApplication()).authAPI, account.getId());
            } else {
                Log.e("TAG", "Can't parse account");
            }
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void configureViews() {
        AppCompatButton loginEnterView = findViewById(R.id.loginEnterView);

        loginEnterView.setOnClickListener(v -> {
//            loginViewModel.makeLogin(((LoftApp) getApplication()).authAPI);
            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
            startActivityForResult(signInIntent, RC_SIGN_IN);
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
                this.overridePendingTransition(R.anim.alpha_login, R.anim.alpha_login);
            }

        });
    }
}
