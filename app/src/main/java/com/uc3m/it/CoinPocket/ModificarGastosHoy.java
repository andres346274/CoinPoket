package com.uc3m.it.CoinPocket;




import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.util.ArrayList;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
 */

public class ModificarGastosHoy extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    EditText campoConcepto, campoCantidad;

    ConexionSQLiteHelper conn;

    public static String id_gasto_seleccionado;

    private ListView mListView;
    ArrayList<String> listaInformacion;
    ArrayList<GastosBD> listaGastos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_hoy);
        mListView = (ListView) findViewById(R.id.listView);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos", null, 1);
        campoConcepto = (EditText) findViewById(R.id.id_concepto_gasto);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_gasto);




        populateListView();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                String name = adapterView.getItemAtPosition(i).toString();
                String[] nameShow = name.split( "#" );
                id_gasto_seleccionado = nameShow[0];
                //Toast.makeText(getApplicationContext(),"Id nameShow: " + nameShow, Toast.LENGTH_SHORT).show();
                //Log.d(TAG, "onItemClick: You Clicked on " + name);
                SQLiteDatabase db = conn.getReadableDatabase();

                Cursor data = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS,null); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Intent editScreenIntent = new Intent(ModificarGastosHoy.this, ModificarEliminar.class);
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

        GastosBD gastos = null;
        listaGastos = new ArrayList<GastosBD>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS,null);

        while (cursor.moveToNext()){
            gastos = new GastosBD();
            gastos.setId(cursor.getInt(0));
            gastos.setConcepto(cursor.getString(1));
            gastos.setCantidad(cursor.getString(2));

            listaGastos.add(gastos);

        }
        obtenerLista();
    }

    private void obtenerLista() {
        listaInformacion = new ArrayList<String>();

        for (int i=0; i<listaGastos.size(); i++){
            listaInformacion.add(listaGastos.get(i).getId()
                    + "#Id#      " + listaGastos.get(i).getConcepto()
                    + " ---> " + listaGastos.get(i).getCantidad() + "€");
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}

