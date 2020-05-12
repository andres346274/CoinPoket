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
        RetrofitInstance.getRetrofitInstance().create(Services.class).getRatios().enqueue(new Callback<RatioResponse>() {
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

    private void nextActivity() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

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
