package com.eridiy.loftmoney_2.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.eridiy.loftmoney_2.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MoneyViewHolder> {

    private ItemAdapterClick itemAdapterClick;
    private List<Item> itemList = new ArrayList<>();
    private int currentPosition;;

    public void setData(List<Item> items, int currentPosition) {
        itemList.clear();
        itemList.addAll(items);
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MoneyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_money, parent, false);
        return new MoneyViewHolder(view, itemAdapterClick, currentPosition);

    }

    @Override
    public void onBindViewHolder(@NonNull MoneyViewHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    static class MoneyViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView price;
        private ItemAdapterClick itemAdapterClick;
        private int currentPosition;

        public MoneyViewHolder(View view, ItemAdapterClick itemAdapterClick, int position) {
            super(view);
            this.itemAdapterClick = itemAdapterClick;
            this.name = view.findViewById(R.id.item_name);
            this.price = view.findViewById(R.id.item_price);
            this.currentPosition = position;
        }

        public void bind(Item item) {
            name.setText(item.getName());
            price.setText(String.valueOf(item.getPrice()));
        }

    }

}
