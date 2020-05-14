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
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite

 --> Comparar fechas: https://www.flipandroid.com/la-mejor-manera-de-comparar-fechas-en-android.html
 */

public class ListaGastosIngresos extends AppCompatActivity {

    //Inicializacion de las variables de la activity
    ListView mListView;
    EditText campoConcepto, campoCantidad;
    Spinner spinnerCuentasAll, spinnerCuentasSeleccion;
    TextView campoTotalLista;
    ArrayList<String>  listaInfoFecha, listaInfoCantidad, listaInfoConcepto,
            listaInfoLocalizacion;
    ArrayList <Integer> listaInfoEmoji, listaInfoGastoIngreso;
    Integer x;
    Double totalLista;
    Date strDate, str1Date;

    //Variables de BD de GastosIngresos
    ConexionSQLiteHelper conn;
    ArrayList<GastosIngresosBD> listaGastos, listaGastosOrder;

    //Variable de formato de decimales
    DecimalFormat formatter = new DecimalFormat("#,###.##");

    //Variables públicas utilizadas por otras activities
    public static Integer posicionListaClick;
    public static ArrayList<String> listaIDs;
    public static Integer posicionAll = 0;
    public static Integer posicionSeleccion = 0;
    //Flag para determinar si quremos mantener la lista anterior o no
    public static Integer flagPosition = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_gastos_ingresos);

        //Asignacion de los componentes que usamos en la activity
        mListView = (ListView) findViewById(R.id.id_list_gastos_ingresos);
        campoConcepto = (EditText) findViewById(R.id.id_concepto_gasto);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_gasto);
        spinnerCuentasAll = (Spinner) findViewById( R.id.id_spinner_cuentas_all );
        spinnerCuentasSeleccion = (Spinner) findViewById( R.id.id_spinner_cuentas_seleccion );
        campoTotalLista = (TextView) findViewById( R.id.id_total_lista );
        //Iniciación de BD de gastos e ingresos
        conn = new ConexionSQLiteHelper(getApplicationContext(), "bd_gastos_ingresos",
                null, 1);


        //Inciación de los Spinner de selección

        //Spinner primario (spinner a la izquierda en la pantalla)
        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasAll = ArrayAdapter.createFromResource(
                this, R.array.day_month_year_all, android.R.layout.simple_spinner_item );
        adaptadorSpinnerCuentasAll.setDropDownViewResource( android.R.layout.simple_spinner_item );
        spinnerCuentasAll.setAdapter(adaptadorSpinnerCuentasAll);
        //OnClickListener del Spinner primario
        spinnerCuentasAll.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override

            public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                //Determinamos si el flag es 1 ó 0 para saber si debemos cargar la selección de spinners anterior
                if (flagPosition == 1)//Cargamos selección anterior
                {
                    //Buscamos en qué posición se encuentra el spinner primario (spinner izquierda)
                    if(posicionAll==0) {//Spinner primario en "Todos los Movimientos"
                        try {
                            populateListView();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        flagPosition = 0;
                        //Hago invisible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.INVISIBLE );
                    }
                    if(posicionAll == 1) {//Spinner primario en "Día"
                        //Inicializamos spinner secundario correspondiente
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.
                                createFromResource( parent.getContext(), R.array.numbers,
                                        android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource(
                                android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);

                        //onClick en spinner secundario (derrecha)
                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                if(flagPosition == 1) {//Si hay que cargar spinner anterior
                                    try {
                                        populateListViewDia(spinnerCuentasSeleccion.getSelectedItem().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    flagPosition = 0;
                                    spinnerCuentasSeleccion.setSelection( posicionSeleccion );

                                }else  {//Si no hay que cargar spinner anterior
                                    try {
                                        populateListViewDia(parent2.getItemAtPosition(position2).toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //Guardo mi nueva posición en este spinner
                                    posicionSeleccion = position2;
                                }
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        } );
                        //Hago visible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                    }
                    if(posicionAll == 2) {//Spinner primario en "Mes"

                        //Inicializamos spinner secundario correspondiente
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.mes, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);;
                        spinnerCuentasSeleccion.setSelection( posicionSeleccion );
                        //onClick en spinner secundario (derecha)
                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            String mesNumber = null;
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                if (flagPosition == 1) {//Si hay que cargar spinner anterior
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
                                }else {//Si no hay que cargar spinner anterior
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
                                    //Guardo mi nueva posición en este spinner
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
                        //Hago visible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                    }
                    if(posicionAll == 3) {//Spinner primario en "Año"
                        //Inicializamos spinner secundario correspondiente
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.createFromResource( parent.getContext(), R.array.year, android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource( android.R.layout.simple_spinner_item );
                        spinnerCuentasSeleccion.setAdapter(adaptadorSpinnerCuentasSeleccion);
                        spinnerCuentasSeleccion.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent2, View view, int position2, long l) {
                                if (flagPosition == 1) {//Si hay que cargar spinner anterior
                                    spinnerCuentasSeleccion.setSelection( posicionSeleccion );
                                    try {
                                        populateListViewAno(spinnerCuentasSeleccion.getSelectedItem().toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    flagPosition = 0;
                                }else {//Si no hay que cargar spinner anterior
                                    try {
                                        populateListViewAno(parent2.getItemAtPosition(position2).toString());
                                    } catch (ParseException e) {
                                        e.printStackTrace();
                                    }
                                    //Guardo mi nueva posición en este spinner
                                    posicionSeleccion = position2;
                                }
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                        //Hago visible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                    }

                    else{
                        Toast.makeText(getApplicationContext(),"Error al cargar la consulta",
                                Toast.LENGTH_SHORT).show();
                    }
                    //Guardo mi nueva posición en spinner primario
                    spinnerCuentasAll.setSelection( posicionAll );
                }else {//Si no hay que cargar spinner anterior (entramos desde MainActivity)
                    //Guardo mi nueva posición en spinner primario
                    posicionAll = position;
                    if(position==0) {//Spinner primario en "Todos los movimientos"
                        try {
                            populateListView();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //Hago invisible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.INVISIBLE );
                    }
                    if(position == 1) {//Spinner primario en "Día"
                        //Hago visible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                        //Inicializamos spinner secundario correspondiente
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.
                                createFromResource( parent.getContext(), R.array.numbers,
                                        android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource(
                                android.R.layout.simple_spinner_item );
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
                    if(position == 2) {//Spinner primario en "Mes"
                        //Hago visible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                        //Inicializamos spinner secundario correspondiente
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.
                                createFromResource( parent.getContext(), R.array.mes,
                                        android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource(
                                android.R.layout.simple_spinner_item );
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
                    if(position == 3) {//Spinner primario en "Año"
                        //Hago visible el spinner secundario
                        spinnerCuentasSeleccion.setVisibility( View.VISIBLE );
                        //Inicializamos spinner secundario correspondiente
                        ArrayAdapter<CharSequence> adaptadorSpinnerCuentasSeleccion = ArrayAdapter.
                                createFromResource( parent.getContext(), R.array.year,
                                        android.R.layout.simple_spinner_item );
                        adaptadorSpinnerCuentasSeleccion.setDropDownViewResource(
                                android.R.layout.simple_spinner_item );
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
                        Toast.makeText(getApplicationContext(),"Error al cargar la consulta",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //Inicialización de escuchadores en las fecha
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


    /**
     * Método creador de lista de Gastos e Ingresos cuando es seleccionado un día en concreto
     * en Spinner secundario (spinner derecho)
     *      listaGastos = lista de objetos IngresoGasto obtenidos de la BD
     *      listaGastosOrder = lista temporal utilizada para oredenar listaGastos
     *      x = variable utilizada en el proceso de ordenar listaGastos
     * @param dia Día seleccionado por el Spinner secundario (derecha)
     * @throws ParseException
     */
    private  void populateListViewDia(String dia) throws  ParseException {
        //Variables de lectura de la BD
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        GastosIngresosBD gastos = null;

        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();
        x = 0;

        //Creación de listaGastos
        while (cursor.moveToNext()){
            gastos = new GastosIngresosBD();
            gastos.setGastoingreso( cursor.getInt(0));
            gastos.setId(cursor.getInt(1));
            gastos.setConcepto(cursor.getString(2));
            gastos.setCantidad(cursor.getString(3));
            gastos.setFecha( cursor.getString( 4 ) );

            listaGastos.add(gastos);
        }
        //Ordenamos listaGastos cronológicamente y exluimos de ella aquello ingresos o gastos que no sean del día seleccionado
        while(listaGastos.size()!=0){
            for(int i=0; i<listaGastos.size(); i++){

                if( CompararFechas( listaGastos.get( x ).getFecha(), listaGastos.get( i ).getFecha() )
                        == listaGastos.get( x ).getFecha() && listaGastos.get( x ).getFecha()!=
                        listaGastos.get( i ).getFecha()){
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
        //Llamo al método obtener lista para obtener los datos necesarios en el adaptador de mi lista
        obtenerLista();
        //Incorporo mi lista a mListview con mi adaptador personalizado para gastos e ingresos
        ArrayAdapter adaptador = new MyAdapterGastosIngresos( ListaGastosIngresos.this,
                listaInfoFecha, listaInfoCantidad, listaInfoConcepto, listaInfoLocalizacion,
                listaInfoEmoji, listaInfoGastoIngreso );
        mListView.setAdapter(adaptador);
    }


    /**
     * Método creador de lista de Gastos e Ingresos cuando es seleccionado un mes en concreto
     * en Spinner secundario (spinner derecho)
     *      listaGastos = lista de objetos IngresoGasto obtenidos de la BD
     *      listaGastosOrder = lista temporal utilizada para oredenar listaGastos
     *      x = variable utilizada en el proceso de ordenar listaGastos
     * @param mes Mes seleccionado por el Spinner secundario (derecha)
     * @throws ParseException
     */
    private  void populateListViewMes(String mes) throws  ParseException {
        //Variables de lectura de la BD
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        GastosIngresosBD gastos = null;

        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();
        x = 0;

        //Creación de listaGastos
        while (cursor.moveToNext()){
            gastos = new GastosIngresosBD();
            gastos.setGastoingreso( cursor.getInt(0));
            gastos.setId(cursor.getInt(1));
            gastos.setConcepto(cursor.getString(2));
            gastos.setCantidad(cursor.getString(3));
            gastos.setFecha( cursor.getString( 4 ) );

            listaGastos.add(gastos);
        }
        //Ordenamos listaGastos cronológicamente y exluimos de ella aquellos ingresos o gastos que no sean del mes seleccionado
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
        //Llamo al método obtener lista para obtener los datos necesarios en el adaptador de mi lista
        obtenerLista();
        //Incorporo mi lista a mListview con mi adaptador personalizado para gastos e ingresos
        ArrayAdapter adaptador = new MyAdapterGastosIngresos( ListaGastosIngresos.this,
                listaInfoFecha, listaInfoCantidad, listaInfoConcepto, listaInfoLocalizacion,
                listaInfoEmoji, listaInfoGastoIngreso );
        mListView.setAdapter(adaptador);
    }

    /**
     * Método creador de lista de Gastos e Ingresos cuando es seleccionado un año en concreto
     * en Spinner secundario (spinner derecho)
     *      listaGastos = lista de objetos IngresoGasto obtenidos de la BD
     *      listaGastosOrder = lista temporal utilizada para oredenar listaGastos
     *      x = variable utilizada en el proceso de ordenar listaGastos
     * @param ano Mes seleccionado por el Spinner secundario (derecha)
     * @throws ParseException
     */
    private  void populateListViewAno(String ano) throws  ParseException {
        //Variables de lectura de la BD
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        GastosIngresosBD gastos = null;

        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();
        //Substring para coger solo los dos últimos números del año seleccionado en el spinner y que así coincida con el formato de fecha.
        ano = ano.substring( 2,4 );
        x = 0;

        //Creación de listaGastos
        while (cursor.moveToNext()){
            gastos = new GastosIngresosBD();
            gastos.setGastoingreso( cursor.getInt(0));
            gastos.setId(cursor.getInt(1));
            gastos.setConcepto(cursor.getString(2));
            gastos.setCantidad(cursor.getString(3));
            gastos.setFecha( cursor.getString( 4 ) );

            listaGastos.add(gastos);
        }
        //Ordenamos listaGastos cronológicamente y exluimos de ella aquellos ingresos o gastos que no sean del año seleccionado
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
        //Llamo al método obtener lista para obtener los datos necesarios en el adaptador de mi lista
        obtenerLista();
        //Incorporo mi lista a mListview con mi adaptador personalizado para gastos e ingresos
        ArrayAdapter adaptador = new MyAdapterGastosIngresos( ListaGastosIngresos.this,
                listaInfoFecha, listaInfoCantidad, listaInfoConcepto, listaInfoLocalizacion,
                listaInfoEmoji, listaInfoGastoIngreso );
        mListView.setAdapter(adaptador);
    }

    /**
     * Método creador de lista de Gastos e Ingresos cuando es seleccionado mostrar la opción de todos
     * los movimientos guardados
     *      listaGastos = lista de objetos IngresoGasto obtenidos de la BD
     *      listaGastosOrder = lista temporal utilizada para oredenar listaGastos
     *      x = variable utilizada en el proceso de ordenar listaGastos
     * @throws ParseException
     */
    private void populateListView() throws ParseException {

        //Variables de lectura de la BD
        SQLiteDatabase db = conn.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        GastosIngresosBD gastos = null;

        listaGastos = new ArrayList<GastosIngresosBD>();
        listaGastosOrder = new ArrayList<GastosIngresosBD>();
        x = 0;

        //Creación de listaGastos
        while (cursor.moveToNext()){
            gastos = new GastosIngresosBD();
            gastos.setGastoingreso( cursor.getInt(0));
            gastos.setId(cursor.getInt(1));
            gastos.setConcepto(cursor.getString(2));
            gastos.setCantidad(cursor.getString(3));
            gastos.setFecha( cursor.getString( 4 ) );
            gastos.setLocalizacion( cursor.getString( 5 ) );

            listaGastos.add(gastos);
        }
        //Ordenamos listaGastos cronológicamente
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
        //Llamo al método obtener lista para obtener los datos necesarios en el adaptador de mi lista
        obtenerLista();
        //Incorporo mi lista a mListview con mi adaptador personalizado para gastos e ingresos
        ArrayAdapter adaptador = new MyAdapterGastosIngresos( ListaGastosIngresos.this,
                listaInfoFecha, listaInfoCantidad, listaInfoConcepto, listaInfoLocalizacion,
                listaInfoEmoji, listaInfoGastoIngreso );
        mListView.setAdapter(adaptador);
    }


    /**
     * Método que obtiene las estructuras y datos necesarios para ser pasados al ArrayAdapter de
     * GastosIngresos personalizado y me crea mi lista de IDs (fundamental al hacer click en un
     * item de la lista para modificarlo)
     */
    private void obtenerLista() {
        //Inicialización de variables para pasar al ArrayAdapter
        listaInfoEmoji = new ArrayList<Integer>();
        listaInfoFecha = new ArrayList<String>();
        listaInfoConcepto = new ArrayList<String>();
        listaInfoCantidad = new ArrayList<String>();
        listaInfoLocalizacion = new ArrayList<String>();
        listaInfoGastoIngreso = new ArrayList<Integer>();
        //Iniciación de la lista que contendrá la información de el Id de cada input y de si este es un gasto o un ingreso
        listaIDs = new  ArrayList<String>();
        //Variable para almacenar el balance total de los movimientos mostrados en la lista por pantalla
        totalLista = 0.0;

        //Asignación de valores a cada variable para pasar al array adapter
        for (int i=0; i<listaGastos.size(); i++){

            if(listaGastos.get(i).getGastoingreso()==0){//Caso de ingreso
                listaInfoEmoji.add( R.drawable.emoji_ingreso );
                listaInfoFecha.add( listaGastos.get( i ).getFecha() );
                listaInfoCantidad.add( "+" + listaGastos.get( i ).getCantidad());
                listaInfoLocalizacion.add( listaGastos.get( i ).getLocalizacion() );
                listaInfoGastoIngreso.add( listaGastos.get( i ).getGastoingreso() );

                if(listaGastos.get(i).getConcepto().length()>15){
                    listaInfoConcepto.add(listaGastos.get(i).getConcepto().substring( 0,15 ) + "...");
                }else{
                    listaInfoConcepto.add(listaGastos.get(i).getConcepto());
                }

            }else {//Caso de gasto
                listaInfoEmoji.add( R.drawable.emoji_gasto );
                listaInfoFecha.add( listaGastos.get( i ).getFecha() );
                listaInfoCantidad.add( "-" + listaGastos.get( i ).getCantidad());
                listaInfoLocalizacion.add( listaGastos.get( i ).getLocalizacion() );
                listaInfoGastoIngreso.add( listaGastos.get( i ).getGastoingreso() );

                if(listaGastos.get(i).getConcepto().length()>15){
                    listaInfoConcepto.add(listaGastos.get(i).getConcepto().substring( 0,15 ) + "...");
                }else{
                    listaInfoConcepto.add(listaGastos.get(i).getConcepto());
                }
            }
            //Contar el total de la lista
            if(listaGastos.get( i ).getCantidad().length()!=0){
                if(listaGastos.get(i).getGastoingreso()==0){
                    totalLista = totalLista + Double.parseDouble( listaGastos.get( i ).getCantidad().toString().trim() );
                }
                else {
                    totalLista = totalLista - Double.parseDouble( listaGastos.get( i ).getCantidad().toString().trim() );
                }

            }
            //Asignación de valores al array de la siguiente forma por ejemplo: [id#1,id#0,id#0,...]
            listaIDs.add(listaGastos.get(i).getId().toString() + "#" + listaGastos.get( i ).getGastoingreso());
        }
        //Asignacion del valor del balance de la lista en su TextView correspondiente
        campoTotalLista.setText( "Balance total de lista: " +  formatter.format(totalLista) + "€" );
    }


    /**
     * Método de comparación de fechas
     * @param z fecha 1
     * @param y fecha 2
     * @return
     * @throws ParseException
     */
    public String CompararFechas(String x, String y) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        strDate = sdf.parse(x);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        str1Date = sdf1.parse( y );

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

