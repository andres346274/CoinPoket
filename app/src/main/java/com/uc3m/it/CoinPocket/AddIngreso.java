package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.adapter.SpinnerAdapter;
import com.uc3m.it.CoinPocket.data.RatioSingleton;
import com.uc3m.it.CoinPocket.response.Ratio;
import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddIngreso extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    private static final int ACTIVITY_CREATE=0;

    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    EditText conceptoIngreso;
    EditText cantidadIngreso;
    TextView etFechaIngreso;
    EditText localizacionIngreso;
    Button buttonSaveIngreso;
    Button buttonLocalizacionIngreso;
    Calendar calendarioIngreso = Calendar.getInstance();
    List<Address> addresses = null;
    Spinner moneda;
    String date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingreso);

        conceptoIngreso = (EditText) findViewById(R.id.id_concepto_ingreso);
        cantidadIngreso = (EditText) findViewById(R.id.id_cantidad_ingreso);
        localizacionIngreso = (EditText) findViewById(R.id.id_localizacion_ingreso);
        etFechaIngreso = (TextView) findViewById(R.id.id_etFecha_ingreso);
        buttonSaveIngreso = (Button) findViewById(R.id.id_save_ingreso);
        buttonLocalizacionIngreso = (Button) findViewById(R.id.id_button_localizacion_ingreso);
        moneda = findViewById(R.id.moneda);
        moneda.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, RatioSingleton.getRatios()));


        etFechaIngreso.setText(date_n);

        etFechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddIngreso.this, date, calendarioIngreso.get(Calendar.YEAR), calendarioIngreso.get(Calendar.MONTH),
                        calendarioIngreso.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSaveIngreso.setOnClickListener(new View.OnClickListener() {
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

    private void registrarGasto(){
        ConexionSQLiteHelper newconn = new ConexionSQLiteHelper(this, "bd_gastos_ingresos", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();

        Integer id_gasto = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(1) );
        }
        while (arra_compare.contains( id_gasto )) {
            id_gasto = id_gasto + 1;
        }

        String cantidad = cantidadIngreso.getText().toString();
        Ratio ratio = (Ratio) moneda.getSelectedItem();
        try {
            Float cantidadInFloat = Float.parseFloat(cantidad);
            Float multiplicacion = cantidadInFloat / ratio.getValue();
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            cantidad = formatter.format(multiplicacion);
        } catch (Exception e) {

        }

        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        ContentValues values = new ContentValues();
        values.put( utilidades.CAMPO_GASTO_INGRESO, "0" );
        values.put(utilidades.CAMPO_ID_GASTO_INGRESO, id_gasto.toString());
        if(conceptoIngreso.getText()!=null){
            values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO, conceptoIngreso.getText().toString());
        }else{
            values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO, "");
        }
        if(cantidadIngreso.getText()!=null){
            values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO, cantidad);
        }else{
            values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO, "0");
        }
        values.put( utilidades.CAMPO_FECHA_GASTO_INGRESO, sdf.format(calendarioIngreso.getTime()));
        if(addresses!=null){
            values.put( utilidades.CAMPO_LOCALIZACION_GASTO_INGRESO, addresses.get(0).getAddressLine(0) );
        }

        Log.d(TAG, "--------------------->>>Values: " + values);

        Long idResultante=db.insert(utilidades.TABLA_GASTOS_INGRESOS_BD,null,values);


        Toast.makeText(getApplicationContext(),"Ingreso Registrado ", Toast.LENGTH_SHORT).show();
        db.close();
        returnHome();

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

        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaIngreso.setText(sdf.format(calendarioIngreso.getTime()));
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}