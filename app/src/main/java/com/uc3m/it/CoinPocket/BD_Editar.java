package com.uc3m.it.CoinPocket;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class BD_Editar extends AppCompatActivity {

    private EditText Concepto;
    private EditText Cantidad;
    private EditText Fecha;
    private Long fila;
    private BDApp baseDatos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.add_ingreso);

        Concepto = findViewById(R.id.id_cIngreso);
        Cantidad = findViewById(R.id.id_nIngreso);
        Fecha = findViewById(R.id.id_calIngreso);
        Button anadirButton = (Button) findViewById(R.id.anadir);

        baseDatos = new BDApp(this);
        baseDatos.open();

        fila = (savedInstanceState == null) ? null :
                (Long) savedInstanceState.getSerializable(BDApp.Fila);
        if (fila == null) {
            Bundle extras = getIntent().getExtras();
            fila = extras != null ? extras.getLong(BDApp.Fila) : null;
        }

        if (fila != null) {
            Cursor note = baseDatos.fetchIng(fila);
            Concepto.setText(note.getString(
                    note.getColumnIndexOrThrow(BDApp.cIngresos)));
            Cantidad.setText(note.getString(
                    note.getColumnIndexOrThrow(BDApp.iCantidad)));
            Fecha.setText(note.getString(
                    note.getColumnIndexOrThrow(BDApp.cIngresos)));

        }

    }

    public void guardarDatos(View view) {
        String cI = Concepto.getText().toString();
        String nI = Cantidad.getText().toString();
        String fI = Fecha.getText().toString();

        if (fila == null) {
            long id = baseDatos.guardarIngreso(cI, nI, fI);
            if (id > 0) {
                fila = id;
            }
        } else {
            baseDatos.updateIng(fila, cI, nI, fI);
        }
        setResult(RESULT_OK);
        baseDatos.close();
        finish();
    }
}