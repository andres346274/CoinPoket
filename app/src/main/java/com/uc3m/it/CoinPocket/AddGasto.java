package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.uc3m.it.CoinPocket.adapter.SpinnerAdapter;
import com.uc3m.it.CoinPocket.data.RatioSingleton;
import com.uc3m.it.CoinPocket.response.Ratio;
import com.uc3m.it.CoinPocket.utilidades.utilidades;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddGasto extends AppCompatActivity {

    //Variable creada para cuando se lance la actividad del mapa al terminar con
    //la asignacion de la localizacion sepa capturar los datos extras que devuelve
    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    //Inicializacion de las variables de la activity
    EditText conceptoGasto;
    EditText cantidadGasto;
    Spinner moneda;
    TextView etFechaGasto;
    EditText localizacionGasto;
    Button buttonSaveGasto;
    Button buttonLocalizacionGasto;
    Calendar calendarioGasto = Calendar.getInstance();
    //Variable para la utilizacion del geocoder
    List<Address> addresses = null;
    //Variable de la fecha actual
    String date_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gasto);

        //Asignacion de los componentes que usamos en la activity
        conceptoGasto = (EditText) findViewById(R.id.id_concepto_gasto);
        cantidadGasto = (EditText) findViewById(R.id.id_cantidad_gasto);
        localizacionGasto = (EditText) findViewById(R.id.id_localizacion_gasto);
        etFechaGasto = (TextView) findViewById(R.id.id_etFecha_gasto);
        buttonSaveGasto = (Button) findViewById(R.id.id_save_gasto);
        buttonLocalizacionGasto = (Button) findViewById(R.id.id_button_localizacion_gasto);
        //Variable moneda para el ingreso de la moneda que usamos
        moneda = findViewById(R.id.moneda);
        moneda.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, RatioSingleton.getRatios()));
        //Asignación de fecha actual
        date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        etFechaGasto.setText(date_n);
        //Asignacion del escuchador por si modificamos la fecha
        etFechaGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddGasto.this, date, calendarioGasto
                        .get(Calendar.YEAR), calendarioGasto.get(Calendar.MONTH),
                        calendarioGasto.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //Escuchador del boton del registro del gasto
        buttonSaveGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarGasto();
            }
        });

    }

    /**
     * Arrancamos la activity del mapa con la indicacion de que es una sub actividad que luego
     * nos devolvera valores
     */

    public void add_localizacion(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, SHOW_SUB_ACTIVITY_ONE);
    }

    @Override

    /**
     * Captura y gestion de la latitud y la longitud que nos devuelve la activity del mapa
     */
    public void onActivityResult(int requestCode,
                                 int resultCode,
                                 Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (SHOW_SUB_ACTIVITY_ONE) : {
                if (resultCode == MapsActivity.RESULT_OK) {

                    double lati = data.getDoubleExtra("LATITUD", -1);
                    double longi = data.getDoubleExtra("LONGITUD", -1);

                    //Comprobacion de si esta devolviendo lo esperado, en caso de no serlo, el metodo nos devolvera -1
                    //localizacionGasto.setText(lati+","+longi);
                    //Arranque del geocoder
                    Geocoder gc = new Geocoder(this, Locale.getDefault());

                    try {
                        //Suponemos que lo coge bien, con sacar el primer resultado valdria
                        addresses = gc.getFromLocation(lati, longi, 2);
                        localizacionGasto.setText(addresses.get(0).getAddressLine(0));
                       // Log.d(TAG, "--------------------->>>Mostrar Localizacion: " + addresses.get(0).getAddressLine(0) );

                    } catch (IOException e) {
                        //A veces da fallo, pero si la aplicacion esta bien arrancada y no se ha hecho ninguna modificacion poco realista d ela ubicacion funciona correctamente
                        //Por si os salta el mensaje, lo ponemos como comprobacion de que ha ido bien, en caso de no ser asi favorecemos la experiencia de usuario comunicandolo
                       localizacionGasto.setText("No se ha podido añadir la ubicación correctamente");
                    }
                }
                break;
            }

        }

    }

    /**
     * Método para registrar el gasto
     *      id_gasto = id asignado a la deuda en su creación
     *      arra_compare = array para movernos con el cursor de la BD a la hora de asignar IDs
     *      sdf = objeto fecha
     *      values = Contenedor introducido en la BD
     */
    private void registrarGasto(){
        ConexionSQLiteHelper newconn = new ConexionSQLiteHelper(this, "bd_gastos_ingresos", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();

        Integer id_gasto = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);

        //Formato de la fecha para la inserciones en la base de datos
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        String cantidad = cantidadGasto.getText().toString();
        Ratio ratio = (Ratio) moneda.getSelectedItem();

        //Contenedor asignado en la BD
        ContentValues values = new ContentValues();

        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(1) );
        }
        while (arra_compare.contains( id_gasto )) {
            id_gasto = id_gasto + 1;
        }


        try {
            Float cantidadInFloat = Float.parseFloat(cantidad);
            Float multiplicacion = cantidadInFloat / ratio.getValue();
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            cantidad = formatter.format(multiplicacion);

        } catch (Exception e) {

        }

        //¿Eliminar?
        /**
        String address = "No especificado";

        if (addresses != null && !addresses.isEmpty()) {

            address = addresses.get(0).getAddressLine(0);

        }*/

        values.put( utilidades.CAMPO_GASTO_INGRESO, "1"); //Flag indicador de que es gasto
        values.put(utilidades.CAMPO_ID_GASTO_INGRESO, id_gasto.toString());
        if(conceptoGasto.getText()!=null){
            values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO, conceptoGasto.getText().toString());
        }else{
            values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO, "");
        }
        if(cantidadGasto.getText()!=null){
            values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO, cantidad);
        }else {
            values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO, "0");
        }
        values.put( utilidades.CAMPO_FECHA_GASTO_INGRESO, sdf.format(calendarioGasto.getTime()));
        if(addresses!=null){
            values.put( utilidades.CAMPO_LOCALIZACION_GASTO_INGRESO, addresses.get(0).getAddressLine(0) );
        }

        Long idResultante=db.insert(utilidades.TABLA_GASTOS_INGRESOS_BD,null,values);


        Toast.makeText(getApplicationContext(),"Gasto Registrado", Toast.LENGTH_SHORT).show();
        db.close();
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
            calendarioGasto.set(Calendar.YEAR, year);
            calendarioGasto.set(Calendar.MONTH, monthOfYear);
            calendarioGasto.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    /**
     * Introduccion de la fecha
     */
    private void actualizarInput() {
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);
        etFechaGasto.setText(sdf.format(calendarioGasto.getTime()));

    }

    /**
     * Método de retorno a MainActivity al añadir gasto
     */
    public void returnHome() {
        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }

}




