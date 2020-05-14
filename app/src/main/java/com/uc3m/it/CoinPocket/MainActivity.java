package com.uc3m.it.CoinPocket;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.uc3m.it.CoinPocket.utilidades.utilidades;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //Inicializacion de las variables de la activity
    public static final String filename = "myfile.txt";

    ArrayList<GastosIngresosBD> listaBalanceObjetivo;
    ArrayList<ObjetivosBD> listaObjetivo;
    Double totalBalanceObjetivo;
    ConexionSQLiteHelper connMovimientos;
    ConexionSQLiteHelperObjetivos connObjetivos;
    TextView balanceMain;
    String fechaHoy;
    //Formato de redondeo de cifras
    DecimalFormat formatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //De BD  y componentes utilizados en la activity
        balanceMain = (TextView) findViewById( R.id.id_balance_main );
        connMovimientos = new ConexionSQLiteHelper(getApplicationContext(),
                "bd_gastos_ingresos", null, 1);
        connObjetivos = new ConexionSQLiteHelperObjetivos(getApplicationContext(),
                "bd_objetivos", null, 1);
        //Asignación de fecha hoy
        fechaHoy = new SimpleDateFormat(
                "dd/MM/yy", Locale.getDefault()).format(new Date());
        //Asignación de formato de decimales
        formatter = new DecimalFormat("#,###.##");


        try {
            balanceMostrado();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * Declaración de métodos onClick asignados a cada button de la activity
     */

    public void add_gasto_main(View view) {
        Intent intent = new Intent(this, AddGasto.class);
        Button okButton = (Button) findViewById(R.id.button_add_gasto);

        Bundle b = new Bundle();

        intent.putExtras(b);

        startActivity(intent);
    }

    public void add_ingreso_main(View view) {
        Intent intent = new Intent(this, AddIngreso.class);
        Button okButton = (Button) findViewById(R.id.button_add_ingreso);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void consultar_main(View view) {
        Intent intent = new Intent(this, ListaGastosIngresos.class);
        Button okButton = (Button) findViewById(R.id.button_consultar);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void objetivos_main(View view) {

        Intent intent = new Intent(this, Objetivos.class);
        Button okButton = (Button) findViewById(R.id.button_objetivos);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    public void deudas_main(View view) {

        Intent intent = new Intent(this, Deudas.class);
        Button okButton = (Button) findViewById(R.id.button_deudas);

        Bundle b = new Bundle();
        intent.putExtras(b);

        startActivity(intent);
    }

    /**
     * Método de cálculo de la situación de balance actual mostrada
     *      balance = variable donde se almacena el balance calculado
     *      listaObjetivo = variable lista de objetivos obtenidos de la BD
     *      objtetivos = variable ObjetivosBD para iir introduciendo en listaObjetivo
     *      objetivopróximo = variable que almacena la posición del objetivo cuyo balance se muestra
     *      i = contador de posición dentro de listaObjetivos
     * @throws ParseException
     */
    public void balanceMostrado() throws ParseException {

        SQLiteDatabase db = connObjetivos.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + utilidades.TABLA_OBJETIVOS_BD,null);

        ObjetivosBD objetivos = null;
        listaObjetivo = new ArrayList<ObjetivosBD>();
        Double balance = 0.0;

        int objetivoproximo = 0;
        int i=0;


        //Creación de lista de objetivos en listaObjetivo
        while (cursor.moveToNext()){
            if ((CompararFechas( cursor.getString(3), fechaHoy ).equals( cursor.getString(3) ) || cursor.getString(3).equals( fechaHoy )) && (CompararFechas( cursor.getString(4), fechaHoy ).equals( fechaHoy ) || cursor.getString(4).equals( fechaHoy ))){
                objetivos = new ObjetivosBD();
                objetivos.setGastoahorro( cursor.getInt(0));
                objetivos.setId(cursor.getInt(1));
                objetivos.setCantidad(cursor.getString(2));
                objetivos.setFechainicio(cursor.getString(3));
                objetivos.setFechafin(cursor.getString(4));
                objetivos.setMotivo( cursor.getString( 5 ) );

                listaObjetivo.add(objetivos);
            }
        }

        if(listaObjetivo.size()!=0){

            //Bucle while para buscar la fecha final de un objetivo más cercana a la actual del usuario
            while (i<listaObjetivo.size()){
                for(i=0; i<listaObjetivo.size(); i++){
                    if(CompararFechas( listaObjetivo.get( objetivoproximo ).getFechafin(), listaObjetivo.get( i ).getFechafin() ) == listaObjetivo.get( i ).getFechafin() && !listaObjetivo.get( objetivoproximo ).getFechafin().equals( listaObjetivo.get( i ).getFechafin() )){
                        objetivoproximo = i;
                        break;
                    }
                }
            }

            //Cálculo de balance y print en pantalla
            if(listaObjetivo.get(objetivoproximo).getGastoahorro()==0){

                if(Double.parseDouble( listaObjetivo.get( objetivoproximo ).getCantidad().trim() )>balanceObjetivo( listaObjetivo.get( objetivoproximo ).getFechainicio(), listaObjetivo.get( objetivoproximo ).getFechafin(), true)){
                    balance = Double.parseDouble( listaObjetivo.get( objetivoproximo ).getCantidad().trim() )-
                            balanceObjetivo( listaObjetivo.get( objetivoproximo ).getFechainicio(),
                                    listaObjetivo.get( objetivoproximo ).getFechafin(), true);
                    balanceMain.setText( "Balance de objetivo actual: 'Tienes que ingresar " +
                            formatter.format(balance)  + " €'");
                }else {
                    balance = balanceObjetivo( listaObjetivo.get( objetivoproximo ).getFechainicio(), listaObjetivo.get( objetivoproximo ).getFechafin(), true)-
                            Double.parseDouble( listaObjetivo.get( objetivoproximo ).getCantidad().trim() );
                    balanceMain.setText( "Balance de objetivo actual: 'Puedes gastar " +
                            formatter.format(balance) + " €'");
                }
            } else {

                if(Double.parseDouble( listaObjetivo.get( objetivoproximo ).getCantidad().trim() )>balanceObjetivo( listaObjetivo.get( objetivoproximo ).getFechainicio(), listaObjetivo.get( objetivoproximo ).getFechafin(), false)){
                    balance = Double.parseDouble( listaObjetivo.get( objetivoproximo ).getCantidad().trim() )-
                            balanceObjetivo( listaObjetivo.get( objetivoproximo ).getFechainicio(), listaObjetivo.get( objetivoproximo ).getFechafin(), false);
                    balanceMain.setText( "Balance de objetivo actual: 'Puedes gastar " +
                            formatter.format(balance) + " €'");
                }else {
                    balance = balanceObjetivo( listaObjetivo.get( objetivoproximo ).getFechainicio(), listaObjetivo.get( objetivoproximo ).getFechafin(), false ) -
                            Double.parseDouble( listaObjetivo.get( objetivoproximo ).getCantidad().trim() );
                    balanceMain.setText( "Balance de objetivo actual: 'Has sobrepasado tu gasto en " +
                            formatter.format(balance) + " €'");
                }
            }
        }else {
            balanceMain.setText( "No hay objetivos que cumplir hoy" );
        }
    }

    /**
     * Método para cálculo de balance de cierto objetivo
     *      listaBalanceObjetivo = variable lista de objetivos
     *      totalBalanceObjetivo = variable que almacena el cálculo del balance
     * @param fechaInic = Fecha de incio de objetivo
     * @param fechaFin = Fecha de fin de objetivo
     * @param ahorrarGasto = flag booleano que indica si se trata de un objetivo de ahorro (true)
     *                    o de máximo gasto (false)
     * @return
     * @throws ParseException
     */
    public Double balanceObjetivo(String fechaInic, String fechaFin, Boolean ahorrarGasto) throws ParseException {
        SQLiteDatabase dbMovimientos = connMovimientos.getReadableDatabase();
        GastosIngresosBD objetivosBalance = null;
        listaBalanceObjetivo = new ArrayList<GastosIngresosBD>();
        Cursor cursorMovimientos = dbMovimientos.rawQuery("SELECT * FROM " + utilidades.TABLA_GASTOS_INGRESOS_BD,null);
        totalBalanceObjetivo = 0.0;

        //Creamos la lista de objetivos en listaBalanceObjetivo
        while (cursorMovimientos.moveToNext()){
            if ((CompararFechas( cursorMovimientos.getString(4), fechaInic ).equals( fechaInic ) || cursorMovimientos.getString(4).equals( fechaFin )) && (CompararFechas( cursorMovimientos.getString(4), fechaFin ).equals( cursorMovimientos.getString(4) ) || cursorMovimientos.getString(4).equals( fechaFin ))){

                objetivosBalance = new GastosIngresosBD();
                objetivosBalance.setGastoingreso( cursorMovimientos.getInt(0));
                objetivosBalance.setId(cursorMovimientos.getInt(1));
                objetivosBalance.setConcepto(cursorMovimientos.getString(2));
                objetivosBalance.setCantidad(cursorMovimientos.getString(3));
                objetivosBalance.setFecha(cursorMovimientos.getString(4));

                listaBalanceObjetivo.add(objetivosBalance);
            }
        }
        //Calculamos el balance para los datos en listaBalanceObjetivo
        if(ahorrarGasto){
            if(listaBalanceObjetivo != null){
                for(int i=0; i<listaBalanceObjetivo.size();i++){
                    if(listaBalanceObjetivo.get( i ).getCantidad().length()!=0){
                        if(listaBalanceObjetivo.get(i).getGastoingreso()==0){
                            totalBalanceObjetivo = totalBalanceObjetivo + Double.parseDouble( listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }
                        else {
                            totalBalanceObjetivo = totalBalanceObjetivo - Double.parseDouble( listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }

                    }
                }
            }
        }else {
            if(listaBalanceObjetivo != null){
                for(int i=0; i<listaBalanceObjetivo.size();i++){
                    //Miro a ver si es gasto
                    if(listaBalanceObjetivo.get( i ).getGastoingreso() == 1){
                        if(listaBalanceObjetivo.get( i ).getCantidad().length()!=0){
                            totalBalanceObjetivo = totalBalanceObjetivo + Double.parseDouble( listaBalanceObjetivo.get( i ).getCantidad().trim() );
                        }
                    }

                }
            }
        }
        return  totalBalanceObjetivo;
    }


    /**
     * Método de comparación de fechas
     * @param x fecha 1
     * @param y fecha 2
     * @return
     * @throws ParseException
     */
    public String CompararFechas(String x, String y) throws ParseException {

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
        Date strDate = sdf.parse(x);

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        Date str1Date = sdf1.parse( y );

        if (str1Date.after(strDate)) {
            return  x;

        }else {
            return y;
        }
    }
}
