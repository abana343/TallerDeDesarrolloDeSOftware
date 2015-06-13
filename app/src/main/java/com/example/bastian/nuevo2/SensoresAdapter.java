package com.example.bastian.nuevo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Bastian on 08-06-2015.
 */
public class SensoresAdapter extends BaseAdapter
{

    private Context context;
    private List<String> sensores;
    public SensoresAdapter(Context context, List<String> sensores) {
        this.context = context;
        this.sensores = sensores;

    }
    @Override
    public int getCount() {
        return this.sensores.size();
    }

    @Override
    public Object getItem(int position) {
        return this.sensores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;

        if (convertView == null) {
            // Create a new view into the list.
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_sensor, parent, false);
        }


        TextView nombre = (TextView) rowView.findViewById(R.id.textNameSensor);
        TextView distancia = (TextView) rowView.findViewById(R.id.TextIntSensor);
       // ImageView imagenDistacia = (ImageView) rowView.findViewById(R.id.ImageDistanciaSensor);

        String sensor= this.sensores.get(position);
        nombre.setText("Sensor Número "+(position+1));
        distancia.setText("Distacia = "+sensor);
        return rowView;
    }

    public void setSensores(List<String> sensores) {
        this.sensores = sensores;
        notifyDataSetChanged();
    }
}
