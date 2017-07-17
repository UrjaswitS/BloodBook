package app.com.project.urjaswitk.bloodbook.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.Utils;
import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.adapters.BloodBankAdapter;
import app.com.project.urjaswitk.bloodbook.adapters.BloodCursorAdapter;
import app.com.project.urjaswitk.bloodbook.adapters.RequestsCursorAdapter;
import app.com.project.urjaswitk.bloodbook.models.Requests;


public class FirstFragment extends Fragment {

    TextView t, mEmptyStateTextView ;
    ListView bloodView;

    CursorAdapter mAdapter ;
    BloodBankAdapter Adapter = null;
    RequestsCursorAdapter mRAdapter= null;

    AdapterView.OnItemClickListener listener;

    String mNewText="";
    int fragmentIdentifier=4;

    public void setFragmentIdentifier(int fragmentIdentifier) {
        this.fragmentIdentifier = fragmentIdentifier;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.requests_fragment,
                container, false);

        t = (TextView)v.findViewById(R.id.tv);

        bloodView= (ListView) v.findViewById(R.id.lv);
       // bloodView.setOnItemClickListener(listener);

        mEmptyStateTextView= (TextView) v.findViewById(R.id.empty_view);

        Log.e("TAG ACTIVITY", "fifo first on create view");

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAG ACTIVITY", "fifo first on activity create");

        // If there is new text or color assigned, set em
        if(!mNewText .equals("")){
            t.setVisibility(View.GONE);
            t.setText(mNewText);
        }

        bloodView.setEmptyView(mEmptyStateTextView);
        Log.e("In first fragment", "check 2 - "+fragmentIdentifier+ "  "+ mNewText);
  //      switchAsPerRequirement(mAdapter.getCursor());
            // Set the adapter on the {@link ListView}
            // so the list can be populated in the user interface
        if (mAdapter==null) {
            Log.e("first fragment", "null here ");
            bloodView.setAdapter(Adapter);
        }
        else {
            Log.e("first fragment", " not null here ");
            bloodView.setAdapter(mAdapter);
        }

      //  switchAsPerRequirement(mAdapter.getCursor());

    }

    private void switchAsPerRequirement(final Cursor cursor) {
        switch (fragmentIdentifier) {
            case 1://requests all
                Log.e("In first fragment", "checkpoint ");
                //bloodView.setOnItemClickListener();
                break;
            case 2://history
              //  MainActivity.getInstance().startRequestCursorLoader(2);



                //bloodView.setAdapter(mRAdapter);
//                (bloodView.findViewById(R.id.accept))
//                        .setVisibility(View.INVISIBLE);
                break;
            case 3://blood bank
//                if(mAdapter != null)
//                {
//                    // Set the adapter on the {@link ListView}
//                    // so the list can be populated in the user interface
//                    bloodView.setAdapter(mAdapter);
//                }
//                else
//                    bloodView.setAdapter(Adapter);
        }
    }
    public void setText(String i)
    {
        mNewText = i;
    }

    public void setList(CursorAdapter ad)
    {
        mAdapter=ad;
    }

    public void setList(BloodBankAdapter ad){
        Adapter = ad;
    }
//    public void setList(RequestsCursorAdapter ad){
//        mRAdapter= ad;
//    }

    public void setListener(AdapterView.OnItemClickListener ab){
        listener= ab;
    }

}
