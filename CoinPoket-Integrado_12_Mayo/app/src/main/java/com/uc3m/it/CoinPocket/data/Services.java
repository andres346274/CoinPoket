package com.uc3m.it.CoinPocket.data;

import com.uc3m.it.CoinPocket.response.RatioResponse;

import retrofit2.Call;
import retrofit2.http.GET;

  /*
   Forma parte de Retrofit, los puntos finales se definen dentro de una interfaz mediante anotaciones
   especiales de modificación para codificar detalles sobre los parámetros y el método de solicitud.
  */

public interface Services {
    @GET("latest")
    Call<RatioResponse> getRatios();
}
