package com.uc3m.it.CoinPocket;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.uc3m.it.CoinPocket.data.RatioSingleton;
import com.uc3m.it.CoinPocket.data.RetrofitInstance;
import com.uc3m.it.CoinPocket.data.Services;
import com.uc3m.it.CoinPocket.response.RatioResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        /*
        Aquí se hace la llamada a la API externa con Retrofit.
        Código basado en: https://medium.com/@prakash_pun/retrofit-a-simple-android-tutorial-48437e4e5a23
        Se mete la llamada a la API en una cola (enqueue()) y cuando recibe respuesta lo notifica
        por medio de un callback.
         */
        RetrofitInstance.getRetrofitInstance().create(Services.class).getRatios().enqueue(new Callback<RatioResponse>() {

            /* Para usar enqueue (), se deben implementar dos métodos de devolución de llamada (callback):
            onResponse() (si la petición está correcta) y onFailure() (si la petición ha fallado,
            no hay respuesta o está dañada)
             */
            @Override
            public void onResponse(Call<RatioResponse> call, Response<RatioResponse> response) {
                if (response.isSuccessful()) {
                    RatioSingleton.getInstance().ratio = response.body();
                    nextActivity();
                } else {
                    mostrarError("Ha ocurrido un error cargando los datos. Solo se podrá utilizar el EURO como moneda principal.");
                    nextActivity();
                }
            }

            @Override
            public void onFailure(Call<RatioResponse> call, Throwable t) {
                mostrarError("Ha ocurrido un error cargando los datos. Solo se podrá utilizar el EURO como moneda principal.");
                nextActivity();
            }
        });
    }

 //  Se llama a la siguiente actividad (MainActivity) con un Intent.
    private void nextActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

 // AlertDialog para mostrar al usuario si ha habido un error en la llamada a la API.
    private void mostrarError(String error) {
        new AlertDialog.Builder(this)
                .setMessage(error)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }
}
