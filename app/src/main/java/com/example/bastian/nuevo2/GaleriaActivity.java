package com.example.bastian.nuevo2;


import android.app.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GaleriaActivity extends Activity {

    /**
     * Constantes para identificar la acci—n realizada (tomar una fotograf’a
     * o bien seleccionarla de la galer’a)
     */
    private static int TAKE_PICTURE = 1;
    private static int SELECT_PICTURE = 2;

    /**
     * Variable que define el nombre para el archivo donde escribiremos
     * la fotograf’a de tama–o completo al tomarla.
     */
    private String name = "";
    private ArrayList<String> fotos;

    private TextView texto ;

    private List<String> myList;
    private List<Bitmap> imagenes;

    /** Este mŽtodo es llamado cuando la actividad es creada */
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);




        myList = new ArrayList<String>();
        imagenes= new ArrayList<Bitmap>();
        File filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String root_sd = Environment.getExternalStorageDirectory().toString();
        File file = new File(filepath.getAbsolutePath()+"/Robotino");
        File list[] = file.listFiles();

        File imag;
        BitmapFactory.Options bit;
        Bitmap bit2;
        for( int i=0; i< list.length; i++)
        {
            imag= null;
            imag= new File(filepath.getAbsolutePath()+"/Robotino",list[i].getName());
            bit = new BitmapFactory.Options();
            bit2 = BitmapFactory.decodeFile(imag.getAbsolutePath(),bit);
            bit2 = Bitmap.createScaledBitmap(bit2, 20, 20, true);
            imagenes.add(bit2);
            myList.add( filepath.getAbsolutePath()+"/Robotino/"+list[i].getName() );

            Log.e("archivo = ", "" + myList.get(i));
        }


    }

    /**
     * Funci—n que se ejecuta cuando concluye el intent en el que se solicita una imagen
     * ya sea de la c‡mara o de la galer’a
     */
    @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * Se revisa si la imagen viene de la c‡mara (TAKE_PICTURE) o de la galer’a (SELECT_PICTURE)
         */
        if (requestCode == TAKE_PICTURE) {
            /**
             * Si se reciben datos en el intent tenemos una vista previa (thumbnail)
             */
            if (data != null) {
                /**
                 * En el caso de una vista previa, obtenemos el extra ÒdataÓ del intent y
                 * lo mostramos en el ImageView
                 */
                if (data.hasExtra("data")) {
                    ImageView iv = (ImageView)findViewById(R.id.imgView);
                    iv.setImageBitmap((Bitmap) data.getParcelableExtra("data"));
                }
                /**
                 * De lo contrario es una imagen completa
                 */
            } else {
                /**
                 * A partir del nombre del archivo ya definido lo buscamos y creamos el bitmap
                 * para el ImageView
                 */
                ImageView iv = (ImageView)findViewById(R.id.imgView);
                iv.setImageBitmap(BitmapFactory.decodeFile(name));
                /**
                 * Para guardar la imagen en la galer’a, utilizamos una conexi—n a un MediaScanner
                 */
                new MediaScannerConnectionClient() {
                    private MediaScannerConnection msc = null; {
                        msc = new MediaScannerConnection(getApplicationContext(), this);
                        msc.connect();
                    }
                    public void onMediaScannerConnected() {
                        msc.scanFile(name, null);
                    }
                    public void onScanCompleted(String path, Uri uri) {
                        msc.disconnect();
                    }
                };
            }
            /**
             * Recibimos el URI de la imagen y construimos un Bitmap a partir de un stream de Bytes
             */
        } else if (requestCode == SELECT_PICTURE){
            Uri selectedImage = data.getData();
            InputStream is;
            try {
                is = getContentResolver().openInputStream(selectedImage);
                BufferedInputStream bis = new BufferedInputStream(is);
                Bitmap bitmap = BitmapFactory.decodeStream(bis);
                ImageView iv = (ImageView)findViewById(R.id.imgView);
                iv.setImageBitmap(bitmap);
              //  name= selectedImage.getPath();
              //  texto.setText(selectedImage.getPath());
            } catch (FileNotFoundException e) {}
        }
    }
}
