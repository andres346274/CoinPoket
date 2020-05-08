package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GastosDia extends AppCompatActivity {

    private Button ButtonModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_dia);
        ButtonModificar = (Button) findViewById(R.id.button_modificar_gastos_hoy);

        ButtonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GastosDia.this, ModificarGastosHoy.class);
                startActivity(intent);
            }
        });
    }


}
