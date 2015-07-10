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

    private Context _context;
    private List<Ruta> _rutas;
    public ListaRutaAdapter(Context context, List<Ruta> _rutas) {
        this._context = context;
        this._rutas = _rutas;
    }
    @Override
    public int getCount() {
        return this._rutas.size();
    }

    @Override
    public Object getItem(int position) {
        return this._rutas.get(position);
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
            rowView = inflater.inflate(R.layout.item_rutas, parent, false);
        }

        // Set data into the view.


        TextView _nombre = (TextView) rowView.findViewById((R.id.textViewNombreRuta));
        TextView _puntos = (TextView) rowView.findViewById((R.id.textViewPuntos));
        TextView _distancia = (TextView) rowView.findViewById((R.id.textViewDistanciaTotal));

        Ruta _ruta = _rutas.get(position);


        //_nombre rutaActivity
        _nombre.setText(_ruta._nombre);

        //puntos rutaActivity
        _puntos.setText("Puntos: " + _ruta.get_puntos().size());

        //distancia recorrida
        double distanciaRecorrida = _ruta.distanciaTotal();
        _distancia.setText("Distancia " + String.format("%.2f", distanciaRecorrida) + "cms");

        return rowView;
    }

    public void setdatos(List<Ruta> rutas){
        this._rutas = rutas;
        notifyDataSetChanged();
    }




}
