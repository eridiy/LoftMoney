package com.eridiy.loftmoney_2.api;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AuthAPI {

    @GET("./auth")
    Single<AuthResponse> makeLogin(@Query("social_user_id") String socialUserId);

}
