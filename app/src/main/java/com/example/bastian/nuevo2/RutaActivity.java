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


import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;



public class RutaActivity extends Activity implements View.OnTouchListener {
    ImageView imageView;
    Bitmap bitmap;
    Canvas canvas;
    Paint paint;
    float downx = 0, downy = 0, upx = 0, upy = 0;


    private ListView listView;

    //
    private Ruta ruta;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta);

        imageView = (ImageView) this.findViewById(R.id.imageView);
        ruta = new Ruta();
        inicializarImageView();
        mensajeInicio();
        imageView.setOnTouchListener(this);

        this.listView = (ListView) findViewById(R.id.listView);





        this.listView.setAdapter(new RutaAdapter(this,ruta.getLista()));
        this.listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);//revisar-------------------------------------------

    }

    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
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
                listView.setAdapter(new RutaAdapter(this, ruta.getLista()));

                downx = upx;
                downy = upy;
                dibujarRuta();
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            default:
                break;
        }
        return true;
    }


    private void dibujarRuta()
    {
        if(ruta.getPuntos().size() == 1)
        {
            dibujarPunto(ruta.getPuntos().get(0),1);
        }

        for(int i = 0; i < ruta.getPuntos().size()-1;i++){
            Point p1 , p2;
            p1 = ruta.getPuntos().get(i);
            p2 = ruta.getPuntos().get(i+1);
            canvas.drawLine(p1.x,p1.y,p2.x,p2.y,paint);


            dibujarPunto(p1,i+1);
            if (i == ruta.getPuntos().size()-2)
            {
                dibujarPunto(p2,i+2);
            }
        }

    }

    private void dibujarPunto(Point point, int numero )
    {
        int radio = 30;
        Paint p = new Paint();
        int textoSize = 40;


        p.setColor(Color.BLACK);
        p.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(point.x, point.y, radio, p);
        p.setStyle(Paint.Style.FILL);
        p.setColor(Color.WHITE);
        canvas.drawCircle(point.x, point.y, radio - 2, p);
        p.setColor(Color.BLACK);
        p.setTextSize(textoSize);
        canvas.drawText(Integer.toString(numero),point.x-textoSize/2,point.y + textoSize/2,p);
    }


    private void inicializarImageView(){



        //System.out.println(imageView.getWidth() + " | " + imageView.getHeight());
        Display display = getWindowManager().getDefaultDisplay();

        Point tam= new Point();
        display.getSize(tam);
        int widthPantalla = 1080;
        int heightPantalla = 903;

        //System.out.println(tam.x + " | " + tam.y);


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
        ruta.setDatosPantalla(escala,widthPantalla,widthPantalla);///////////////ARREGLAR////////////////////////////////////
    }

    private void mensajeInicio(){

        paint.setTextSize(100);
        canvas.drawText("Inicia tu ruta",250,250,paint);
        paint.setStrokeWidth(10);
    }


    public void onClickotro(View view){

        System.out.println("-------------------");
    }

    public void onClickButtonEliminar(View view){
        int posicion = (Integer)view.getTag();
        ruta.eliminarPunto(posicion +1);
        if (ruta.getPuntos().size() == 1)
        {
            ruta.eliminarPunto(0);
        }
        listView.setAdapter(new RutaAdapter(this, ruta.getLista()));
        inicializarImageView();

    }




}
