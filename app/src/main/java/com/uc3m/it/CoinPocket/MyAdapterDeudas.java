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

public class MyAdapterDeudas extends ArrayAdapter<String> {

    ArrayList<String> fecha;
    ArrayList<String> nombre;
    ArrayList<String> concepto;
    ArrayList<String> cantidad;
    ArrayList<Integer> emoji;
    ArrayList<Integer> pagardeber;
    Context mContext;

    public MyAdapterDeudas(Context context, ArrayList<String> fecha, ArrayList<String> nombre, ArrayList<String> cantidad, ArrayList<String> concepto, ArrayList<Integer> emoji, ArrayList<Integer> pagardeber){
        super(context, R.layout.listview_item_deuda);
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.concepto = concepto;
        this.nombre = nombre;
        this.emoji = emoji;
        this.pagardeber = pagardeber;
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
            convertView = mInflater.inflate( R.layout.listview_item_deuda, parent, false);
            mViewHolder.mEmoji = (ImageView) convertView.findViewById( R.id.imageView_deuda);
            mViewHolder.mFecha = (TextView) convertView.findViewById( R.id.textView_fecha_deuda);
            mViewHolder.mConcepto = (TextView) convertView.findViewById( R.id.textView_concepto_deuda);
            mViewHolder.mNombre = (TextView) convertView.findViewById( R.id.textView_nombre_deuda);
            mViewHolder.mdeberPagar = (TextView) convertView.findViewById( R.id.textView_apagar_adeber );
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        mViewHolder.mEmoji.setImageResource( emoji.get( position ) );
        mViewHolder.mFecha.setText( fecha.get( position ) );
        mViewHolder.mNombre.setText( nombre.get( position ) );
        if(concepto.get( position )!=null){
            if(concepto.get( position ).length()>20){
                mViewHolder.mConcepto.setText( concepto.get( position ).substring( 0,20 ) + "..." );
            }else{
                mViewHolder.mConcepto.setText( concepto.get( position ) );
            }
        }
        if(pagardeber.get( position ) == 0){
            mViewHolder.mdeberPagar.setText( "A pagar "  + cantidad.get( position ) + "€");
        }
        if(pagardeber.get( position ) == 1){
            mViewHolder.mdeberPagar.setText( "A deber " + cantidad.get( position ) + "€" );
        }

        return convertView;
    }

    static class ViewHolder {
        ImageView mEmoji;
        TextView mFecha;
        TextView mConcepto;
        TextView mNombre;
        TextView mdeberPagar;
    }
}
