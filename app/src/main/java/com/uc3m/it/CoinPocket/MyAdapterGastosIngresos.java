package com.uc3m.it.CoinPocket;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * https://github.com/codingdemos/CustomList/tree/master/app/src/main/res
 */

public class MyAdapterGastosIngresos extends ArrayAdapter<String> {

    ArrayList<String> fecha;
    ArrayList<String> cantidad;
    ArrayList<String> concepto;
    ArrayList<String> localizacion;
    ArrayList<Integer> emoji;
    ArrayList<Integer> gastoIngreso;
    Context mContext;
    DecimalFormat formatter = new DecimalFormat("#,###.##");


    public MyAdapterGastosIngresos(Context context, ArrayList<String> fecha, ArrayList<String> cantidad, ArrayList<String> concepto, ArrayList<String> localizacion, ArrayList<Integer> emoji, ArrayList<Integer> gastoIngreso){
        super(context, R.layout.listview_item_ingreso_gasto);
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.localizacion = localizacion;
        this.emoji = emoji;
        this.gastoIngreso = gastoIngreso;
        this.mContext = context;
    }

    public int getCount(){
        return  fecha.size();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = new ViewHolder();
        if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate( R.layout.listview_item_ingreso_gasto, parent, false);
            mViewHolder.mEmoji = (ImageView) convertView.findViewById( R.id.imageView);
            mViewHolder.mFecha = (TextView) convertView.findViewById( R.id.textView_fecha_ingreso_gasto);
            mViewHolder.mCantidad = (TextView) convertView.findViewById( R.id.textView_catidad_ingreso_gasto);
            mViewHolder.mConcepto = (TextView) convertView.findViewById( R.id.textView_concepto_ingreso_gasto);
            mViewHolder.mLocalizacion = (TextView) convertView.findViewById( R.id.textView_localización_ingreso_gasto);
            mViewHolder.mgastoIngreso = (TextView) convertView.findViewById( R.id.textView_ingreso_gasto );
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mEmoji.setImageResource( emoji.get( position ));
        mViewHolder.mFecha.setText( fecha.get( position ) );
        mViewHolder.mCantidad.setText( cantidad.get( position ) + "€" );
        mViewHolder.mConcepto.setText( concepto.get( position ) );
        if(localizacion.get( position )!=null){
            if(localizacion.get( position ).length()>20){
                mViewHolder.mLocalizacion.setText( localizacion.get( position ).substring( 0,20 ) + "..." );
            }else{
                mViewHolder.mLocalizacion.setText( localizacion.get( position ));
            }
        }
        if(gastoIngreso.get( position ) == 0){
            mViewHolder.mgastoIngreso.setText( "Concepto ingreso:" );
        }
        if(gastoIngreso.get( position )==1){
            mViewHolder.mgastoIngreso.setText( "Concepto gasto:" );
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView mEmoji;
        TextView mFecha;
        TextView mCantidad;
        TextView mConcepto;
        TextView mLocalizacion;
        TextView mgastoIngreso;
    }
}
