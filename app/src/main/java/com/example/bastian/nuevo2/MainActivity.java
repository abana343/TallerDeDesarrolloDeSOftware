package com.example.bastian.nuevo2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends Activity {

    public String server;
    private static final String NAMESPACE = "http://services.ws.robotino/";

    public float init_x;
    ViewFlipper vf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vf = (ViewFlipper) findViewById(R.id.ViewFlipper);

        vf.setOnTouchListener(new ListenerTouchViewFlipper());

        EditText editTextServer = (EditText) findViewById(R.id.IpWebservice);
        editTextServer.setText(R.string.ipwebservice);
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
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void conectarServidor(View view)
    {
        EditText editTextServer = (EditText) findViewById(R.id.IpWebservice);
        server = editTextServer.getText().toString();//192.168.56.1
        TextView tv = (TextView) findViewById(R.id.RB);
        tv.setText(server);
        final String URL = "http://" + server+":8080/RBTNWS/RobotinoServices";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "conectar";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta;
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
                EditText editText = (EditText) findViewById(R.id.IpRobotino);
                String hostname = editText.getText().toString();
                request.addProperty("hostnameIP",hostname);
                //request.addProperty("hostnameIP","172.26.201.3");//el primer argumento es el string que identifica el web param
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);
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
                        Toast.makeText(MainActivity.this,respuesta,Toast.LENGTH_LONG).show();
                        TextView textView = (TextView) findViewById(R.id.estado_coneccion);
                        textView.setText(respuesta);
                    }
                });
            }
        };

        nt.start();
    }

    //clase de view flipper
    private class ListenerTouchViewFlipper implements View.OnTouchListener
    {


        @Override
        public boolean onTouch(View v, MotionEvent event)
        {
            switch (event.getAction())
            {
                case MotionEvent.ACTION_DOWN: //Cuando el usuario toca la pantalla por primera vez
                    init_x=event.getX();
                    return true;
                case MotionEvent.ACTION_UP: //Cuando el usuario deja de presionar
                    float distance =init_x-event.getX();

                    if(distance>0)
                    {
                        vf.showPrevious();
                    }

                    if(distance<0)
                    {
                        vf.showNext();
                    }

                default:
                    break;
            }
            return false;

        }
    }

    public void onClickButtonListaRutas(View view){
        Intent i = new Intent(this, ListarRutaActivity.class);
        startActivity(i);

    }

    public void onClickButtonIrMovimiento(View view){
        Intent i = new Intent(this, movimiento.class);
        startActivity(i);

    }
}
