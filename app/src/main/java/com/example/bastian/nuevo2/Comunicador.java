package com.example.bastian.nuevo2;

/**
 * Created by Javier Aros on 14-05-2015.
 * Enviar objeto entre diferentes ativity
 */
class Comunicador {
    private static Object objeto = null;

    public static void setObjeto(Object newObjeto) {
        objeto = newObjeto;
    }

    public static Object getObjeto() {
        return objeto;
    }
}