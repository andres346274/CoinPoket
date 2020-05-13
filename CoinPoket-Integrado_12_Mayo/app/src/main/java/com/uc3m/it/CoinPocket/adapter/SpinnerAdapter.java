package com.uc3m.it.CoinPocket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uc3m.it.CoinPocket.response.Ratio;

import java.util.List;

   /*
   Se utiliza para el uso del spinner en la selección de la divisa.
   Código basado en: https://stackoverflow.com/questions/1625249/android-how-to-bind-spinner-to-custom-object-list
    */

public class SpinnerAdapter extends ArrayAdapter<Ratio> {

    private Context context;
    private List<Ratio> ratios;
    private int layout;

    public SpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Ratio> objects) {
        super(context, resource, objects);
        this.ratios = objects;
        this.context = context;
        this.layout = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        Ratio ratio = ratios.get(position);
        label.setText(ratio.getName());
        return label;
    }

  //getDropDownvView: Obtiene una vista que muestra en el menú desplegable los datos.
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(ratios.get(position).getName());

        return label;
    }

    public List<Ratio> getCurrentList() {
        return ratios;
    }

}
