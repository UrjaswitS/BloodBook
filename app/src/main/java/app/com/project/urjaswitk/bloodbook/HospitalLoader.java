package app.com.project.urjaswitk.bloodbook;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.com.project.urjaswitk.bloodbook.models.Hospitals;

/**
 * Created by UrJasWitK on 14-Jul-17.
 */


public class HospitalLoader extends AsyncTaskLoader<List<Hospitals>> {

    /** Tag for log messages */
    private static final String LOG_TAG = HospitalLoader.class.getName();

    /** Query URL */
    private String mUrl;

    ArrayList<Hospitals> hospital = new ArrayList<Hospitals>();

    public HospitalLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<Hospitals> loadInBackground() {
        if (mUrl == null) {
            Log.e(LOG_TAG, "Hospital MURL is null");
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        int offset=1;
        for(int i=0;i<=302;i++){
            Log.e(LOG_TAG, "Hospital MURL is not null");

            hospital = QueryUtils.fetchHospitalsData(mUrl, hospital);
            if(hospital!=null){
                mUrl = "https://data.gov.in/api/datastore/resource.json?" +
                        "resource_id=37670b6f-c236-49a7-8cd7-cc2dc610e32d&api-key=2575dbfedd0d64318f30910fcfe1facc";
                Uri baseUri = Uri.parse(mUrl);
                Uri.Builder uriBuilder = baseUri.buildUpon();

                uriBuilder.appendQueryParameter("offset", Integer.toString(offset));
                mUrl = uriBuilder.toString();
                offset++;}
        }
        return hospital;
    }
}
