package com.example.bastian.nuevo2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import android.os.Bundle;
import android.view.Display;


import android.view.MotionEvent;
import android.view.View;

import android.widget.ImageView;


public class RutaActivity extends Activity implements View.OnTouchListener {
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;


//comentariooooo

    int tiempo = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        imageView = (ImageView) this.findViewById(R.id.imageView);

        inicializarImageView();
        mensajeInicio();
        imageView.setOnTouchListener(this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                inicializarImageView();
                downx = event.getX();
                downy = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                tiempo++;
                if(tiempo%5==0)
                {
                    upx = event.getX();
                    upy = event.getY();
                    canvas.drawLine(downx, downy, upx, upy, paint);
                    downx = upx;
                    downy=upy;
                    imageView.invalidate();
                    tiempo = 0;
                }
                break;
            case MotionEvent.ACTION_UP:
                //upx = event.getX();
                //upy = event.getY();
                //canvas.drawLine(downx, downy, upx, upy, paint);






                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }

    private void inicializarImageView(){



        System.out.println(imageView.getWidth() + " | " + imageView.getHeight());
        Display display = getWindowManager().getDefaultDisplay();

        Point tam= new Point();
        display.getSize(tam);
        int widthPantalla = 1080;
        int heightPantalla = 903;


        bitmap = Bitmap.createBitmap(widthPantalla,heightPantalla,Bitmap.Config.ARGB_8888);

        canvas = new Canvas(bitmap);
        paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setStrokeWidth(10);
        imageView.setImageBitmap(bitmap);
    }

    private void mensajeInicio(){

        paint.setTextSize(100);
        canvas.drawText("Inicia tu ruta",250,250,paint);
        paint.setStrokeWidth(10);
    }
}
