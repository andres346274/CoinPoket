package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class modificarEliminar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_eliminar);
    }
    /**
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.modificar_gasto_concreto:
                String memName_upd = et.getText().toString();
                dbcon.actualizarDatos(member_id, memName_upd);
                this.returnHome();
                break;

            case R.id.eliminar_gasto_concreto:
                dbcon.deleteData(member_id);
                this.returnHome();
                break;
        }
    }*/


    public void returnHome() {

        Intent home_intent = new Intent(getApplicationContext(),
                Modificar_gastos_hoy.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(home_intent);
    }
}