package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Objetivos extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    private ListView mListView;

    ConexionSQLiteHelperObjetivos conn;

    public static Integer posicionListaClick;

    ArrayList<ObjetivosBD> listaObjetivos, listaObjetivosOrder;
    Integer x;

    ArrayList<String> listaInformacion;
    public static ArrayList<String> listaIDs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivos);

        mListView = (ListView) findViewById( R.id.list_view_objetivos );
        conn = new ConexionSQLiteHelperObjetivos(getApplicationContext(), "bd_objetivos", null, 1);

        try {
            populateListView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                SQLiteDatabase db = conn.getReadableDatabase();
                Cursor data = db.rawQuery("SELECT * FROM " + utilidades.TABLA_OBJETIVOS_BD,null); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Intent editScreenIntent = new Intent(Objetivos.this, ModificarObjetivo.class);
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

    public void add_objetivo (View view) {

        Intent intent = new Intent(this, AddObjetivo.class);
        Button okButton = (Button) findViewById(R.id.id_button_add_objetivo);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    private void populateListView() throws ParseException {


        Log.d(TAG, "--------------------->>> ENTRE POPULATE: ");
        SQLiteDatabase db = conn.getReadableDatabase();

        ObjetivosBD objetivos = null;
        listaObjetivos = new ArrayList<ObjetivosBD>();
        listaObjetivosOrder = new ArrayList<ObjetivosBD>();

        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_OBJETIVOS_BD,null);

        while (cursor.moveToNext()){
            objetivos = new ObjetivosBD();
            objetivos.setGastoahorro( cursor.getInt(0));
            objetivos.setId(cursor.getInt(1));
            objetivos.setCantidad(cursor.getString(2));
            objetivos.setFechainicio(cursor.getString(3));
            objetivos.setFechafin(cursor.getString(4));
            objetivos.setMotivo( cursor.getString( 5 ) );

            listaObjetivos.add(objetivos);
        }
        x = 0;
        Log.d(TAG, "--------------------->>> TAMAÑO LISTA: " + listaObjetivos.size());
        while(listaObjetivos.size()!=0){
            Log.d(TAG, "--------------------->>> ENTRE WHILE: ");

            for(int i=0; i<listaObjetivos.size(); i++){

                Log.d(TAG, "--------------------->>> ENTRE FOR: " + listaObjetivos.get( x ).getFechainicio());
                if( CompararFechas( listaObjetivos.get( x ).getFechainicio(), listaObjetivos.get( i ).getFechainicio() ) == listaObjetivos.get( x ).getFechainicio() && listaObjetivos.get( x ).getFechainicio()!= listaObjetivos.get( i ).getFechainicio()){
                    Log.d(TAG, "--------------------->>> ENTRE IF: ");
                    x = i;
                    break;
                } else {
                    Log.d(TAG, "--------------------->>> ENTRE ELSE: ");
                    if (i == listaObjetivos.size()-1){

                        listaObjetivosOrder.add( listaObjetivos.get( x ) );
                        Log.d(TAG, "--------------------->>> PRUEBA: " + listaObjetivos.get( x ).getMotivo().toString());
                        listaObjetivos.remove( listaObjetivos.get( x ) );
                        x = 0;
                        break;

                    }
                }
            }
        }

        listaObjetivos = listaObjetivosOrder;
        obtenerLista();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);
    }

    private void obtenerLista() {
        Log.d(TAG, "--------------------->>> ENTRE OBTENER LISTA: ");
        listaInformacion = new ArrayList<String>();
        listaIDs = new  ArrayList<String>();

        for (int i=0; i<listaObjetivos.size(); i++){
            if(listaObjetivos.get(i).getGastoahorro()==0){
                if(listaObjetivos.get(i).getMotivo().length()>15){
                    listaInformacion.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) +
                            "..." + " --->  " + "Ahorrar: " + listaObjetivos.get(i).getCantidad() + "€");
                }else{
                    listaInformacion.add(listaObjetivos.get(i).getMotivo() +
                            " --->  " + "Ahorrar: " + listaObjetivos.get(i).getCantidad() + "€");
                }

            }else {
                if(listaObjetivos.get(i).getMotivo().length()>15){
                    listaInformacion.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) +
                            "..." + " ---> " + "Gasto máximo: " + listaObjetivos.get(i).getCantidad() + "€");
                }else {
                    listaInformacion.add( listaObjetivos.get( i ).getMotivo()
                            + " ---> " + "Gasto máximo: " + listaObjetivos.get( i ).getCantidad() + "€" );
                }
            }
            listaIDs.add(listaObjetivos.get(i).getId().toString() + "#" + listaObjetivos.get( i ).getGastoahorro());
            Log.d(TAG, "------------------->>> Has entrado en:" + listaIDs );
        }
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

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}