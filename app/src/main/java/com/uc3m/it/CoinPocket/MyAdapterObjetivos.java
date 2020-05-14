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

    ArrayList<String> fechaInic;
    ArrayList<String> fechaFin;
    ArrayList<String> cantidad;
    ArrayList<String> motivo;
    ArrayList<Integer> emoji;
    ArrayList<Integer> ahorrargastar;
    ArrayList<Double> balance;
    ArrayList<Integer> flagBalance;
    String fechaHoy = new SimpleDateFormat("dd/MM/yy", Locale.getDefault()).format(new Date());
    Context mContext;
    DecimalFormat formatter = new DecimalFormat("#,###.##");

    public MyAdapterObjetivos(Context context, ArrayList<String> fechaInic, ArrayList<String> fechaFin, ArrayList<String> cantidad, ArrayList<String> motivo, ArrayList<Integer> emoji, ArrayList<Integer> ahorrargastar, ArrayList<Double> balance, ArrayList<Integer> flagBalance){
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

    public int getCount(){
        return  fechaInic.size();
    }

    @SuppressLint("ResourceAsColor")
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        mViewHolder.mEmoji.setImageResource( emoji.get( position ) );
        mViewHolder.mFecha.setText( fechaInic.get( position ) + " - " + fechaFin.get( position ));
        mViewHolder.mMotivo.setText( motivo.get( position ) );
        if(motivo.get( position )!=null){
            if(motivo.get( position ).length()>20){
                mViewHolder.mMotivo.setText( motivo.get( position ).substring( 0,20 ) + "..." );
            }else{
                mViewHolder.mMotivo.setText( motivo.get( position ) );
            }
        }
        if(ahorrargastar.get( position ) == 0){
            mViewHolder.mahorrarGastarOrig.setText( "Conseguir ahorrar:"  + cantidad.get( position ) + "€");
        }
        if(ahorrargastar.get( position )==1){
            mViewHolder.mahorrarGastarOrig.setText( "No gastar más de:" + cantidad.get( position ) + "€" );
        }

        if(flagBalance.get( position )==1){
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    mViewHolder.layout.setBackgroundColor( 0xaaff4730 );
                    if(Double.parseDouble( cantidad.get( position ).trim() ) - balance.get( position )>0){
                        mViewHolder.mahorrarGastarBalance.setTextSize( 15 );
                        mViewHolder.mahorrarGastarBalance.setText( "Conseguido ahorrar " + formatter.format((Double.parseDouble( cantidad.get( position ).trim() ) - balance.get( position ))) + "€. No llegaste al objetivo." );
                    }else{
                        mViewHolder.mahorrarGastarBalance.setTextSize( 15 );
                        mViewHolder.mahorrarGastarBalance.setText( "Jornada de balance negativo " + formatter.format((Double.parseDouble( cantidad.get( position ).trim() ) - balance.get( position ))) + "€." );
                    }

                }else{
                    mViewHolder.mahorrarGastarBalance.setText( "Tienes que ingresar " + formatter.format(balance.get( position )) + "€." );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(flagBalance.get( position )==2){
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    mViewHolder.layout.setBackgroundColor( 0xaaaaff55 );
                    mViewHolder.mahorrarGastarBalance.setText( "Conseguido ahorrar " + formatter.format((balance.get( position ) + Double.parseDouble( cantidad.get( position ).trim() ))) + "€." );
                }else{
                    mViewHolder.mahorrarGastarBalance.setText( " Puedes gastar " + formatter.format(balance.get( position )) + "€." );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if(flagBalance.get( position )==3){
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    mViewHolder.layout.setBackgroundColor( 0xaaaaff55 );
                    mViewHolder.mahorrarGastarBalance.setText( "Solo gastaste " + formatter.format((Double.parseDouble( cantidad.get( position ).trim() ) - balance.get( position ) )) + "€." );
                }else{
                    mViewHolder.mahorrarGastarBalance.setText( " Puedes gastar " + formatter.format(balance.get( position )) + "€.");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        if(flagBalance.get( position )==4){
            try {
                if(CompararFechas( fechaFin.get( position ), fechaHoy ) == fechaFin.get( position )){
                    mViewHolder.layout.setBackgroundColor( 0xaaff4730 );
                    mViewHolder.mahorrarGastarBalance.setTextSize( 15 );
                    mViewHolder.mahorrarGastarBalance.setText( "Sobrepasaste el gasto máximo en " + formatter.format(balance.get( position )) + "€. Gastaste " + formatter.format((Double.parseDouble( cantidad.get( position ) ) + balance.get( position ))) + "€ esta jornada." );
                }else{
                    mViewHolder.mahorrarGastarBalance.setText( "Has sobrepasado en " + formatter.format(balance.get( position )) + "€ tu máximo gasto." );
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

        }
        return convertView;
    }

    static class ViewHolder {
        ImageView mEmoji;
        TextView mFecha;
        TextView mMotivo;
        TextView rootMotivo;
        TextView mahorrarGastarOrig;
        TextView rootGastarOrig;
        TextView mahorrarGastarBalance;
        TextView rootGastarBalance;
        LinearLayout layout;
    }

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
