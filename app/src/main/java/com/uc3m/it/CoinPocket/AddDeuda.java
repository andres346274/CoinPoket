package com.uc3m.it.CoinPocket;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

//https://codinginflow.com/tutorials/android/radio-buttons-radio-group
// codigo del los radio buttons https://stackoverflow.com/questions/6440259/how-to-get-the-selected-index-of-a-radiogroup-in-android

public class AddDeuda extends AppCompatActivity {


    private static final String TAG = "ListDataActivity";

    //EditText id_gasto;
    EditText conceptoDeuda;
    EditText importeDeuda;
    EditText nombreDeuda;
    EditText etFechaDeuda;
    //EditText localizacionGasto;
    Button buttonSaveDeuda;
    Calendar calendarioDeuda = Calendar.getInstance();
    RadioGroup pagarDeber;
    RadioButton apagar, adeber;
    String date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deuda);

        pagarDeber = (RadioGroup)findViewById(R.id.id_radio_group);
        nombreDeuda = (EditText) findViewById( R.id.id_persona_deuda);
        importeDeuda = (EditText) findViewById(R.id.id_cantidad_deuda);
        etFechaDeuda = findViewById(R.id.id_etFecha_deuda);
        conceptoDeuda = (EditText) findViewById(R.id.id_concepto_deuda);
        apagar = (RadioButton) findViewById( R.id.radio1 );
        adeber = (RadioButton) findViewById( R.id.radio2 );

        buttonSaveDeuda = (Button) findViewById(R.id.id_save_deuda);

        etFechaDeuda.setText(date_n);

        etFechaDeuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddDeuda.this, date, calendarioDeuda
                        .get(Calendar.YEAR), calendarioDeuda.get(Calendar.MONTH),
                        calendarioDeuda.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSaveDeuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarDeuda();
            }
        });


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

    @SuppressLint("ResourceType")
    private void registrarDeuda(){
        ConexionSQLiteHelperDeudas newconn = new ConexionSQLiteHelperDeudas(this, "bd_deudas", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();

        Integer id_deuda = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_DEUDAS_BD,null);
        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(1) );
        }
        while (arra_compare.contains( id_deuda )) {
            id_deuda = id_deuda + 1;
        }

        Log.d(TAG, "--------------------->>> RADIO: " + pagarDeber.getCheckedRadioButtonId());


        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        ContentValues values = new ContentValues();
        if(nombreDeuda.getText().length()!= 0 && importeDeuda.length()!=0 && calendarioDeuda!=null){
            if(apagar.getId() == pagarDeber.getCheckedRadioButtonId()){
                values.put( utilidades.CAMPO_PAGAR_DEBER_BD, "0"); //A pagar
            }else {
                values.put( utilidades.CAMPO_PAGAR_DEBER_BD, "1"); // A deber
            }
            values.put(utilidades.CAMPO_ID_DEUDA, id_deuda.toString());
            values.put(utilidades.CAMPO_NOMBRE_DEUDA, nombreDeuda.getText().toString());
            values.put(utilidades.CAMPO_IMPORTE_DEUDA, importeDeuda.getText().toString());
            values.put( utilidades.CAMPO_FECHA_DEUDA, sdf.format(calendarioDeuda.getTime()));
            values.put( utilidades.CAMPO_CONCEPTO_DEUDA, conceptoDeuda.getText().toString());
            Log.d(TAG, "--------------------->>>GEtTEXT: " + nombreDeuda.getText().length());

            Long idResultante=db.insert(utilidades.TABLA_DEUDAS_BD,null,values);


            Toast.makeText(getApplicationContext(),"Deuda Registrada" + idResultante, Toast.LENGTH_SHORT).show();
            db.close();
            returnHome();
        }else {
            if(nombreDeuda.getText().length() == 0){
                Toast.makeText(getApplicationContext(),"Nombre vacío", Toast.LENGTH_SHORT).show();
            }else {
                if(importeDeuda.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"Importe nulo", Toast.LENGTH_SHORT).show();
                }else {
                    if(calendarioDeuda==null){
                        Toast.makeText(getApplicationContext(),"Error al cargar la fecha", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Error en el proceso", Toast.LENGTH_SHORT).show();
                    }
                }

            }

        }


    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioDeuda.set(Calendar.YEAR, year);
            calendarioDeuda.set(Calendar.MONTH, monthOfYear);
            calendarioDeuda.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaDeuda.setText(sdf.format(calendarioDeuda.getTime()));
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Deudas.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}




