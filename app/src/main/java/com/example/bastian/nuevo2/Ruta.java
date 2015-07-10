package com.example.bastian.nuevo2;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Javier Aros on 23-04-2015.
 * Contiene los _puntos de una Ruta y su informacion
 */
public class Ruta {

    private int _ID;
    private ArrayList<Point> _puntos;
    private List _lista;
    public String _nombre;

    public int _escala;
    int _anchoPantalla;
    int _altoPantalla;


    public Ruta()
    {
        this._puntos = new ArrayList<>();
        this._lista = new ArrayList<String>();
        this._nombre = "";
        this._escala = 15;
        this.set_ID(-1);
    }



    public Ruta(int _ID, int _escala, String _nombre)
    {
        this._ID = _ID;
        this._escala = _escala;
        this._nombre = _nombre;
        this._puntos = new ArrayList<>();
        this._lista = new ArrayList<String>();
    }

    public void actualizarLista()
    {
        this._lista = new ArrayList<String>();
        for(int i = 0 ; i< _puntos.size()-1 ;i++){
            _lista.add(distancia(_puntos.get(i), _puntos.get(i + 1)));
        }
    }

    private double distancia(Point p1, Point p2)
    {
        Double x = (((double)p1.x - p2.x)/150)* _escala;
        Double y = (((double)p1.y - p2.y)/150)* _escala;
        return  new Double(Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)));

    }

    public double distanciaTotal(){
        double resultadoFinal = 0;
        for(int i = 0 ; i< _puntos.size()-1; i++){
            resultadoFinal += distancia(_puntos.get(i), _puntos.get(i+1));
        }
        return resultadoFinal;
    }

    public ArrayList<Point> get_puntos(){
        return _puntos;
    }

    public void setDatosPantalla(int escala,int widthPantalla, int heightPantalla){
        this._escala = escala;
        this._anchoPantalla = widthPantalla;
        this._altoPantalla = heightPantalla;
    }

    public void agregarPunto(float xini, float yini){
        _puntos.add(new Point((int) xini, (int) yini));
        actualizarLista();
    }

    public void eliminarPunto(int i)
    {

        if(_puntos.size() > i && _puntos.size() != 0 && i != -1) {
            System.out.println(_puntos.size() + "  " +i);
            _puntos.remove(i);
            actualizarLista();
        }

    }

    public int get_ID() {
        return _ID;
    }

    public void set_ID(int _ID) {
        this._ID = _ID;
    }

    public String get_nombre() {
        return _nombre;
    }

    public void set_nombre(String _nombre) {
        this._nombre = _nombre;
    }

    public int get_escala() {
        return _escala;
    }

    public void set_escala(int _escala) {
        this._escala = _escala;
    }

    public void set_puntos(ArrayList<Point> _puntos) {
        this._puntos = _puntos;
    }
}
