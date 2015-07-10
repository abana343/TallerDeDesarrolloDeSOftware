package com.example.bastian.nuevo2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Javier Aros on 28-05-2015.
 * Contiene el surface para dibujar una ruta
 */
public class RutaSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MySurfaceThread _thread;
    RutaActivity _rutaActivity;
    Ruta _ruta;
    int _ancho;
    int _alto;
    int _escala;

    String _accionActual = "ninguna";
    float _touchX;
    float _touchY;

    int _radioPunto = 30;
    Point _puntoSelecionado = null;
    int _seleccionNodoEliminar = -1;

    public RutaSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //usaremos esta clase como manejador
        getHolder().addCallback(this);
        _escala = 5;



        _ruta = (Ruta)Comunicador.getObjeto();
        if (_ruta == null)
        {
            //Si no existe rutaActivity guardada crea una nueva
            _ruta = new Ruta();
        }


    }



    private void dibujarRuta(Canvas canvas)
    {
        int _rutaPuntosSize = _ruta.get_puntos().size();
        Paint _paint = new Paint();
        _paint.setColor(Color.BLACK);
        _paint.setStyle(Paint.Style.FILL);
        _paint.setStrokeWidth(4);
        if(_rutaPuntosSize != 0 && _accionActual.equals("AgregarNodo") && _touchX != -1){
            canvas.drawLine(_ruta.get_puntos().get(_rutaPuntosSize-1).x, _ruta.get_puntos().get(_rutaPuntosSize-1).y, _touchX, _touchY,_paint);

        }


        if(_rutaPuntosSize == 1 && _accionActual.equals("AgregarNodo")){

            if ( _seleccionNodoEliminar == 0){
                dibujarPunto(_ruta.get_puntos().get(0),1,canvas,true);
            }
            else{
                dibujarPunto(_ruta.get_puntos().get(0),1,canvas,false);
            }

        }

        for(int i = 0; i < _rutaPuntosSize-1;i++){
            Point _p1 , _p2;
            _p1 = _ruta.get_puntos().get(i);
            _p2 = _ruta.get_puntos().get(i+1);

            canvas.drawLine(_p1.x,_p1.y,_p2.x,_p2.y,_paint);
            if ( _seleccionNodoEliminar == i){
                dibujarPunto(_p1,i+1,canvas,true);
            }
            else{
                dibujarPunto(_p1,i+1,canvas,false);
            }


            if (i == _rutaPuntosSize-2)
            {

                if ( _seleccionNodoEliminar == i+1){
                    dibujarPunto(_p2,i+2,canvas,true);
                }
                else{
                    dibujarPunto(_p2,i+2,canvas,false);
                }
            }
        }


    }

    public Point seleccionarPunto()
    {
        Point _p = null;
        for(int i = 0; i< _ruta.get_puntos().size(); i++){
            if (_ruta.get_puntos().get(i).x - _radioPunto < _touchX &&
               (_ruta.get_puntos().get(i).x + _radioPunto) > _touchX &&
                _ruta.get_puntos().get(i).y - _radioPunto < _touchY &&
               (_ruta.get_puntos().get(i).y + _radioPunto) > _touchY)
            {
                _p = _ruta.get_puntos().get(i);

            }
        }
        return  _p;
    }

    public int seleccionarPuntoPosicion()
    {
        int _p = -1;
        for(int i = 0; i< _ruta.get_puntos().size(); i++){
            if (_ruta.get_puntos().get(i).x - _radioPunto < _touchX &&
                    (_ruta.get_puntos().get(i).x + _radioPunto) > _touchX &&
                    _ruta.get_puntos().get(i).y - _radioPunto < _touchY &&
                    (_ruta.get_puntos().get(i).y + _radioPunto) > _touchY)
            {
                _p = i;

            }
        }
        return  _p;
    }

    private void dibujarPunto(Point point, int numero , Canvas canvas, boolean eliminar)
    {

        Paint _p = new Paint();
        int _textoSize = 40;
        _p.setColor(Color.BLACK);
        _p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(point.x, point.y, _radioPunto, _p);
        _p.setStyle(Paint.Style.FILL);
        if (eliminar)
            _p.setColor(Color.RED);
        else
            _p.setColor(Color.WHITE);
        canvas.drawCircle(point.x, point.y, _radioPunto - 2, _p);
        _p.setColor(Color.BLACK);
        _p.setTextSize(_textoSize);
        canvas.drawText(Integer.toString(numero),point.x-_textoSize/2,point.y + _textoSize/2,_p);
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        // TODO Auto-generated method stub
        this._thread = new MySurfaceThread(getHolder(),this);
        this._thread.setRuning(true);
        this._thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.e("surfaceDestroyed", "hilo detenido");

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

    public void set_rutaActivity(RutaActivity ruta)
    {
        try {
            this._rutaActivity = ruta;
        }
        catch (Exception e){}
    }


    @Override
    public void draw(Canvas canvas){


        try{
            //se utiliza para dibujar
            iniCanvas(canvas);
            dibujarRuta(canvas);


        }
        catch (Exception exeption){

        }
    }
    //Inicialisa el canvas
    public void iniCanvas(Canvas canvas){
        canvas.drawColor(Color.WHITE);
        _ancho = canvas.getWidth();
        _alto = canvas.getHeight();
        cuadriculaCanvas(canvas);

    }

    public void cuadriculaCanvas(Canvas canvas){
        Paint _paint = new Paint();
        _paint.setStrokeWidth(_escala * 3);
        _paint.setColor(Color.BLACK);
        canvas.drawLine(0,0, _ancho,0,_paint);
        canvas.drawLine(0,0,0, _alto,_paint);
        canvas.drawLine(0, _alto, _ancho, _alto,_paint);
        canvas.drawLine(_ancho,0, _ancho, _alto,_paint);


        _paint.setColor(Color.GRAY);
        _paint.setStrokeWidth(7);

        for(int i=0; i<= _ancho || i <= _alto; i+=150)
        {


            if(i< _ancho) {
                canvas.drawLine(i, 0, i, _alto, _paint);
            }
            if(i< _alto) {
                canvas.drawLine(0, i, _ancho, i, _paint);
            }
        }

    }

    public void set_accionActual(String _accionActual) {
        this._accionActual = _accionActual;

    }

    public void reinicioTouch(){
        _touchX = -1;
        _touchY = -1;
        _seleccionNodoEliminar = -1;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        _touchX = event.getX();
        _touchY = event.getY();
        switch (_accionActual) {
            case "AgregarNodo":
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if(_ruta.get_puntos().size()==0)
                        {

                            _ruta.agregarPunto(_touchX, _touchY);
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        break;
                    case MotionEvent.ACTION_UP:
                        if(_ruta.get_puntos().size()==0)
                        {
                            _ruta.agregarPunto(_touchX, _touchY);
                        }
                        _ruta.agregarPunto(_touchX, _touchY);
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    default:
                        break;
                }
                break;
            case "EditarNodo":

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://cuando se toca la pantalla
                        _puntoSelecionado = seleccionarPunto();
                    case MotionEvent.ACTION_MOVE://cuando se desplaza el dedo por la pantalla
                        if (_puntoSelecionado != null) {
                            _puntoSelecionado.x = (int) _touchX;
                            _puntoSelecionado.y = (int) _touchY;
                        }
                        break;
                    case MotionEvent.ACTION_UP://cuando levantamos el dedo de la pantalla
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        break;
                    default:
                }
                break;
            case "EliminarNodo":
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN://cuando se toca la pantalla
                        if (_seleccionNodoEliminar == seleccionarPuntoPosicion())
                        {
                            _ruta.eliminarPunto(_seleccionNodoEliminar);
                            if(_ruta.get_puntos().size() == 1)
                                _ruta.eliminarPunto(0);
                            _seleccionNodoEliminar =-1;
                        }
                        else{
                            _seleccionNodoEliminar = seleccionarPuntoPosicion();
                        }
                        break;
                    case MotionEvent.ACTION_MOVE://cuando se desplaza el dedo por la pantalla
                        break;
                    case MotionEvent.ACTION_UP://cuando levantamos el dedo de la pantalla
                        break;
                    case MotionEvent.ACTION_CANCEL:
                        break;
                    case MotionEvent.ACTION_OUTSIDE:
                        break;
                    default:
                }
                break;
            default:

        }

        return true;
    }




}
