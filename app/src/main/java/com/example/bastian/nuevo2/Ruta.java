package com.example.bastian.nuevo2;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier Aros on 23-04-2015.
 * Contiene los puntos de una ruta
 */
public class Ruta {

    private int ID;
    private ArrayList<Point> puntos;
    private List lista;
    public ImageView imagenView;
    public Bitmap bitmap;
    public Canvas canvas;
    public String nombre;

    int escala;
    int widthPantalla;
    int heightPantalla;


    public Ruta()
    {
        this.puntos = new ArrayList<>();
        this.lista = new ArrayList<String>();
        this.nombre = "nombre";  ///editar
    }



    public Ruta(int ID, int escala, String nombre)
    {
        this.ID = ID;
        this.escala = escala;
        this.nombre = nombre;
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
        Double x = (((double)p1.x - p2.x)/150)*escala;
        Double y = (((double)p1.y - p2.y)/150)*escala;
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

    public int getID() {
        return ID;
    }


}
