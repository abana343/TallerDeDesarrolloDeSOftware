package com.example.bastian.nuevo2;



import com.example.bastian.nuevo2.DB.BaseDatoRuta;

/**
 * Created by Javier Aros on 14-05-2015.
 * Enviar objeto entre diferentes ativity
 */
class Comunicador {
    private static Object objeto = null;



    private static BaseDatoRuta BASE_DATO_RUTA = null;

    private static String IP_WEB_SERVICE = "172.17.50.211";
    private static String IP_ROBOTINO = "172.26.201.3";
    public static final String NAMESPACE = "http://services.ws.rws/";
    public static String PUERTO = "8080";

    public static String getPUERTO() {
        return PUERTO;
    }

    public static void setPUERTO(String PUERTO) {
        Comunicador.PUERTO = PUERTO;
    }

    public static boolean CAMARA;
    private static int MOVIMIENTO;

    private static String[] SENSORES;


    private static boolean ESTADO_SERVICIO = false;//defecto   false

    public static boolean isESTADO_SERVICIO() {
        return ESTADO_SERVICIO;
    }

    public static void setESTADO_SERVICIO(boolean ESTADO_SERVICIO) {
        Comunicador.ESTADO_SERVICIO = ESTADO_SERVICIO;
    }


    public static void setIP_ROBOTINO(String IP_ROBOTINO) {
        Comunicador.IP_ROBOTINO = IP_ROBOTINO;
    }

    public static String getIP_WEB_SERVICE() {
        return IP_WEB_SERVICE;
    }

    public static void setIP_WEB_SERVICE(String IP_WEB_SERVICE) {
        Comunicador.IP_WEB_SERVICE = IP_WEB_SERVICE;
    }

    public static void setObjeto(Object newObjeto) {
        objeto = newObjeto;
    }

    public static Object getObjeto() {
        return objeto;
    }



    public static void setMOVIMIENTO(int mov)
    {
        MOVIMIENTO =mov;
    }
    public static int getMOVIMIENTO()
    {
        return MOVIMIENTO;
    }

    public static void setCAMARA(boolean cam)
    {
        CAMARA =cam;
    }

    public static boolean getCAMARA()
    {
        return CAMARA;
    }




    public static BaseDatoRuta getBASE_DATO_RUTA() {
        return BASE_DATO_RUTA;
    }

    public static void setBASE_DATO_RUTA(BaseDatoRuta BASE_DATO_RUTA) {
        Comunicador.BASE_DATO_RUTA = BASE_DATO_RUTA;
    }



}