package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.SimpleCursorAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class add_ingreso extends AppCompatActivity {

    private BDApp baseDatos;

    private static final int ACTIVITY_CREATE=0;

    EditText conIngreso;
    EditText numIngreso;
    EditText fechaIngreso;
    Calendar calIngreso = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_ingreso);

        conIngreso = (EditText) findViewById(R.id.id_cIngreso);
        numIngreso = (EditText) findViewById(R.id.id_nIngreso);
        fechaIngreso = (EditText) findViewById(R.id.id_calIngreso);

        fechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(add_ingreso.this, date, calIngreso.get(Calendar.YEAR), calIngreso.get(Calendar.MONTH),
                        calIngreso.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        guardarDatos();

    }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                calIngreso.set(Calendar.YEAR, year);
                calIngreso.set(Calendar.MONTH, monthOfYear);
                calIngreso.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                actualizarInput();

            }
        };


        private void actualizarInput() {

            String formatoDeFecha = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

            fechaIngreso.setText(sdf.format(calIngreso.getTime()));
        }

        private void guardarDatos() {
        Intent i = new Intent(this, BD_Editar.class);
        startActivityForResult(i, ACTIVITY_CREATE);
        }
    }