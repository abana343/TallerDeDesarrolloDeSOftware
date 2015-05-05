package com.example.bastian.nuevo2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier Aros on 23-04-2015.
 */
public class Ruta {

    private ArrayList<Point> puntos;
    private List lista;
    public ImageView imagenView;
    public Bitmap bitmap;
    public Canvas canvas;


    int escala;
    int widthPantalla;
    int heightPantalla;


    public Ruta()
    {
        this.puntos = new ArrayList<>();
        this.lista = new ArrayList<String>();
    }

    public void actualizarLista()
    {
        this.lista = new ArrayList<String>();
        for(int i = 0 ; i< puntos.size()-1 ;i++){
            lista.add(distancia(puntos.get(i),puntos.get(i+1)));
        }
    }

    private String distancia(Point p1, Point p2)
    {
        Double x = new Double(escala * (p1.x - p2.x) / widthPantalla);
        Double y = new Double(escala * (p1.y - p2.y) / heightPantalla);
        Double distancia = new Double(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));
        return "Distancia: " + String.format("%.2f", distancia);


    }

    public ArrayList<Point> getPuntos(){
        return puntos;
    }

    public void setDatosPantalla(int escala,int widthPantalla, int heightPantalla){
        this.escala = escala;
        this.widthPantalla = widthPantalla;
        this.heightPantalla = heightPantalla;
    }


    public List getLista(){
        return lista;
    }




    public void agregarPunto(float xini, float yini){
        puntos.add(new Point((int)xini,(int)yini));
        actualizarLista();
    }



    public void eliminarPunto(int i)
    {
        puntos.remove(i);
        actualizarLista();

    }
}
