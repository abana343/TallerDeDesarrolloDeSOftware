package com.example.bastian.nuevo2.DB;

/**
 * Created by Javier Aros on 07-06-2015.
 */
public class TablaRuta {
    public static final String TABLE_NAME = "ruta";
    public static final String FIELD_ID = "id";
    public static final String FIELD_ESCALA = "_escala";
    public static final String FIELD_NOMBRE_RUTA = "nombreRuta";
    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "(" +
            FIELD_ID + " integer primary key AUTOINCREMENT not null ," +
            FIELD_ESCALA + " integer not null,"+
            FIELD_NOMBRE_RUTA + " varchar(32) not null);";


}
