package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void add_gasto_main(View view) {
        Intent intent = new Intent(this, add_gasto.class);
        Button okButton = (Button) findViewById(R.id.button_add_gasto);

        Bundle b = new Bundle();

        intent.putExtras(b);

        startActivity(intent);
    }

    public void add_ingreso_main(View view) {
        Intent intent = new Intent(this, add_ingreso.class);
        Button okButton = (Button) findViewById(R.id.button_add_ingreso);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void consultar_main(View view) {
        Intent intent = new Intent(this, consultar.class);
        Button okButton = (Button) findViewById(R.id.button_consultar);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void objetivos_main(View view) {
        Intent intent = new Intent(this, objetivos.class);
        Button okButton = (Button) findViewById(R.id.button_objetivos);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void deudas_main(View view) {
        Intent intent = new Intent(this, deudas.class);
        Button okButton = (Button) findViewById(R.id.button_deudas);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }
}
