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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModificarDeuda extends AppCompatActivity {
    private static final String TAG = "ListDataActivity";

    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    ConexionSQLiteHelperDeudas conn;

    EditText campoConcepto, campoImporte, campoNombre;
    TextView campofecha, deberpagar;

    Button modificar_duda, eliminar_deuda;

    Calendar calendarioModificar = Calendar.getInstance();

    List<Address> addresses = null;

    ListaDeudas seleccion = new ListaDeudas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_deuda );
        conn = new ConexionSQLiteHelperDeudas(getApplicationContext(), "bd_deudas", null, 1);
        campoConcepto = (EditText) findViewById(R.id.id_concepto_modificar_deuda);
        campoNombre = (EditText) findViewById(R.id.id_nombre_modificar_deuda);
        campoImporte = (EditText) findViewById( R.id.id_importe_modificar_deuda);
        campofecha = (TextView) findViewById( R.id.id_fecha_modificar_deuda);
        modificar_duda = (Button) findViewById( R.id.id_modificar_deuda_concreta);
        eliminar_deuda = (Button) findViewById( R.id.id_eliminar_deuda_concreta);
        deberpagar = (TextView) findViewById( R.id.id_pagar_deber_modificar_deuda );

        consultarDeuda();

        campofecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModificarDeuda.this, date, calendarioModificar
                        .get( Calendar.YEAR), calendarioModificar.get(Calendar.MONTH),
                        calendarioModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        modificar_duda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificar();
            }
        });
        eliminar_deuda.setOnClickListener(new View.OnClickListener() {
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
            Cursor cursor=db.rawQuery("SELECT * FROM " +utilidades.TABLA_DEUDAS_BD+" WHERE "+utilidades.CAMPO_ID_DEUDA+"=? ",parametros);

            cursor.moveToFirst();
            Log.d(TAG, "------------------->>> CampoNombre:" + cursor.getString(2) );
            Log.d(TAG, "------------------->>> CampoImporte:" + cursor.getString(3) );
            Log.d(TAG, "------------------->>> campofecha:" + cursor.getString(4) );
            Log.d(TAG, "------------------->>> campoConcepto:" + cursor.getString(5) );

            if(cursor.getString( 0 ).equals( "0" )) {
                deberpagar.setText( "A pagar: " );
            }else {
                deberpagar.setText( "A deber: " );
            }
            campoNombre.setText(cursor.getString(2));
            Log.d(TAG, "------------------->>> CampoNombre:" + cursor.getString(2) );
            campoImporte.setText(cursor.getString(3));
            Log.d(TAG, "------------------->>> CampoImporte:" + cursor.getString(3) );
            campofecha.setText(cursor.getString(4));
            Log.d(TAG, "------------------->>> campofecha:" + cursor.getString(4) );
            campoConcepto.setText( cursor.getString( 5 ) );
            Log.d(TAG, "------------------->>> campoConcepto:" + cursor.getString(5) );


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Opción no encontrada",Toast.LENGTH_LONG).show();
        }

    }


    private void modificar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};
        ContentValues values=new ContentValues();

        if(campoNombre.getText().length()!= 0 && campoImporte.length()!=0 && campofecha!=null){
            values.put(utilidades.CAMPO_NOMBRE_DEUDA,campoNombre.getText().toString());
            values.put(utilidades.CAMPO_IMPORTE_DEUDA,campoImporte.getText().toString());
            values.put(utilidades.CAMPO_FECHA_DEUDA,campofecha.getText().toString());
            values.put(utilidades.CAMPO_CONCEPTO_DEUDA,campoConcepto.getText().toString());

            db.update(utilidades.TABLA_DEUDAS_BD,values,utilidades.CAMPO_ID_DEUDA+"=?",parametros);
            Toast.makeText(getApplicationContext(),"Deuda Modificada",Toast.LENGTH_LONG).show();
            db.close();
            returnHome();
        }else {
            if(campoNombre.getText().length() == 0){
                Toast.makeText(getApplicationContext(),"Nombre vacío", Toast.LENGTH_SHORT).show();
            }else {
                if(campoImporte.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"Importe nulo", Toast.LENGTH_SHORT).show();
                }else {
                    if(campofecha==null){
                        Toast.makeText(getApplicationContext(),"Error al cargar la fecha", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Error en el proceso", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }
    private void eliminar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        db.delete(utilidades.TABLA_DEUDAS_BD,utilidades.CAMPO_ID_DEUDA+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Eliminado",Toast.LENGTH_LONG).show();
        db.close();
        returnHome();
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
                ListaDeudas.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }

    /**
     private void toastMessage(String message){
     Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
     }*/
}