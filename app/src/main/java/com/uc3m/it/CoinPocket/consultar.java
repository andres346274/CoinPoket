package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class consultar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consultar);
    }

    public void gastos_dia_main(View view) {
        Intent intent = new Intent(this, gastos_dia.class);
        Button okButton = (Button) findViewById(R.id.button_gastos_dia);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void gastos_mes_main(View view) {
        Intent intent = new Intent(this, gastos_mes.class);
        Button okButton = (Button) findViewById(R.id.button_gastos_mes);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void gastos_year_main(View view) {
        Intent intent = new Intent(this, gastos_year.class);
        Button okButton = (Button) findViewById(R.id.button_gastos_year);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void gastos_total_main(View view) {
        Intent intent = new Intent(this, gastos_total.class);
        Button okButton = (Button) findViewById(R.id.button_gastos_total);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }
}