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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class ListaDeudas extends AppCompatActivity {


    private static final String TAG = "ListDataActivity";
    private ListView mListView;

    ConexionSQLiteHelperDeudas conn;

    public static Integer posicionListaClick;

    ArrayList<String> listaInformacion;
    public static ArrayList<String> listaIDs;

    ArrayList<DeudasBD> listaDeudas, listaDeudasOrder;

    Integer x;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_deudas);

        mListView = (ListView) findViewById( R.id.id_lista_deudas );
        conn = new ConexionSQLiteHelperDeudas(getApplicationContext(), "bd_deudas", null, 1);

        try {
            populateListView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                SQLiteDatabase db = conn.getReadableDatabase();
                Cursor data = db.rawQuery("SELECT * FROM " + utilidades.TABLA_DEUDAS_BD,null); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Intent editScreenIntent = new Intent(ListaDeudas.this, ModificarDeuda.class);
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

    private void populateListView() throws ParseException {


        Log.d(TAG, "--------------------->>> ENTRE POPULATE: ");
        SQLiteDatabase db = conn.getReadableDatabase();

        DeudasBD deudas = null;
        listaDeudas = new ArrayList<DeudasBD>();
        listaDeudasOrder = new ArrayList<DeudasBD>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_DEUDAS_BD,null);

        while (cursor.moveToNext()){
            deudas = new DeudasBD();
            deudas.setPagardeber( cursor.getInt(0));
            deudas.setId(cursor.getInt(1));
            deudas.setNombre(cursor.getString(2));
            deudas.setImporte(cursor.getString(3));
            deudas.setFecha(cursor.getString(4));
            deudas.setConcepto( cursor.getString( 5 ) );

            listaDeudas.add(deudas);
        }
        x = 0;
        Log.d(TAG, "--------------------->>> TAMAÑO LISTA: " + listaDeudas.size());
        while(listaDeudas.size()!=0){
            Log.d(TAG, "--------------------->>> ENTRE WHILE: ");

            for(int i=0; i<listaDeudas.size(); i++){

                Log.d(TAG, "--------------------->>> ENTRE FOR: " + listaDeudas.get( x ).getFecha());
                if( CompararFechas( listaDeudas.get( x ).getFecha(), listaDeudas.get( i ).getFecha() ) == listaDeudas.get( x ).getFecha() && listaDeudas.get( x ).getFecha()!= listaDeudas.get( i ).getFecha()){
                    Log.d(TAG, "--------------------->>> ENTRE IF: ");
                    x = i;
                    break;
                } else {
                    Log.d(TAG, "--------------------->>> ENTRE ELSE: ");
                    if (i == listaDeudas.size()-1){

                        listaDeudasOrder.add( listaDeudas.get( x ) );
                        Log.d(TAG, "--------------------->>> PRUEBA: " + listaDeudas.get( x ).getImporte().toString());
                        listaDeudas.remove( listaDeudas.get( x ) );
                        x = 0;
                        break;

                    }
                }
            }
        }

        listaDeudas = listaDeudasOrder;
        obtenerLista();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);
    }

    private void obtenerLista() {
        Log.d(TAG, "--------------------->>> ENTRE OBTENER LISTA: ");
        listaInformacion = new ArrayList<String>();
        listaIDs = new  ArrayList<String>();

        for (int i=0; i<listaDeudas.size(); i++){
            if(listaDeudas.get(i).getPagardeber()==0){
                if(listaDeudas.get(i).getConcepto().length()>15){
                    listaInformacion.add(listaDeudas.get(i).getNombre().substring( 0,15 ) +
                            "..." + " --->  " + "A pagar: " + listaDeudas.get(i).getImporte() + "€");
                }else{
                    listaInformacion.add(listaDeudas.get(i).getNombre() +
                            " --->  " + "A pagar: " + listaDeudas.get(i).getImporte() + "€");
                }

            }else {
                if(listaDeudas.get(i).getConcepto().length()>15){
                    listaInformacion.add(listaDeudas.get(i).getNombre().substring( 0,15 ) +
                            "..." + " ---> " + "A deber: " + listaDeudas.get(i).getImporte() + "€");
                }else {
                    listaInformacion.add( listaDeudas.get( i ).getNombre()
                            + " ---> " + "A deber: " + listaDeudas.get( i ).getImporte() + "€" );
                }
            }
            listaIDs.add(listaDeudas.get(i).getId().toString() + "#" + listaDeudas.get( i ).getPagardeber());
            Log.d(TAG, "------------------->>> Has entrado en:" + listaIDs );
        }
    }


    public String CompararFechas(String z, String y) throws ParseException {
        Log.d(TAG, "--------------------->>> ENTRE COMPARAR FECHAS: " + z + "--" + y);

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

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


}

