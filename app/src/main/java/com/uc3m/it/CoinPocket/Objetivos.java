package com.uc3m.it.CoinPocket;

/**
 *  --> Comparar fechas: https://www.flipandroid.com/la-mejor-manera-de-comparar-fechas-en-android.html
 */

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
import java.util.Locale;

public class Objetivos extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";
    private ListView mListView;

    ConexionSQLiteHelperObjetivos conn;
    ConexionSQLiteHelper connMovimientos;

    public static Integer posicionListaClick;

    ArrayList<ObjetivosBD> listaObjetivos, listaObjetivosOrder;
    ArrayList<GastosIngresosBD> listaBalanceObjetivo;
    Integer x;
    Double totalBalanceObjetivo;

    ArrayList<String> listaInformacion;
    public static ArrayList<String> listaIDs;
    Date strDate, str1Date;

    ArrayList<String>  listaInfoFechaInic, listaInfoFechaFin, listaInfoCantidad, listaInfoMotivo;
    ArrayList <Integer> listaInfoEmoji, listaInfoAhorrarGastar, flagListaInfoBalance;
    ArrayList<Double> listaInfoBalance;
    String fechaHoy = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivos);

        mListView = (ListView) findViewById( R.id.list_view_objetivos );
        conn = new ConexionSQLiteHelperObjetivos(getApplicationContext(), "bd_objetivos", null, 1);
        connMovimientos = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos_ingresos", null, 1);

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
        Log.d(TAG, "--------------------->>> FECHA INICIO OBJETIVOS: " + listaObjetivos.size());
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
        ArrayAdapter adaptador = new MyAdapterObjetivos( Objetivos.this, listaInfoFechaInic, listaInfoFechaFin, listaInfoCantidad, listaInfoMotivo, listaInfoEmoji, listaInfoAhorrarGastar, listaInfoBalance, flagListaInfoBalance );
        //ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);
    }

    private void obtenerLista() throws ParseException {
        Log.d(TAG, "--------------------->>> ENTRE OBTENER LISTA: ");
        listaInformacion = new ArrayList<String>();
        listaInfoEmoji = new ArrayList<Integer>();
        listaInfoFechaInic = new ArrayList<String>();
        listaInfoFechaFin = new ArrayList<String>();
        listaInfoMotivo = new ArrayList<String>();
        listaInfoCantidad = new ArrayList<String>();
        listaInfoAhorrarGastar = new ArrayList<Integer>();
        listaInfoBalance = new ArrayList<Double>();
        flagListaInfoBalance = new ArrayList<Integer>();
        listaIDs = new  ArrayList<String>();
        Double balance = 0.0;

        for (int i=0; i<listaObjetivos.size(); i++){
            if(listaObjetivos.get(i).getGastoahorro()==0){

                if(Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )>balanceObjetivo( listaObjetivos.get( i ).getFechainicio(), listaObjetivos.get( i ).getFechafin(), true)){
                    balance = Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )-
                            balanceObjetivo( listaObjetivos.get( i ).getFechainicio(), listaObjetivos.get( i ).getFechafin(), true);

                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) == listaObjetivos.get( i ).getFechafin()){
                        listaInfoEmoji.add( R.drawable.emoji_cross );
                    }else{
                        listaInfoEmoji.add( R.drawable.emoji_ahorrar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 1 );
                    Log.d(TAG, "--------------------->>> ENTRO EN BALANCE 1. ");

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }

                }else {
                    balance = balanceObjetivo( listaObjetivos.get( i ).getFechainicio(), listaObjetivos.get( i ).getFechafin(), true)-
                            Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() );

                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) == listaObjetivos.get( i ).getFechafin()){
                        listaInfoEmoji.add( R.drawable.emoji_check );
                    }else{
                        listaInfoEmoji.add( R.drawable.emoji_ahorrar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 2 );
                    Log.d(TAG, "--------------------->>> ENTRO EN BALANCE 2. ");

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }
                }

            } else {

                if(Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )>balanceObjetivo( listaObjetivos.get( i ).getFechainicio(), listaObjetivos.get( i ).getFechafin(), false)){
                    balance = Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )-
                            balanceObjetivo( listaObjetivos.get( i ).getFechainicio(), listaObjetivos.get( i ).getFechafin(), false);

                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) == listaObjetivos.get( i ).getFechafin()){
                        listaInfoEmoji.add( R.drawable.emoji_check );
                    }else{
                        listaInfoEmoji.add( R.drawable.emoji_gastar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 3 );
                    Log.d(TAG, "--------------------->>> ENTRO EN BALANCE 3. ");

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }
                }else {
                    balance = balanceObjetivo( listaObjetivos.get( i ).getFechainicio(), listaObjetivos.get( i ).getFechafin(), false ) -
                            Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() );


                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) == listaObjetivos.get( i ).getFechafin()){
                        listaInfoEmoji.add( R.drawable.emoji_cross );
                    }else{
                        listaInfoEmoji.add( R.drawable.emoji_gastar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 4 );
                    Log.d(TAG, "--------------------->>> ENTRO EN BALANCE 4. ");

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }
                }
            }
            listaIDs.add(listaObjetivos.get(i).getId().toString() + "#" + listaObjetivos.get( i ).getGastoahorro());
            Log.d(TAG, "------------------->>> Has entrado en:" + listaIDs );
        }
    }


    /**
     *
     * @param fechaInic
     * @param fechaFin
     * @param ahorroGasto Indica si estoy buscando obtener el balance para un objetivo de ahorro
     *                    (true) o de gasto (false)
     * @return
     * @throws ParseException
     */
    public Double balanceObjetivo(String fechaInic, String fechaFin, Boolean ahorroGasto) throws ParseException {
        SQLiteDatabase dbMovimientos = connMovimientos.getReadableDatabase();
        GastosIngresosBD objetivosBalance = null;
        listaBalanceObjetivo = new ArrayList<GastosIngresosBD>();
        Cursor cursorMovimientos = dbMovimientos.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        totalBalanceObjetivo = 0.0;

        while (cursorMovimientos.moveToNext()){
            if ((CompararFechas( cursorMovimientos.getString(4), fechaInic ).equals( fechaInic ) || cursorMovimientos.getString(4).equals( fechaFin )) && (CompararFechas( cursorMovimientos.getString(4), fechaFin ).equals( cursorMovimientos.getString(4) ) || cursorMovimientos.getString(4).equals( fechaFin ))){

                objetivosBalance = new GastosIngresosBD();
                objetivosBalance.setGastoingreso( cursorMovimientos.getInt(0));
                objetivosBalance.setId(cursorMovimientos.getInt(1));
                objetivosBalance.setConcepto(cursorMovimientos.getString(2));
                objetivosBalance.setCantidad(cursorMovimientos.getString(3));
                objetivosBalance.setFecha(cursorMovimientos.getString(4));

                listaBalanceObjetivo.add(objetivosBalance);
            }
        }
        if(ahorroGasto){
            if(listaBalanceObjetivo != null){
                for(int i=0; i<listaBalanceObjetivo.size();i++){
                    if(listaBalanceObjetivo.get( i ).getCantidad().length()!=0){
                        if(listaBalanceObjetivo.get(i).getGastoingreso()==0){
                            totalBalanceObjetivo = totalBalanceObjetivo + Double.parseDouble( listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }
                        else {
                            totalBalanceObjetivo = totalBalanceObjetivo - Double.parseDouble( listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }

                    }
                }
            }
        }else {
            if(listaBalanceObjetivo != null){
                for(int i=0; i<listaBalanceObjetivo.size();i++){
                    //Miro a ver si es gasto
                    if(listaBalanceObjetivo.get( i ).getGastoingreso() == 1){
                        if(listaBalanceObjetivo.get( i ).getCantidad().length()!=0){
                            totalBalanceObjetivo = totalBalanceObjetivo + Double.parseDouble( listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }
                    }

                }
            }
        }
        return  totalBalanceObjetivo;
    }

    public String CompararFechas(String x, String y) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        Date strDate = sdf.parse(x);
        /**
         int year = strDate.getYear(); // this is deprecated
         int month = strDate.getMonth(); // this is deprecated
         int day = strDate.getDay(); // this is deprecated

         Calendar primeraFecha = Calendar.getInstance();
         primeraFecha.set(day, month, year);*/

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        Date str1Date = sdf1.parse( y );
        /**int year_1 = str1Date.getYear();
         int month_1 = str1Date.getMonth();
         int day_1 = str1Date.getDay();

         Calendar segundaFecha = Calendar.getInstance();
         segundaFecha.set( day_1, month_1, year_1 );*/

        if (str1Date.after(strDate)) {
            return  x;

        }else {
            return y;
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}