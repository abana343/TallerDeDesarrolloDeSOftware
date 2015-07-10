package com.example.bastian.nuevo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Javier Aros on 23-04-2015.
 * Adapta los punts de una rutaActivity para ser mostrados como una lista
 */
public class RutaAdapter extends BaseAdapter {

    private Context _context;
    private List<String> _linea;
    public RutaAdapter(Context context, List<String> linea) {
        this._context = context;
        this._linea = linea;
    }

    @Override
    public int getCount() {
        return this._linea.size();
    }

    @Override
    public Object getItem(int position) {
        return this._linea.get(position);
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
            rowView = inflater.inflate(R.layout.linea, parent, false);
        }

        // Set data into the view.

        TextView _textView = (TextView) rowView.findViewById(R.id.label);
        _textView.setText(_linea.get(position));

        TextView _identificador = (TextView) rowView.findViewById((R.id.identificadorNumerico));
        _identificador.setText(Integer.toString(position + 1) + "-" + Integer.toString(position + 2));
        ImageButton _botonCerrar = (ImageButton) rowView.findViewById(R.id.buttonEliminar);
        _botonCerrar.setTag(position);
        ImageButton _botonEditar = (ImageButton) rowView.findViewById(R.id.buttonEditar);
        _botonEditar.setTag(position);
        return rowView;
    }


}
