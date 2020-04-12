package com.uc3m.it.CoinPocket;


import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.util.ArrayList;

/**
 * Created by User on 2/28/2017.
 */

public class Modificar_gastos_hoy extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    EditText campoConcepto, campoCantidad;

    ConexionSQLiteHelper conn;

    private ListView mListView;
    ArrayList<String> listaInformacion;
    ArrayList<Gastos_BD> listaGastos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modificar_hoy);
        mListView = (ListView) findViewById(R.id.listView);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos", null, 1);
        campoConcepto = (EditText) findViewById(R.id.id_concepto);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad);



        populateListView();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);

        /**mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {

                tv_miemID = (TextView) view.findViewById(R.id.miembro_id);
                tv_miemNombre = (TextView) view.findViewById(R.id.miembro_nombre);

                String aux_miembroId = tv_miemID.getText().toString();
                String aux_miembroNombre = tv_miemNombre.getText().toString();

                Intent modify_intent = new Intent(getApplicationContext(), modificarEliminar.class);
                modify_intent.putExtra("miembroId", aux_miembroId);
                modify_intent.putExtra("miembroNombre", aux_miembroNombre);
                startActivity(modify_intent);
            }
        });*/

       /**mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);
                SQLiteDatabase db = conn.getReadableDatabase();

                Cursor data = db.; //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(Modificar_gastos_hoy.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",name);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });*/
    }

    private void populateListView() {

        SQLiteDatabase db = conn.getReadableDatabase();

        Gastos_BD gastos = null;
        listaGastos = new ArrayList<Gastos_BD>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS,null);

        while (cursor.moveToNext()){
            gastos = new Gastos_BD();
            gastos.setId(cursor.getInt(0));
            gastos.setConcepto(cursor.getString(1));
            gastos.setCantidad(cursor.getString(2));

            listaGastos.add(gastos);

        }
        obtenerLista();



        /**

        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos", null, 1);
        //SQLiteDatabase db = conn.getWritableDatabase();
        //String[] parametros = {campoConcepto.getText().toString()};



        try {
            Cursor data = conn.getData();
            if(data==null) Toast.makeText(getApplicationContext(), "Te peinas",Toast.LENGTH_SHORT).show();
            ArrayList<String> listData = new ArrayList<>();
            while(data.moveToNext()){
                //get the value from the database in column 1
                //then add it to the ArrayList
                listData.add(data.getString(1));
            }
            //create the list adapter and set the adapter
            ListAdapter adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listData);
            mListView.setAdapter(adapter);


        }catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error en la consulta",Toast.LENGTH_SHORT).show();
        }

        //get the data and append to a list


        //set an onItemClickListener to the ListView
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                Log.d(TAG, "onItemClick: You Clicked on " + name);

                Cursor data = mDatabaseHelper.getItemID(name); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Log.d(TAG, "onItemClick: The ID is: " + itemID);
                    Intent editScreenIntent = new Intent(Modificar_gastos_hoy.this, EditDataActivity.class);
                    editScreenIntent.putExtra("id",itemID);
                    editScreenIntent.putExtra("name",name);
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("No ID associated with that name");
                }
            }
        });*/
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();

        for (int i=0; i<listaGastos.size(); i++){
            listaInformacion.add(listaGastos.get(i).getId()
                    + " ---> " + listaGastos.get(i).getConcepto()
            + " ---> " + listaGastos.get(i).getCantidad() + "€");
        }
    }



    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    private void actualizarUsuario() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={campoConcepto.getText().toString()};
        ContentValues values=new ContentValues();
        values.put(utilidades.CAMPO_CONCEPTO_GASTO,campoConcepto.getText().toString());
        values.put(utilidades.CAMPO_CANTIDAD_GASTO,campoCantidad.getText().toString());

        db.update(utilidades.TABLA_GASTOS,values,utilidades.CAMPO_CONCEPTO_GASTO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Ya se actualizó el usuario",Toast.LENGTH_LONG).show();
        db.close();

    }

    private void eliminarUsuario() {
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros={campoConcepto.getText().toString()};

        db.delete(utilidades.TABLA_GASTOS,utilidades.CAMPO_CONCEPTO_GASTO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Ya se Eliminó el usuario",Toast.LENGTH_LONG).show();
        db.close();
    }
}

//Fuente del código https://www.youtube.com/watch?v=ml0i0hnL_WY

//https://github.com/mitchtabian/SaveReadWriteDeleteSQLite/tree/master/SaveAndDisplaySQL/app/src/main/java/com/tabian/saveanddisplaysql