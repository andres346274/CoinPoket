package com.uc3m.it.CoinPocket.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;
/**
 *
 * Ejemplo sacado de: https://medium.com/mindorks/custom-array-adapters-made-easy-b6c4930560dd
 */
import com.uc3m.it.CoinPocket.response.Ratio;

import java.util.List;

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
