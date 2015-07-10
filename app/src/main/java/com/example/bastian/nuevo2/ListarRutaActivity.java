package com.example.bastian.nuevo2;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;


public class ListarRutaActivity extends Activity {

    private ListView _listViewRutas;
    private List<Ruta> _lista;
    private String _respuestaWS;

    private Boolean _esperandoThread = true;



    private ListaRutaAdapter _adapterRuta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ruta);

        _respuestaWS = "error";


        this._listViewRutas = (ListView) findViewById(R.id.listViewRutas);

        _lista = cargarRutaInterna();



        _adapterRuta =new ListaRutaAdapter(this, _lista);
        this._listViewRutas.setAdapter(_adapterRuta);
        this._listViewRutas.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        _lista = cargarRutaInterna();
        _adapterRuta.setdatos(_lista);
    }

    //metodo para generar rutas prueba
    public List<Ruta> ruta()
    {
        List<Ruta> list = new ArrayList<>();

        for(int i = 1 ; i <8;i++)
        {
            Ruta ruta = new Ruta(00, 50, "test " + i);

            ruta.setDatosPantalla(i*5,800,800);

            for(int j = 10 ; j <17 ; j++)
            {
                ruta.agregarPunto(i*j+30,j*j*i +99);
            }
            list.add(ruta);
        }
        return list;

    }

    public List<Ruta> cargarRutaInterna(){
        return Comunicador.getBaseDatoRuta().getRutas();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int _id = item.getItemId();
        if (_id == R.id.action_inicio) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (_id == R.id.action_galeria) {
            Intent i = new Intent(this, GaleriaActivity.class);
            startActivity(i);
        }
        if (_id == R.id.action_rutas) {
            Intent i = new Intent(this, ListarRutaActivity.class);
            startActivity(i);
        }
        if (_id == R.id.action_movimiento) {
            Intent i = new Intent(this, movimiento.class);
            startActivity(i);
        }
        if (_id == R.id.action_sensores) {
            Intent i = new Intent(this, SensoresActivity.class);
            startActivity(i);
        }
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    public void onClickButtonIrRuta(View view){
        Comunicador.setObjeto(null);
        Intent i = new Intent(this, RutaActivity.class);
        startActivity(i);

    }



    public void deleteSelected(View view) {
        //Obtengo los elementos seleccionados de mi lista
        SparseBooleanArray _seleccionados = _listViewRutas.getCheckedItemPositions();

        if(_seleccionados==null || _seleccionados.size()==0){
            //Si no había elementos seleccionados...
            Toast.makeText(this, "No hay elementos seleccionados", Toast.LENGTH_SHORT).show();
        }else{
            int _resultado = 0;
            final int _size=_seleccionados.size();
            for (int i=0; i<_size; i++) {
                if (_seleccionados.valueAt(i)) {
                    _resultado = _seleccionados.keyAt(i);

                }
            }

            Comunicador.getBaseDatoRuta().eliminarRuta(_lista.get(_resultado).get_ID());
            _lista = cargarRutaInterna();
            _adapterRuta.setdatos(_lista);




        }
    }
    public void onClickCargaRuta(View view){
        SparseBooleanArray seleccionados = _listViewRutas.getCheckedItemPositions();
        if(seleccionados==null || seleccionados.size()==0) {
            //Si no había elementos seleccionados...
            Toast.makeText(this, "No hay elementos seleccionados", Toast.LENGTH_SHORT).show();
        }
        else{
            int resultado = 0;
            final int size=seleccionados.size();
            for (int i=0; i<size; i++) {
                if (seleccionados.valueAt(i)) {
                    resultado = seleccionados.keyAt(i);

                }
            }
            Comunicador.getBaseDatoRuta().cargarPuntosARuta(_lista.get(resultado));
            Comunicador.setObjeto(_lista.get(resultado));
            Intent i = new Intent(this, RutaActivity.class);
            startActivity(i);

        }

    }


}
