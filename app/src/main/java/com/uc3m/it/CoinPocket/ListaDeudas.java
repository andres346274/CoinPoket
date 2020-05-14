package com.uc3m.it.CoinPocket;


/**
 *  --> Comparar fechas: https://www.flipandroid.com/la-mejor-manera-de-comparar-fechas-en-android.html
 */

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

    //Inicializacion de las variables de la activity
    ArrayList<String>  listaInfoFecha, listaInfoCantidad, listaInfoConcepto, listaInfoNombre;
    ArrayList <Integer> listaInfoEmoji, listaInfoPagarDeber;
    ListView mListView;
    Integer x;
    Date strDate, str1Date;
    //Variables de BD de Deudas
    ConexionSQLiteHelperDeudas conn;
    ArrayList<DeudasBD> listaDeudas, listaDeudasOrder;
    //Variables públicas utilizadas por otras activities
    public static Integer posicionListaClick;
    public static ArrayList<String> listaIDs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_deudas);

        //Asignacion de los componentes que usamos en la activity
        mListView = (ListView) findViewById( R.id.id_lista_deudas );
        //Iniciación de BD de Deudas
        conn = new ConexionSQLiteHelperDeudas(getApplicationContext(), "bd_deudas", null, 1);

        //Llamada al creador de la lista de deudas
        try {
            populateListView();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //Inicialización de escuchadores en las fechas
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
                    posicionListaClick = i;
                    startActivity(editScreenIntent);
                }
                else{
                    toastMessage("Item not found");
                }
            }
        });
    }


    /**
     * Método creador de lista deudas
     *      listaDeudas = lista de objetos Deuda obtenidos de la BD
     *      listaDeudasOrder = lista temporal utilizada para oredenar listaDeudas
     *      x = variable utilizada en el proceso de ordenar listaDeudas
     * @throws ParseException
     */
    private void populateListView() throws ParseException {

        //Variables de lectura de la BD
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " +
                utilidades.TABLA_DEUDAS_BD,null);
        DeudasBD deudas = null;

        listaDeudas = new ArrayList<DeudasBD>();
        listaDeudasOrder = new ArrayList<DeudasBD>();
        x = 0;

        //Creación de listaDeudas
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

        //Ordenamos cronológicamente listaDeudas en función de la fecha de cada deuda
        while(listaDeudas.size()!=0){

            for(int i=0; i<listaDeudas.size(); i++){
                if( CompararFechas( listaDeudas.get( x ).getFecha(), listaDeudas.get( i ).
                        getFecha() ) == listaDeudas.get( x ).getFecha() && listaDeudas.get( x ).
                        getFecha()!= listaDeudas.get( i ).getFecha()){
                    x = i;
                    break;
                } else {
                    if (i == listaDeudas.size()-1){

                        listaDeudasOrder.add( listaDeudas.get( x ) );
                        listaDeudas.remove( listaDeudas.get( x ) );
                        x = 0;
                        break;

                    }
                }
            }
        }
        listaDeudas = listaDeudasOrder;
        //Llamo al método obtener lista para obtener los datos necesarios en el adaptador de mi lista
        obtenerLista();
        //Incorporo mi lista a mListview con mi adaptador personalizado para deudas
        ArrayAdapter adaptador = new MyAdapterDeudas( ListaDeudas.this, listaInfoFecha,
                listaInfoNombre, listaInfoCantidad, listaInfoConcepto, listaInfoEmoji,
                listaInfoPagarDeber );
        mListView.setAdapter(adaptador);
    }

    /**
     * Obtiene las estructuras y datos necesarios para ser pasados al ArrayAdapter de deudas
     * personalizado y me crea mi lista de IDs (fundamental al hacer click en un item de la
     * lista para modificarlo)
     */
    private void obtenerLista() {

        //Inicialización de variables para pasar al ArrayAdapter
        listaInfoEmoji = new ArrayList<Integer>();
        listaInfoFecha = new ArrayList<String>();
        listaInfoConcepto = new ArrayList<String>();
        listaInfoCantidad = new ArrayList<String>();
        listaInfoNombre = new ArrayList<String>();
        listaInfoPagarDeber = new ArrayList<Integer>();
        //Iniciación de la lista que contendrá la información de el Id de cada deuda y de si este es pagar o deber
        listaIDs = new  ArrayList<String>();

        //Asignación de valores a cada variable para pasar al array adapter
        for (int i=0; i<listaDeudas.size(); i++){
            if(listaDeudas.get(i).getPagardeber()==0){//Si es deuda a pagar
                listaInfoEmoji.add( R.drawable.emoji_deuda_apagar );
                listaInfoFecha.add( listaDeudas.get( i ).getFecha() );
                listaInfoCantidad.add( listaDeudas.get( i ).getImporte());
                listaInfoNombre.add( listaDeudas.get( i ).getNombre() );
                listaInfoPagarDeber.add( listaDeudas.get( i ).getPagardeber() );

                if(listaDeudas.get(i).getConcepto().length()>15){
                    listaInfoConcepto.add(listaDeudas.get(i).getConcepto().substring( 0,15 ) + "...");
                }else{
                    listaInfoConcepto.add(listaDeudas.get(i).getConcepto());
                }

            }else {//Si es deuda a deber
                listaInfoEmoji.add( R.drawable.emoji_deuda_adeber );
                listaInfoFecha.add( listaDeudas.get( i ).getFecha() );
                listaInfoCantidad.add( listaDeudas.get( i ).getImporte());
                listaInfoNombre.add( listaDeudas.get( i ).getNombre() );
                listaInfoPagarDeber.add( listaDeudas.get( i ).getPagardeber() );

                if(listaDeudas.get(i).getConcepto().length()>15){
                    listaInfoConcepto.add(listaDeudas.get(i).getConcepto().substring( 0,15 ) + "...");
                }else{
                    listaInfoConcepto.add(listaDeudas.get(i).getConcepto());
                }
            }
            //Asignación de valores al array de la siguiente forma, por ejemplo: [id#1,id#0,id#0,...]
            listaIDs.add(listaDeudas.get(i).getId().toString() + "#" + listaDeudas.get( i ).getPagardeber());
        }
    }


    /**
     * Método de comparación de fechas
     * @param z fecha 1
     * @param y fecha 2
     * @return
     * @throws ParseException
     */
    public String CompararFechas(String z, String y) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        strDate = sdf.parse(z);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        str1Date = sdf1.parse( y );

        if (str1Date.after(strDate)) {
            return  z;

        }else {
            return y;
        }
    }

    /**
     * Método para crear un mensaje Toast que se muestre en la App
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


}

