package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddIngreso extends AppCompatActivity {


    private static final int ACTIVITY_CREATE=0;

    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    EditText conceptoIngreso;
    EditText cantidadIngreso;
    EditText etFechaIngreso;
    EditText localizacionIngreso;
    Button buttonSaveIngreso;
    Button buttonLocalizacionIngreso;
    Calendar calendarioIngreso = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingreso);

        conceptoIngreso = (EditText) findViewById(R.id.id_concepto_ingreso);
        cantidadIngreso = (EditText) findViewById(R.id.id_cantidad_ingreso);
        localizacionIngreso = (EditText) findViewById(R.id.id_localizacion_ingreso);
        //id_gasto = (EditText) findViewById(R.id.id_gasto);
        etFechaIngreso = findViewById(R.id.id_etFecha_ingreso);
        buttonSaveIngreso = (Button) findViewById(R.id.id_save_ingreso);
        buttonLocalizacionIngreso = (Button) findViewById(R.id.id_button_localizacion_ingreso);

        etFechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddIngreso.this, date, calendarioIngreso.get(Calendar.YEAR), calendarioIngreso.get(Calendar.MONTH),
                        calendarioIngreso.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    public void add_localizacion(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, SHOW_SUB_ACTIVITY_ONE);
    }

    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SHOW_SUB_ACTIVITY_ONE) : {
                if (resultCode == MapsActivity.RESULT_OK) {

                    double lati = data.getDoubleExtra("LATITUD", -1);
                    double longi = data.getDoubleExtra("LONGITUD", -1);

                    List<Address> addresses = null;
                    Geocoder gc = new Geocoder(this, Locale.getDefault());
                    try {

                        addresses = gc.getFromLocation(lati, longi, 10);
                        localizacionIngreso.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        localizacionIngreso.setText("No se ha podido añadir la ubicación correctamente");
                    }
                }
                break;
            }

        }
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioIngreso.set(Calendar.YEAR, year);
            calendarioIngreso.set(Calendar.MONTH, monthOfYear);
            calendarioIngreso.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();

        }
    };

    private void actualizarInput() {

        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaIngreso.setText(sdf.format(calendarioIngreso.getTime()));
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}