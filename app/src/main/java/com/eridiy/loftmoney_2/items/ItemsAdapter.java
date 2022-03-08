package com.eridiy.loftmoney_2.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.eridiy.loftmoney_2.R;

import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.MoneyViewHolder> {

    private ItemAdapterClick itemAdapterClick;
    private List<Item> itemList = new ArrayList<>();
    private int currentPosition;
    ;

    public void setData(List<Item> items, int currentPosition) {
        itemList.clear();
        itemList.addAll(items);
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public List<Item> getItemList() {
        return itemList;
    }

    public void updateItem(Item item) {
        int itemPosition = itemList.indexOf(item);
        itemList.set(itemPosition, item);
        notifyItemChanged(itemPosition);
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
        holder.bind(itemList.get(position), currentPosition);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setItemAdapterClick(ItemAdapterClick itemAdapterClick) {
        this.itemAdapterClick = itemAdapterClick;
    }

    public void clearSelections() {
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

        public void bind(Item item, int position) {
            if (position == 0) {
                price.setTextColor(ContextCompat.getColor(price.getContext(), R.color.color_expense));
            } else {
                price.setTextColor(ContextCompat.getColor(price.getContext(), R.color.color_income));
            }
            name.setText(item.getName());
            price.setText(String.valueOf(item.getPrice() + " ₽"));

            itemView.setBackgroundColor(ContextCompat.getColor(itemView.getContext(),
                    item.isSelected() ? R.color.itemSelectionColor : R.color.white));

            // Здесь нужен обычный сетОнКликЛистенер?
            itemView.setOnClickListener(view -> {
                if (itemAdapterClick != null) {
                    itemAdapterClick.onItemClick(item);
                }
            });

            itemView.setOnLongClickListener(view -> {
                if (itemAdapterClick != null) {
                    itemAdapterClick.onItemLongClick(item);
                }
                return true;
            });
        }

    }

}
