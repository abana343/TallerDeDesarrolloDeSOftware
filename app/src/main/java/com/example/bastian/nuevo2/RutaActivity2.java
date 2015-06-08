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
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class RutaActivity2 extends Activity{
    RutaSurfaceView surfaceView;

    ToggleButton botonAgregarNodo;
    ToggleButton botonEditarNodo;
    ToggleButton botonEliminarNodo;


    final Context context = this;
    private String respuestaServidor = "";
    private Boolean esperandoThread = true;
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

        botonAgregarNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                button.setSelected(!button.isSelected());
                if(button.isSelected()){
                    surfaceView.setAccionActual("AgregarNodo");
                    botonEliminarNodo.setChecked(false);
                    botonEliminarNodo.setSelected(false);
                    botonEditarNodo.setChecked(false);
                    botonEditarNodo.setSelected(false);

                } else{
                    surfaceView.setAccionActual("ninguna");
                    botonEliminarNodo.setChecked(false);
                    botonEditarNodo.setChecked(false);
                }
                surfaceView.reinicioTouch();
            }
        });



        botonEditarNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                button.setSelected(!button.isSelected());

                if(button.isSelected()){
                    surfaceView.setAccionActual("EditarNodo");
                    botonEliminarNodo.setChecked(false);
                    botonEliminarNodo.setSelected(false);
                    botonAgregarNodo.setChecked(false);
                    botonAgregarNodo.setSelected(false);
                } else{
                    surfaceView.setAccionActual("ninguna");
                    botonEliminarNodo.setChecked(false);
                    botonAgregarNodo.setChecked(false);
                }
                surfaceView.reinicioTouch();
            }
        });

        botonEliminarNodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                button.setSelected(!button.isSelected());

                if(button.isSelected()){
                    surfaceView.setAccionActual("EliminarNodo");
                    botonEditarNodo.setChecked(false);
                    botonEditarNodo.setSelected(false);
                    botonAgregarNodo.setChecked(false);
                    botonAgregarNodo.setSelected(false);
                } else{
                    surfaceView.setAccionActual("ninguna");
                    botonEditarNodo.setChecked(false);
                    botonAgregarNodo.setChecked(false);
                }
                surfaceView.reinicioTouch();
            }
        });
    }




    public void onClickButtonGuardarRuta2(View view){

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
                        Comunicador.getBaseDatoRuta().InsertarRuta(surfaceView.ruta);
                        Toast.makeText(context, "Ruta guardada", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();

                    }
                }

            });
            dialog.show();
        }

    }




    public void GuardarRuta(final String nombre)
    {
        String server = Comunicador.getIpWebService();

        final String URL = "http://" + server+":8080/WSR/Servicios";//no sirve localhost si no se usa el emulador propio de androidstudio
        final String METHOD_NAME = "crearRuta";
        final String SOAP_ACTION = Comunicador.NAMESPACE + METHOD_NAME;



        Thread nt = new Thread()
        {
            @Override
            public void run()
            {
                SoapObject request = new SoapObject(Comunicador.NAMESPACE,METHOD_NAME);

                request.addProperty("formatoRuta",nombre);
                //request.addProperty("hostnameIP","172.26.201.3");//el primer argumento es el string que identifica el web param
                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                HttpTransportSE transportSE = new HttpTransportSE(URL);
                try {
                    transportSE.call(SOAP_ACTION,envelope);
                    respuestaServidor = "Ruta Guardada";
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
            }

        };

        nt.start();
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
}