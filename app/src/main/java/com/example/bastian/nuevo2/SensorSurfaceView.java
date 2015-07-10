package com.example.bastian.nuevo2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier Aros on 23-06-2015.
 *
 */
public class SensorSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MySurfaceThread _thread;
    private Bitmap _imagenRobot;
    private List<String> _sensores;
    Context _context;
    private ArrayList<Point> _puntos;
    private int _ancho;
    private int _alto;
    public SensorSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        _imagenRobot = BitmapFactory.decodeResource(getResources(), R.drawable.robot_sensores);
        _sensores = new ArrayList<>();
        this._context = context;



    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this._thread = new MySurfaceThread(getHolder(),this);
        this._thread.setRuning(true);
        this._thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this._ancho = width;
        this._alto = height;
        // Read your drawable from somewhere
        Drawable _dr = getResources().getDrawable(R.drawable.robot_sensores2);
        Bitmap _bitmap = ((BitmapDrawable) _dr).getBitmap();
// Scale it to 50 x 50
        Drawable _d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(_bitmap, width, height, true));
// Set your new, scaled drawable "d"
        _imagenRobot = ((BitmapDrawable) _d).getBitmap();
        generaPuntos();
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

    @Override
    public void draw(Canvas canvas){
        try{
            int y = 45;


            canvas.drawBitmap(_imagenRobot,0,0,new Paint());
            if(_sensores.size() > 0){

                if(!_sensores.get(0).equals(_context.getString(R.string.sensor_no_encontrado))){
                    for(int i = 0 ; i< _sensores.size();i++){
                        float _valor = Float.parseFloat(_sensores.get(i));
                        Paint _paint = new Paint();
                        if (_valor <= 0.5){
                            _paint.setColor(Color.GREEN);
                        }
                        else if(_valor <=2){
                            _paint.setColor(Color.YELLOW);
                        }
                        else{
                            _paint.setColor(Color.RED);
                        }

                        canvas.drawCircle(_puntos.get(i).x, _puntos.get(i).y,40,_paint);
                        _paint.setTextSize(50);
                        _paint.setColor(Color.BLACK);
                        canvas.drawText("" + (i + 1), _puntos.get(i).x - 10, _puntos.get(i).y + 10, _paint);
                    }

                }
            }



        }
        catch (Exception exeption){

        }
    }

    private void generaPuntos(){
        _puntos = new ArrayList<>();

        _puntos.add(new Point(515,675));

        _puntos.add(new Point(760, 580));
        _puntos.add(new Point(850, 435));
        _puntos.add(new Point(874, 280));
        _puntos.add(new Point(715, 85));

        _puntos.add(new Point(280, 85));
        _puntos.add(new Point(109, 280));
        _puntos.add(new Point(130, 435));
        _puntos.add(new Point(210, 580));

    }

    public void set_sensores(List lista){
        _sensores = lista;

    }


    public void set_imagenRobot(Bitmap _imagenRobot){

        //this.imagenRobot = imagenRobot;
    }
}
