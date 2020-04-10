package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class gastos_dia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gastos_dia);
    }

    public void change_hoy_main(View view) {
        Intent intent = new Intent(this, change_hoy.class);
        Button okButton = (Button) findViewById(R.id.button_gastos_categoria_dia);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }
}