package com.example.bastian.nuevo2;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class movimiento extends Activity {


    private static final String NAMESPACE = "http://services.ws.robotino/";
    private static String URL = "http://192.168.1.129:8080/RBTNWS/RobotinoServices";//no sirve localhost si no se usa el emulador propio de androidstudio
    private static String server="172.26.201.1";

    private MySurfaceView analog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //ocultamos el titulo
       // requestWindowFeature(Window.FEATURE_NO_TITLE);
        //ocultamos la barra de notificaciones
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //se define que en ves de usar una vista de Layout se usa la vista entregada por MySurfaceView
        setContentView(R.layout.activity_movimiento);

        try {
            analog = (MySurfaceView) findViewById(R.id.analogo);
            analog.setMovimiento(this);

        }
        catch (Exception e){}

        try {
            EditText editTextServer = (EditText) findViewById(R.id.IpWebservice);
            server = editTextServer.getText().toString();//192.168.56.1
            URL= "http://"+server+":8080/RBTNWS/RobotinoServices";
        }catch (Exception e){}

        Log.e("server=",""+server);
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_movimiento, menu);
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
    */

    public void parar(View view)
    {
        final String METHOD_NAME = "parar";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                      //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void adelante(View view)
    {
        final String METHOD_NAME = "adelante";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void atras(View view)
    {
        final String METHOD_NAME = "atras";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void derecha(View view)
    {
        final String METHOD_NAME = "derecha";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void izquierda(View view)
    {
        final String METHOD_NAME = "izquierda";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void dNE(View view)
    {
        final String METHOD_NAME = "dNE";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void dSO(View view)
    {
        final String METHOD_NAME = "dSO";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void dSE(View view)
    {
        final String METHOD_NAME = "dSE";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void dNO(View view)
    {
        final String METHOD_NAME = "dNO";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void gizquierda(View view)
    {
        final String METHOD_NAME = "gizquierda";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }

    public void gderecha(View view)
    {
        final String METHOD_NAME = "gderecha";
        final String SOAP_ACTION = NAMESPACE + METHOD_NAME;
        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(NAMESPACE,METHOD_NAME);
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
                        //  Toast.makeText(movimiento.this,respuesta,Toast.LENGTH_LONG).show();
                    }
                });
            }
        };

        nt.start();
    }
}

