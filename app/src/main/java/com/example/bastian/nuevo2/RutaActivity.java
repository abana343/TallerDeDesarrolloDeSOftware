package com.example.bastian.nuevo2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;


import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class RutaActivity extends Activity{
    RutaSurfaceView surfaceView;

    ToggleButton botonAgregarNodo;
    ToggleButton botonEditarNodo;
    ToggleButton botonEliminarNodo;
    ToggleButton botonEjecutar;
    EditText editTextEscala;


    final Context context = this;
    private String respuestaServidor = "";
    private String ejecucionRuta= "";
    private boolean esperandoThread = true;
    private boolean rutaEjecutada = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta2);


        try {
            surfaceView = (RutaSurfaceView) findViewById(R.id.rutaSurfaceView);
            surfaceView.set_rutaActivity(this);

        }
        catch (Exception e){}

        botonAgregarNodo = (ToggleButton) findViewById(R.id.imageButtonAgregarNodo);
        botonEditarNodo = (ToggleButton) findViewById(R.id.imageButtonEditarNodo);
        botonEliminarNodo = (ToggleButton) findViewById(R.id.imageButtonEliminarNodo);
        botonEjecutar = (ToggleButton) findViewById(R.id.buttonRun);
        editTextEscala = (EditText) findViewById(R.id.escala);

        botonAgregarNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                if(!rutaEjecutada) {
                    button.setSelected(!button.isSelected());
                    if (button.isSelected()) {
                        surfaceView.set_accionActual("AgregarNodo");
                        botonEliminarNodo.setChecked(false);
                        botonEliminarNodo.setSelected(false);
                        botonEditarNodo.setChecked(false);
                        botonEditarNodo.setSelected(false);

                    } else {
                        surfaceView.set_accionActual("ninguna");
                        botonEliminarNodo.setChecked(false);
                        botonEditarNodo.setChecked(false);
                    }
                    surfaceView.reinicioTouch();
                }
            }
        });





        botonEditarNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                if(!rutaEjecutada) {
                    button.setSelected(!button.isSelected());

                    if (button.isSelected()) {
                        surfaceView.set_accionActual("EditarNodo");
                        botonEliminarNodo.setChecked(false);
                        botonEliminarNodo.setSelected(false);
                        botonAgregarNodo.setChecked(false);
                        botonAgregarNodo.setSelected(false);
                    } else {
                        surfaceView.set_accionActual("ninguna");
                        botonEliminarNodo.setChecked(false);
                        botonAgregarNodo.setChecked(false);
                    }
                    surfaceView.reinicioTouch();
                }
            }
        });

        botonEliminarNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                if(!rutaEjecutada) {


                    button.setSelected(!button.isSelected());

                    if (button.isSelected()) {
                        surfaceView.set_accionActual("EliminarNodo");
                        botonEditarNodo.setChecked(false);
                        botonEditarNodo.setSelected(false);
                        botonAgregarNodo.setChecked(false);
                        botonAgregarNodo.setSelected(false);
                    } else {
                        surfaceView.set_accionActual("ninguna");
                        botonEditarNodo.setChecked(false);
                        botonAgregarNodo.setChecked(false);
                    }
                    surfaceView.reinicioTouch();
                }
            }
        });


        botonEjecutar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {

                if (surfaceView._ruta.get_puntos().size() == 0) {
                    Toast.makeText(context, "Agrega puntos a la ruta", Toast.LENGTH_SHORT).show();
                } else {
                    if(Comunicador.isESTADO_SERVICIO()){
                        esperandoThread = true;
                        preguntaRobotEjecutando();
                        while (esperandoThread) {
                        }
                        if (respuestaServidor == "si"){
                            rutaEjecutada = true;
                        }
                        else
                            rutaEjecutada = false;

                        button.setSelected(!button.isSelected());

                        if (button.isSelected() && !rutaEjecutada) {


                            String listaPuntos = "" + surfaceView._ruta.get_nombre() + "-";
                            for (int i = 0; i < surfaceView._ruta.get_puntos().size(); i++) {
                                listaPuntos += "(" + surfaceView._ruta.get_puntos().get(i).x + "," + surfaceView._ruta.get_puntos().get(i).y + ")-";
                            }
                            EditText escalaText = (EditText) findViewById(R.id.escala);
                            int escala = Integer.parseInt(escalaText.getText().toString()) * 10;
                            listaPuntos += Integer.toString(escala);

                            esperandoThread = true;
                            ejecutaRuta(listaPuntos);
                            bloquearRutaEnEjecucion();



                            ToggleButton toggleButton = (ToggleButton) findViewById(R.id.buttonRun);
                            toggleButton.setChecked(true);
                        }
                        else{
                            cancelarEjecucion();
                            ToggleButton toggleButton = (ToggleButton) findViewById(R.id.buttonRun);
                            toggleButton.setChecked(false);

                        }
                    }
                    else{
                        Toast.makeText(context, "Comprueba la coneccion", Toast.LENGTH_SHORT).show();
                        ToggleButton toggleButton = (ToggleButton) findViewById(R.id.buttonRun);
                        toggleButton.setChecked(!toggleButton.isChecked());
                    }

                }
            }
        });

        editTextEscala.setText(""+surfaceView._ruta.get_escala());

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




    public void onClickButtonGuardarRuta2(View view){
        if(!rutaEjecutada){
            if(surfaceView._ruta.get_puntos().size()==0){
                Toast.makeText(this.context, "Agrega puntos  a rutaActivity", Toast.LENGTH_SHORT).show();
            }
            else {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_ruta);
                dialog.setTitle("Nombre de la ruta");

                // set the custom dialog components - text, image and button
                final EditText text = (EditText) dialog.findViewById(R.id.editTextNombreRuta);
                text.setText(surfaceView._ruta.get_nombre());

                Button dialogButtonCancelar = (Button) dialog.findViewById(R.id.dialogButtonCancelar);
                // if button is clicked, close the custom dialog
                dialogButtonCancelar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Button dialogButtonGuardarRuta = (Button) dialog.findViewById(R.id.dialogButtonGuardar);
                dialogButtonGuardarRuta.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (text.getText().toString().length() == 0) {
                            Toast.makeText(context, "Ingresa nommbre de ruta", Toast.LENGTH_SHORT).show();
                        } else {

                            /*
                            String listaPuntos = "" + text.getText() + "-";
                            for (int i = 0; i < surfaceView._ruta.get_puntos().size(); i++) {
                                listaPuntos += "(" + surfaceView._ruta.get_puntos().get(i).x + "," + surfaceView._ruta.get_puntos().get(i).y + ")-";
                            }
                            EditText escalaText = (EditText) findViewById(R.id._escala);
                            int _escala = Integer.parseInt(escalaText.getText().toString());
                            listaPuntos += Integer.toString(_escala);

                            //System.out.println(listaPuntos);
                            //Toast.makeText(context, "Guardado", Toast.LENGTH_SHORT).show();
                            //dialog.dismiss();
                            GuardarRuta(listaPuntos);

                            while(esperandoThread)
                            {
                            }
                            Toast.makeText(context, respuestaServidor, Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                            */                    //DAtos para guardar en WS
                            EditText escalaText = (EditText) findViewById(R.id.escala);
                            int escala = Integer.parseInt(escalaText.getText().toString());
                            surfaceView._ruta.set_escala(escala);
                            surfaceView._ruta.set_nombre(text.getText().toString());
                            if (surfaceView._ruta.get_ID() == -1) {
                                Comunicador.getBASE_DATO_RUTA().InsertarRuta(surfaceView._ruta);
                                Toast.makeText(context, "Ruta guardada", Toast.LENGTH_SHORT).show();
                            } else {
                                Comunicador.getBASE_DATO_RUTA().editarRuta(surfaceView._ruta);
                                Toast.makeText(context, "Ruta editada", Toast.LENGTH_SHORT).show();

                            }

                            dialog.dismiss();

                        }
                    }

                });
                dialog.show();
            }
        }

    }








    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle("Salir")
                    .setMessage("Estás seguro?")
                    .setNegativeButton(android.R.string.cancel, null)//sin listener
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {//un listener que al pulsar, cierre la aplicacion
                        @Override
                        public void onClick(DialogInterface dialog, int which){
                            //Salir
                            finish();
                        }
                    })
                    .show();

            // Si el listener devuelve true, significa que el evento esta procesado, y nadie debe hacer nada mas
            return true;
        }
//para las demas cosas, se reenvia el evento al listener habitual
        return super.onKeyDown(keyCode, event);
    }



    public void ejecutaRuta(final String nombre)
    {
        String server = Comunicador.getIP_WEB_SERVICE();

        final String URL = "http://" + server+":"+Comunicador.getPUERTO()+"/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "ejecutarRuta";
        final String SOAP_ACTION = Comunicador.NAMESPACE + METHOD_NAME;



        Thread nt = new Thread()
        {
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(Comunicador.NAMESPACE,METHOD_NAME);

                request.addProperty("ruta",nombre);
                //request.addProperty("hostnameIP","172.26.201.3");//el primer argumento es el string que identifica el web param
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);
                try {
                    transportSE.call(SOAP_ACTION,envelope);
                    SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                    ejecucionRuta = resultado.toString();
                    respuestaServidor = "Ruta terminada";

                    esperandoThread = false;


                } catch (IOException e) {
                    respuestaServidor = "Error conexion";
                    esperandoThread = false;
                    e.printStackTrace();

                } catch (XmlPullParserException e) {
                    respuestaServidor = "Error conexion";
                    esperandoThread = false;
                    e.printStackTrace();
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(ejecucionRuta.equals("ruta ejecutada")) {
                            terminarEjecucion();
                            botonEjecutar.setChecked(false);
                        }
                    }
                });
            }


        };
        nt.start();

    }


    private void cancelarEjecucion(){

        String server = Comunicador.getIP_WEB_SERVICE();

        final String URL = "http://" + server+":"+Comunicador.getPUERTO()+"/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "abortarEjecucionRuta";
        final String SOAP_ACTION = Comunicador.NAMESPACE + METHOD_NAME;

        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(Comunicador.NAMESPACE,METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);
                try {
                    transportSE.call(SOAP_ACTION,envelope);
                    SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                    respuesta = resultado.toString();
                    terminarEjecucion();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                }
                esperandoThread = false;

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


    private void bloquearRutaEnEjecucion(){
        botonEditarNodo.setChecked(false);
        botonEditarNodo.setSelected(false);
        botonAgregarNodo.setChecked(false);
        botonAgregarNodo.setSelected(false);
        botonEliminarNodo.setChecked(false);
        botonEliminarNodo.setSelected(false);
        surfaceView.set_accionActual("ninguna");
        surfaceView.reinicioTouch();
        rutaEjecutada = true;

        botonAgregarNodo.setClickable(false);
        botonEditarNodo.setClickable(false);
        botonEliminarNodo.setClickable(false);
    }


    private void terminarEjecucion(){
        botonAgregarNodo.setClickable(true);
        botonEditarNodo.setClickable(true);
        botonEliminarNodo.setClickable(true);
        rutaEjecutada = false;
    }


    private void preguntaRobotEjecutando(){
        String server = Comunicador.getIP_WEB_SERVICE();

        final String URL = "http://" + server+":"+Comunicador.getPUERTO()+"/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "ejecutandoRuta";
        final String SOAP_ACTION = Comunicador.NAMESPACE + METHOD_NAME;

        Thread nt = new Thread()
        {
            String respuesta = "Coneccion Fallida";
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(Comunicador.NAMESPACE,METHOD_NAME);
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);
                try {
                    transportSE.call(SOAP_ACTION,envelope);
                    SoapPrimitive resultado = (SoapPrimitive) envelope.getResponse();
                    respuesta = resultado.toString();
                    respuestaServidor = resultado.toString();
                } catch (IOException e) {
                    e.printStackTrace();
                    respuestaServidor = "no";
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                    respuestaServidor = "no";
                }
                esperandoThread = false;

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