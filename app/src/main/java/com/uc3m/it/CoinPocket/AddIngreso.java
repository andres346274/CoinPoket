package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class AddIngreso extends AppCompatActivity {

    private static final int ACTIVITY_CREATE=0;

    //Variable para recuperar latitud y longitud cuando lanzamos la activity de mapas
    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    //Inicializacion de las variables de la activity
    EditText conceptoIngreso;
    EditText cantidadIngreso;
    TextView etFechaIngreso;
    EditText localizacionIngreso;
    Button buttonSaveIngreso;
    Button buttonLocalizacionIngreso;
    Calendar calendarioIngreso = Calendar.getInstance();
    //Variable para la utilizacion del geocoder
    List<Address> addresses = null;
    Spinner moneda;
    //Variable de la fecha actual
    String date_n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ingreso);

        //Asignacion de los componentes que usamos en la activity
        conceptoIngreso = (EditText) findViewById(R.id.id_concepto_ingreso);
        cantidadIngreso = (EditText) findViewById(R.id.id_cantidad_ingreso);
        localizacionIngreso = (EditText) findViewById(R.id.id_localizacion_ingreso);
        etFechaIngreso = (TextView) findViewById(R.id.id_etFecha_ingreso);
        buttonSaveIngreso = (Button) findViewById(R.id.id_save_ingreso);
        buttonLocalizacionIngreso = (Button) findViewById(R.id.id_button_localizacion_ingreso);
        //Variable moneda para el ingreso de la moneda que usamos
        moneda = findViewById(R.id.moneda);
        moneda.setAdapter(new SpinnerAdapter(this, android.R.layout.simple_spinner_dropdown_item, RatioSingleton.getRatios()));
        //Asignación de fecha actual
        date_n = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
        etFechaIngreso.setText(date_n);

        //Asignacion del escuchador por si modificamos la fecha
        etFechaIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddIngreso.this, date, calendarioIngreso.get(Calendar.YEAR), calendarioIngreso.get(Calendar.MONTH),
                        calendarioIngreso.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //Escuchador del boton del registro del ingreso
        buttonSaveIngreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarIngreso();
            }
        });

    }
    //Arrancamos la activity del mapa con la indicacion de que es una sub actividad que luego nos devolvera valores
    public void add_localizacion(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, SHOW_SUB_ACTIVITY_ONE);
    }

    @Override
    //Captura y gestion de la latitud y la longitud que nos devuelve la activity del mapa
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
                    //Arranque del geocoder
                    Geocoder gc = new Geocoder(this, Locale.getDefault());
                    try {
                        //Suponemos que lo coge bien, con sacar el primer resultado valdria
                        addresses = gc.getFromLocation(lati, longi, 2);
                        localizacionIngreso.setText(addresses.get(0).getAddressLine(0));

                    } catch (IOException e) {
                        //A veces da fallo, pero si la aplicacion esta bien arrancada y no se ha hecho ninguna modificacion poco realista d ela ubicacion funciona correctamente
                        //Por si os salta el mensaje, lo ponemos como comprobacion de que ha ido bien, en caso de no ser asi favorecemos la experiencia de usuario comunicandolo
                        localizacionIngreso.setText("No se ha podido añadir la ubicación correctamente");
                    }
                }
                break;
            }

        }
    }
    /**
     * Método para registrar el Ingreso
     *      id_ingreso = id asignado a la ingreso en su creación
     *      arra_compare = array para movernos con el cursor de la BD a la hora de asignar IDs
     *      sdf = objeto fecha
     *      values = Contenedor introducido en la BD
     */
    private void registrarIngreso(){
        ConexionSQLiteHelper newconn = new ConexionSQLiteHelper(this, "bd_gastos_ingresos", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();

        Integer id_ingreso = 0;
        ArrayList arra_compare = new ArrayList();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        while (cursor.moveToNext()){
            arra_compare.add( cursor.getInt(1) );
        }
        while (arra_compare.contains( id_ingreso )) {
            id_ingreso = id_ingreso + 1;
        }

        String cantidad = cantidadIngreso.getText().toString();
        Ratio ratio = (Ratio) moneda.getSelectedItem();
        try {
            Float cantidadInFloat = Float.parseFloat(cantidad);
            Float multiplicacion = cantidadInFloat / ratio.getValue();
            DecimalFormat formatter = new DecimalFormat("#,###.##");
            cantidad = formatter.format(multiplicacion);
        } catch (Exception e) {

        }

        //Formato de la fecha para la inserciones en la base de datos
        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        ContentValues values = new ContentValues();
        values.put( utilidades.CAMPO_GASTO_INGRESO, "0" );//Flag indicador de que es ingreso
        values.put(utilidades.CAMPO_ID_GASTO_INGRESO, id_ingreso.toString());
        if(conceptoIngreso.getText()!=null){
            values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO, conceptoIngreso.getText().toString());
        }else{
            values.put(utilidades.CAMPO_CONCEPTO_GASTO_INGRESO, "");
        }
        if(cantidadIngreso.getText()!=null){
            values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO, cantidad);
        }else{
            values.put(utilidades.CAMPO_CANTIDAD_GASTO_INGRESO, "0");
        }
        values.put( utilidades.CAMPO_FECHA_GASTO_INGRESO, sdf.format(calendarioIngreso.getTime()));
        if(addresses!=null){
            values.put( utilidades.CAMPO_LOCALIZACION_GASTO_INGRESO, addresses.get(0).getAddressLine(0) );
        }

        Long idResultante=db.insert(utilidades.TABLA_GASTOS_INGRESOS_BD,null,values);


        Toast.makeText(getApplicationContext(),"Ingreso Registrado ", Toast.LENGTH_SHORT).show();
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
            calendarioIngreso.set(Calendar.YEAR, year);
            calendarioIngreso.set(Calendar.MONTH, monthOfYear);
            calendarioIngreso.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();

        }
    };


    /**
     * Introduccion de la fecha
     */
    private void actualizarInput() {

        String formatoDeFecha = "dd/MM/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaIngreso.setText(sdf.format(calendarioIngreso.getTime()));
    }


    /**
     * Método de retorno a MainActivity al añadir ingreso
     */
    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(), MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}