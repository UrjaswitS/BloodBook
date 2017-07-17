package app.com.project.urjaswitk.bloodbook.fragment_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.fragments.FirstFragment;
import app.com.project.urjaswitk.bloodbook.fragments.SecondFragment;

/**
 * Created by wasif on 02-07-2017.
 */

public class HistoryAdapter extends FragmentPagerAdapter {

    FragmentManager fm;
    int choice=2;


    public HistoryAdapter(FragmentManager f){
        super(f);
        fm=f;
        MainActivity.getInstance().startRequestCursorLoader(2);
        MainActivity.getInstance().startRequestCursorLoader(3);
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            FirstFragment ff = new FirstFragment();
            ff.setText("Donate");
            Log.e("TAG ACTIVITY", "FIRST");
            ff.setFragmentIdentifier(2);
            choice=2;
            return ff;
        }
        else{
            SecondFragment sf = new SecondFragment();
            sf.setText("Recieved");
            Log.e("TAG ACTIVITY", "SECOND");
            sf.setFragmentIdentifier(2);
            sf.setList(MainActivity.getInstance().mRequestAdapter2);
            choice= 3;
            return sf;}
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
        {
            return "Donated";
        }
        else
            return "Requests";
    }


}
