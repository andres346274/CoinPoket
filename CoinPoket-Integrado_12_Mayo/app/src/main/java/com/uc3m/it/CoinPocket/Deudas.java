package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Deudas extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deudas);
    }
    //Arranque de la activity para añadir una deuda
    public void add_deuda (View view) {

        Intent intent = new Intent(this, AddDeuda.class);
        Button okButton = (Button) findViewById(R.id.id_button_add_deuda);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    //Arranque de la activity para ver las deudas enlistadas
    public void list_deudas (View view) {

        Intent intent = new Intent(this, ListaDeudas.class);
        Button okButton = (Button) findViewById(R.id.id_button_consultar_deudas);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }
}