package com.eridiy.loftmoney_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.eridiy.loftmoney_2.databinding.ActivityAddItemBinding;
import com.eridiy.loftmoney_2.databinding.ActivityMainBinding;
import com.eridiy.loftmoney_2.items.Item;

public class AddItemActivity extends AppCompatActivity {

    private ActivityAddItemBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddItemBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = binding.editTextItem.getText().toString();
                int price = Integer.parseInt(binding.editTextPrice.getText().toString());
                Intent intent = new Intent();
                intent.putExtra(MainActivity.ARG_ADD_ITEM_NAME, name);
                intent.putExtra(MainActivity.ARG_ADD_ITEM_PRICE, price);
                setResult(MainActivity.ARG_EXTRA, intent);
                finish();
            }
        });

    }
}
