package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GastosMes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos_mes);
    }

    public void change_mes_main(View view) {
        Intent intent = new Intent(this, ChangeMes.class);
        Button okButton = (Button) findViewById(R.id.button_gastos_categoria_mes);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }
}