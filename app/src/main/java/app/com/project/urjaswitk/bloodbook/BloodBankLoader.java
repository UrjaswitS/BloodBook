package app.com.project.urjaswitk.bloodbook;

import android.content.Context;
import android.net.Uri;
import android.content.AsyncTaskLoader;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import app.com.project.urjaswitk.bloodbook.models.BloodBanks;

/**
 * Created by UrJasWitK on 12-Jul-17.
 */


public class BloodBankLoader extends AsyncTaskLoader<List<BloodBanks>> {

    /** Tag for log messages */
    private static final String LOG_TAG = BloodBankLoader.class.getName();

    /** Query URL */
    private String mUrl;

    ArrayList<BloodBanks> blood = new ArrayList<BloodBanks>();

    public BloodBankLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<BloodBanks> loadInBackground() {
        if (mUrl == null) {
            Log.e(LOG_TAG, "MURL is null");
            return null;
        }

        // Perform the network request, parse the response, and extract a list of earthquakes.
        int offset=1;
        for(int i=0;i<=29;i++){
            Log.e(LOG_TAG, "MURL is not null");

            blood = QueryUtils.fetchBloodBankData(mUrl, blood);
            if(blood!=null){
                mUrl = "https://data.gov.in/api/datastore/resource.json?resource_id=e16c75b6-7ee6-4ade-8e1f-2cd3043ff4c9" +
                        "&api-key=2575dbfedd0d64318f30910fcfe1facc";
                Uri baseUri = Uri.parse(mUrl);
                Uri.Builder uriBuilder = baseUri.buildUpon();

                uriBuilder.appendQueryParameter("offset", Integer.toString(offset));
                mUrl = uriBuilder.toString();
                offset++;}
        }
        return blood;
    }
}
