package com.example.bastian.nuevo2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Javier Aros on 23-04-2015.
 */
public class RutaAdapter extends BaseAdapter {

    private Context context;
    private List<String> linea;
    public RutaAdapter(Context context, List<String> linea) {
        this.context = context;
        this.linea = linea;
    }

    @Override
    public int getCount() {
        return this.linea.size();
    }

    @Override
    public Object getItem(int position) {
        return this.linea.get(position);
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
            rowView = inflater.inflate(R.layout.linea, parent, false);
        }

        // Set data into the view.

        TextView textView = (TextView) rowView.findViewById(R.id.label);
        textView.setText(linea.get(position));

        TextView identificador = (TextView) rowView.findViewById((R.id.identificadorNumerico));
        identificador.setText(Integer.toString(position+1) + "-" + Integer.toString(position+2));
       //imagenRuta.setImageResource(R.drawable.number1);
        //imagenRuta.setImageBitmap(ruta.bitmap);
        ImageButton botonCerrar = (ImageButton) rowView.findViewById(R.id.buttonEliminar);
        botonCerrar.setTag(position);

        return rowView;
    }
}
