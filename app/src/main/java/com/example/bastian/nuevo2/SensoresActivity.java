package com.example.bastian.nuevo2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Base64;
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
import java.util.HashMap;
import java.util.List;


public class SensoresActivity extends Activity {


    private static final String NAMESPACE = "http://services.ws.rws/";
    private static String URL = "http://172.26.201.4:8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
    private static String server="172.26.201.4";

    private ListView sensores;
    private SensoresAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensores);
        this.sensores= (ListView) findViewById(R.id.listsensores);

        this.adapter = new SensoresAdapter(this,new ArrayList<String>() );
        this.sensores.setAdapter(adapter);

        try {
            server = Comunicador.getIpWebService();
            URL= "http://"+server+":8080/WSR/Servicios";
        }catch (Exception e){}

        getSensores();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sensores, menu);
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

    public void getSensores()
    {
        //esta parte se agrego para poder pedir la imagen sin necesidad de conectar ya que toma la ip del server del edittext
        //EditText editTextServer = (EditText) findViewById(R.id.editTextIpServer);
        // String localserver = editTextServer.getText().toString();//192.168.56.1
        //localserver = "192.168.1.40";
        final String URL = "http://"+server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        //final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "mediaDatoSensores";//mediaDatosSensores
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
                    while (Comunicador.getCamara()) {
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
                                    String [] senso =respuesta.split("-");
                                    pintar(senso);
                                }
                                catch (Exception e)
                                {
                                }


                                //Toast.makeText(MainActivity.this,String.valueOf(contador),Toast.LENGTH_SHORT).show();
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
        List<String> lista= new ArrayList<String>();
        for (String e:sen)
        {
            lista.add(e);
        }
        adapter.setSensores(lista);
    }
}
