package com.example.bastian.nuevo2.DB;

/**
 * Created by Javier Aros on 07-06-2015.
 */
public class TablaPasosRuta {
    public static final String TABLE_NAME = "pasos_ruta";
    public static final String FIELD_ID_PASO = "idPaso";
    public static final String FIELD_ID_RUTA = "idRuta";
    public static final String FIELD_X = "X";
    public static final String FIELD_Y = "Y";
    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "(" +
            FIELD_ID_PASO + " integer primary key AUTOINCREMENT not null," +
            FIELD_ID_RUTA + " integer unsigned not null," +
            FIELD_X + " integer not null,"+
            FIELD_Y + " integer not null,"+
            "constraint fk_"+TABLE_NAME +" foreign key ("+FIELD_ID_RUTA+") references "+ TablaRuta.TABLE_NAME+ "("+TablaRuta.FIELD_ID + ")" +
            "on delete cascade on update cascade );";

}
