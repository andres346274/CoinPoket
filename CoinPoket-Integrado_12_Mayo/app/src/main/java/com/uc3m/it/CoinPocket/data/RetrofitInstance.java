package com.uc3m.it.CoinPocket.data;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

  /*
   RetrofitInstance:
   Código basado en https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23
   Para emitir solicitudes de red a la API externa (Foreign Exchange Rates) con Retrofit, necesitamos
   crear una instancia usando la clase Retrofit.Builder y configurarla con la URL deseada.
   Se utiliza GSON para convertir JSON a objetos Java.
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