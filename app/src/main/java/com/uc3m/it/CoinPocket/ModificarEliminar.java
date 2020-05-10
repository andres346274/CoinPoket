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
import android.location.Geocoder;
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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModificarEliminar extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";

    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    ConexionSQLiteHelper conn;

    EditText campoConcepto, campoCantidad, campoLocalizacion;
    TextView campofecha, positivo_negativo;

    Button modificar_gasto, eliminar_gasto, button_modificar_localizacion;

    Calendar calendarioModificar = Calendar.getInstance();

    ListaGastosIngresos seleccion = new ListaGastosIngresos();

    List<Address> addresses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos_ingresos", null, 1);
        campoConcepto = (EditText) findViewById(R.id.id_concepto_modificar_eliminar);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_modificar_eliminar);
        campofecha = (TextView) findViewById( R.id.id_fecha_modificar_eliminar);
        campoLocalizacion = (EditText) findViewById( R.id.id_localizacion_modificar_eliminar);
        modificar_gasto = (Button) findViewById( R.id.id_modificar_gasto_concreto);
        eliminar_gasto = (Button) findViewById( R.id.id_eliminar_gasto_concreto);
        button_modificar_localizacion = (Button) findViewById(R.id.id_button_modificar_localizacion);
        positivo_negativo = (TextView) findViewById( R.id.id_positivo_negativo_modificar_eliminar );

        consultarGasto();

        campofecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModificarEliminar.this, date, calendarioModificar
                        .get( Calendar.YEAR), calendarioModificar.get(Calendar.MONTH),
                        calendarioModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        button_modificar_localizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_localizacion(view);
            }
        });

        modificar_gasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificar();
            }
        });
        eliminar_gasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
    }

    public void consultarGasto() {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};
        //Log.d(TAG, "------------------->>> Parametros en deudas:" + parametros );

        try {
            Cursor cursor=db.rawQuery("SELECT * FROM " +utilidades.TABLA_GASTOS_INGRESOS_BD+" WHERE "+utilidades.CAMPO_ID_GASTO_INGRESO+"=? ",parametros);

            cursor.moveToFirst();
            if(cursor.getString( 0 ).equals( "0" )) {
                positivo_negativo.setText( "+" );
            }else {
                positivo_negativo.setText( "-" );
            }
            campoConcepto.setText(cursor.getString(2));
            campoCantidad.setText(cursor.getString(3));
            campofecha.setText( cursor.getString( 4 ) );
            campoLocalizacion.setText( cursor.getString( 5 ) );

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Opción no encontrada",Toast.LENGTH_LONG).show();
        }

    }


    private void modificar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};
        ContentValues values=new ContentValues();
        values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO,campoConcepto.getText().toString());
        values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO,campoCantidad.getText().toString());
        values.put(utilidades.CAMPO_FECHA_GASTO_INGRESO,campofecha.getText().toString());
        values.put(utilidades.CAMPO_LOCALIZACION_GASTO_INGRESO,campoLocalizacion.getText().toString());

        db.update(utilidades.TABLA_GASTOS_INGRESOS_BD,values,utilidades.CAMPO_ID_GASTO_INGRESO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Modificado",Toast.LENGTH_LONG).show();
        db.close();
        returnHome();
    }
    private void eliminar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        db.delete(utilidades.TABLA_GASTOS_INGRESOS_BD,utilidades.CAMPO_ID_GASTO_INGRESO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Eliminado",Toast.LENGTH_LONG).show();
        db.close();
        returnHome();
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
                        campoLocalizacion.setText(addresses.get(0).getAddressLine(0));
                        Log.d(TAG, "--------------------->>>Mostrar Localizacion: " + addresses.get(0).getAddressLine(0) );

                    } catch (IOException e) {
                        campoLocalizacion.setText("No se ha podido añadir la ubicación correctamente");
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
            calendarioModificar.set(Calendar.YEAR, year);
            calendarioModificar.set(Calendar.MONTH, monthOfYear);
            calendarioModificar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        campofecha.setText(sdf.format(calendarioModificar.getTime()));

    }


    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                ListaGastosIngresos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }

    /**
     private void toastMessage(String message){
     Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
     }*/
}