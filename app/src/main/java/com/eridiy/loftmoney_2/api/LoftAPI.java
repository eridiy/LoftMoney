package com.eridiy.loftmoney_2.api;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoftAPI {

    @GET("./items")
    Single<Response> getItems(@Query("type") String type);

    @POST("./items/add")
    @FormUrlEncoded
    Completable postMoney(@Field("price") int price, @Query("name") String name,
                          @Query("type") String type);
}
