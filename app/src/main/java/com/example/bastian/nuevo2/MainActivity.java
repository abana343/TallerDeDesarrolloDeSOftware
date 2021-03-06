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

import com.example.bastian.nuevo2.DB.BaseDatoRuta;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class MainActivity extends Activity {

    public String server;
    private static final String NAMESPACE = "http://services.ws.rws/";

    public float init_x;
    ViewFlipper vf;

    BaseDatoRuta baseDato;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        vf = (ViewFlipper) findViewById(R.id.ViewFlipper);

        vf.setOnTouchListener(new ListenerTouchViewFlipper());

        EditText editTextServer = (EditText) findViewById(R.id.IpWebservice);
        editTextServer.setText(R.string.ipwebservice);

        EditText editTextrobot = (EditText) findViewById(R.id.IpRobotino);
        editTextrobot.setText(R.string.iprobotino);

        EditText editTextPuertoWS = (EditText) findViewById(R.id.PuertoWS);
        editTextPuertoWS.setText(Comunicador.getPUERTO());

        baseDato = new BaseDatoRuta(this);
        Comunicador.setBASE_DATO_RUTA(baseDato);
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

    public void conectarServidor(View view)
    {
        EditText editTextPuertoWS = (EditText) findViewById(R.id.PuertoWS);
        Comunicador.setPUERTO(editTextPuertoWS.getText().toString());
        EditText editTextServer = (EditText) findViewById(R.id.IpWebservice);
        server = editTextServer.getText().toString();//192.168.56.1
        TextView tv = (TextView) findViewById(R.id.RB);
        tv.setText(server);
        final String URL = "http://" + server+":"+Comunicador.getPUERTO()+"/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "conectar";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta="Error de conexión";
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
                    if(respuesta.equals("Conectado"))
                        Comunicador.setESTADO_SERVICIO(true);
                    else
                        Comunicador.setESTADO_SERVICIO(false);
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
                Comunicador.setIP_ROBOTINO(hostname);
                Comunicador.setIP_WEB_SERVICE(server);
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

    public void onClickButtonIrSensores(View view){
        Intent i = new Intent(this, SensoresActivity.class);
        startActivity(i);

    }

    public void onClickButtonIrGaleria(View view){
        Intent i = new Intent(this, GaleriaActivity.class);
        startActivity(i);

    }

    public void onClickButtonPruebaRutaSurface(View view){
        Intent i = new Intent(this, RutaActivity.class);
        startActivity(i);
    }

    public void clasico(View view)
    {
        Comunicador.setMOVIMIENTO(1);
        this.onClickButtonIrMovimiento(view);

    }

    public void tactil(View view)
    {
        Comunicador.setMOVIMIENTO(2);
        this.onClickButtonIrMovimiento(view);
    }
}
