package com.julioflores.prueba1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Main2Activity extends ArrayAdapter<Almacen> {

    private Context contexto;
    private ArrayList<Almacen> listitems;

    public Main2Activity(Activity context, ArrayList<Almacen> almacen) {
        super(context, 0, almacen);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_main2, parent, false);
        }
        Almacen almacen = getItem(position);
        TextView t1 = (TextView) listItemView.findViewById(R.id.lote2);
        t1.setText(String.valueOf(almacen.getLotemp()));
        TextView t2 = (TextView) listItemView.findViewById(R.id.ubicacion2);
        t2.setText(String.valueOf(almacen.getRack()+"-"+almacen.getFila()+"-"+almacen.getColumna()));
        TextView t3 = (TextView) listItemView.findViewById(R.id.cantidad2);
        t3.setText(String.valueOf(almacen.getCantidad()));
        TextView t4 = (TextView) listItemView.findViewById(R.id.materiapri2);
        t4.setText(almacen.getMateriaprima());
        return listItemView;
    }
}