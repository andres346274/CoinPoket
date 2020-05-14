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

    //Inicializacion de las variables de la activity
    ListView mListView;
    ConexionSQLiteHelperObjetivos conn;
    ConexionSQLiteHelper connMovimientos;
    ArrayList<ObjetivosBD> listaObjetivos, listaObjetivosOrder;
    ArrayList<GastosIngresosBD> listaBalanceObjetivo;
    ArrayList<String>  listaInfoFechaInic, listaInfoFechaFin, listaInfoCantidad, listaInfoMotivo;
    ArrayList <Integer> listaInfoEmoji, listaInfoAhorrarGastar, flagListaInfoBalance;
    ArrayList<Double> listaInfoBalance;
    Integer x;
    Double totalBalanceObjetivo;

    //Inicialización de variable de la fecha de hoy
    String fechaHoy;

    //Variables públicas utilizadas por otras activities
    public static ArrayList<String> listaIDs;
    public static Integer posicionListaClick;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objetivos);

        //Asignacion de los componentes que usamos en la activity
        mListView = (ListView) findViewById( R.id.list_view_objetivos );

        //Iniciación de BD de Objetivos y de gastosIngresos
        conn = new ConexionSQLiteHelperObjetivos(getApplicationContext(), "bd_objetivos", null, 1);
        connMovimientos = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos_ingresos", null, 1);

        //Iniciación de la fecha actual
        fechaHoy = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());

        //Llamada al creador de la lista de objetivos
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
                Cursor data = db.rawQuery("SELECT * FROM " + utilidades.TABLA_OBJETIVOS_BD,null); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    Intent editScreenIntent = new Intent(Objetivos.this, ModificarObjetivo.class);
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
     * Método de onClick en el botón de Añadir Objetivo
     * @param view
     */
    public void add_objetivo (View view) {

        Intent intent = new Intent(this, AddObjetivo.class);
        Button okButton = (Button) findViewById(R.id.id_button_add_objetivo);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }


    /**
     * Método creador de lista deudas
     *      listaObjetivos = lista de objetos Objetivo obtenidos de la BD
     *      listaObjetivosOrder = lista temporal utilizada para oredenar listaObjetivos
     *      x = variable utilizada en el proceso de ordenar listaObjetivos
     * @throws ParseException
     */
    private void populateListView() throws ParseException {

        //Variables de lectura de la BD
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_OBJETIVOS_BD,null);
        ObjetivosBD objetivos = null;

        listaObjetivos = new ArrayList<ObjetivosBD>();
        listaObjetivosOrder = new ArrayList<ObjetivosBD>();
        x = 0;

        //Creación de listaObjetivos
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

        //Ordenamos cronológicamente listaObjetivos en función de la fecha de inicio de cada objetivo
        while(listaObjetivos.size()!=0){

            for(int i=0; i<listaObjetivos.size(); i++){

                if( CompararFechas( listaObjetivos.get( x ).getFechainicio(), listaObjetivos.get( i ).getFechainicio() ) == listaObjetivos.get( x ).getFechainicio() && listaObjetivos.get( x ).getFechainicio()!= listaObjetivos.get( i ).getFechainicio()){
                    x = i;
                    break;
                } else {
                    if (i == listaObjetivos.size()-1){

                        listaObjetivosOrder.add( listaObjetivos.get( x ) );
                        listaObjetivos.remove( listaObjetivos.get( x ) );
                        x = 0;
                        break;

                    }
                }
            }
        }

        listaObjetivos = listaObjetivosOrder;
        //Llamo al método obtener lista para obtener los datos necesarios en el adaptador de mi lista
        obtenerLista();
        //Incorporo mi lista a mListview con mi adaptador personalizado para objetivos
        ArrayAdapter adaptador = new MyAdapterObjetivos( Objetivos.this,
                listaInfoFechaInic, listaInfoFechaFin, listaInfoCantidad, listaInfoMotivo,
                listaInfoEmoji, listaInfoAhorrarGastar, listaInfoBalance, flagListaInfoBalance );
        mListView.setAdapter(adaptador);
    }

    /**
     * Obtiene las estructuras y datos necesarios para ser pasados al ArrayAdapter de objetivos
     * personalizado y me crea mi lista de IDs (fundamental al hacer click en un item de la
     * lista para modificarlo)
     */
    private void obtenerLista() throws ParseException {

        //Inicialización de variables para pasar al ArrayAdapter
        listaInfoEmoji = new ArrayList<Integer>();
        listaInfoFechaInic = new ArrayList<String>();
        listaInfoFechaFin = new ArrayList<String>();
        listaInfoMotivo = new ArrayList<String>();
        listaInfoCantidad = new ArrayList<String>();
        listaInfoAhorrarGastar = new ArrayList<Integer>();
        listaInfoBalance = new ArrayList<Double>();
        flagListaInfoBalance = new ArrayList<Integer>();
        //Incialización de la lista que contendrá la información de el Id de cada deuda y de si este es pagar o deber
        listaIDs = new  ArrayList<String>();
        //Incialización de la variable balance que almacenará el balance de cada objetivo
        Double balance = 0.0;

        //Asignación de valores a cada variable para pasar al array adapter
        for (int i=0; i<listaObjetivos.size(); i++){
            if(listaObjetivos.get(i).getGastoahorro()==0){ //Caso ahorro

                //Caso el objetivo no sea logrado o no esté siendo logrado
                if(Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )>
                        balanceObjetivo( listaObjetivos.get( i ).getFechainicio(),
                                listaObjetivos.get( i ).getFechafin(), true)){
                    balance = Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )-
                            balanceObjetivo( listaObjetivos.get( i ).getFechainicio(), listaObjetivos.get( i ).getFechafin(), true);

                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) ==
                            listaObjetivos.get( i ).getFechafin()){ //Caso objetivo finalizado
                        listaInfoEmoji.add( R.drawable.emoji_cross );
                    }else{ //Caso objetivo no finalizado
                        listaInfoEmoji.add( R.drawable.emoji_ahorrar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 1 ); //Flag de objetivo ahorro no logrado

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }

                 //Caso el objetivo se haya logrado o esté siendo logrado
                }else {
                    balance = balanceObjetivo( listaObjetivos.get( i ).getFechainicio(),
                            listaObjetivos.get( i ).getFechafin(), true)-
                            Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() );

                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) ==
                            listaObjetivos.get( i ).getFechafin()){ //Caso objetivo finalizado
                        listaInfoEmoji.add( R.drawable.emoji_check );
                    }else{ //Caso objetivo no finalizado
                        listaInfoEmoji.add( R.drawable.emoji_ahorrar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 2 ); //Flag de Objetivo ahorro logrado

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }
                }

            } else { //Caso gasto

                //Caso el objetivo se haya logrado o esté siendo logrado
                if(Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )>
                        balanceObjetivo( listaObjetivos.get( i ).getFechainicio(),
                                listaObjetivos.get( i ).getFechafin(), false)){
                    balance = Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() )-
                            balanceObjetivo( listaObjetivos.get( i ).getFechainicio(),
                                    listaObjetivos.get( i ).getFechafin(), false);

                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) ==
                            listaObjetivos.get( i ).getFechafin()){ //Caso objetivo finalizado
                        listaInfoEmoji.add( R.drawable.emoji_check );
                    }else{ //Caso objetivo  no finalizado
                        listaInfoEmoji.add( R.drawable.emoji_gastar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 3 );

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }

                //Caso el objetivo no sea logrado o no esté siendo logrado
                }else {
                    balance = balanceObjetivo( listaObjetivos.get( i ).getFechainicio(),
                            listaObjetivos.get( i ).getFechafin(), false ) -
                            Double.parseDouble( listaObjetivos.get( i ).getCantidad().trim() );


                    if(CompararFechas( listaObjetivos.get( i ).getFechafin(), fechaHoy ) ==
                            listaObjetivos.get( i ).getFechafin()){ //Caso objetivo finalizado
                        listaInfoEmoji.add( R.drawable.emoji_cross );
                    }else{ //Caso objetivo no finalizado
                        listaInfoEmoji.add( R.drawable.emoji_gastar );
                    }
                    listaInfoFechaInic.add( listaObjetivos.get( i ).getFechainicio() );
                    listaInfoFechaFin.add( listaObjetivos.get( i ).getFechafin() );
                    listaInfoCantidad.add( listaObjetivos.get( i ).getCantidad());
                    listaInfoAhorrarGastar.add( listaObjetivos.get( i ).getGastoahorro() );
                    listaInfoBalance.add( balance );
                    flagListaInfoBalance.add( 4 );

                    if(listaObjetivos.get(i).getMotivo().length()>15){
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo().substring( 0,15 ) + "...");
                    }else{
                        listaInfoMotivo.add(listaObjetivos.get(i).getMotivo());
                    }
                }
            }
            //Asignación de valores al array de la siguiente forma, por ejemplo: [id#1,id#0,id#0,...]
            listaIDs.add(listaObjetivos.get(i).getId().toString() + "#" + listaObjetivos.get( i ).getGastoahorro());
        }
    }


    /**
     * Método que calcula el balance para cierto objetivo en función de los datos guardados en la BD
     * de gastosIngresos durante la jornada que dura el objetivo
     * @param fechaInic
     * @param fechaFin
     * @param ahorroGasto Indica si estoy buscando obtener el balance para un objetivo de ahorro
     *                    (true) o de máximo gasto (false)
     * @return
     * @throws ParseException
     */
    public Double balanceObjetivo(String fechaInic, String fechaFin, Boolean ahorroGasto) throws ParseException {

        //Variables de lectura de la BD de gastosIngresos
        SQLiteDatabase dbMovimientos = connMovimientos.getReadableDatabase();
        GastosIngresosBD objetivosBalance = null;
        Cursor cursorMovimientos = dbMovimientos.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);

        listaBalanceObjetivo = new ArrayList<GastosIngresosBD>();
        totalBalanceObjetivo = 0.0;

        //Creación de listaBalanceObjetivo (lista de gastos e ingresos)
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

        //Cálculo del balance en función de los gastos y/o ingresos contenidos en listaBalanceObjetivo
        if(ahorroGasto){ //Caso objetivo ahorro
            if(listaBalanceObjetivo != null){
                for(int i=0; i<listaBalanceObjetivo.size();i++){
                    if(listaBalanceObjetivo.get( i ).getCantidad().length()!=0){
                        if(listaBalanceObjetivo.get(i).getGastoingreso()==0){
                            totalBalanceObjetivo = totalBalanceObjetivo + Double.parseDouble(
                                    listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }
                        else {
                            totalBalanceObjetivo = totalBalanceObjetivo - Double.parseDouble(
                                    listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }

                    }
                }
            }
        }else { //Caso objetivo máximo gasto
            if(listaBalanceObjetivo != null){
                for(int i=0; i<listaBalanceObjetivo.size();i++){

                    if(listaBalanceObjetivo.get( i ).getGastoingreso() == 1){ //Si se trata de un gasto
                        if(listaBalanceObjetivo.get( i ).getCantidad().length()!=0){
                            totalBalanceObjetivo = totalBalanceObjetivo + Double.parseDouble(
                                    listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }
                    }

                }
            }
        }
        return  totalBalanceObjetivo;
    }

    /**
     * Método de comparación de fechas
     * @param x fecha 1
     * @param y fecha 2
     * @return
     * @throws ParseException
     */
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

    /**
     * Método para crear un mensaje Toast que se muestre en la App
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}