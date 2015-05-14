package com.example.bastian.nuevo2;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class ListarRutaActivity extends Activity {

    private ListView listViewRutas;
    private List<Ruta> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ruta);




        this.listViewRutas = (ListView) findViewById(R.id.listViewRutas);

        list = ruta();
        this.listViewRutas.setAdapter(new ListaRutaAdapter(this,list));

    }



    public List<Ruta> ruta()
    {
        List<Ruta> list = new ArrayList<>();

        for(int i = 1 ; i <10;i++)
        {
            Ruta ruta = new Ruta();
            ruta.setDatosPantalla(i*50,800,800);
            for(int j = 3 ; j <7 ; j++)
            {
                ruta.agregarPunto(i*i*i,j*j*j);
                ruta.agregarPunto(i*i*j,j*j*i);
            }
            list.add(ruta);
        }
        return list;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_listar_ruta, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onClickButtonIrRuta(View view){
        Comunicador.setObjeto(null);
        Intent i = new Intent(this, RutaActivity.class);
        startActivity(i);

    }


    public void onClickButtonCargarRuta(View view){
        int posicion = (Integer)view.getTag();
        Comunicador.setObjeto(list.get(posicion));
        Intent i = new Intent(this, RutaActivity.class);
        startActivity(i);
    }

}
