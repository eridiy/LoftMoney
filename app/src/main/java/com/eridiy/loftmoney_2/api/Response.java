package com.eridiy.loftmoney_2.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Response {

    @SerializedName("status")
    private String status;
    @SerializedName("data")
    private List<RemoteItem>  itemsList;

    public String getStatus() {
        return status;
    }

    public List<RemoteItem> getItemsList() {
        return itemsList;
    }
}
