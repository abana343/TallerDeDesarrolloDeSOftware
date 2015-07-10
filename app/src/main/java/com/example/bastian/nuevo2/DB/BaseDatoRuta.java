package com.example.bastian.nuevo2.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

import com.example.bastian.nuevo2.Ruta;

import java.util.ArrayList;

/**
 * Created by Javier Aros on 07-06-2015.
 */
public class BaseDatoRuta extends SQLiteOpenHelper {

    private static final String DB_NAME = "robotino";
    private static  final int SCHEME_VERSION = 1;
    private SQLiteDatabase db;

    public BaseDatoRuta(Context context) {
        super(context, DB_NAME, null, SCHEME_VERSION);
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TablaRuta.CREATE_DB_TABLE);
        db.execSQL(TablaPasosRuta.CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private ContentValues generaValoresRuta(Ruta ruta){
        ContentValues valores = new ContentValues();
        valores.put(TablaRuta.FIELD_ESCALA, ruta._escala);
        valores.put(TablaRuta.FIELD_NOMBRE_RUTA,ruta._nombre);
        return valores;
    }

    public void InsertarRuta(Ruta ruta){
        db.insert(TablaRuta.TABLE_NAME,null,generaValoresRuta(ruta));
        int ultimoID = getUltimoIdTablaRuta();
        insertarPuntoRuta(ruta.get_puntos(),ultimoID);

    }


    public void editarRuta(Ruta ruta){
        ContentValues valores = new ContentValues();
        valores.put(TablaRuta.FIELD_NOMBRE_RUTA, ruta.get_nombre());
        valores.put(TablaRuta.FIELD_ESCALA, ruta.get_escala());
        db.update(TablaRuta.TABLE_NAME, valores, TablaRuta.FIELD_ID+ "=" + ruta.get_ID(), null);

        eliminarPuntosRuta(ruta.get_ID());
        insertarPuntoRuta(ruta.get_puntos(),ruta.get_ID());
    }

    private void insertarPuntoRuta(ArrayList<Point> puntos , int id){
        for(Point point:puntos){
            db.insert(TablaPasosRuta.TABLE_NAME, null, generarValoresPasosRuta(point, id));
        }
    }

    public void eliminarRuta(int id){
        db.delete(TablaRuta.TABLE_NAME, TablaRuta.FIELD_ID + " = " + id, null);
    }

    private void eliminarPuntosRuta(int id){
        db.delete(TablaPasosRuta.TABLE_NAME, TablaPasosRuta.FIELD_ID_RUTA + " = " + id, null);
    }



    public ContentValues generarValoresPasosRuta(Point point,int id){
        ContentValues valores = new ContentValues();
        valores.put(TablaPasosRuta.FIELD_ID_RUTA, id);
        valores.put(TablaPasosRuta.FIELD_X, point.x);
        valores.put(TablaPasosRuta.FIELD_Y, point.y);
        return valores;
    }


    public ArrayList<Ruta> getRutas(){
        ArrayList<Ruta> rutas = new ArrayList<>();
        String columnas[] = {TablaRuta.FIELD_ID ,TablaRuta.FIELD_NOMBRE_RUTA, TablaRuta.FIELD_ESCALA};

        Cursor c = db.query(TablaRuta.TABLE_NAME,columnas,null,null,null,null,null);
        if(c.moveToFirst())
        {
            do {
                Ruta r = new Ruta();
                r.set_ID(c.getInt(0));
                r.set_nombre(c.getString(1));
                r.set_escala(c.getInt(2));
                r.set_puntos(getPuntosRuta(r.get_ID()));
                rutas.add(r);

            }while(c.moveToNext());
        }
        return rutas;
    }

    public ArrayList<Point> getPuntosRuta(int IdRuta){
        ArrayList<Point> puntos = new ArrayList<>();
        //IdRuta--;
        System.out.println("IdRuta " + IdRuta);
        //String query = "select X,Y from " + TablaPasosRuta.TABLE_NAME + " where " + TablaPasosRuta.FIELD_ID_RUTA + " =?";
        String[] campos = new String[] {"X","Y","idRuta"};
        String[] args = new String[]{""+ IdRuta};

        //Cursor c = db.rawQuery(query, args);
        Cursor c = db.query(TablaPasosRuta.TABLE_NAME,campos,"`"+TablaPasosRuta.FIELD_ID_RUTA+ "`=?",args,null,null,null);

        if(c.moveToFirst())
        {
            do {
                Point point = new Point();
                point.set(c.getInt(0), c.getInt(1));
                puntos.add(point);
                System.out.println(point);
            }while(c.moveToNext());
        }
        return puntos;
    }

    public Ruta cargarPuntosARuta(Ruta ruta){
        ruta.set_puntos(getPuntosRuta(ruta.get_ID()));
        return ruta;
    }



    public int getUltimoIdTablaRuta(){

        String query = "select MAX(`id`) as max_id from " + TablaRuta.TABLE_NAME;
        Cursor cursor = db.rawQuery(query,null);
        int id = 0;
        if(cursor.moveToFirst())
        {
            do{
                id = cursor.getInt(0);
            }while(cursor.moveToNext());
        }
        System.out.println("ID Nueva: " + id);
        return id;
    }


}
