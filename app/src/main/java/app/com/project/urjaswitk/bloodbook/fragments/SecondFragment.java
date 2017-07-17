package app.com.project.urjaswitk.bloodbook.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.adapters.RequestsCursorAdapter;


public class SecondFragment extends Fragment {

    TextView t, mEmptyStateTextView;
    String mNewText;
    int fragmentIdentifier=4;
    CursorAdapter mRAdapter= null;
    ListView bloodView;

    public void setFragmentIdentifier(int fragmentIdentifier) {
        this.fragmentIdentifier = fragmentIdentifier;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.requests_fragment,
                container, false);
        Log.e("TAG ACTIVITY", "fifo second on create view");



        t = (TextView) v.findViewById(R.id.tv);
        bloodView= (ListView) v.findViewById(R.id.lv);
        mEmptyStateTextView= (TextView) v.findViewById(R.id.empty_view);
        return v;
    }

    private void switchAsPerRequirement() {
        switch (fragmentIdentifier) {
            case 2://received
               // bloodView.setAdapter(mRAdapter);
                MainActivity.getInstance().startRequestCursorLoader(3);
                break;
            case 3://hospitals
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.e("TAG ACTIVITY", "fifo second on activity create");

        // If there is new text or color assigned, set em
        if(mNewText != ""){
            t.setVisibility(View.GONE);
            t.setText(mNewText);
        }

        bloodView.setEmptyView(mEmptyStateTextView);
       // switchAsPerRequirement();
        bloodView.setAdapter(mRAdapter);

    }

    public void setText(String i)
    {
        mNewText = i;
    }
    public void setList(CursorAdapter ad){
        mRAdapter= ad;
    }

}
