package com.eridiy.loftmoney_2.items;

import com.eridiy.loftmoney_2.api.RemoteItem;

public class Item {

    private final String name;
    private final int price;

    public Item(String name, int price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public static Item getInstance(RemoteItem remoteItem) {
        return new Item(remoteItem.getName(), (int) remoteItem.getPrice()); //тут обратить внимание на разницу с String
    }
}
