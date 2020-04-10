package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BDAppActivity extends AppCompatActivity {

    private BDApp baseDatos;

    // para indicar en un Intent si se quiere crear una nueva nota o editar una existente
    private static final int ACTIVITY_CREATE=0;
    private static final int ACTIVITY_EDIT=1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_ingreso);

        createNote();

    }

    private void createNote() {
        Intent i = new Intent(this, BD_Editar.class);
        startActivityForResult(i, ACTIVITY_CREATE);
    }

}
