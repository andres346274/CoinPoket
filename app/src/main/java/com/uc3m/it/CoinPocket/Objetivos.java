package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Objetivos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivos);
    }

    public void add_objetivo (View view) {

        Intent intent = new Intent(this, AddObjetivo.class);
        Button okButton = (Button) findViewById(R.id.id_button_add_objetivo);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }
}