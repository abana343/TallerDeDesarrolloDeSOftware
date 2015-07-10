package com.example.bastian.nuevo2;



import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GaleriaActivity extends FragmentActivity {

    DemoCollectionPagerAdapter _mDemoCollectionPagerAdapter;
    ViewPager _mViewPager;
    private List<String> _lista;
    private static List<Bitmap> _imagenes;

    /** Este metodo es llamado cuando la actividad es creada */
    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_galeria);


        _lista = new ArrayList<String>();
        _imagenes = new ArrayList<Bitmap>();
        File _filepath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        String _root_sd = Environment.getExternalStorageDirectory().toString();
        File _file = new File(_filepath.getAbsolutePath()+"/Robotino");
        File _list[] = _file.listFiles();

        File _imag;
        BitmapFactory.Options _bit;
        Bitmap _bit2;
        for( int i=0; i< _list.length; i++)
        {
            _imag= null;
            _imag= new File(_filepath.getAbsolutePath()+"/Robotino",_list[i].getName());
            _bit = new BitmapFactory.Options();
            _bit2 = BitmapFactory.decodeFile(_imag.getAbsolutePath(),_bit);
            _bit2 = Bitmap.createScaledBitmap(_bit2, 200, 200, true);
            _imagenes.add(_bit2);
            _lista.add(_filepath.getAbsolutePath() + "/Robotino/" + _list[i].getName());

            Log.e("archivo = ", "" + _lista.get(i));
        }




        _mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        _mViewPager = (ViewPager) findViewById(R.id.pager);
        _mViewPager.setAdapter(_mDemoCollectionPagerAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_conectar) {
            return true;
        }
        */
        if (id == R.id.action_inicio) {
            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_galeria) {
            Intent i = new Intent(this, GaleriaActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_rutas) {
            Intent i = new Intent(this, ListarRutaActivity.class);
            startActivity(i);
        }
        if (id == R.id.action_movimiento) {
            Intent i = new Intent(this, movimiento.class);
            startActivity(i);
        }
        if (id == R.id.action_sensores) {
            Intent i = new Intent(this, SensoresActivity.class);
            startActivity(i);
        }
        /*
        if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }


    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment _fragment = new DemoObjectFragment();
            Bundle _args = new Bundle();

            // Our object is just an integer :-P
            _args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
            _fragment.setArguments(_args);

            return _fragment;
        }

        /*
        Cantidad de objetos en la lista
         */
        @Override
        public int getCount() {
            return _imagenes.size()-1;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "OBJECT " + (position + 1);
        }
    }

    public static class DemoObjectFragment extends Fragment {
        public static final String ARG_OBJECT = "object";
        //public static List<Bitmap> imagenes;
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState) {

            View _rootView = inflater.inflate(
                    R.layout.item_collection, container, false);
            Bundle args = getArguments();
            ((ImageView) _rootView.findViewById(android.R.id.text1)).setImageBitmap(
                    _imagenes.get(args.getInt(ARG_OBJECT)));
            return _rootView;
        }

        public static void setImagenes(List<Bitmap> img)
        {
            _imagenes = img;
        }
    }
}
