package com.uc3m.it.CoinPocket.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *retrofit
 */
public class RetrofitInstance {

    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.exchangeratesapi.io/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}