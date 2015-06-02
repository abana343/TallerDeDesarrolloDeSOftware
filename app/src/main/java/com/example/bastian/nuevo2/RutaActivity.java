package com.example.bastian.nuevo2;

import android.app.Activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import android.os.Bundle;
import android.view.Display;


import android.view.MotionEvent;
import android.view.View;


import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


public class RutaActivity extends Activity implements View.OnTouchListener {
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;


   // private ListView listView;

    //
    private Ruta ruta;
    private int editarPunto;



    //
    final Context context = this;
    //
    private String respuestaServidor = "";
    private Boolean esperandoThread = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        imageView = (ImageView) this.findViewById(R.id.imageView);
        //Carga rutaActivity
        ruta = (Ruta)Comunicador.getObjeto();
        if (ruta == null)
        {
            //Si no existe rutaActivity guardada crea una nueva
            ruta = new Ruta();
        }
        else{
            //inicialisa los campos de rutaActivity
            Point ultimoPunto = ruta.getPuntos().get(ruta.getPuntos().size()-1);
            downx = ultimoPunto.x;
            downy = ultimoPunto.y;
            upx = ultimoPunto.x;
            upy = ultimoPunto.y;
        }

        inicializarImageView();
        imageView.setOnTouchListener(this);
        //this.listView = (ListView) findViewById(R.id.listView);





        //this.listView.setAdapter(new RutaAdapter(this,rutaActivity.getLista()));


        editarPunto =  -1;
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();

        if(editarPunto != -1)
        {
            Point puntoEditado = ruta.getPuntos().get(editarPunto);
            inicializarImageView();
            switch (action){
                case MotionEvent.ACTION_DOWN:
                    puntoEditado.x = (int)event.getX();
                    puntoEditado.y = (int)event.getY();
                    ruta.getPuntos().set(editarPunto,puntoEditado);
                    break;
                case MotionEvent.ACTION_MOVE:
                    puntoEditado.x = (int)event.getX();
                    puntoEditado.y = (int)event.getY();
                    ruta.getPuntos().set(editarPunto,puntoEditado);
                    break;
                case MotionEvent.ACTION_UP:
                    puntoEditado.x = (int)event.getX();
                    puntoEditado.y = (int)event.getY();
                    ruta.getPuntos().set(editarPunto,puntoEditado);
                    this.editarPunto = -1;
                    dibujarRuta();
                    break;
            }
            ruta.actualizarLista();
            //listView.setAdapter(new RutaAdapter(this, rutaActivity.getLista()));

        }

        else
        {
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                    inicializarImageView();
                    if(ruta.getPuntos().size()==0)
                    {
                        downx = event.getX();
                        downy = event.getY();
                        ruta.agregarPunto(downx,downy);
                    }
                    else
                    {
                        upx = event.getX();
                        upy = event.getY();
                    }

                    dibujarRuta();

                    break;
                case MotionEvent.ACTION_MOVE:
                    inicializarImageView();
                    upx = event.getX();
                    upy = event.getY();
                    canvas.drawLine(downx, downy, upx, upy, paint);

                    dibujarRuta();


                    break;
                case MotionEvent.ACTION_UP:
                    canvas.drawLine(downx, downy, upx, upy, paint);


                    upx = event.getX();
                    upy = event.getY();
                    if(ruta.getPuntos().size()==0)
                    {
                        ruta.agregarPunto(downx,downy);
                    }

                    ruta.imagenView = imageView;
                    ruta.imagenView.setDrawingCacheEnabled(true);
                    ruta.bitmap = Bitmap.createBitmap(v.getDrawingCache());
                    v.destroyDrawingCache();
                    ruta.canvas =canvas;
                    ruta.agregarPunto(upx,upy);
                    //listView.setAdapter(new RutaAdapter(this, rutaActivity.getLista()));

                    downx = upx;
                    downy = upy;
                    dibujarRuta();
                    break;
                case MotionEvent.ACTION_CANCEL:
                    break;
                default:
                    break;
            }

        }
        return true;
    }


    private void dibujarRuta()
    {
        for(int i = 0; i < ruta.getPuntos().size()-1;i++){
            Point p1 , p2;
            p1 = ruta.getPuntos().get(i);
            p2 = ruta.getPuntos().get(i+1);
            canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);

            if( i == editarPunto)
                dibujarPunto(p1,i+1,true);
            else
                dibujarPunto(p1,i+1,false);

            if (i == ruta.getPuntos().size()-2)
            {
                dibujarPunto(p2,i+2,false);
            }
        }

    }

    private void dibujarPunto(Point point, int numero , boolean puntoEditado)
    {
        int radio = 30;
        Paint p = new Paint();
        int textoSize = 40;
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(point.x, point.y, radio, p);
        p.setStyle(Paint.Style.FILL);
        if (puntoEditado)
            p.setColor(Color.YELLOW);
        else
            p.setColor(Color.WHITE);
        canvas.drawCircle(point.x, point.y, radio - 2, p);
        p.setColor(Color.BLACK);
        p.setTextSize(textoSize);
        canvas.drawText(Integer.toString(numero),point.x-textoSize/2,point.y + textoSize/2,p);
    }


    private void inicializarImageView(){
        Display display = getWindowManager().getDefaultDisplay();

        Point tam= new Point();
        display.getSize(tam);
        int widthPantalla = tam.x;
        int heightPantalla = tam.y/2;






        bitmap = Bitmap.createBitmap(widthPantalla,heightPantalla,Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        imageView.setImageBitmap(bitmap);



        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(7);

        for(int i=0; i<= widthPantalla || i <= heightPantalla; i+=150)
        {


            if(i<widthPantalla) {
                canvas.drawLine(i, 0, i, heightPantalla, paint);
            }
            if(i<heightPantalla) {
                canvas.drawLine(0, i, widthPantalla, i, paint);
            }
        }
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(5);
        dibujarRuta();


        EditText escalaText = (EditText)findViewById(R.id.escala);
        int escala = Integer.parseInt(escalaText.getText().toString());
        ruta.setDatosPantalla(escala,widthPantalla,heightPantalla);///////////////revisar////////////////////////////////////
    }



    public void onClickButtonEliminar(View view){
        int posicion = (Integer)view.getTag();
        ruta.eliminarPunto(posicion +1);
        if (ruta.getPuntos().size() == 1)
        {
            ruta.eliminarPunto(0);
        }
        //listView.setAdapter(new RutaAdapter(this, rutaActivity.getLista()));

        this.editarPunto = -1;
        if (posicion+1 == ruta.getPuntos().size()) {
            downx = ruta.getPuntos().get(posicion).x;
            downy = ruta.getPuntos().get(posicion).y;
        }
        inicializarImageView();
    }

    public  void onClickButtonEditar(View view){
        this.editarPunto = (Integer)view.getTag();
        dibujarRuta();

    }

    public void onClickButtonGuardarRuta(View view){

        if(ruta.getPuntos().size()==0){
            Toast.makeText(context, "Agrega puntos  a rutaActivity", Toast.LENGTH_SHORT).show();
        }
        else {
            // custom dialog
            final Dialog dialog = new Dialog(context);
            dialog.setContentView(R.layout.dialog_ruta);
            dialog.setTitle("Nombre de la rutaActivity");

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
                        Toast.makeText(context, "Ingresa nombre de rutaActivity", Toast.LENGTH_SHORT).show();
                    } else {


                        String listaPuntos = "" + text.getText() + "-";
                        for (int i = 0; i < ruta.getPuntos().size(); i++) {
                            listaPuntos += "(" + ruta.getPuntos().get(i).x + "," + ruta.getPuntos().get(i).y + ")-";
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




}
