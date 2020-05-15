package com.uc3m.it.CoinPocket;

/**
 *  --> Comparar fechas: https://www.flipandroid.com/la-mejor-manera-de-comparar-fechas-en-android.html
 */

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
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddObjetivo extends AppCompatActivity {


    //Inicializacion de las variables de la activity
    EditText motivoObjetivo;
    EditText cantidadObjetivo;
    TextView etFechaInicioObjetivo, etFechaFinalObjetivo;
    Button buttonSaveObjetivo;
    Calendar calendarioInicioObjetivo = Calendar.getInstance();
    Calendar calendarrioFinalObjetivo = Calendar.getInstance();
    RadioGroup radioGroupObjetivo;
    RadioButton radioahorro, radioMaxGasto;
    Date str1Date, strDate;
    //Variable de fecha actual
    String date_n;
    //Variable de formato de decimales
    DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_objetivo);


        //Asignacion de los componentes que usamos en la activity
        cantidadObjetivo = (EditText) findViewById(R.id.id_cantidad_objetivo);
        etFechaInicioObjetivo = (TextView) findViewById(R.id.id_etFecha_inicio_objetivo);
        etFechaFinalObjetivo = (TextView) findViewById(R.id.id_etFecha_final_objetivo);
        motivoObjetivo = (EditText) findViewById(R.id.id_motivo_objetivo);
        buttonSaveObjetivo = (Button) findViewById(R.id.id_save_objetivo);
        radioGroupObjetivo = (RadioGroup) findViewById( R.id.id_radio_group_objetivos );
        radioahorro = (RadioButton) findViewById( R.id.radio_objetivo_ahorro );
        radioMaxGasto = (RadioButton) findViewById( R.id.radio_objetivo_gastomax);

        //Asignación al formato de decimales
        formatter = new DecimalFormat("####.##");

        //Asignación inicial de fecha actual en las componentes correspondientes
        date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        etFechaInicioObjetivo.setText(date_n);
        etFechaFinalObjetivo.setText(date_n);



        //Inicialización de escuchadores en las fechas
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

        //Inicialización de botón para guardar el objetivo
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


    /**
     * Método para registrar el Objetivo
     *      id_objetivo = id asignado al objetivo en su creación
     *      arra_compare = array para movernos con el cursor de la BD a la hora de asignar IDs
     *      sdf = objeto fecha
     *      values = Contenedor introducido en la BD
     */
    private void registrarObjetivo() throws ParseException {
        //Llamada a la BD de objetivos
        ConexionSQLiteHelperObjetivos newconn = new ConexionSQLiteHelperObjetivos(
                this, "bd_objetivos", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();
        ContentValues values = new ContentValues();

        //Asignación de variables para cálculo de id
        Integer id_objetivo = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                utilidades.TABLA_OBJETIVOS_BD,null);

        //Asignación de variables para la fecha
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        //Cálculo de id
        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(1) );
        }
        while (arra_compare.contains( id_objetivo )) {
            id_objetivo = id_objetivo + 1;
        }

        //Registro de objetivo y casos de registro no válido
        if(cantidadObjetivo.getText().length() != 0 && CompararFechas(
                sdf.format(calendarioInicioObjetivo.getTime()), sdf.format(calendarrioFinalObjetivo.
                        getTime())).equals(sdf.format(calendarioInicioObjetivo.getTime()) ) ){
            if(radioGroupObjetivo.getCheckedRadioButtonId() == radioahorro.getId()){
                values.put( utilidades.CAMPO_GASTO_AHORRO, "0"); // Objetivo de ahorro
            }else{
                values.put( utilidades.CAMPO_GASTO_AHORRO, "1"); // Objetivo de maximo gasto
            }
            values.put(utilidades.CAMPO_ID_OBJETIVO, id_objetivo.toString());
            values.put(utilidades.CAMPO_CANTIDAD_OBJETIVO, formatter.format(
                    Double.parseDouble( cantidadObjetivo.getText().toString() ) ));
            values.put( utilidades.CAMPO_FECHA_INICIO_OBJETIVO, sdf.format(
                    calendarioInicioObjetivo.getTime()));
            values.put( utilidades.CAMPO_FECHA_FIN_OBJETIVO, sdf.format(
                    calendarrioFinalObjetivo.getTime()));
            values.put( utilidades.CAMPO_MOTIVO_OBJETIVO, motivoObjetivo.getText().toString());

            Long idResultante=db.insert(utilidades.TABLA_OBJETIVOS_BD,null,values);


            Toast.makeText(getApplicationContext(),"Objetivo Registrado",
                    Toast.LENGTH_SHORT).show();
            db.close();
            returnHome();
        }else {
            if(cantidadObjetivo.getText().length()==0) {
                Toast.makeText(getApplicationContext(),"Cantidad no especificada",
                        Toast.LENGTH_SHORT).show();
            }else {
                if(CompararFechas( sdf.format(calendarioInicioObjetivo.getTime()),
                        sdf.format(calendarrioFinalObjetivo.getTime())).equals(
                                sdf.format(calendarrioFinalObjetivo.getTime()))){
                    Toast.makeText(getApplicationContext(),
                            "Fecha Final posterior a Fecha Inicial", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error en el proceso",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }



    }

    /**
     * Data picker para tomar la fecha inicial de ovjetivo introducida en caso de cambiarla
     */
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

    /**
     * Data picker para tomar la fecha final de objetivo introducida en caso de cambiarla
     */
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


    /**
     * Introduccion de la fecha Inicial de objetivo
     */
    private void actualizarInputInicio() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaInicioObjetivo.setText(sdf.format(calendarioInicioObjetivo.getTime()));

    }

    /**
     * Introduccion de la fecha Final de o objetivo
     */
    private void actualizarInputFin() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaFinalObjetivo.setText(sdf.format(calendarrioFinalObjetivo.getTime()));

    }

    /**
     * Método de comparación de fechas
     * @param z fecha 1
     * @param y fecha 2
     * @return
     * @throws ParseException
     */
    public String CompararFechas(String z, String y) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        strDate = sdf.parse(z);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        str1Date = sdf1.parse( y );

        if (str1Date.after(strDate)) {
            return  z;

        }else {
            return y;
        }
    }


    /**
     * Método de retorno a la lista de objetivos al añadir objetivo
     */
    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Objetivos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }


}




