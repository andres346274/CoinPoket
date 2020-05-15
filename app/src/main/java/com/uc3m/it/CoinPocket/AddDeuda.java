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

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

// referencia de codigo de los radio buttons
//https://codinginflow.com/tutorials/android/radio-buttons-radio-group
//https://stackoverflow.com/questions/6440259/how-to-get-the-selected-index-of-a-radiogroup-in-android

public class AddDeuda extends AppCompatActivity {

    //Inicializacion de las variables de la activity
    EditText conceptoDeuda;
    EditText importeDeuda;
    EditText nombreDeuda;
    EditText etFechaDeuda;
    Button buttonSaveDeuda;
    Calendar calendarioDeuda = Calendar.getInstance();
    RadioGroup pagarDeber;
    RadioButton apagar, adeber;
    //Variable de la fecha actual
    String date_n;
    //Formato de redondeo de cifras
    DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deuda);

        //Asignacion de los componentes que usamos en la activity
        pagarDeber = (RadioGroup)findViewById(R.id.id_radio_group);
        nombreDeuda = (EditText) findViewById( R.id.id_persona_deuda);
        importeDeuda = (EditText) findViewById(R.id.id_cantidad_deuda);
        etFechaDeuda = findViewById(R.id.id_etFecha_deuda);
        conceptoDeuda = (EditText) findViewById(R.id.id_concepto_deuda);
        apagar = (RadioButton) findViewById( R.id.radio1 );
        adeber = (RadioButton) findViewById( R.id.radio2 );
        buttonSaveDeuda = (Button) findViewById(R.id.id_save_deuda);
        //Asignación de la fecha actual
        date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        etFechaDeuda.setText(date_n);
        //Asignación al formato de decimales
        formatter = new DecimalFormat("####.##");

        //Escuchador para modificar la fecha clickando en la fecha
        etFechaDeuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddDeuda.this, date, calendarioDeuda
                        .get(Calendar.YEAR), calendarioDeuda.get(Calendar.MONTH),
                        calendarioDeuda.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Boton de registro de la la operacion OJO! es donde luego se lleva acabo el registro en la base de datos
        buttonSaveDeuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarDeuda();
            }
        });

    }

    @SuppressLint("ResourceType")

    /**
     * Método para registrar la Deuda
     *      id_deuda = id asignado a la deuda en su creación
     *      arra_compare = array para movernos con el cursor de la BD a la hora de asignar IDs
     *      sdf = objeto fecha
     *      values = Contenedor introducido en la BD
     */
    private void registrarDeuda(){

        //Llamada a la Base de Datos de Deudas
        ConexionSQLiteHelperDeudas newconn = new ConexionSQLiteHelperDeudas(this, "bd_deudas", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();
        Integer id_deuda = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_DEUDAS_BD,null);

        //Formato de la fecha para la inserciones en la base de datos
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        ContentValues values = new ContentValues();

        //Cálculo de Id
        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(1) );
        }
        while (arra_compare.contains( id_deuda )) {
            id_deuda = id_deuda + 1;
        }

        //Asignaciión de datos a la Base de Datos
        if(nombreDeuda.getText().length()!= 0 && importeDeuda.length()!=0 && calendarioDeuda!=null){

            if(apagar.getId() == pagarDeber.getCheckedRadioButtonId()){
                values.put( utilidades.CAMPO_PAGAR_DEBER_BD, "0"); //A pagar
            }else {
                values.put( utilidades.CAMPO_PAGAR_DEBER_BD, "1"); //A deber
            }

            values.put(utilidades.CAMPO_ID_DEUDA, id_deuda.toString());
            values.put(utilidades.CAMPO_NOMBRE_DEUDA, nombreDeuda.getText().toString());
            values.put(utilidades.CAMPO_IMPORTE_DEUDA, formatter.format( Double.parseDouble(
                    importeDeuda.getText().toString().trim() ) ));
            values.put( utilidades.CAMPO_FECHA_DEUDA, sdf.format(calendarioDeuda.getTime()));
            values.put( utilidades.CAMPO_CONCEPTO_DEUDA, conceptoDeuda.getText().toString());

            Long idResultante=db.insert(utilidades.TABLA_DEUDAS_BD,null,values);

            Toast.makeText(getApplicationContext(),"Deuda Registrada", Toast.LENGTH_SHORT).show();
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

    /**
     * Asignacion del escuchador para introducir la fecha
     */
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

    /**
     * Metodo para introducir la fecha en caso de usar el data picker y no la fecha que viene por defecto
     */
    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yy"; //Formato ene el que queremos la fecha, consultar en developer.android si queremos meter otro (esta en el que usamos en España)
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);
        etFechaDeuda.setText(sdf.format(calendarioDeuda.getTime()));
    }

    /**
     * Método de retorno a Deudas.class una vez añadida la deuda
     */
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(),Deudas.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}