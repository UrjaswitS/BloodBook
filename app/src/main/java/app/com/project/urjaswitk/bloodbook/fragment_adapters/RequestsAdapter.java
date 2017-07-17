package app.com.project.urjaswitk.bloodbook.fragment_adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.fragments.FirstFragment;

/**
 * Created by wasif on 02-07-2017.
 */

public class RequestsAdapter extends FragmentPagerAdapter {

    public RequestsAdapter(FragmentManager f, int ch){
        super(f);
        MainActivity.getInstance().startRequestCursorLoader(ch);
    }
//    public RequestsAdapter(FragmentManager f){
//        super(f);
//    }

    @Override
    public Fragment getItem(int position) {
        FirstFragment f = new FirstFragment();
        f.setFragmentIdentifier(1);
        f.setText("Requests");
        return f;
    }

    @Override
    public int getCount() {
        return 1;
    }
}
