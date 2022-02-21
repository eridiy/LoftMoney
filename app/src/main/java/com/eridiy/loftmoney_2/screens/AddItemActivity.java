package com.eridiy.loftmoney_2.screens;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Toast;

import com.eridiy.loftmoney_2.LoftApp;
import com.eridiy.loftmoney_2.R;
import com.eridiy.loftmoney_2.databinding.ActivityAddItemBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {

    private ActivityAddItemBinding binding;
    public static final String ARG_POSITION = "arg_position";
    private int currentPosition;
    private boolean isEditTextPriceEmpty = true;
    private boolean isEditTextItemEmpty = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();

        if (arguments != null) {
            currentPosition = arguments.getInt(ARG_POSITION);
        }
        validateButton();
        configureLayuot();
        checkTextInput();
    }

    private void checkTextInput() {
        binding.editTextPrice.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isEditTextPriceEmpty = TextUtils.isEmpty(charSequence.toString().trim());
                validateButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        binding.editTextItem.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                isEditTextItemEmpty = TextUtils.isEmpty(charSequence.toString().trim());
                validateButton();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void validateButton() {
        binding.addItem.setEnabled(!isEditTextPriceEmpty && !isEditTextItemEmpty);
    }

    private void configureLayuot() {
        binding.addItem.setOnClickListener(view -> {
            if (binding.editTextItem.getText().toString().equals("") || binding.editTextItem.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_LONG).show();
                return;
            }

            String position = "";
            if (currentPosition == 0) {
                position = "expense";
            } else {
                position = "income";
            }
            Disposable disposable = ((LoftApp) getApplication()).loftAPI.postMoney(
                    Integer.parseInt(binding.editTextPrice.getText().toString()),
                    binding.editTextItem.getText().toString(), position
            )
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(() -> {
                        if (currentPosition == 0) {
                            Toast.makeText(getApplicationContext(), getString(R.string.success_expense_added), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.success_income_added), Toast.LENGTH_LONG).show();
                        }
                        finish();
                    }, throwable -> {
                        Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}
