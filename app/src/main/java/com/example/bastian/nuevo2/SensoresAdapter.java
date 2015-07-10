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
 * Muestra la lista de sensores
 */
public class SensoresAdapter extends BaseAdapter
{

    private Context _context;
    private List<String> _sensores;
    public SensoresAdapter(Context context, List<String> sensores) {
        this._context = context;
        this._sensores = sensores;

    }
    @Override
    public int getCount() {
        return this._sensores.size();
    }

    @Override
    public Object getItem(int position) {
        return this._sensores.get(position);
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
            LayoutInflater inflater = (LayoutInflater) _context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.item_sensor, parent, false);
        }


        TextView _nombre = (TextView) rowView.findViewById(R.id.textNameSensor);
        TextView _distancia = (TextView) rowView.findViewById(R.id.TextIntSensor);
       // ImageView imagenDistacia = (ImageView) rowView.findViewById(R.id.ImageDistanciaSensor);

        String _sensor= this._sensores.get(position);
        _nombre.setText("Sensor Número " + (position + 1));
        String _dato = "";
        if(_sensor.equals("Sensor no encontrado")){
            _distancia.setText("Sensor no encontrado");
        }
        else {


            float _valor = Float.parseFloat(_sensor);
            if (_valor > 2.5)
                _dato = "0";
            else if (_valor < 0.5)
                _dato = "libre";
            else {
                //valor = (float) 12.5 - (5 * valor);
                float _n = (float) Math.pow(_valor,(float)-0.911);
                _valor =  (float) 12.482 * _n;
                _dato = String.format("%.2f", _valor);
            }
            if (_dato.equals("libre")) {
                _distancia.setText("Sensor sin detectar objeto");
            } else {
                _distancia.setText("Distacia aproximada: " + _dato + "cms");
            }
        }

        return rowView;
    }

    public void set_sensores(List<String> _sensores) {
        this._sensores = _sensores;
        notifyDataSetChanged();
    }
}
