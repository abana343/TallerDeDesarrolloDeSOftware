package com.example.bastian.nuevo2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
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


public class RutaActivity2 extends Activity{
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
            surfaceView.setRutaActivity(this);

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
                        surfaceView.setAccionActual("AgregarNodo");
                        botonEliminarNodo.setChecked(false);
                        botonEliminarNodo.setSelected(false);
                        botonEditarNodo.setChecked(false);
                        botonEditarNodo.setSelected(false);

                    } else {
                        surfaceView.setAccionActual("ninguna");
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
                        surfaceView.setAccionActual("EditarNodo");
                        botonEliminarNodo.setChecked(false);
                        botonEliminarNodo.setSelected(false);
                        botonAgregarNodo.setChecked(false);
                        botonAgregarNodo.setSelected(false);
                    } else {
                        surfaceView.setAccionActual("ninguna");
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
                        surfaceView.setAccionActual("EliminarNodo");
                        botonEditarNodo.setChecked(false);
                        botonEditarNodo.setSelected(false);
                        botonAgregarNodo.setChecked(false);
                        botonAgregarNodo.setSelected(false);
                    } else {
                        surfaceView.setAccionActual("ninguna");
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

                if (surfaceView.ruta.getPuntos().size() == 0) {
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


                            String listaPuntos = "" + surfaceView.ruta.getNombre() + "-";
                            for (int i = 0; i < surfaceView.ruta.getPuntos().size(); i++) {
                                listaPuntos += "(" + surfaceView.ruta.getPuntos().get(i).x + "," + surfaceView.ruta.getPuntos().get(i).y + ")-";
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

        editTextEscala.setText(""+surfaceView.ruta.getEscala());

    }




    public void onClickButtonGuardarRuta2(View view){
        if(!rutaEjecutada){
            if(surfaceView.ruta.getPuntos().size()==0){
                Toast.makeText(this.context, "Agrega puntos  a rutaActivity", Toast.LENGTH_SHORT).show();
            }
            else {
                // custom dialog
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_ruta);
                dialog.setTitle("Nombre de la ruta");

                // set the custom dialog components - text, image and button
                final EditText text = (EditText) dialog.findViewById(R.id.editTextNombreRuta);
                text.setText(surfaceView.ruta.getNombre());

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
                            Toast.makeText(context, "Ingresa nombre de ruta", Toast.LENGTH_SHORT).show();
                        } else {

                            /*
                            String listaPuntos = "" + text.getText() + "-";
                            for (int i = 0; i < surfaceView.ruta.getPuntos().size(); i++) {
                                listaPuntos += "(" + surfaceView.ruta.getPuntos().get(i).x + "," + surfaceView.ruta.getPuntos().get(i).y + ")-";
                            }
                            EditText escalaText = (EditText) findViewById(R.id.escala);
                            int escala = Integer.parseInt(escalaText.getText().toString());
                            listaPuntos += Integer.toString(escala);

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
                            surfaceView.ruta.setEscala(escala);
                            surfaceView.ruta.setNombre(text.getText().toString());
                            if (surfaceView.ruta.getID() == -1) {
                                Comunicador.getBaseDatoRuta().InsertarRuta(surfaceView.ruta);
                                Toast.makeText(context, "Ruta guardada", Toast.LENGTH_SHORT).show();
                            } else {
                                Comunicador.getBaseDatoRuta().editarRuta(surfaceView.ruta);
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
        String server = Comunicador.getIpWebService();

        final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
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

        String server = Comunicador.getIpWebService();

        final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
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
        surfaceView.setAccionActual("ninguna");
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
        String server = Comunicador.getIpWebService();

        final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
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