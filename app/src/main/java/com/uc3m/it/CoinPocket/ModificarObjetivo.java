package com.uc3m.it.CoinPocket;

/**Código basado en las siguientes fuentes:

 --> https://github.com/chenaoh/EjemploSQLite

 --> https://github.com/mitchtabian/SaveReadWriteDeleteSQLite
 --> Comparar fechas: https://www.flipandroid.com/la-mejor-manera-de-comparar-fechas-en-android.html
 */

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ModificarObjetivo extends AppCompatActivity {


    //Inicializacion de las variables de la activity
    EditText campoCantidad, campoMotivo;
    TextView campofechaInicio, campofechaFin, ahorrogasto;
    Button modificar_objetivo, eliminar_objetivo;
    Calendar calendarioInicioModificar = Calendar.getInstance();
    Calendar calendarioFinModificar = Calendar.getInstance();

    //Variables de BD de Objetivos
    ConexionSQLiteHelperObjetivos conn;

    //Incialización de constructor de un objeto de Objetivos()
    Objetivos seleccion = new Objetivos();

    //Variable de formato de decimales
    DecimalFormat formatter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_objetivo );

        //Asignacion de los componentes que usamos en la activity
        campoCantidad = (EditText) findViewById(R.id.id_cantidad_modificar_objetivo);
        campoMotivo = (EditText) findViewById(R.id.id_motivo_modificar_objetivo);
        campofechaInicio = (TextView) findViewById( R.id.id_fechainicio_modificar_objetivo);
        campofechaFin = (TextView) findViewById( R.id.id_fechafin_modificar_objetivo);
        modificar_objetivo = (Button) findViewById( R.id.id_modificar_objetivo_concreto);
        eliminar_objetivo = (Button) findViewById( R.id.id_eliminar_objetivo_concreto);
        ahorrogasto = (TextView) findViewById( R.id.id_ahorro_gasto_modificar_objetivo );
        //Iniciación de BD de Objetivos
        conn = new ConexionSQLiteHelperObjetivos(getApplicationContext(),
                "bd_objetivos", null, 1);
        //Asignación al formato de decimales
        formatter = new DecimalFormat("####.##");

        //La primera acción es consultar el objetivo en cuestión para ver sus parámetros
        consultarObjetivo();

        //Inicialización de escuchadores en las fechas
        campofechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {   //FechaInicio
                new DatePickerDialog(ModificarObjetivo.this, dateInicio, calendarioInicioModificar
                        .get( Calendar.YEAR), calendarioInicioModificar.get(Calendar.MONTH),
                        calendarioInicioModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        campofechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {     //FechaFin
                new DatePickerDialog(ModificarObjetivo.this, dateFin, calendarioFinModificar
                        .get( Calendar.YEAR), calendarioFinModificar.get(Calendar.MONTH),
                        calendarioFinModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Inicialización de escuchador botón de modificar el objetivo
        modificar_objetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    modificar();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        //Inicialización de escuchador botón de eliminar el objetivo
        eliminar_objetivo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
    }

    /**
     * Método para consultar a la BD de objetivos por un objetivo en concreto
     *      parámetros = valor del id del objetivo seleccionado en la lista obtenido del array listaIDs
     *          en la posición traida de la clase objetivos y guardada en la variable posicionListaClick
     */
    public void consultarObjetivo() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        //Lectura de la BD a través del ID del objetivo en cuestión y asignación a cada campo correspondiente
        try {
            Cursor cursor=db.rawQuery("SELECT * FROM " +utilidades.TABLA_OBJETIVOS_BD+
                    " WHERE "+utilidades.CAMPO_ID_OBJETIVO+"=? ",parametros);

            cursor.moveToFirst();

            if(cursor.getString( 0 ).equals( "0" )) {
                ahorrogasto.setText( "Ahorrar: " );
            }else {
                ahorrogasto.setText( "Máximo gasto: " );
            }
            campoCantidad.setText(cursor.getString(2));
            campofechaInicio.setText(cursor.getString(3));
            campofechaFin.setText(cursor.getString(4));
            campoMotivo.setText( cursor.getString( 5 ) );


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Opción no encontrada",Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Método utilizado para modificar los valores de cierto objetivo concreto en la BD de objetivos
     * cuendo pulsamos el botón de modificar.
     *      parámetros = valor del id del objetivo seleccionado en la lista obtenido del array listaIDs
     *         en la posición traida de la clase objetivos y guardada en la variable posicionListaClick
     * @throws ParseException
     */
    private void modificar() throws ParseException {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getWritableDatabase();
        ContentValues values=new ContentValues();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};


        //Asignamos los nuevos campos en la BD si se cumplen las condiciones necesarias para asignar un objetivo
        if(campoCantidad.getText().length() != 0 && CompararFechas( campofechaInicio.getText().
                toString(), campofechaFin.getText().toString()).equals(campofechaInicio.getText().toString())){
            values.put(utilidades.CAMPO_CANTIDAD_OBJETIVO,formatter.format( Double.parseDouble(
                    campoCantidad.getText().toString().trim() ) ));
            values.put(utilidades.CAMPO_FECHA_INICIO_OBJETIVO,campofechaInicio.getText().toString());
            values.put(utilidades.CAMPO_FECHA_FIN_OBJETIVO,campofechaFin.getText().toString());
            values.put(utilidades.CAMPO_MOTIVO_OBJETIVO,campoMotivo.getText().toString());

            db.update(utilidades.TABLA_OBJETIVOS_BD,values,utilidades.CAMPO_ID_OBJETIVO+"=?",parametros);
            Toast.makeText(getApplicationContext(),"Objetivo Modificado",Toast.LENGTH_LONG).show();
            db.close();
            //Volvemos a pantalla de Objetivos
            returnHome();
        }else{
            if(campoCantidad.getText().length() == 0){//Caso de no introducir cantidad
                Toast.makeText(getApplicationContext(),"Cantidad no especificada", Toast.LENGTH_SHORT).show();
            }else{
                if(CompararFechas( campofechaInicio.getText().toString(), campofechaFin.getText().
                        toString()).equals(campofechaFin.getText().toString())){//Caso de fechaFin<fechaInic
                    Toast.makeText(getApplicationContext(),"Fecha Final posterior a Fecha Inicial", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(),"Error en el proceso", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    /**
     * Método utilizado para eliminar un objetivo concreto de la BD a través de su ID
     *      parámetros = valor del id del objetivo seleccionado en la lista obtenido del array listaIDs
     *         en la posición traida de la clase objetivos y guardada en la variable posicionListaClick
     */
    private void eliminar() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        //Eliminamos el objetivo en cuestión
        db.delete(utilidades.TABLA_OBJETIVOS_BD,utilidades.CAMPO_ID_OBJETIVO+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Eliminado",Toast.LENGTH_LONG).show();
        db.close();
        //Volvemos a pantalla de Objetivos
        returnHome();
    }

    /**
     * Data pickers para tomar las fechas introducidas en caso de cambiarlas
     */
    DatePickerDialog.OnDateSetListener dateInicio = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioInicioModificar.set(Calendar.YEAR, year);
            calendarioInicioModificar.set(Calendar.MONTH, monthOfYear);
            calendarioInicioModificar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputInicio();
        }

    };
    DatePickerDialog.OnDateSetListener dateFin = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendarioFinModificar.set(Calendar.YEAR, year);
            calendarioFinModificar.set(Calendar.MONTH, monthOfYear);
            calendarioFinModificar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInputFin();
        }

    };

    /**
     * Introduccion de la fecha de Inicio
     */
    private void actualizarInputInicio() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        campofechaInicio.setText(sdf.format(calendarioInicioModificar.getTime()));

    }

    /**
     * Introduccion de la fecha Final
     */
    private void actualizarInputFin() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        campofechaFin.setText(sdf.format(calendarioFinModificar.getTime()));

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
        Date strDate = sdf.parse(z);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        Date str1Date = sdf1.parse( y );

        if (str1Date.after(strDate)) {
            return  z;

        }else {
            return y;
        }
    }


    /**
     * Método de retorno a Objetivos al modificar o eliminar un objetivo
     */
    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Objetivos.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}