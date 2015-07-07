package com.example.bastian.nuevo2;

import android.content.Context;
import android.telephony.TelephonyManager;

import com.example.bastian.nuevo2.DB.BaseDatoRuta;

/**
 * Created by Javier Aros on 14-05-2015.
 * Enviar objeto entre diferentes ativity
 */
class Comunicador {
    private static Object objeto = null;



    private static BaseDatoRuta baseDatoRuta = null;

    private static String IpWebService = "172.17.50.211";
    private static String IpRobotino = "172.26.201.3";
    public static final String NAMESPACE = "http://services.ws.rws/";
    public static String PUERTO = "8080";

    public static String getPUERTO() {
        return PUERTO;
    }

    public static void setPUERTO(String PUERTO) {
        Comunicador.PUERTO = PUERTO;
    }

    public static boolean camara;
    private static int movimiento;

    private static String[] sensores;


    private static boolean ESTADO_SERVICIO = false;//defecto   false

    public static boolean isESTADO_SERVICIO() {
        return ESTADO_SERVICIO;
    }

    public static void setESTADO_SERVICIO(boolean ESTADO_SERVICIO) {
        Comunicador.ESTADO_SERVICIO = ESTADO_SERVICIO;
    }

    public static String getIpRobotino() {
        return IpRobotino;
    }

    public static void setIpRobotino(String ipRobotino) {
        IpRobotino = ipRobotino;
    }

    public static String getIpWebService() {
        return IpWebService;
    }

    public static void setIpWebService(String ipWebService) {
        IpWebService = ipWebService;
    }

    public static void setObjeto(Object newObjeto) {
        objeto = newObjeto;
    }

    public static Object getObjeto() {
        return objeto;
    }

    public static String getNamespace(){
        return NAMESPACE;
    }

    public static void setMovimiento(int mov)
    {
        movimiento=mov;
    }
    public static int getMovimiento()
    {
        return movimiento;
    }

    public static void setCamara(boolean cam)
    {
        camara=cam;
    }

    public static boolean getCamara()
    {
        return camara;
    }



    public static String getIMEI(Context context){

        TelephonyManager mngr = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
        String imei = mngr.getDeviceId();
        return imei;

    }


    public static BaseDatoRuta getBaseDatoRuta() {
        return baseDatoRuta;
    }

    public static void setBaseDatoRuta(BaseDatoRuta baseDatoRuta) {
        Comunicador.baseDatoRuta = baseDatoRuta;
    }


    public static void setSensores(String [] sen)
    {
        sensores = sen;
    }

    public static String[] getSensores()
    {
        return sensores;
    }
}