package com.example.bastian.nuevo2;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SensoresActivity extends Activity {


    private static final String NAMESPACE = "http://services.ws.rws/";
    private static String URL = "http://172.26.201.4:"+Comunicador.getPUERTO()+"/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
    private static String SERVER ="172.26.201.4";



    private ListView _listViewSensores;
    private SensoresAdapter _adapter;
    private List<String> _datosSensores;
    private SensorSurfaceView _surfaceViewSensor;
    private Bitmap _imagenRobot;
    boolean _iteracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);
        inicializaDatosSensores();
        this._listViewSensores = (ListView) findViewById(R.id.listsensores);

        this._adapter = new SensoresAdapter(this, _datosSensores);
        this._listViewSensores.setAdapter(_adapter);
        _surfaceViewSensor = (SensorSurfaceView) findViewById(R.id.surfaceViewSensor);
        try {
            SERVER = Comunicador.getIP_WEB_SERVICE();
            URL= "http://"+ SERVER +":"+Comunicador.getPUERTO()+"/WSR/Servicios";
        }catch (Exception e){}

        _imagenRobot = BitmapFactory.decodeResource(getResources(), R.drawable.robot_sensores);
        _surfaceViewSensor.set_imagenRobot(_imagenRobot);
        _surfaceViewSensor.set_sensores(_datosSensores);
        getSensores();
    }


    private void inicializaDatosSensores(){
        _datosSensores = new ArrayList<>();
        for(int i = 0 ; i<9;i++){
            _datosSensores.add(getString(R.string.sensor_no_encontrado));
        }
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_conectar) {
            return true;
        }
        */
        if (id == R.id.action_inicio) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_galeria) {
            Intent i = new Intent(this, GaleriaActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_rutas) {
            Intent i = new Intent(this, ListarRutaActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_movimiento) {
            Intent i = new Intent(this, movimiento.class);
            startActivity(i);
        }
        if (id == R.id.action_sensores) {
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

    public void getSensores()
    {
        _iteracion = true;
        //esta parte se agrego para poder pedir la imagen sin necesidad de conectar ya que toma la ip del server del edittext
        //EditText editTextServer = (EditText) findViewById(R.id.editTextIpServer);
        // String localserver = editTextServer.getText().toString();//192.168.56.1
        //localserver = "192.168.1.40";
        final String URL = "http://"+ SERVER +":"+Comunicador.getPUERTO()+"/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        //final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "mediaDatosSensores";//mediaDatosSensores
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta;
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);


                try {
                    while (_iteracion) {
                        Thread.sleep(100);
                        try {
                            transportSE.call(SOAP_ACTION,envelope);
                            SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                            respuesta = resultado.toString();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String [] senso = respuesta.split("-");
                                    pintar(senso);
                                }
                                catch (Exception e)
                                {
                                }


                                       }
                        });
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        nt.start();
    }



    public void  pintar(String [] sen)
    {
        List<String> lista= new ArrayList<>();
        for (String e:sen)
        {
            lista.add(e);

        }
        _adapter.set_sensores(lista);
        _surfaceViewSensor.set_sensores(lista);
    }

    /*
    Detiene la obtencion de los sensores cuando sale de la pantalla.
     */
    @Override
    public void onBackPressed()
    {
        //Toast.makeText(SensoresActivity.this,"Detenido", Toast.LENGTH_SHORT).show();
        _iteracion =false;
        SensoresActivity.super.onBackPressed();
    }


}
