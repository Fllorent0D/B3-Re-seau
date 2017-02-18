package com.example.bastin.application_vendeurs_4.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.bastin.application_vendeurs_4.R;

import java.util.ArrayList;

import dismap.models.TypeAppareil;

/**
 * Created by bastin on 13-11-16.
 */

public class TypesAppareilAdapter extends BaseAdapter {

    protected Context context;
    protected ArrayList<TypeAppareil> dataList;

    public TypesAppareilAdapter(Context context, ArrayList<TypeAppareil> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Object getItem(int position) {
        return dataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null)
            convertView = LayoutInflater.from(context).inflate(R.layout.item_type_appareil, parent, false);

        TypeAppareil tp = dataList.get(position);

        TextView serialNumber = (TextView) convertView.findViewById(R.id.serialNumber);
        TextView label = (TextView) convertView.findViewById(R.id.label);
        TextView sellingPrice = (TextView) convertView.findViewById(R.id.sellingPrice);
        TextView brand = (TextView) convertView.findViewById(R.id.brand);
        TextView available = (TextView) convertView.findViewById(R.id.available);

        serialNumber.setText("Numéro de série: " + tp.getIdTypeAppareil());
        label.setText((position+1) + ". " + tp.getLibelle());
        sellingPrice.setText("Prix de vente: " + String.valueOf(tp.getPrixVenteBase()) + " €");
        brand.setText("Marque: " + tp.getMarque());
        available.setText("Disponibilite: " + String.valueOf(tp.getAvailable()));

        return convertView;
    }
}
