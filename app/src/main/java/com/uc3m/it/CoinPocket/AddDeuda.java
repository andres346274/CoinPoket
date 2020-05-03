package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

//https://codinginflow.com/tutorials/android/radio-buttons-radio-group
// codigo del los radio buttons https://stackoverflow.com/questions/6440259/how-to-get-the-selected-index-of-a-radiogroup-in-android

public class AddDeuda<checkedRadioButtonId> extends AppCompatActivity {

    private static final int SHOW_SUB_ACTIVITY_ONE = 1;

    //EditText id_gasto;
    EditText conceptoDeuda;
    EditText cantidadDeuda;
    EditText etFechaDeuda;
    EditText localizacionGasto;
    Button buttonSaveDeuda;
    Calendar calendarioDeuda = Calendar.getInstance();
    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_deuda);

        conceptoDeuda = (EditText) findViewById(R.id.id_concepto_deuda);
        cantidadDeuda = (EditText) findViewById(R.id.id_cantidad_deuda);
        //id_gasto = (EditText) findViewById(R.id.id_gasto);
        etFechaDeuda = findViewById(R.id.id_etFecha_deuda);
        buttonSaveDeuda = (Button) findViewById(R.id.id_save_deuda);
        radioGroup = (RadioGroup)findViewById(R.id.id_radio_group);

        etFechaDeuda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddDeuda.this, date, calendarioDeuda
                        .get(Calendar.YEAR), calendarioDeuda.get(Calendar.MONTH),
                        calendarioDeuda.get(Calendar.DAY_OF_MONTH)).show();
            }
        });



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
            calendarioDeuda.set(Calendar.YEAR, year);
            calendarioDeuda.set(Calendar.MONTH, monthOfYear);
            calendarioDeuda.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }

    };

    private void actualizarInput() {
        String formatoDeFecha = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);

        etFechaDeuda.setText(sdf.format(calendarioDeuda.getTime()));
    }

    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}




