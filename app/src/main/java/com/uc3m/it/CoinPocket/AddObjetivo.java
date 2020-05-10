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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddObjetivo extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    //EditText id_gasto;
    EditText motivoObjetivo;
    EditText cantidadObjetivo;
    //TextView fecha;
    TextView etFechaInicioObjetivo, etFechaFinalObjetivo;
    Button buttonSaveObjetivo;
    Calendar calendarioInicioObjetivo = Calendar.getInstance();
    Calendar calendarrioFinalObjetivo = Calendar.getInstance();
    RadioGroup radioGroupObjetivo;
    RadioButton radioahorro, radioMaxGasto;
    String date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_objetivo);


        cantidadObjetivo = (EditText) findViewById(R.id.id_cantidad_objetivo);
        etFechaInicioObjetivo = (TextView) findViewById(R.id.id_etFecha_inicio_objetivo);
        etFechaFinalObjetivo = (TextView) findViewById(R.id.id_etFecha_final_objetivo);
        motivoObjetivo = (EditText) findViewById(R.id.id_motivo_objetivo);
        //fecha = (TextView) findViewById(R.id.id_fecha);
        buttonSaveObjetivo = (Button) findViewById(R.id.id_save_objetivo);
        radioGroupObjetivo = (RadioGroup) findViewById( R.id.id_radio_group_objetivos );
        radioahorro = (RadioButton) findViewById( R.id.radio_objetivo_ahorro );
        radioMaxGasto = (RadioButton) findViewById( R.id.radio_objetivo_gastomax);



        etFechaInicioObjetivo.setText(date_n);
        etFechaFinalObjetivo.setText(date_n);



        etFechaInicioObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddObjetivo.this, dateInicio, calendarioInicioObjetivo
                        .get(Calendar.YEAR), calendarioInicioObjetivo.get(Calendar.MONTH),
                        calendarioInicioObjetivo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        etFechaFinalObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddObjetivo.this, dateFinal, calendarrioFinalObjetivo
                        .get(Calendar.YEAR), calendarrioFinalObjetivo.get(Calendar.MONTH),
                        calendarrioFinalObjetivo.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        buttonSaveObjetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    registrarObjetivo();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void registrarObjetivo() throws ParseException {
        ConexionSQLiteHelperObjetivos newconn = new ConexionSQLiteHelperObjetivos(this, "bd_objetivos", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();

        Integer id_objetivo = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_OBJETIVOS_BD,null);
        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(1) );
        }
        while (arra_compare.contains( id_objetivo )) {
            id_objetivo = id_objetivo + 1;
        }

        //Log.d(TAG, "--------------------->>> RADIO: " + pagarDeber.getCheckedRadioButtonId());


        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        ContentValues values = new ContentValues();

        Log.d(TAG, "--------------------->>>FECHA INICIO: " + sdf.format(calendarioInicioObjetivo.getTime()));
        Log.d(TAG, "--------------------->>>FECHA FIN: " + sdf.format(calendarrioFinalObjetivo.getTime()));
        Log.d(TAG, "--------------------->>>FECHA COMPARE: " + CompararFechas( sdf.format(calendarioInicioObjetivo.getTime()), sdf.format(calendarrioFinalObjetivo.getTime())));

        if(cantidadObjetivo.getText().length() != 0 && CompararFechas( sdf.format(calendarioInicioObjetivo.getTime()), sdf.format(calendarrioFinalObjetivo.getTime())).equals(sdf.format(calendarioInicioObjetivo.getTime()) ) ){
            if(radioGroupObjetivo.getCheckedRadioButtonId() == radioahorro.getId()){
                values.put( utilidades.CAMPO_GASTO_AHORRO, "0"); // Objetivo de ahorro
            }else{
                values.put( utilidades.CAMPO_GASTO_AHORRO, "1"); // Objetivo de maximo gasto
            }
            values.put(utilidades.CAMPO_ID_OBJETIVO, id_objetivo.toString());
            values.put(utilidades.CAMPO_CANTIDAD_OBJETIVO, cantidadObjetivo.getText().toString());
            values.put( utilidades.CAMPO_FECHA_INICIO_OBJETIVO, sdf.format(calendarioInicioObjetivo.getTime()));
            values.put( utilidades.CAMPO_FECHA_FIN_OBJETIVO, sdf.format(calendarrioFinalObjetivo.getTime()));
            values.put( utilidades.CAMPO_MOTIVO_OBJETIVO, motivoObjetivo.getText().toString());
            Log.d(TAG, "--------------------->>>Values: " + values);

            Long idResultante=db.insert(utilidades.TABLA_OBJETIVOS_BD,null,values);


            Toast.makeText(getApplicationContext(),"Objetivo Registrado" + idResultante, Toast.LENGTH_SHORT).show();
            db.close();
            returnHome();
        }else {
            if(cantidadObjetivo.getText().length()==0) {
                Toast.makeText(getApplicationContext(),"Cantidad no especificada", Toast.LENGTH_SHORT).show();
            }else {
                if(CompararFechas( sdf.format(calendarioInicioObjetivo.getTime()), sdf.format(calendarrioFinalObjetivo.getTime())).equals( sdf.format(calendarrioFinalObjetivo.getTime()))){
                    Toast.makeText(getApplicationContext(),"Fecha Final posterior a Fecha Inicial", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error en el proceso", Toast.LENGTH_SHORT).show();
                }
            }
        }



    }

    DatePickerDialog.OnDateSetListener dateInicio = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioInicioObjetivo.set(Calendar.YEAR, year);
            calendarioInicioObjetivo.set(Calendar.MONTH, monthOfYear);
            calendarioInicioObjetivo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputInicio();
        }

    };
    DatePickerDialog.OnDateSetListener dateFinal = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarrioFinalObjetivo.set(Calendar.YEAR, year);
            calendarrioFinalObjetivo.set(Calendar.MONTH, monthOfYear);
            calendarrioFinalObjetivo.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputFin();
        }

    };

    private void actualizarInputInicio() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaInicioObjetivo.setText(sdf.format(calendarioInicioObjetivo.getTime()));

    }
    private void actualizarInputFin() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaFinalObjetivo.setText(sdf.format(calendarrioFinalObjetivo.getTime()));

    }

    public String CompararFechas(String z, String y) throws ParseException {
        Log.d(TAG, "--------------------->>> ENTRE COMPARAR FEHCAS: " + z + "--" + y);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        Date strDate = sdf.parse(z);

        int year = strDate.getYear(); // this is deprecated
        int month = strDate.getMonth(); // this is deprecated
        int day = strDate.getDay(); // this is deprecated

        Calendar primeraFecha = Calendar.getInstance();
        primeraFecha.set(year, month, day);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        Date str1Date = sdf1.parse( y );
        int year_1 = str1Date.getYear();
        int month_1 = str1Date.getMonth();
        int day_1 = str1Date.getDay();

        Calendar segundaFecha = Calendar.getInstance();
        segundaFecha.set( year_1, month_1, day_1 );

        if (segundaFecha.after(primeraFecha)) {
            return  z;

        }else {
            return y;
        }
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Objetivos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }


}




