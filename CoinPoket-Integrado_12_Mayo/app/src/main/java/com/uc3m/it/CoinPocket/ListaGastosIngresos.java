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
import android.widget.Spinner;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
 */

public class ListaGastosIngresos extends AppCompatActivity {

    private static final String TAG = "ListDataActivity";

    EditText campoConcepto, campoCantidad;
    Spinner spinnerCuentasAll, spinnerCuentasSeleccion;

    ConexionSQLiteHelper conn;

    public static Integer posicionListaClick;

    private ListView mListView;
    ArrayList<String> listaInformacion;
    public static ArrayList<String> listaIDs;
    ArrayList<GastosIngresosBD> listaGastos, listaGastosOrder;

    Integer x;
    public static Integer posicionAll = 0;
    public static Integer posicionSeleccion = 0;
    //Flag para determinar si iniciamos la lista inicial de "todos los movimientos o si
    //hemos modificado o eliminado algún movimiento y simplemente queremos volver a nuestra
    //selección de lista anterior
    public static Integer flagPosition = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos_ingresos);
        mListView = (ListView) findViewById(R.id.id_list_gastos_ingresos);
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos_ingresos", null, 1);
        campoConcepto = (EditText) findViewById(R.id.id_concepto_gasto);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_gasto);
        spinnerCuentasAll = (Spinner) findViewById( R.id.id_spinner_cuentas_all );
        spinnerCuentasSeleccion = (Spinner) findViewById( R.id.id_spinner_cuentas_seleccion );

        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasAll = ArrayAdapter.createFromResource( this, R.array.day_month_year_all, android.R.layout.simple_spinner_item );
        adaptadorSpinnerCuentasAll.setDropDownViewResource( android.R.layout.simple_spinner_item );
        spinnerCuentasAll.setAdapter(adaptadorSpinnerCuentasAll);

        spinnerCuentasAll.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {


            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                if (flagPosition == 1)
                {
                    if(posicionAll==0) {
                        try {
                            populateListView();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        flagPosition = 0;
                        spinnerCuentasSeleccion.setVisibility( View.INVISIBLE );
                    }
                    if(posicionAll == 1) {
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.numbers, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);

                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                if(flagPosition == 1) {
                                    try {
                                        populateListViewDia(spinnerCuentasSeleccion.getSelectedItem().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    flagPosition = 0;
                                    spinnerCuentasSeleccion.setSelection( posicionSeleccion );

                                }else  {
                                    try {
                                        populateListViewDia(parent2.getItemAtPosition(position2).toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    posicionSeleccion = position2;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        } );

                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                    }
                    if(posicionAll == 2) {

                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.mes, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);
                        //Log.d(TAG, "--------------------->>> POSICION SELECCION: " + posicionSeleccion);
                        spinnerCuentasSeleccion.setSelection( posicionSeleccion );

                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            String mesNumber = null;
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                if (flagPosition == 1) {
                                    spinnerCuentasSeleccion.setSelection( posicionSeleccion );
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Enero" )) mesNumber = "01";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Febrero" )) mesNumber = "02";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Marzo" )) mesNumber = "03";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Abril" )) mesNumber = "04";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Mayo" )) mesNumber = "05";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Junio" )) mesNumber = "06";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Julio" )) mesNumber = "07";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Agosto" )) mesNumber = "08";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Septiembre" )) mesNumber = "09";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Octubre" )) mesNumber = "10";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Noviembre" )) mesNumber = "11";
                                    if(spinnerCuentasSeleccion.getSelectedItem().toString().equals( "Diciembre" )) mesNumber = "12";
                                    flagPosition = 0;
                                }else {
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Enero" )) mesNumber = "01";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Febrero" )) mesNumber = "02";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Marzo" )) mesNumber = "03";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Abril" )) mesNumber = "04";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Mayo" )) mesNumber = "05";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Junio" )) mesNumber = "06";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Julio" )) mesNumber = "07";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Agosto" )) mesNumber = "08";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Septiembre" )) mesNumber = "09";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Octubre" )) mesNumber = "10";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Noviembre" )) mesNumber = "11";
                                    if(parent2.getItemAtPosition(position2).toString().equals( "Diciembre" )) mesNumber = "12";
                                    posicionSeleccion = position2;
                                }
                                try {
                                    populateListViewMes(mesNumber);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                    }
                    if(posicionAll == 3) {
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.year, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);
                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                if (flagPosition == 1) {
                                    spinnerCuentasSeleccion.setSelection( posicionSeleccion );
                                    try {
                                        populateListViewAno(spinnerCuentasSeleccion.getSelectedItem().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    flagPosition = 0;
                                }else {
                                    try {
                                        populateListViewAno(parent2.getItemAtPosition(position2).toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    posicionSeleccion = position2;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        //Log.d(TAG, "--------------------->>> POSICION SELECCION: " + posicionSeleccion);

                        //Log.d(TAG, "--------------------->>> POSICION SELECCION string: " + spinnerCuentasSeleccion.getSelectedItem().toString());


                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                    }

                    else{
                        Log.d(TAG, "Error al cargar movimientos");
                    }
                    spinnerCuentasAll.setSelection( posicionAll );
                }else {
                    posicionAll = position;
                    if(position==0) {
                        try {
                            populateListView();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        spinnerCuentasSeleccion.setVisibility( View.INVISIBLE );
                    }
                    if(position == 1) {
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.numbers, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);
                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                try {
                                    populateListViewDia(parent2.getItemAtPosition(position2).toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                posicionSeleccion = position2;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        } );
                    }
                    if(position == 2) {
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.mes, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);
                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                String mesNumber = null;
                                if(parent2.getItemAtPosition(position2).toString().equals( "Enero" )) mesNumber = "01";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Febrero" )) mesNumber = "02";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Marzo" )) mesNumber = "03";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Abril" )) mesNumber = "04";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Mayo" )) mesNumber = "05";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Junio" )) mesNumber = "06";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Julio" )) mesNumber = "07";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Agosto" )) mesNumber = "08";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Septiembre" )) mesNumber = "09";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Octubre" )) mesNumber = "10";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Noviembre" )) mesNumber = "11";
                                if(parent2.getItemAtPosition(position2).toString().equals( "Diciembre" )) mesNumber = "12";
                                try {
                                    populateListViewMes(mesNumber);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                posicionSeleccion = position2;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        } );
                    }
                    if(position == 3) {
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.year, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);
                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {

                                try {
                                    populateListViewAno(parent2.getItemAtPosition(position2).toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                posicionSeleccion = position2;
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        } );
                    }

                    else{
                        Log.d(TAG, "Error al cargar movimientos");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        /**populateListView();
        //ArrayAdapter adaptador = new  AdaptadorLista(this, listaInformacion);
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);*/
        //mListView.setAdapter(adaptador);


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView adapterView, View view, int i, long l) {
                SQLiteDatabase db = conn.getReadableDatabase();
                Cursor data = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null); //get the id associated with that name
                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    flagPosition = 1;
                    Intent editScreenIntent = new Intent(ListaGastosIngresos.this, ModificarEliminar.class);
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



    private  void populateListViewDia(String dia) throws  ParseException {
        SQLiteDatabase db = conn.getReadableDatabase();

        GastosIngresosBD gastos = null;
        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();

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
        x = 0;
        while(listaGastos.size()!=0){
            for(int i=0; i<listaGastos.size(); i++){

                if( CompararFechas( listaGastos.get( x ).getFecha(), listaGastos.get( i ).getFecha() ) == listaGastos.get( x ).getFecha() && listaGastos.get( x ).getFecha()!= listaGastos.get( i ).getFecha()){
                    x = i;
                    break;
                } else {
                    if (i == listaGastos.size()-1){
                        if (listaGastos.get( x ).getFecha().split( "/" )[0].equals( dia )){
                            listaGastosOrder.add( listaGastos.get( x ) );
                        }
                        listaGastos.remove( listaGastos.get( x ) );
                        x = 0;
                        break;

                    }
                }
            }
        }
        listaGastos = listaGastosOrder;
        obtenerLista();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);
    }

    private  void populateListViewMes(String mes) throws  ParseException {
        SQLiteDatabase db = conn.getReadableDatabase();

        GastosIngresosBD gastos = null;
        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();

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
        x = 0;
        while(listaGastos.size()!=0){
            for(int i=0; i<listaGastos.size(); i++){

                if( CompararFechas( listaGastos.get( x ).getFecha(), listaGastos.get( i ).getFecha() ) == listaGastos.get( x ).getFecha() && listaGastos.get( x ).getFecha()!= listaGastos.get( i ).getFecha()){
                    x = i;
                    break;
                } else {
                    if (i == listaGastos.size()-1){
                        if (listaGastos.get( x ).getFecha().split( "/" )[1].equals( mes )){
                            listaGastosOrder.add( listaGastos.get( x ) );
                        }
                        listaGastos.remove( listaGastos.get( x ) );
                        x = 0;
                        break;

                    }
                }
            }
        }
        listaGastos = listaGastosOrder;
        obtenerLista();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);
    }

    private  void populateListViewAno(String ano) throws  ParseException {
        ano = ano.substring( 2,4 );
        SQLiteDatabase db = conn.getReadableDatabase();

        GastosIngresosBD gastos = null;
        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();

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
        x = 0;
        while(listaGastos.size()!=0){
            for(int i=0; i<listaGastos.size(); i++){

                if( CompararFechas( listaGastos.get( x ).getFecha(), listaGastos.get( i ).getFecha() ) == listaGastos.get( x ).getFecha() && listaGastos.get( x ).getFecha()!= listaGastos.get( i ).getFecha()){
                    x = i;
                    break;
                } else {
                    if (i == listaGastos.size()-1){
                        if (listaGastos.get( x ).getFecha().split( "/" )[2].equals( ano )){
                            listaGastosOrder.add( listaGastos.get( x ) );
                        }
                        listaGastos.remove( listaGastos.get( x ) );
                        x = 0;
                        break;

                    }
                }
            }
        }
        listaGastos = listaGastosOrder;
        obtenerLista();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);
    }

    private void populateListView() throws ParseException {


        SQLiteDatabase db = conn.getReadableDatabase();

        GastosIngresosBD gastos = null;
        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();

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
        x = 0;
        while(listaGastos.size()!=0){

            for(int i=0; i<listaGastos.size(); i++){

                if( CompararFechas( listaGastos.get( x ).getFecha(), listaGastos.get( i ).getFecha() ) == listaGastos.get( x ).getFecha()&& listaGastos.get( x ).getFecha()!=listaGastos.get(i).getFecha()){
                        x = i;
                        break;

                } else {
                        if (i == listaGastos.size()-1){

                            listaGastosOrder.add( listaGastos.get( x ) );
                            listaGastos.remove( listaGastos.get( x ) );
                            x = 0;
                            break;

                        }


                }
            }
        }

        listaGastos = listaGastosOrder;
        obtenerLista();
        ArrayAdapter adaptador = new ArrayAdapter(this,android.R.layout.simple_list_item_1, listaInformacion);
        mListView.setAdapter(adaptador);
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
    }


    public String CompararFechas(String x, String y) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        Date strDate = sdf.parse(x);

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
            return  x;

        }else {
            return y;
        }
    }

    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }
}

