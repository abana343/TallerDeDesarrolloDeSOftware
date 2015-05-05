package com.example.bastian.nuevo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.jar.Attributes;

/**
 * Created by Bastian on 28-04-2015.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    //referencia un thread que usaremos para dibujar.
    private MySurfaceThread thread;
    //variables que indican donde toco la pantalla
    public int touched_x, touched_y;
    //variable que indica si se esta tocando o no la pantalla
    public boolean touched;

    //para generar el movimiento
    public int x,y;
    public boolean mover;


    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //usaremos esta clase como manejador
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        this.thread = new MySurfaceThread(getHolder(),this);
        this.thread.setRuning(true);
        this.thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.e("surfaceDestroyed","hilo detenido");

        boolean retry = true;
        //el hilo se detendra.
        this.thread.setRuning(false);
        while(retry)
        {
            try
            {
                this.thread.join();
                retry=false;
            }
            catch (InterruptedException e){}
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        //se utiliza para dibujar
        int ancho = canvas.getWidth();
        int alto = canvas.getHeight();
        int anchoW = ancho/2;
        int altoH = alto/2;
/*
    Bitmap imgBoton_libre = BitmapFactory.decodeResource(getResources(),R.drawable.botonlibre);
        Bitmap imgBoton_puch = BitmapFactory.decodeResource(getResources(),R.drawable.botonpress);

        int anchobitmap= imgBoton_libre.getWidth();
        int altobitmap= imgBoton_puch.getHeight();
 */
        /// imagen del boton


        //pintamos el fondo de color blanco
        canvas.drawColor(Color.GRAY);

        //definimos circulo externo
        Paint pcirculo = new Paint();
        pcirculo.setColor(Color.BLUE);
        pcirculo.setStyle(Paint.Style.FILL);
        canvas.drawCircle(anchoW, altoH, altoH, pcirculo);

        //circulo interno
        Paint pcirculointerno = new Paint();
        pcirculointerno.setColor(Color.BLACK);
        pcirculointerno.setStyle(Paint.Style.FILL);

        // circulo interno
        Paint imgboton = new Paint();
        imgboton.setAntiAlias(true);
        imgboton.setFilterBitmap(true);
        imgboton.setDither(true);

        /*
        validacion sobre el movimineto de la palanca, si estamos tocando la pantalla y en donde
        tocamos esta dentro del sector de la palanca la dibujaremso en donde toco, esto dara la censacion de movimiento.
        */

        if(touched && //si tocamos
                //eje Y
                ((anchoW>touched_x &&(anchoW-touched_x)<=altoH) || ((anchoW<touched_x && (touched_x-anchoW)<= altoH))) &&
                ((altoH> touched_y &&(altoH-touched_y)<=altoH) || ((altoH<touched_y &&(touched_y-altoH)<= altoH)))
          )
        {
            canvas.drawCircle(touched_x,touched_y,35,pcirculointerno);
            this.x=touched_x;
            this.y=touched_y;
            this.mover=true;
        }
        else
        {
            canvas.drawCircle(anchoW,altoH,35,pcirculointerno);
            this.mover=false;
        }

        /*
        dibujamos el boton
        validamos si estamos tocando dentro del boton

        if(touched &&
                (touched_y>altoH + (altoH/2) &&
                 ( touched_y< altoH + (altoH/2)+altobitmap)
                ) &&
                (touched_x>anchoW +(anchoW/2) &&
                 touched_x<anchoW +(anchoW/2)+anchobitmap
                )
           )
        {
            canvas.drawBitmap(imgBoton_puch, ancho*2/3, alto*3/4,null);
        }
        else
        {
            canvas.drawBitmap(imgBoton_libre, ancho*2/3, alto*3/4,null);
        }
*/
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //eventos que suceden cuando se toca la pantalla.

        touched_x =(int) event.getX();
        touched_y =(int) event.getY();
        //leemos el codigo de acción
        int action = event.getAction();
        Log.e("touched (x,y)","("+touched_x+","+touched_y+")");

        switch (action)
        {
            case MotionEvent.ACTION_DOWN://cuando se toca la pantalla
                Log.e("touchEven ACTION_DOWN","se toco la pantalla");
                touched=true;
                break;
            case MotionEvent.ACTION_MOVE://cuando se desplaza el dedo por la pantalla
                touched=true;
                Log.e("TouchEven ACTION_MOVE", "nos desplazamos por la pantalla");
                break;
            case MotionEvent.ACTION_UP://cuando levantamos el dedo de la pantalla
                touched=false;
                Log.e("TouchEven ACTION_UP", "ya no tocamos la pantalla");
                break;
            case MotionEvent.ACTION_CANCEL:
                touched=false;
                Log.e("TouchEven ACTION_CANCEL","");
                break;
            case MotionEvent.ACTION_OUTSIDE:
                touched=false;
                break;
            default:
        }
        return true;
    }

    public void mover()
    {

    }
}
