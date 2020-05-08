package com.uc3m.it.CoinPocket.data;

import com.uc3m.it.CoinPocket.response.RatioResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Services {
    @GET("latest")
    Call<RatioResponse> getRatios();
}
