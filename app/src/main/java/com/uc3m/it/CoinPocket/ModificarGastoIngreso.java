package com.uc3m.it.CoinPocket;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
 */

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModificarGastoIngreso extends AppCompatActivity {

    //Variable creada para cuando se lance la actividad del mapa al terminar con
    //la asignacion de la localizacion sepa capturar los datos extras que devuelve
    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    //Inicializacion de las variables de la activity
    EditText campoConcepto, campoCantidad, campoLocalizacion;
    TextView campofecha, positivo_negativo;
    Button modificar_gasto, eliminar_gasto, button_modificar_localizacion;
    Calendar calendarioModificar = Calendar.getInstance();
    List<Address> addresses = null;

    //Variables de BD de GastosIngresos
    ConexionSQLiteHelper conn;

    //Incialización de constructor de un objeto de ListaGastosIngresos
    ListaGastosIngresos seleccion = new ListaGastosIngresos();

    //Variable de formato de decimales
    DecimalFormat formatter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar);

        //Asignacion de los componentes que usamos en la activity
        campoConcepto = (EditText) findViewById(R.id.id_concepto_modificar_eliminar);
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_modificar_eliminar);
        campofecha = (TextView) findViewById( R.id.id_fecha_modificar_eliminar);
        campoLocalizacion = (EditText) findViewById( R.id.id_localizacion_modificar_eliminar);
        modificar_gasto = (Button) findViewById( R.id.id_modificar_gasto_concreto);
        eliminar_gasto = (Button) findViewById( R.id.id_eliminar_gasto_concreto);
        button_modificar_localizacion = (Button) findViewById(R.id.id_button_modificar_localizacion);
        positivo_negativo = (TextView) findViewById( R.id.id_positivo_negativo_modificar_eliminar );
        //Iniciación de BD de gastosIngresos
        conn = new ConexionSQLiteHelper(getApplicationContext(),
                "bd_gastos_ingresos", null, 1);
        //Asignación al formato de decimales
        formatter = new DecimalFormat("####.##");

        //La primera acción es consultar el gasto o ingreso en cuestión para ver sus parámetros
        consultarGastoIngreso();

        //Inicialización de escuchador en la fecha
        campofecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog( ModificarGastoIngreso.this, date, calendarioModificar
                        .get( Calendar.YEAR), calendarioModificar.get(Calendar.MONTH),
                        calendarioModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Inicialización de escuchador botón de modificar la localización
        button_modificar_localizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add_localizacion(view);
            }
        });

        //Inicialización de escuchador botón de modificar el gasto o ingreso
        modificar_gasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificar();
            }
        });

        //Inicialización de escuchador botón de eliminar el gasto o ingreso
        eliminar_gasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
    }

    /**
     * Método para consultar a la BD de gastosIngresos por un gasto o ingreso en concreto.
     *      parámetros = valor del id del gasto o ingreso seleccionado en la lista obtenido del array listaIDs
     *          en la posición traida de la clase ListaGastosIngresos y guardada en la variable posicionListaClick
     */
    public void consultarGastoIngreso() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        //Lectura de la BD a través del ID del gasto o ingreso en cuestión y asignación a cada campo correspondiente
        try {
            Cursor cursor=db.rawQuery("SELECT * FROM " +utilidades.TABLA_GASTOS_INGRESOS_BD+
                    " WHERE "+utilidades.CAMPO_ID_GASTO_INGRESO+"=? ",parametros);

            cursor.moveToFirst();
            if(cursor.getString( 0 ).equals( "0" )) {
                positivo_negativo.setText( "+" );
            }else {
                positivo_negativo.setText( "-" );
            }
            campoConcepto.setText(cursor.getString(2));
            campoCantidad.setText(cursor.getString(3));
            campofecha.setText( cursor.getString( 4 ) );
            campoLocalizacion.setText( cursor.getString( 5 ) );

        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Opción no encontrada",Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Método utilizado para modificar los valores de cierto ingreso o gasto concreto en la BD de gastosIngresos
     * cuendo pulsamos el botón de modificar.
     *      parámetros = valor del id del gasto o ingreso seleccionado en la lista obtenido del array listaIDs
     *         en la posición traida de la clase ListaGastosIngresos y guardada en la variable posicionListaClick
     */
    private void modificar() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        //Asignamos los nuevos campos en la BD si se cumplen las condiciones necesarias para asignar un ingreso o gasto
        values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO,campoConcepto.getText().toString());
        values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO,formatter.format( Double.parseDouble(
                campoCantidad.getText().toString().trim() ) ));
        values.put(utilidades.CAMPO_FECHA_GASTO_INGRESO,campofecha.getText().toString());
        values.put(utilidades.CAMPO_LOCALIZACION_GASTO_INGRESO,campoLocalizacion.getText().toString());

        db.update(utilidades.TABLA_GASTOS_INGRESOS_BD,values,utilidades.CAMPO_ID_GASTO_INGRESO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Modificado",Toast.LENGTH_LONG).show();
        db.close();
        //Volvemos a pantalla de ListaIngresosGastos
        returnHome();
    }

    /**
     * Método utilizado para eliminar un gasto o ingreso concreto de la BD a través de su ID
     *      parámetros = valor del id del gasto o ingreso seleccionado en la lista obtenido del array listaIDs
     *         en la posición traida de la clase ListaGastosIngresos y guardada en la variable posicionListaClick
     */
    private void eliminar() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        //Eliminamos el ingreso o gasto en cuestión
        db.delete(utilidades.TABLA_GASTOS_INGRESOS_BD,utilidades.CAMPO_ID_GASTO_INGRESO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Eliminado",Toast.LENGTH_LONG).show();
        db.close();
        //Volvemos a pantalla de ListaIngresosGastos
        returnHome();
    }

    /**
     * Arrancamos la activity del mapa con la indicacion de que es una sub actividad que luego
     * nos devolvera valores
     */
    public void add_localizacion(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, SHOW_SUB_ACTIVITY_ONE);
    }

    /**
     * Captura y gestion de la latitud y la longitud que nos devuelve la activity del mapa
     */
    @Override
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SHOW_SUB_ACTIVITY_ONE) : {
                if (resultCode == MapsActivity.RESULT_OK) {

                    double lati = data.getDoubleExtra("LATITUD", -1);
                    double longi = data.getDoubleExtra("LONGITUD", -1);


                    Geocoder gc = new Geocoder(this, Locale.getDefault());
                    try {

                        addresses = gc.getFromLocation(lati, longi, 10);
                        campoLocalizacion.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        campoLocalizacion.setText("No se ha podido añadir la ubicación correctamente");
                    }
                }
                break;
            }

        }
    }

    /**
     * Data picker para tomar la fecha introducida en caso de cambiarla
     */
    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioModificar.set(Calendar.YEAR, year);
            calendarioModificar.set(Calendar.MONTH, monthOfYear);
            calendarioModificar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    /**
     * Introduccion de la fecha
     */
    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        campofecha.setText(sdf.format(calendarioModificar.getTime()));

    }


    /**
     * Método de retorno a MainActivity al modificar o eliminar un gasto o ingreso
     */
    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                ListaGastosIngresos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}