package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import static com.uc3m.it.CoinPocket.MainActivity.filename;

public class gastos_dia extends AppCompatActivity {

    private Button ButtonModificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastos_dia);
        ButtonModificar = (Button) findViewById(R.id.button_modificar_gastos_hoy);

        ButtonModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(gastos_dia.this, Modificar_gastos_hoy.class);
                startActivity(intent);
            }
        });
    }


}
