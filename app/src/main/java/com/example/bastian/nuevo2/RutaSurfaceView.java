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
 */
public class RutaSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MySurfaceThread thread;
    RutaActivity2 rutaActivity;
    Ruta ruta;
    int ancho;
    int alto;
    int escala;

    String accionActual = "ninguna";
    float touchX;
    float touchY;

    int radioPunto = 30;
    Point puntoSelecionado  = null;
    int seleccionNodoEliminar = -1;

    public RutaSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //usaremos esta clase como manejador
        getHolder().addCallback(this);
        escala = 5;



        ruta = (Ruta)Comunicador.getObjeto();
        if (ruta == null)
        {
            //Si no existe rutaActivity guardada crea una nueva
            ruta = new Ruta();
        }

/*
        for(int j = 3,i=5 ; j <7 ; j++ , i+=3)
        {
            ruta.agregarPunto(i*i*i,j*j*j);
            ruta.agregarPunto(i*i*j,j*j*i);
        }
        //////////////////////////
*/


    }



    private void dibujarRuta(Canvas canvas)
    {
        int rutaPuntosSize = ruta.getPuntos().size();
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(4);
        if(rutaPuntosSize != 0 && accionActual.equals("AgregarNodo") && touchX != -1){
            canvas.drawLine(ruta.getPuntos().get(rutaPuntosSize-1).x,ruta.getPuntos().get(rutaPuntosSize-1).y,touchX,touchY,paint);

        }


        if(rutaPuntosSize == 1 && accionActual.equals("AgregarNodo")){

            if ( seleccionNodoEliminar == 0){
                dibujarPunto(ruta.getPuntos().get(0),1,canvas,true);
            }
            else{
                dibujarPunto(ruta.getPuntos().get(0),1,canvas,false);
            }

        }

        for(int i = 0; i < rutaPuntosSize-1;i++){
            Point p1 , p2;
            p1 = ruta.getPuntos().get(i);
            p2 = ruta.getPuntos().get(i+1);

            canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);
            if ( seleccionNodoEliminar == i){
                dibujarPunto(p1,i+1,canvas,true);
            }
            else{
                dibujarPunto(p1,i+1,canvas,false);
            }


            if (i == rutaPuntosSize-2)
            {

                if ( seleccionNodoEliminar == i+1){
                    dibujarPunto(p2,i+2,canvas,true);
                }
                else{
                    dibujarPunto(p2,i+2,canvas,false);
                }
            }
        }


    }

    public Point seleccionarPunto()
    {
        Point p = null;
        for(int i = 0; i< ruta.getPuntos().size(); i++){
            if (ruta.getPuntos().get(i).x -radioPunto < touchX &&
               (ruta.getPuntos().get(i).x + radioPunto) > touchX &&
                ruta.getPuntos().get(i).y -radioPunto < touchY &&
               (ruta.getPuntos().get(i).y + radioPunto) > touchY )
            {
                p = ruta.getPuntos().get(i);

            }
        }
        return  p;
    }

    public int seleccionarPuntoPosicion()
    {
        int p = -1;
        for(int i = 0; i< ruta.getPuntos().size(); i++){
            if (ruta.getPuntos().get(i).x -radioPunto < touchX &&
                    (ruta.getPuntos().get(i).x + radioPunto) > touchX &&
                    ruta.getPuntos().get(i).y -radioPunto < touchY &&
                    (ruta.getPuntos().get(i).y + radioPunto) > touchY )
            {
                p = i;

            }
        }
        return  p;
    }

    private void dibujarPunto(Point point, int numero , Canvas canvas, boolean eliminar)
    {

        Paint p = new Paint();
        int textoSize = 40;
        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(point.x, point.y, radioPunto, p);
        p.setStyle(Paint.Style.FILL);
        if (eliminar)
            p.setColor(Color.RED);
        else
            p.setColor(Color.WHITE);
        canvas.drawCircle(point.x, point.y, radioPunto - 2, p);
        p.setColor(Color.BLACK);
        p.setTextSize(textoSize);
        canvas.drawText(Integer.toString(numero),point.x-textoSize/2,point.y + textoSize/2,p);
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
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        Log.e("surfaceDestroyed", "hilo detenido");

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

    public void setRutaActivity(RutaActivity2 ruta)
    {
        try {
            this.rutaActivity = ruta;
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
        ancho = canvas.getWidth();
        alto = canvas.getHeight();
        cuadriculaCanvas(canvas);

    }

    public void cuadriculaCanvas(Canvas canvas){
        Paint paint = new Paint();
        paint.setStrokeWidth(escala*3);
        paint.setColor(Color.BLACK);
        canvas.drawLine(0,0,ancho,0,paint);
        canvas.drawLine(0,0,0,alto,paint);
        canvas.drawLine(0,alto,ancho,alto,paint);
        canvas.drawLine(ancho,0,ancho,alto,paint);


        paint.setColor(Color.GRAY);
        paint.setStrokeWidth(7);

        for(int i=0; i<= ancho || i <= alto; i+=150)
        {


            if(i<ancho) {
                canvas.drawLine(i, 0, i, alto, paint);
            }
            if(i<alto) {
                canvas.drawLine(0, i, ancho, i, paint);
            }
        }

    }

    public String getAccionActual() {
        return accionActual;
    }

    public void setAccionActual(String accionActual) {
        this.accionActual = accionActual;

    }

    public void reinicioTouch(){
        touchX = -1;
        touchY = -1;
        seleccionNodoEliminar = -1;
    }



    @Override
    public boolean onTouchEvent(MotionEvent event){
        touchX = event.getX();
        touchY = event.getY();
        switch (accionActual) {
            case "AgregarNodo":
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        if(ruta.getPuntos().size()==0)
                        {

                            ruta.agregarPunto(touchX,touchY);
                        }


                        break;
                    case MotionEvent.ACTION_MOVE:
                        //canvas.drawLine(ruta.getUltimoPunto().x, ruta.getUltimoPunto().y, touchX, touchY, paint);




                        break;
                    case MotionEvent.ACTION_UP:
                        //canvas.drawLine(ruta.getUltimoPunto().x, ruta.getUltimoPunto().y, touchX, touchY, paint);



                        if(ruta.getPuntos().size()==0)
                        {
                            ruta.agregarPunto(touchX,touchY);
                        }


                        ruta.agregarPunto(touchX,touchY);

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
                        puntoSelecionado = seleccionarPunto();
                    case MotionEvent.ACTION_MOVE://cuando se desplaza el dedo por la pantalla
                        if (puntoSelecionado != null) {
                            puntoSelecionado.x = (int) touchX;
                            puntoSelecionado.y = (int) touchY;
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
                        if (seleccionNodoEliminar == seleccionarPuntoPosicion())
                        {
                            ruta.eliminarPunto(seleccionNodoEliminar);
                            if(ruta.getPuntos().size() == 1)
                                ruta.eliminarPunto(0);
                            seleccionNodoEliminar =-1;
                        }
                        else{
                            seleccionNodoEliminar = seleccionarPuntoPosicion();
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
