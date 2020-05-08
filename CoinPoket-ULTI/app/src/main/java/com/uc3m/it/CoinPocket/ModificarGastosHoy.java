package com.uc3m.it.CoinPocket;




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
import android.widget.ListView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.sql.Array;
import java.util.ArrayList;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
 */

public class ModificarGastosHoy extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    EditText campoConcepto, campoCantidad;

    ConexionSQLiteHelper conn;

    public static Integer posicionListaClick;

    private ListView mListView;
    ArrayList<String> listaInformacion;
    public static ArrayList<String> listaIDs;
    ArrayList<GastosIngresosBD> listaGastos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_hoy);
        mListView = (ListView) findViewById(R.id.listView);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos_ingresos", null, 1);
        campoConcepto = (EditText) findViewById(R.id.id_concepto_gasto);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_gasto);




        populateListView();
        //ArrayAdapter adaptador = new  AdaptadorLista(this, listaInformacion);
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                //String[] nameShow = name.split( "#" );
                //id_gasto_seleccionado = nameShow[0];
                //Toast.makeText(getApplicationContext(),"Id nameShow: " + nameShow, Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onItemClick: You Clicked on " + name);
                SQLiteDatabase db = conn.getReadableDatabase();

                Cursor data = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Intent editScreenIntent = new Intent(ModificarGastosHoy.this, ModificarEliminar.class);
                    Toast.makeText(getApplicationContext(),"Id nameShow: " + i, Toast.LENGTH_SHORT).show();
                    posicionListaClick = i;
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("Item not found");
                }
            }
        });
    }



    private void populateListView() {

        SQLiteDatabase db = conn.getReadableDatabase();

        GastosIngresosBD gastos = null;
        listaGastos = new ArrayList<GastosIngresosBD>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);

        while (cursor.moveToNext()){
            gastos = new GastosIngresosBD();
            gastos.setGastoingreso( cursor.getInt(0));
            gastos.setId(cursor.getInt(1));
            gastos.setConcepto(cursor.getString(2));
            gastos.setCantidad(cursor.getString(3));
            gastos.setFecha( cursor.getString( 4 ) );

            listaGastos.add(gastos);
        }
        //Estaria bien ordenar por fecha los gastos
        /**for(int i=0; i<listaGastos.size();i++){
            String[] fechaArrayGeneral = listaGastos.get( i ).getFecha().split( "/" );
            for(int n=0; n<listaGastos.size();n++){
                String[] fechaArrayAnoActual = listaGastos.get( n ).getFecha().split( "/" );
                if(Integer.getInteger( fechaArrayAnoActual[2] ) > Integer.getInteger( fechaArrayGeneral[2] )){
                    for(int k=n; k<listaGastos.size();k++){
                        listaGastos.get( k ) = listaGastos.get( k + 1 );
                    }
                }
            }
        }*/
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();
        listaIDs = new  ArrayList<String>();

        for (int i=0; i<listaGastos.size(); i++){
            if(listaGastos.get(i).getGastoingreso()==0){
                if(listaGastos.get(i).getConcepto().length()>15){
                    listaInformacion.add(listaGastos.get(i).getConcepto().substring( 0,15 ) +
                            "..." + " --->  " + "+ " + listaGastos.get(i).getCantidad() + "€");
                }else{
                    listaInformacion.add(listaGastos.get(i).getConcepto() +
                            " --->  " + "+ " + listaGastos.get(i).getCantidad() + "€");
                }

            }else {
                if(listaGastos.get(i).getConcepto().length()>15){
                    listaInformacion.add(listaGastos.get(i).getConcepto().substring( 0,15 ) +
                            "..." + " ---> " + "- " + listaGastos.get(i).getCantidad() + "€");
                }else {
                    listaInformacion.add( listaGastos.get( i ).getConcepto()
                            + " ---> " + "- " + listaGastos.get( i ).getCantidad() + "€" );
                }
            }
            listaIDs.add(listaGastos.get(i).getId().toString() + "#" + listaGastos.get( i ).getGastoingreso());
        }
        //Log.d(TAG, "--------------------->>>Lista Gastos: " + listaInformacion);

        //Log.d(TAG, "--------------------->>>Lista IDS: " + listaIDs);

    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}

