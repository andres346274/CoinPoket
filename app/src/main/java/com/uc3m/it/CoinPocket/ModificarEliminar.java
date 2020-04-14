package com.uc3m.it.CoinPocket;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
 */

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

public class ModificarEliminar extends AppCompatActivity {

    ConexionSQLiteHelper conn;

    EditText campoConcepto, campoCantidad;

    Button modificar_gasto, eliminar_gasto;

    ModificarGastosHoy id_gasto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos", null, 1);
        campoConcepto = (EditText) findViewById(R.id.concepto_modificar_eliminar);
        campoCantidad = (EditText) findViewById(R.id.cantidad_modificar_eliminar);
        consultarGasto( id_gasto.id_gasto_seleccionado );
        modificar_gasto = (Button) findViewById( R.id.modificar_gasto_concreto );
        eliminar_gasto = (Button) findViewById( R.id.eliminar_gasto_concreto );

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

    public void consultarGasto(String campoId) {
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros={campoId.toString()};

        try {
            //select nombre,telefono from usuario where codigo=?
            Cursor cursor=db.rawQuery("SELECT "+utilidades.CAMPO_CONCEPTO_GASTO+","+utilidades.CAMPO_CANTIDAD_GASTO+
                    " FROM "+utilidades.TABLA_GASTOS+" WHERE "+utilidades.CAMPO_ID_GASTO+"=? ",parametros);

            cursor.moveToFirst();
            campoConcepto.setText(cursor.getString(0));
            campoCantidad.setText(cursor.getString(1));

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Opción no encontrada",Toast.LENGTH_LONG).show();
        }

    }


    private void modificar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {id_gasto.id_gasto_seleccionado};
        ContentValues values=new ContentValues();
        values.put(utilidades.CAMPO_CONCEPTO_GASTO,campoConcepto.getText().toString());
        values.put(utilidades.CAMPO_CANTIDAD_GASTO,campoCantidad.getText().toString());

        db.update(utilidades.TABLA_GASTOS,values,utilidades.CAMPO_ID_GASTO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Modificado",Toast.LENGTH_LONG).show();
        db.close();
        returnHome();
    }
    private void eliminar() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {id_gasto.id_gasto_seleccionado};

        db.delete(utilidades.TABLA_GASTOS,utilidades.CAMPO_ID_GASTO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Eliminado",Toast.LENGTH_LONG).show();
        db.close();
        returnHome();
    }



    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }

    /**
     private void toastMessage(String message){
     Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
     }*/
}