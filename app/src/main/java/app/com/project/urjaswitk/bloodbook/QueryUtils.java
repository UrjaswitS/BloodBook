package app.com.project.urjaswitk.bloodbook;

import android.content.ContentValues;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.models.BloodBanks;
import app.com.project.urjaswitk.bloodbook.models.Hospitals;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract;

/**
 * Created by UrJasWitK on 12-Jul-17.
 */

public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static ArrayList<BloodBanks> fetchBloodBankData(String requestUrl , ArrayList<BloodBanks> blood) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        blood = extractBloodBanks(jsonResponse, blood);

        // Return the {@link Event}
        return blood;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(100000 /* milliseconds */);
            urlConnection.setConnectTimeout(150000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link BloodBanks} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<BloodBanks> extractBloodBanks(String bloodBankJSON , ArrayList<BloodBanks> blood) {

        if (TextUtils.isEmpty(bloodBankJSON)) {
            return null;
        }

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of BloodBank objects with the corresponding data.

            JSONObject jsonObject = new JSONObject(bloodBankJSON);
            JSONArray records = jsonObject.getJSONArray("records");

            for (int i = 0; i < records.length(); i++) {
                JSONObject bloodBank = records.getJSONObject(i);
                String name = bloodBank.getString("h_name");
                String add = bloodBank.getString("address");
                String city = bloodBank.getString("city");
                String dist = bloodBank.getString("district");
                String state = bloodBank.getString("state");
                String pin = bloodBank.getString("pincode");
                String phone = bloodBank.getString("contact");

                BloodBanks b = new BloodBanks(name, add, city, dist, state, pin, phone);
                blood.add(b);


                ContentValues values = new ContentValues();

                values.put(CentreContract.bloodEntry.COLUMN_BANK_NAME, b.getName());
                values.put(CentreContract.bloodEntry.COLUMN_BANK_ADDRESS, b.getAdd());
                values.put(CentreContract.bloodEntry.COLUMN_BANK_CITY, b.getCity());
                values.put(CentreContract.bloodEntry.COLUMN_BANK_DISTRICT, b.getDistrict());
                values.put(CentreContract.bloodEntry.COLUMN_BANK_STATE, b.getState());
                values.put(CentreContract.bloodEntry.COLUMN_BANK_PIN, b.getPin());
                values.put(CentreContract.bloodEntry.COLUMN_BANK_PHONE, b.getPhone());

                Uri newUri = MainActivity.getInstance().getContentResolver().insert(CentreContract.bloodEntry.CONTENT_URI, values);

            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Blood Bank JSON results", e);
        }

        // Return the list of bloodBanks;
        return blood;
    }

    /**
     * Return a list of {@link Hospitals} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Hospitals> extractHospitals(String hospitalsJSON , ArrayList<Hospitals> hospital) {

        if (TextUtils.isEmpty(hospitalsJSON)) {
            return null;
        }

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Hospitals objects with the corresponding data.

            JSONObject jsonObject = new JSONObject(hospitalsJSON);
            JSONArray records = jsonObject.getJSONArray("records");

            for (int i = 0; i < records.length(); i++) {
                JSONObject hos = records.getJSONObject(i);
                String name = hos.getString("Hospital_Name");
                String add = hos.getString("Location");
                String dist = hos.getString("District");
                String state = hos.getString("State");
                String pin = hos.getString("Pincode");
                String phone = hos.getString("Telephone");
                String mob = hos.getString("Mobile_Number");

                Hospitals h = new Hospitals(name, add, dist, state, pin, phone, mob);
                hospital.add(h);


                ContentValues values = new ContentValues();

                values.put(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_NAME, h.getName());
                values.put(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_ADDRESS, h.getAdd());
                values.put(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_DISTRICT, h.getDistrict());
                values.put(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_STATE, h.getState());
                values.put(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PIN, h.getPin());
                values.put(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PHONE, h.getPhone());
                values.put(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_MOB, h.getMob());

                Uri newUri = MainActivity.getInstance().getContentResolver().insert(CentreContract.HospitalsEntry.CONTENT_URI, values);

            }



        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Hospital JSON results", e);
        }

        // Return the list of bloodBanks;
        return hospital;
    }

    public static ArrayList<Hospitals> fetchHospitalsData(String requestUrl , ArrayList<Hospitals> hospital) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        hospital = extractHospitals(jsonResponse, hospital);

        // Return the {@link Event}
        return hospital;
    }

}

