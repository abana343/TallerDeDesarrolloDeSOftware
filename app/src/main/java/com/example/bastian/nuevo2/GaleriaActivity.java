package com.example.bastian.nuevo2;



import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GaleriaActivity extends FragmentActivity {

    DemoCollectionPagerAdapter mDemoCollectionPagerAdapter;
    ViewPager mViewPager;
    private List<String> myList;
    private static List<Bitmap> imagenes;

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
            bit2 = Bitmap.createScaledBitmap(bit2, 200, 200, true);
            imagenes.add(bit2);
            myList.add( filepath.getAbsolutePath()+"/Robotino/"+list[i].getName() );

            Log.e("archivo = ", "" + myList.get(i));
        }




        mDemoCollectionPagerAdapter =
                new DemoCollectionPagerAdapter(
                        getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mDemoCollectionPagerAdapter);

    }


    public class DemoCollectionPagerAdapter extends FragmentStatePagerAdapter {
        public DemoCollectionPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Fragment fragment = new DemoObjectFragment();
            Bundle args = new Bundle();

            // Our object is just an integer :-P
            args.putInt(DemoObjectFragment.ARG_OBJECT, i + 1);
            fragment.setArguments(args);

            return fragment;
        }

        /*
        Cantidad de objetos en la lista
         */
        @Override
        public int getCount() {
            return imagenes.size()-1;
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
            // The last two arguments ensure LayoutParams are inflated
            // properly.
            //imagenes=;
            View rootView = inflater.inflate(
                    R.layout.item_collection, container, false);
            Bundle args = getArguments();
            ((ImageView) rootView.findViewById(android.R.id.text1)).setImageBitmap(
                    imagenes.get(args.getInt(ARG_OBJECT)));
            return rootView;
        }

        public static void setImagenes(List<Bitmap> img)
        {
            imagenes= img;
        }
    }
}
