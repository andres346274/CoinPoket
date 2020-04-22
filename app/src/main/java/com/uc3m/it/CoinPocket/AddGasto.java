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
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AddGasto extends AppCompatActivity {

    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    //EditText id_gasto;
    EditText conceptoIngreso;
    EditText cantidadGasto;
    EditText etFechaGasto;
    EditText localizacionGasto;
    Button buttonSaveGasto;
    Button buttonLocalizacionGasto;
    Calendar calendarioGasto = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gasto);

        conceptoIngreso = (EditText) findViewById(R.id.id_concepto_gasto);
        cantidadGasto = (EditText) findViewById(R.id.id_cantidad_gasto);
        localizacionGasto = (EditText) findViewById(R.id.id_localizacion_gasto);
        //id_gasto = (EditText) findViewById(R.id.id_gasto);
        etFechaGasto = findViewById(R.id.id_etFecha_gasto);
        buttonSaveGasto = (Button) findViewById(R.id.id_save_gasto);
        buttonLocalizacionGasto = (Button) findViewById(R.id.id_button_localizacion_gasto);


        etFechaGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddGasto.this, date, calendarioGasto
                        .get(Calendar.YEAR), calendarioGasto.get(Calendar.MONTH),
                        calendarioGasto.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_gastos", null, 1);

        buttonSaveGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarGasto();
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
                        localizacionGasto.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        localizacionGasto.setText("No se ha podido añadir la ubicación correctamente");
                    }
                }
                break;
            }

        }
    }

    private void registrarGasto(){
        ConexionSQLiteHelper newconn = new ConexionSQLiteHelper(this, "bd_gastos", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();

        Integer id_gasto = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS,null);
        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(0) );
        }
        while (arra_compare.contains( id_gasto )) {
            id_gasto = id_gasto + 1;
        }

        ContentValues values = new ContentValues();
        values.put(utilidades.CAMPO_ID_GASTO, id_gasto.toString());
        values.put(utilidades.CAMPO_CONCEPTO_GASTO, conceptoIngreso.getText().toString());
        values.put(utilidades.CAMPO_CANTIDAD_GASTO, cantidadGasto.getText().toString());

        Long idResultante=db.insert(utilidades.TABLA_GASTOS,utilidades.CAMPO_CONCEPTO_GASTO,values);


        Toast.makeText(getApplicationContext(),"Gasto Registrado " + idResultante, Toast.LENGTH_SHORT).show();
        db.close();
        returnHome();

    }


/*        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioGasto.set(Calendar.YEAR, year);
            calendarioGasto.set(Calendar.MONTH, monthOfYear);
            calendarioGasto.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaGasto.setText(sdf.format(calendarioGasto.getTime()));
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}



