package com.uc3m.it.CoinPocket;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
 */

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModificarObjetivo extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";

    ConexionSQLiteHelperObjetivos conn;

    EditText campoCantidad, campoMotivo;
    TextView campofechaInicio, campofechaFin, ahorrogasto;

    Button modificar_objetivo, eliminar_objetivo;

    Calendar calendarioInicioModificar = Calendar.getInstance();
    Calendar calendarioFinModificar = Calendar.getInstance();

    List<Address> addresses = null;

    Objetivos seleccion = new Objetivos();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_objetivo );
        conn = new ConexionSQLiteHelperObjetivos(getApplicationContext(), "bd_objetivos", null, 1);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_modificar_objetivo);
        campoMotivo = (EditText) findViewById(R.id.id_motivo_modificar_objetivo);
        campofechaInicio = (TextView) findViewById( R.id.id_fechainicio_modificar_objetivo);
        campofechaFin = (TextView) findViewById( R.id.id_fechafin_modificar_objetivo);
        modificar_objetivo = (Button) findViewById( R.id.id_modificar_objetivo_concreto);
        eliminar_objetivo = (Button) findViewById( R.id.id_eliminar_objetivo_concreto);
        ahorrogasto = (TextView) findViewById( R.id.id_ahorro_gasto_modificar_objetivo );

        consultarDeuda();

        campofechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModificarObjetivo.this, dateInicio, calendarioInicioModificar
                        .get( Calendar.YEAR), calendarioInicioModificar.get(Calendar.MONTH),
                        calendarioInicioModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        campofechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModificarObjetivo.this, dateFin, calendarioFinModificar
                        .get( Calendar.YEAR), calendarioFinModificar.get(Calendar.MONTH),
                        calendarioFinModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        modificar_objetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modificar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        eliminar_objetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
    }

    public void consultarDeuda() {
        SQLiteDatabase db=conn.getReadableDatabase();
        Log.d(TAG, "------------------->>> Lista IDS:" + seleccion.listaIDs );
        Log.d(TAG, "------------------->>> ID seleccionado:" + seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0] );
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};
        Log.d(TAG, "------------------->>> Has entrado en:" + parametros );

        try {
            Cursor cursor=db.rawQuery("SELECT * FROM " +utilidades.TABLA_OBJETIVOS_BD+" WHERE "+utilidades.CAMPO_ID_OBJETIVO+"=? ",parametros);

            cursor.moveToFirst();
            Log.d(TAG, "------------------->>> CampoNombre:" + cursor.getString(2) );
            Log.d(TAG, "------------------->>> CampoImporte:" + cursor.getString(3) );
            Log.d(TAG, "------------------->>> campofecha:" + cursor.getString(4) );
            Log.d(TAG, "------------------->>> campoConcepto:" + cursor.getString(5) );

            if(cursor.getString( 0 ).equals( "0" )) {
                ahorrogasto.setText( "Ahorrar: " );
            }else {
                ahorrogasto.setText( "Máximo gasto: " );
            }
            campoCantidad.setText(cursor.getString(2));
            Log.d(TAG, "------------------->>> CampoNombre:" + cursor.getString(2) );
            campofechaInicio.setText(cursor.getString(3));
            Log.d(TAG, "------------------->>> CampoImporte:" + cursor.getString(3) );
            campofechaFin.setText(cursor.getString(4));
            Log.d(TAG, "------------------->>> campofecha:" + cursor.getString(4) );
            campoMotivo.setText( cursor.getString( 5 ) );
            Log.d(TAG, "------------------->>> campoConcepto:" + cursor.getString(5) );


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Opción no encontrada",Toast.LENGTH_LONG).show();
        }

    }


    private void modificar() throws ParseException {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};
        ContentValues values=new ContentValues();
        if(campoCantidad.getText().length() != 0 && CompararFechas( campofechaInicio.getText().toString(), campofechaFin.getText().toString()).equals(campofechaInicio.getText().toString())){
            values.put(utilidades.CAMPO_CANTIDAD_OBJETIVO,campoCantidad.getText().toString());
            values.put(utilidades.CAMPO_FECHA_INICIO_OBJETIVO,campofechaInicio.getText().toString());
            values.put(utilidades.CAMPO_FECHA_FIN_OBJETIVO,campofechaFin.getText().toString());
            values.put(utilidades.CAMPO_MOTIVO_OBJETIVO,campoMotivo.getText().toString());

            db.update(utilidades.TABLA_OBJETIVOS_BD,values,utilidades.CAMPO_ID_OBJETIVO+"=?",parametros);
            Toast.makeText(getApplicationContext(),"Objetivo Modificado",Toast.LENGTH_LONG).show();
            db.close();
            returnHome();
        }else{
            if(campoCantidad.getText().length() == 0){
                Toast.makeText(getApplicationContext(),"Cantidad no especificada", Toast.LENGTH_SHORT).show();
            }else{
                if(CompararFechas( campofechaInicio.getText().toString(), campofechaFin.getText().toString()).equals(campofechaFin.getText().toString())){
                    Toast.makeText(getApplicationContext(),"Fecha Final posterior a Fecha Inicial", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error en el proceso", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
    private void eliminar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        db.delete(utilidades.TABLA_OBJETIVOS_BD,utilidades.CAMPO_ID_OBJETIVO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Eliminado",Toast.LENGTH_LONG).show();
        db.close();
        returnHome();
    }

    DatePickerDialog.OnDateSetListener dateInicio = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioInicioModificar.set(Calendar.YEAR, year);
            calendarioInicioModificar.set(Calendar.MONTH, monthOfYear);
            calendarioInicioModificar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputInicio();
        }

    };

    DatePickerDialog.OnDateSetListener dateFin = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioFinModificar.set(Calendar.YEAR, year);
            calendarioFinModificar.set(Calendar.MONTH, monthOfYear);
            calendarioFinModificar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputFin();
        }

    };

    private void actualizarInputInicio() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        campofechaInicio.setText(sdf.format(calendarioInicioModificar.getTime()));

    }

    private void actualizarInputFin() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        campofechaFin.setText(sdf.format(calendarioFinModificar.getTime()));

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

    /**
     private void toastMessage(String message){
     Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
     }*/
}