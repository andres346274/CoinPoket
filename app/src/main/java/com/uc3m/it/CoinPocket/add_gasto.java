package com.uc3m.it.CoinPocket;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.uc3m.it.CoinPocket.MainActivity.filename;

public class add_gasto extends AppCompatActivity {

    EditText concepto;
    EditText cantidad;
    EditText etFecha;
    Button ButtonSaveGasto;
    Calendar calendario = Calendar.getInstance();
    DatabaseHelper mDatabaseHelper;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_gasto);

        concepto = (EditText) findViewById(R.id.id_concepto);
        cantidad = (EditText) findViewById(R.id.id_cantidad);
        etFecha = findViewById(R.id.id_etFecha);
        ButtonSaveGasto = (Button) findViewById(R.id.SaveGasto);
        mDatabaseHelper = new DatabaseHelper(this);




        etFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(add_gasto.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        ButtonSaveGasto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEntry = concepto.getText().toString();
                if (concepto.length() != 0) {
                    save_gasto(newEntry);
                    concepto.setText("");
                } else {
                    toastMessage("You must put something in the field");
                }
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

    public void save_gasto(String newEntry) {
        boolean insertData = mDatabaseHelper.addData(newEntry);

        if(insertData) {
            toastMessage("Data Successfully Inserted!");
        } else {
            toastMessage("Something went wrong");
        }
    }

    private void toastMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}

//Fuente de la BD https://www.youtube.com/redirect?q=https%3A%2F%2Fgithub.com%2Fmitchtabian%2FSaveReadWriteDeleteSQLite&redir_token=AM04Oaop5J59AFPRyfVjP_A_3Et8MTU4NjQzMzUzM0AxNTg2MzQ3MTMz&event=video_description&v=aQAIMY-HzL8


