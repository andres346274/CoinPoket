package com.uc3m.it.CoinPocket;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * https://github.com/codingdemos/CustomList/tree/master/app/src/main/res
 */

public class MyAdapterObjetivos extends ArrayAdapter<String> {

    //Asignación de campos que tendrá mi Adaptador
    Context mContext;
    ArrayList<String> fechaInic;
    ArrayList<String> fechaFin;
    ArrayList<String> cantidad;
    ArrayList<String> motivo;
    ArrayList<Integer> emoji;
    ArrayList<Integer> ahorrargastar;
    ArrayList<Double> balance;
    ArrayList<Integer> flagBalance;

    //Variable de la fecha actual
    String fechaHoy = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
    //Variable de formato de decimales
    DecimalFormat formatter = new DecimalFormat("#,###.##");

    //Creación el constructor
    public MyAdapterObjetivos(Context context, ArrayList<String> fechaInic, ArrayList<String> fechaFin,
                              ArrayList<String> cantidad, ArrayList<String> motivo, ArrayList<Integer> emoji,
                              ArrayList<Integer> ahorrargastar, ArrayList<Double> balance,
                              ArrayList<Integer> flagBalance){
        super(context, R.layout.listview_item_objetivo);
        this.fechaInic = fechaInic;
        this.fechaFin = fechaFin;
        this.cantidad = cantidad;
        this.motivo = motivo;
        this.emoji = emoji;
        this.ahorrargastar = ahorrargastar;
        this.balance = balance;
        this.flagBalance = flagBalance;
        this.mContext = context;
    }

    //Método auxiliar para obtener el tamaño de nuestra lista a mostrar
    public int getCount(){
        return  fechaInic.size();
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //Inicialización del holder
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            //Asignación de los componentes que usamos en la activity
            convertView = mInflater.inflate( R.layout.listview_item_objetivo, parent, false);
            mViewHolder.mEmoji = (ImageView) convertView.findViewById( R.id.imageView_objetivo);
            mViewHolder.mFecha = (TextView) convertView.findViewById( R.id.textView_fecha_objetivo);
            mViewHolder.mMotivo = (TextView) convertView.findViewById( R.id.textView_motivo_objetivo);
            mViewHolder.mahorrarGastarOrig = (TextView) convertView.findViewById( R.id.textView_ahorro_gasto_original);
            mViewHolder.mahorrarGastarBalance = (TextView) convertView.findViewById( R.id.textView_ahorro_gasto_balance );
            mViewHolder.layout = (LinearLayout) convertView.findViewById( R.id.layout_item_objetivo );
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        //Asignación del valor correspondiente de los arrays traidos en el constructor a cada valor del holder
        mViewHolder.mEmoji.setImageResource( emoji.get( position ) );
        mViewHolder.mFecha.setText( fechaInic.get( position ) + " - " + fechaFin.get( position ));
        mViewHolder.mMotivo.setText( motivo.get( position ) );
        if(motivo.get( position )!=null){//Recortar motivo al mostrarlo en la lista si es muy largo
            if(motivo.get( position ).length()>20){
                mViewHolder.mMotivo.setText( motivo.get( position ).substring( 0,20 ) + "..." );
            }else{
                mViewHolder.mMotivo.setText( motivo.get( position ) );
            }
        }
        if(ahorrargastar.get( position ) == 0){ //Caso objetivo ahorro
            mViewHolder.mahorrarGastarOrig.setText( "Conseguir ahorrar:"  + cantidad.get( position ) + "€");
        }
        if(ahorrargastar.get( position )==1){//Caso objetivo gasto
            mViewHolder.mahorrarGastarOrig.setText( "No gastar más de:" + cantidad.get( position ) + "€" );
        }

        //El flagBalance nos indica en qué situación estamos con respecto al objetivo en cuestión
        if(flagBalance.get( position )==1){ // Se trata de un objetivo de ahorro que no hemos cumplido
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    //Caso de que la fecha fin del objetivo de haya sobrepasado
                    mViewHolder.layout.setBackgroundColor( 0xaaff4730 );
                    if(Double.parseDouble( cantidad.get( position ).trim() ) - balance.get( position )>0){
                        //Caso de no haber llegado al ahorro deseado pero haber ahorrado algo
                        mViewHolder.mahorrarGastarBalance.setTextSize( 15 );
                        mViewHolder.mahorrarGastarBalance.setText( "Conseguido ahorrar " + formatter.
                                format((Double.parseDouble( cantidad.get( position ).trim() ) -
                                        balance.get( position ))) + "€. No llegaste al objetivo." );
                    }else{
                        //Caso haber tenido balance negativo
                        mViewHolder.mahorrarGastarBalance.setTextSize( 15 );
                        mViewHolder.mahorrarGastarBalance.setText( "Jornada de balance negativo " +
                                formatter.format((Double.parseDouble( cantidad.get( position ).trim() ) -
                                        balance.get( position ))) + "€." );
                    }

                }else{//Caso de que el objetivo aún siga vigente
                    mViewHolder.mahorrarGastarBalance.setText( "Tienes que ingresar " + formatter.
                            format(balance.get( position )) + "€." );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(flagBalance.get( position )==2){//Se trata de un objetivo de ahorro que sí hemos cumplido
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    //Caso de que la fecha fin del objetivo de haya sobrepasado
                    mViewHolder.layout.setBackgroundColor( 0xaaaaff55 );
                    mViewHolder.mahorrarGastarBalance.setText( "Conseguido ahorrar " + formatter.format((balance.get( position ) + Double.parseDouble( cantidad.get( position ).trim() ))) + "€." );
                }else{
                    //Caso de que el objetivo aún siga vigente
                    mViewHolder.mahorrarGastarBalance.setText( " Puedes gastar " + formatter.format(balance.get( position )) + "€." );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(flagBalance.get( position )==3){ //Se trata de un objetivo de máximo gasto que sí hemos cumplido
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    //Caso de que la fecha fin del objetivo de haya sobrepasado
                    mViewHolder.layout.setBackgroundColor( 0xaaaaff55 );
                    mViewHolder.mahorrarGastarBalance.setText( "Solo gastaste " + formatter.format((
                            Double.parseDouble( cantidad.get( position ).trim() ) -
                                    balance.get( position ) )) + "€." );
                }else{
                    //Caso de que el objetivo aún siga vigente
                    mViewHolder.mahorrarGastarBalance.setText( " Puedes gastar " + formatter.
                            format(balance.get( position )) + "€.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(flagBalance.get( position )==4){//Se trata de un objetivo de máximo gasto que no hemos cumplido
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    //Caso de que la fecha fin del objetivo de haya sobrepasado
                    mViewHolder.layout.setBackgroundColor( 0xaaff4730 );
                    mViewHolder.mahorrarGastarBalance.setTextSize( 15 );
                    mViewHolder.mahorrarGastarBalance.setText( "Sobrepasaste el gasto máximo en "
                            + formatter.format(balance.get( position )) + "€. Gastaste " +
                            formatter.format((Double.parseDouble( cantidad.get( position ) )
                                    + balance.get( position ))) + "€ esta jornada." );
                }else{
                    //Caso de que el objetivo aún siga vigente
                    mViewHolder.mahorrarGastarBalance.setTextSize( 15 );
                    mViewHolder.mahorrarGastarBalance.setText( "Has sobrepasado en " +
                            formatter.format(balance.get( position )) + "€ tu máximo gasto." );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return convertView;
    }

    /**
     * Clase ViewHolder para almaacenar los datos a asignar en la lista
     */
    static class ViewHolder {
        ImageView mEmoji;
        TextView mFecha;
        TextView mMotivo;
        TextView mahorrarGastarOrig;
        TextView mahorrarGastarBalance;
        LinearLayout layout;
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
        /**
         int year = strDate.getYear(); // this is deprecated
         int month = strDate.getMonth(); // this is deprecated
         int day = strDate.getDay(); // this is deprecated

         Calendar primeraFecha = Calendar.getInstance();
         primeraFecha.set(day, month, year);*/

        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yy");
        Date str1Date = sdf1.parse( y );
        /**int year_1 = str1Date.getYear();
         int month_1 = str1Date.getMonth();
         int day_1 = str1Date.getDay();

         Calendar segundaFecha = Calendar.getInstance();
         segundaFecha.set( day_1, month_1, year_1 );*/

        if (str1Date.after(strDate)) {
            return  x;

        }else {
            return y;
        }
    }
}
