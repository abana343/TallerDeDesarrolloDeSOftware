package com.example.bastian.nuevo2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Bastian on 28-04-2015.
 */
public class MySurfaceView extends SurfaceView implements SurfaceHolder.Callback
{
    //referencia un _thread que usaremos para dibujar.
    private MySurfaceThread _thread;
    //variables que indican donde toco la pantalla
    public int _touched_x, _touched_y;
    //variable que indica si se esta tocando o no la pantalla
    public boolean _touched;

    /*
    referencia a la clase movimiento para poder ejecutar los comandos
    de movimientos con el dibujo del analogo.
     */
    private movimiento _mov;
    private float _movx =0, _movy =0;
    private boolean _adentro =false;
    private int _ejecutandose =0;



    public MySurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //usaremos esta clase como manejador
        getHolder().addCallback(this);
    }

    public void setMovimiento(movimiento mov)
    {
        try {
            this._mov = mov;
        }
        catch (Exception e){}
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub comienza la ejecucion del dibujo del analogo
        this._thread = new MySurfaceThread(getHolder(),this);
        this._thread.setRuning(true);
        this._thread.start();

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        //Cuando se produce en onBackPressed detiene el hilo de ejecucion.
        Comunicador.setCamara(false);
        boolean retry = true;
        //el hilo se detendra.
        this._thread.setRuning(false);
        while(retry)
        {
            try
            {
                this._thread.join();
                retry=false;
            }
            catch (InterruptedException e){}
        }
    }

    @Override
    public void draw(Canvas canvas)
    {
        try
        {
            //se utiliza para dibujar
            int ancho = canvas.getWidth();
            int alto = canvas.getHeight();
            int anchoW = ancho/2;
            int altoH = alto/2;
            int radio;
            if(anchoW<=altoH)
            {
                radio=anchoW;
            }
            else
            {
                radio=altoH;
            }


            //pintamos el fondo de color Transparente.
            canvas.drawColor(Color.TRANSPARENT);

            //definimos circulo externo
            Paint pcirculo = new Paint();
            pcirculo.setColor(Color.BLUE);
            pcirculo.setStyle(Paint.Style.FILL);
            canvas.drawCircle(anchoW, altoH, radio, pcirculo);

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
        Validamos que el punto donde se este tocando la pantalla este dentro del circulo.
        para dibujar el analogo y mandar mas tarde la informacion de movimiento.
        */
            this._movx = _touched_x -anchoW;
            this._movy = _touched_y -altoH;
            double hipo =Math.pow(_movx,2)+ Math.pow(_movy,2);
            hipo=Math.sqrt(hipo);
            if(_touched && //si tocamos
                    hipo<radio
                    )
            {
                //dibujamos el circulo interno donde tocamos la pantalla.
                canvas.drawCircle(_touched_x, _touched_y,35,pcirculointerno);
                this._adentro =true;
            }
            else
            {
                //si no se esta tocando se mantendra en el centro.
                canvas.drawCircle(anchoW,altoH,35,pcirculointerno);
                this._adentro =false;
            }
        }catch (Exception e)
        {}
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //eventos que suceden cuando se toca la pantalla.
        this.mover();
        _touched_x =(int) event.getX();
        _touched_y =(int) event.getY();
        //leemos el codigo de acción
        int action = event.getAction();

        switch (action)
        {
            case MotionEvent.ACTION_DOWN://cuando se toca la pantalla
                _touched =true;
                break;
            case MotionEvent.ACTION_MOVE://cuando se desplaza el dedo por la pantalla
                _touched =true;
                break;
            case MotionEvent.ACTION_UP://cuando levantamos el dedo de la pantalla
                _touched =false;
                _mov.parar(this);
                _ejecutandose =0;
                break;
            case MotionEvent.ACTION_CANCEL:
                _touched =false;
                _mov.parar(this);
                _ejecutandose =0;
                break;
            case MotionEvent.ACTION_OUTSIDE:
                _touched =false;
                _mov.parar(this);
                _ejecutandose =0;
                break;
            default:
        }
        return true;
    }

    private void mover()
    {
        //dependiendo de los puntos de donde se toque el analogo se movera el robot.
        float div;
        if(this._adentro)
        {

            if(this._movx !=0)
            {
                div = this._movy /this._movx;
                if(div>0.41 && div<=2.41)
                {
                    if(this._movx <0)
                    {
                        if(_ejecutandose !=1)
                        {
                           // Log.e("mover en ","diagonal arriba iz");
                            _mov.dNO(this);
                            _ejecutandose =1;
                        }
                    }else
                    {
                        if(_ejecutandose !=2)
                        {
                           // Log.e("mover en ","diagonal abajo der");
                            _mov.dSE(this);
                            _ejecutandose =2;
                        }

                    }
                }
                //................
                if(div<=-0.41 && div>-2.41)
                {
                    if(this._movx <0)
                    {
                        if(_ejecutandose !=3){
                            //Log.e("mover en ","diagonal abajo iz");
                            _mov.dSO(this);
                            _ejecutandose =3;
                        }
                    }else
                    {
                        if(_ejecutandose !=4) {
                           // Log.e("mover en ", "diagonal arriba der");
                            _mov.dNE(this);
                            _ejecutandose =4;
                        }
                    }
                }
                //................
                if((div>-0.41 && div<=0) || (div<=0.41 && div>=0))
                {
                    if(this._movx <0)
                    {
                        if(_ejecutandose !=5) {
                          //  Log.e("mover en ", "Izquierda");
                            _mov.izquierda(this);
                            _ejecutandose =5;
                        }
                    }else
                    {
                        if(_ejecutandose !=6)
                        {
                            //Log.e("mover en ","derecha");
                            _mov.derecha(this);
                            _ejecutandose =6;
                        }

                    }
                }
                //................
                if(div<= -2.41 || div> 2.41)
                {
                    if(this._movy <0)
                    {
                        if(_ejecutandose !=7) {
                           // Log.e("mover en ", "arriba");
                            _mov.adelante(this);
                            _ejecutandose =7;
                        }
                    }else
                    {
                        if(_ejecutandose !=8) {
                           // Log.e("mover en ", "abajo");
                            _mov.atras(this);
                            _ejecutandose =8;
                        }
                    }
                }

            }else
            {
                if(_movy <0)
                {
                    if(_ejecutandose !=7){
                      //  Log.e("mover en ","arriba");
                        _mov.adelante(this);
                        _ejecutandose =7;
                    }
                }
                else
                {
                    if(_ejecutandose !=8) {
                       // Log.e("mover en ", "abajo");
                        _mov.atras(this);
                        _ejecutandose =8;
                    }
                }
            }
        }

    }
}
