package com.example.bastian.nuevo2;

/**
 * Created by Javier Aros on 14-05-2015.
 * Enviar objeto entre diferentes ativity
 */
class Comunicador {
    private static Object objeto = null;



    private static String IpWebService = "172.17.50.211";
    private static String IpRobotino = "172.26.201.3";
    public static final String NAMESPACE = "http://services.ws.rws/";



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


}