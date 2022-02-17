package com.eridiy.loftmoney_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.eridiy.loftmoney_2.databinding.ActivityAddItemBinding;
import com.eridiy.loftmoney_2.databinding.ActivityMainBinding;
import com.eridiy.loftmoney_2.items.Item;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class AddItemActivity extends AppCompatActivity {

    private ActivityAddItemBinding binding;
    public static final String ARG_POSITION = "arg_position";
    private int currentPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle arguments = getIntent().getExtras();

        if(arguments != null) {
            currentPosition = arguments.getInt(ARG_POSITION);
        }

        configureLayuot();
    }

    private void configureLayuot() {
        binding.addItem.setOnClickListener(view -> {
            if (binding.editTextItem.getText().equals("") || binding.editTextItem.getText().equals("")) {
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
                        Toast.makeText(getApplicationContext(), getString(R.string.success_added), Toast.LENGTH_LONG).show();
                        finish();
                    }, throwable -> {
                        Toast.makeText(getApplicationContext(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    });
        });
    }
}
