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
    private EditText edit_text_item;
    private EditText edit_text_price;
    private LinearLayout add_item;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        edit_text_item = findViewById(R.id.edit_text_item);
        edit_text_price = findViewById(R.id.edit_text_price);
        add_item = findViewById(R.id.add_item);

        configureLayuot();
    }

    private void configureLayuot() {
        edit_text_item.setOnClickListener(view -> {
            if (edit_text_item.getText().equals("") || edit_text_price.getText().equals("")) {
                Toast.makeText(getApplicationContext(), getString(R.string.fill_fields), Toast.LENGTH_LONG).show();
                return;
            }

            Disposable disposable = ((LoftApp) getApplication()).loftAPI.postMoney(
                    Integer.parseInt(edit_text_price.getText().toString()),
                    edit_text_item.getText().toString(),
                    "income"
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

    //    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(R.layout.activity_add_item);
//
//        binding.addItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String name = binding.editTextItem.getText().toString();
//                int price = Integer.parseInt(binding.editTextPrice.getText().toString());
//                Intent intent = new Intent();
//                intent.putExtra(MainActivity.ARG_ADD_ITEM_NAME, name);
//                intent.putExtra(MainActivity.ARG_ADD_ITEM_PRICE, price);
//                setResult(MainActivity.ARG_EXTRA, intent);
//                finish();
//            }
//        });
//
//    }
}
