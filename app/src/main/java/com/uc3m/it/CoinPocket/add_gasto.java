package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.uc3m.it.CoinPocket.MainActivity.filename;

public class add_gasto extends AppCompatActivity {

    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    //EditText id_gasto;
    EditText concepto;
    EditText cantidad;
    EditText etFecha;
    EditText localizacion;
    Button ButtonSaveGasto;
    Button buttonLocalizacion;
    Calendar calendario = Calendar.getInstance();






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gasto);

        concepto = (EditText) findViewById(R.id.id_concepto);
        cantidad = (EditText) findViewById(R.id.id_cantidad);
        localizacion = (EditText) findViewById(R.id.id_localizacion);
        //id_gasto = (EditText) findViewById(R.id.id_gasto);
        etFecha = findViewById(R.id.id_etFecha);
        ButtonSaveGasto = (Button) findViewById(R.id.SaveGasto);
        buttonLocalizacion = (Button) findViewById(R.id.id_button_localizacion);


        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(add_gasto.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        ConexionSQLiteHelper conn = new ConexionSQLiteHelper(this, "bd_gastos", null, 1);

        ButtonSaveGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarGasto();
            }
        });

    }
    public void add_localizacion(View view) {

        Intent intent = new Intent(this, MapsActivity.class);
        startActivityForResult(intent, SHOW_SUB_ACTIVITY_ONE);
    }

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

                    String latitude = Double.valueOf(lati).toString();
                    String longitude = Double.valueOf(longi).toString();

                    localizacion.setText(latitude+", "+longitude);
                }
                break;
            }

        }
    }


    private void registrarGasto(){
        ConexionSQLiteHelper newconn = new ConexionSQLiteHelper(this, "bd_gastos", null, 1);
        SQLiteDatabase db = newconn.getWritableDatabase();

        Integer id_gasto = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS,null);
        while (cursor.moveToNext()){
            id_gasto = id_gasto + 1;
        }


        ContentValues values = new ContentValues();
        values.put(utilidades.CAMPO_ID_GASTO, id_gasto.toString());
        values.put(utilidades.CAMPO_CONCEPTO_GASTO, concepto.getText().toString());
        values.put(utilidades.CAMPO_CANTIDAD_GASTO, cantidad.getText().toString());

        Long idResultante=db.insert(utilidades.TABLA_GASTOS,utilidades.CAMPO_CONCEPTO_GASTO,values);


        Toast.makeText(getApplicationContext(),"Id Registro: " + idResultante, Toast.LENGTH_SHORT).show();
        db.close();

        finish();
    }


/*        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }*/

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, monthOfYear);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFecha.setText(sdf.format(calendario.getTime()));
    }
}

//Fuente de la BD https://www.youtube.com/redirect?q=https%3A%2F%2Fgithub.com%2Fmitchtabian%2FSaveReadWriteDeleteSQLite&redir_token=AM04Oaop5J59AFPRyfVjP_A_3Et8MTU4NjQzMzUzM0AxNTg2MzQ3MTMz&event=video_description&v=aQAIMY-HzL8


