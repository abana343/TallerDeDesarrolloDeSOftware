package com.example.bastian.nuevo2;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;


public class ListarRutaActivity extends Activity {

    private ListView listViewRutas;
    private List<Ruta> list;
    private String respuestaWS;

    private Boolean esperandoThread = true;
    private Time tiempoEsperaWebService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ruta);

        respuestaWS = "error";


        this.listViewRutas = (ListView) findViewById(R.id.listViewRutas);

        list = ruta();




        this.listViewRutas.setAdapter(new ListaRutaAdapter(this,list));

        obtenerRutas();
        while(esperandoThread) {


        }
        esperandoThread=true;
        if(!respuestaWS.equals("error"))
        {
            list = crearRutas();

            this.listViewRutas.setAdapter(new ListaRutaAdapter(this,list));
        }


    }



    public List<Ruta> ruta()
    {
        List<Ruta> list = new ArrayList<>();

        for(int i = 1 ; i <8;i++)
        {
            Ruta ruta = new Ruta(00, 50, "test");

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
        respuestaWS = "error";
        cargarRuta(Integer.toString(list.get(posicion).getID()));
        while(esperandoThread){}
        esperandoThread=true;

            if(!respuestaWS.equals("error")) {
                if(list.get(posicion).getPuntos().size()==0) {
                    String puntosRuta[] = respuestaWS.split("-");
                    for (int j = 0; j < puntosRuta.length; j++) {
                        String punto[] = puntosRuta[j].split(",");
                        System.out.println(punto[0] + "  " + punto[1]);
                        list.get(posicion).agregarPunto(Float.parseFloat(punto[0]), Float.parseFloat(punto[1]));
                    }
                }
                Comunicador.setObjeto(list.get(posicion));
                Intent i = new Intent(this, RutaActivity.class);
                startActivity(i);
            }





    }


    public void obtenerRutas()
    {
        String server = Comunicador.getIpWebService();

        final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "obtenerRutas";
        final String SOAP_ACTION = Comunicador.NAMESPACE + METHOD_NAME;


        Thread nt = new Thread()
        {
            @Override
            public void run()
            {
                respuestaWS = "error";
                SoapObject request = new SoapObject(Comunicador.NAMESPACE,METHOD_NAME);
                //request.addProperty("hostnameIP","172.26.201.3");//el primer argumento es el string que identifica el web param
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);
                try {
                    transportSE.call(SOAP_ACTION,envelope);
                    SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                    respuestaWS = resultado.toString();
                    esperandoThread = false;

                } catch (IOException e) {
                    respuestaWS = "error";
                    esperandoThread = false;
                } catch (XmlPullParserException e) {
                    respuestaWS = "error";
                    esperandoThread = false;
                }

            }

        };

        nt.start();

    }

    public void cargarRuta(final String idRuta)
    {
        String server = Comunicador.getIpWebService();

        final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "pasosRuta";
        final String SOAP_ACTION = Comunicador.NAMESPACE + METHOD_NAME;


        Thread nt = new Thread()
        {
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(Comunicador.NAMESPACE,METHOD_NAME);
                request.addProperty("idRuta",idRuta);
                //request.addProperty("hostnameIP","172.26.201.3");//el primer argumento es el string que identifica el web param
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);
                try {
                    transportSE.call(SOAP_ACTION,envelope);
                    SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                    respuestaWS = resultado.toString();
                    System.out.println("dentro   " + respuestaWS);
                    esperandoThread = false;


                } catch (IOException e) {
                    respuestaWS = "error: ";
                    esperandoThread = false;
                } catch (XmlPullParserException e) {
                    respuestaWS = "error";
                    esperandoThread = false;
                }

            }

        };

        nt.start();

    }




    private List<Ruta> crearRutas()
    {

        List<Ruta> rutas = new ArrayList<>();
        //generamos las rutas sin puntos con los parametros ID, escala y nombre
        String rutasCadena[] = respuestaWS.split("<->");
        for(int i = 0 ; i< rutasCadena.length;i++){
            String rutaDatos[] = rutasCadena[i].split(",");
            Ruta ruta = new Ruta(Integer.parseInt(rutaDatos[0]),Integer.parseInt(rutaDatos[1]),rutaDatos[2]);
            rutas.add(ruta);
        }
        if(rutas.size() >0)
        {
            respuestaWS = "error";
        }
        return rutas;
    }

    public void onClickButtonActualizarRutas(View view){
        obtenerRutas();
        while(esperandoThread) {}
        esperandoThread=true;
        if(!respuestaWS.equals("error"))
        {
            list = crearRutas();

            this.listViewRutas.setAdapter(new ListaRutaAdapter(this,list));
        }

    }

}
