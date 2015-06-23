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
 */
public class SensorSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private MySurfaceThread thread;
    private Bitmap imagenRobot;
    private List<String> sensores;
    Context context;
    private ArrayList<Point> puntos;
    private int width;
    private int height;
    public SensorSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        imagenRobot = BitmapFactory.decodeResource(getResources(), R.drawable.robot_sensores);
        sensores = new ArrayList<>();
        this.context = context;




    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        this.thread = new MySurfaceThread(getHolder(),this);
        this.thread.setRuning(true);
        this.thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.width = width;
        this.height = height;
        System.out.println("width  " + width + "  height:  " + height);
        // Read your drawable from somewhere
        Drawable dr = getResources().getDrawable(R.drawable.robot_sensores);
        Bitmap bitmap = ((BitmapDrawable) dr).getBitmap();
// Scale it to 50 x 50
        Drawable d = new BitmapDrawable(getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));
// Set your new, scaled drawable "d"
        imagenRobot = ((BitmapDrawable) d).getBitmap();
        generaPuntos();
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

    @Override
    public void draw(Canvas canvas){
        try{
            int y = 45;


            canvas.drawBitmap(imagenRobot,0,0,new Paint());
            if(sensores.size() > 0){

                if(!sensores.get(0).equals(context.getString(R.string.sensor_no_encontrado))){
                    for(int i = 0 ; i< sensores.size();i++){
                        float valor = Float.parseFloat(sensores.get(i));
                        Paint paint = new Paint();
                        if (valor <= 1){
                            paint.setColor(Color.GREEN);
                        }
                        else if(valor <=2){
                            paint.setColor(Color.YELLOW);
                        }
                        else{
                            paint.setColor(Color.RED);
                        }
                        canvas.drawCircle(puntos.get(i).x,puntos.get(i).y,40,paint);
                    }

                }
            }



        }
        catch (Exception exeption){

        }
    }

    private void generaPuntos(){
        puntos = new ArrayList<>();
        puntos.add(new Point(6*width/11,height-(int)(height*0.1)));

        puntos.add(new Point(4*width/5,height-(int)(height*0.2)));
        puntos.add(new Point(8*width/9,height-(int)(height*0.4)));
        puntos.add(new Point(8*width/9,height-(int)(height*0.6)));
        puntos.add(new Point(6*width/8,height-(int)(height*0.9)));

        puntos.add(new Point(2*width/7,height-(int)(height*0.9)));
        puntos.add(new Point(1*width/9,height-(int)(height*0.6)));
        puntos.add(new Point(1*width/9,height-(int)(height*0.4)));
        puntos.add(new Point(1*width/5,height-(int)(height*0.2)));
    }

    public void setSensores(List lista){
        sensores = lista;


    }


    public void setImagenRobot(Bitmap imagenRobot){

        //this.imagenRobot = imagenRobot;
    }
}
