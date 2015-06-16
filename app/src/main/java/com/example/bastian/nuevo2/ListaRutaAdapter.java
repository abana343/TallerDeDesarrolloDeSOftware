package com.example.bastian.nuevo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Javier Aros on 12-05-2015.
 * Adapta la rutaActivity para ser mostradas como una lista
 */
public class ListaRutaAdapter extends BaseAdapter {

    private Context context;
    private List<Ruta> rutas;
    public ListaRutaAdapter(Context context, List<Ruta> rutas) {
        this.context = context;
        this.rutas = rutas;
    }
    @Override
    public int getCount() {
        return this.rutas.size();
    }

    @Override
    public Object getItem(int position) {
        return this.rutas.get(position);
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
            rowView = inflater.inflate(R.layout.item_rutas, parent, false);
        }

        // Set data into the view.


        TextView nombre = (TextView) rowView.findViewById((R.id.textViewNombreRuta));
        TextView puntos = (TextView) rowView.findViewById((R.id.textViewPuntos));
        TextView distancia = (TextView) rowView.findViewById((R.id.textViewDistanciaTotal));

        Ruta ruta = rutas.get(position);


        //nombre rutaActivity
        nombre.setText(ruta.nombre);

        //puntos rutaActivity
        puntos.setText("Puntos: " + ruta.getPuntos().size());

        //distancia recorrida
        double distanciaRecorrida = ruta.distanciaTotal();
        distancia.setText("Distancia " + String.format("%.2f", distanciaRecorrida) + "cms");

        return rowView;
    }

    public void setdatos(List<Ruta> rutas){
        this.rutas = rutas;
        notifyDataSetChanged();
    }




}
