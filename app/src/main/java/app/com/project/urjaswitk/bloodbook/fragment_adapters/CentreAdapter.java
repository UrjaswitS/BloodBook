package app.com.project.urjaswitk.bloodbook.fragment_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.fragments.FirstFragment;
import app.com.project.urjaswitk.bloodbook.fragments.SecondFragment;

/**
 * Created by wasif on 03-07-2017.
 */

public class CentreAdapter extends FragmentPagerAdapter {

    public CentreAdapter(FragmentManager f){super(f);
        MainActivity.getInstance().startBloodDataLoader();
        MainActivity.getInstance().startHospitalDataLoader();
    }

    @Override
    public Fragment getItem(int position) {
        if(position==0){
            return new FirstFragment();
        }
        else{
            SecondFragment sf = new SecondFragment();
            sf.setText("Hospitals");
            sf.setList(MainActivity.getInstance().hospitalDataAdapter);
            return sf;}
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position==0)
        {
            return "Blood Banks";
        }
        else
            return "Hospitals";
    }

    @Override
    public int getCount() {
        return 2;
    }
}
