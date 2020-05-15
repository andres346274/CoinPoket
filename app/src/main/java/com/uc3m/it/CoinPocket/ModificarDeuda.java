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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ModificarDeuda extends AppCompatActivity {

    //Variable creada para cuando se lance la actividad del mapa al terminar con
    //la asignacion de la localizacion sepa capturar los datos extras que devuelve
    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    //Inicializacion de las variables de la activity
    EditText campoConcepto, campoImporte, campoNombre;
    TextView campofecha, deberpagar;
    Button modificar_duda, eliminar_deuda;
    Calendar calendarioModificar = Calendar.getInstance();

    //Variables de BD de Deudas
    ConexionSQLiteHelperDeudas conn;

    //Incialización de constructor de un objeto de ListaDeudas
    ListaDeudas seleccion = new ListaDeudas();

    DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_deuda );

        //Asignacion de los componentes que usamos en la activity
        campoConcepto = (EditText) findViewById(R.id.id_concepto_modificar_deuda);
        campoNombre = (EditText) findViewById(R.id.id_nombre_modificar_deuda);
        campoImporte = (EditText) findViewById( R.id.id_importe_modificar_deuda);
        campofecha = (TextView) findViewById( R.id.id_fecha_modificar_deuda);
        modificar_duda = (Button) findViewById( R.id.id_modificar_deuda_concreta);
        eliminar_deuda = (Button) findViewById( R.id.id_eliminar_deuda_concreta);
        deberpagar = (TextView) findViewById( R.id.id_pagar_deber_modificar_deuda );
        //Iniciación de BD de Deudas
        conn = new ConexionSQLiteHelperDeudas(getApplicationContext(), "bd_deudas", null, 1);
        formatter = new DecimalFormat("####.##");

        //La primera acción es consultar la deuda en cuestión para ver sus parámetros
        consultarDeuda();

        //Inicialización de escuchador en la fecha
        campofecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(ModificarDeuda.this, date, calendarioModificar
                        .get( Calendar.YEAR), calendarioModificar.get(Calendar.MONTH),
                        calendarioModificar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Inicialización de escuchador botón de modificar la deuda gasto o ingreso
        modificar_duda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                modificar();
            }
        });

        //Inicialización de escuchador botón de eliminar la deuda
        eliminar_deuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                eliminar();
            }
        });
    }


    /**
     * Método para consultar a la BD de deudas por un gasto o ingreso en concreto.
     *      parámetros = valor del id de la deuda seleccionada en la lista obtenido del array listaIDs
     *          en la posición traida de la clase ListaDeudas y guardada en la variable posicionListaClick
     */
    public void consultarDeuda() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getReadableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        //Lectura de la BD a través del ID de la deuda en cuestión y asignación a cada campo correspondiente
        try {
            Cursor cursor=db.rawQuery("SELECT * FROM " +utilidades.TABLA_DEUDAS_BD+" WHERE "+utilidades.CAMPO_ID_DEUDA+"=? ",parametros);

            cursor.moveToFirst();

            if(cursor.getString( 0 ).equals( "0" )) {
                deberpagar.setText( "A pagar: " );
            }else {
                deberpagar.setText( "A deber: " );
            }
            campoNombre.setText(cursor.getString(2));
            campoImporte.setText(cursor.getString(3));
            campofecha.setText(cursor.getString(4));
            campoConcepto.setText( cursor.getString( 5 ) );


        }catch (Exception e){
            Toast.makeText(getApplicationContext(),"Opción no encontrada",Toast.LENGTH_LONG).show();
        }

    }


    /**
     * Método utilizado para modificar los valores de cierta deuda concreta en la BD de deudas
     * cuendo pulsamos el botón de modificar.
     *      parámetros = valor del id de la deuda seleccionada en la lista obtenido del array listaIDs
     *         en la posición traida de la clase ListaDeudas y guardada en la variable posicionListaClick
     */
    private void modificar() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};
        ContentValues values=new ContentValues();

        //Asignamos los nuevos campos en la BD si se cumplen las condiciones necesarias para asignar una deuda
        if(campoNombre.getText().length()!= 0 && campoImporte.length()!=0 && campofecha!=null){
            values.put(utilidades.CAMPO_NOMBRE_DEUDA,campoNombre.getText().toString());
            values.put(utilidades.CAMPO_IMPORTE_DEUDA,formatter.format( Double.parseDouble(
                    campoImporte.getText().toString().trim() ) ));
            values.put(utilidades.CAMPO_FECHA_DEUDA,campofecha.getText().toString());
            values.put(utilidades.CAMPO_CONCEPTO_DEUDA,campoConcepto.getText().toString());

            db.update(utilidades.TABLA_DEUDAS_BD,values,utilidades.CAMPO_ID_DEUDA+"=?",parametros);
            Toast.makeText(getApplicationContext(),"Deuda Modificada",Toast.LENGTH_LONG).show();
            db.close();
            //Volvemos a pantalla de ListaDeudas
            returnHome();
        }else {
            if(campoNombre.getText().length() == 0){
                Toast.makeText(getApplicationContext(),"Nombre vacío", Toast.LENGTH_SHORT).show();
            }else {
                if(campoImporte.getText().length()==0){
                    Toast.makeText(getApplicationContext(),"Importe nulo", Toast.LENGTH_SHORT).show();
                }else {
                    if(campofecha==null){
                        Toast.makeText(getApplicationContext(),"Error al cargar la fecha", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(),"Error en el proceso", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }

    /**
     * Método utilizado para eliminar una deuda concreta de la BD a través de su ID
     *      parámetros = valor del id dela deuda seleccionada en la lista obtenido del array listaIDs
     *         en la posición traida de la clase ListaDeudas y guardada en la variable posicionListaClick
     */
    private void eliminar() {

        //Variables de lectura de la BD
        SQLiteDatabase db=conn.getWritableDatabase();
        String[] parametros= {seleccion.listaIDs.get( seleccion.posicionListaClick).split( "#" )[0]};

        //Eliminamos la deuda en cuestión
        db.delete(utilidades.TABLA_DEUDAS_BD,utilidades.CAMPO_ID_DEUDA+"=?",parametros);
        Toast.makeText(getApplicationContext(),"Gasto Eliminado",Toast.LENGTH_LONG).show();
        db.close();
        //Volvemos a pantalla de listaDeudas
        returnHome();
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
     * Método de retorno a ListaDeudas al modificar o eliminar una deuda
     */
    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                ListaDeudas.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}