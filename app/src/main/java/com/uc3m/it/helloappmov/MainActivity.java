package com.uc3m.it.helloappmov;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //  Metodo que procesa la pulsacion (onClick) del boton
    //  se indica en el atributo "android:onClick" del elemento Button definido en XML
    public void add_gasto_main(View view) {
        // Creamos el Intent que va a lanzar la segunda activity (SecondActivity)
        Intent intent = new Intent(this, add_gasto.class);
        // Obtenemos referencias a los elementos del interfaz grafico
        //EditText nameText = (EditText) findViewById(R.id.edit_message);
        Button okButton = (Button) findViewById(R.id.button_add_gasto);

        // Creamos la informacion a pasar entre actividades
        Bundle b = new Bundle();
        //b.putString("NAME", nameText.getText().toString());

        // Asociamos esta informacion al intent
        intent.putExtras(b);

        // Iniciamos la nueva actividad
        startActivity(intent);
    }

    public void add_ingreso_main(View view) {
        Intent intent = new Intent(this, add_ingreso.class);
        Button okButton = (Button) findViewById(R.id.button_add_ingreso);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void consultar_main(View view) {
        Intent intent = new Intent(this, consultar.class);
        Button okButton = (Button) findViewById(R.id.button_consultar);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void objetivos_main(View view) {
        Intent intent = new Intent(this, objetivos.class);
        Button okButton = (Button) findViewById(R.id.button_objetivos);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void deudas_main(View view) {
        Intent intent = new Intent(this, deudas.class);
        Button okButton = (Button) findViewById(R.id.button_deudas);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Se recrea el menu que aparece en ActionBar de la actividad.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Se obtiene el identificador del item que ha sido seleccionado
        int id = item.getItemId();

        // Se comprueba cuál de las dos posibles opciones es, settings o about
        if (id == R.id.action_settings) {
            System.out.println("APPMOV: Settings action...");
            return true;
        }

        if (id == R.id.action_about) {
            System.out.println("APPMOV: About action...");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
