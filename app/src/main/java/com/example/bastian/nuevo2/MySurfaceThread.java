package com.example.bastian.nuevo2;

import android.graphics.Canvas;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Created by Bastian on 28-04-2015.
 */
public class MySurfaceThread extends Thread
{
    private SurfaceHolder _sh;
    private SurfaceView _view;
    private boolean _run;

    //el constructor recive los dos tipos de surface
    public MySurfaceThread(SurfaceHolder sh, SurfaceView view)
    {
        this._sh = sh;
        this._view =view;
        this._run =false;
    }

    //se utiliza para establecer si esta corriendo o no
    public void setRuning(boolean run)
    {
        this._run =run;
    }

    public void run()
    {
        //se instancia el canvas
        Canvas canvas;
        while(_run)
        {
            canvas = null;
            try
            {
                //se define nulo el area de pintado
                canvas = _sh.lockCanvas(null);
                //se utiliza el Synchronized  para asegurarce que no se este usando el objeto en ningun otro thread
                synchronized (_sh)
                {
                    //se ejecuta el metodo para que dibuje pasandole en canvas
                    this._view.draw(canvas);
                }
            }
            finally
            {
                if(canvas != null)
                {
                    //se libera el canvas
                    _sh.unlockCanvasAndPost(canvas);
                }
            }
        }

    }

}
