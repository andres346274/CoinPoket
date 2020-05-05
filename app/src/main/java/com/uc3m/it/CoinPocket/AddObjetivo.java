package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddObjetivo extends AppCompatActivity {



    //EditText id_gasto;
    EditText conceptoObjetivo;
    EditText cantidadObjetivo;
    //TextView fecha;
    TextView etFechaObjetivo;
    Button buttonSaveObjetivo;
    Calendar calendarioObjetivo = Calendar.getInstance();
    String date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_objetivo);

        conceptoObjetivo = (EditText) findViewById(R.id.id_concepto_objetivo);
        cantidadObjetivo = (EditText) findViewById(R.id.id_cantidad_objetivo);
        etFechaObjetivo = (TextView) findViewById(R.id.id_etFecha_obetivo);
        //fecha = (TextView) findViewById(R.id.id_fecha);
        buttonSaveObjetivo = (Button) findViewById(R.id.id_save_objetivo);



        etFechaObjetivo.setText(date_n);


        etFechaObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddObjetivo.this, date, calendarioObjetivo
                        .get(Calendar.YEAR), calendarioObjetivo.get(Calendar.MONTH),
                        calendarioObjetivo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



    }


    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioObjetivo.set(Calendar.YEAR, year);
            calendarioObjetivo.set(Calendar.MONTH, monthOfYear);
            calendarioObjetivo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaObjetivo.setText(sdf.format(calendarioObjetivo.getTime()));

    }


}




