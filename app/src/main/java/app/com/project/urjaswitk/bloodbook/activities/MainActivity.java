package app.com.project.urjaswitk.bloodbook.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import app.com.project.urjaswitk.bloodbook.BloodBankLoader;
import app.com.project.urjaswitk.bloodbook.HospitalLoader;
import app.com.project.urjaswitk.bloodbook.Utils;
import app.com.project.urjaswitk.bloodbook.adapters.BloodBankAdapter;
import app.com.project.urjaswitk.bloodbook.adapters.BloodCursorAdapter;
import app.com.project.urjaswitk.bloodbook.adapters.HospitalsCursorAdapter;
import app.com.project.urjaswitk.bloodbook.fragment_adapters.CentreAdapter;
import app.com.project.urjaswitk.bloodbook.adapters.RequestsCursorAdapter;
import app.com.project.urjaswitk.bloodbook.fragments.FirstFragment;
import app.com.project.urjaswitk.bloodbook.fragment_adapters.HistoryAdapter;
import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.fragment_adapters.RequestsAdapter;
import app.com.project.urjaswitk.bloodbook.fragments.SecondFragment;
import app.com.project.urjaswitk.bloodbook.models.BloodBanks;
import app.com.project.urjaswitk.bloodbook.models.Hospitals;
import app.com.project.urjaswitk.bloodbook.models.Requests;
import app.com.project.urjaswitk.bloodbook.models.UserProfile;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract;
import app.com.project.urjaswitk.bloodbook.provider.CentreDbHelper;

public class MainActivity extends BaseActivity {

    /**
     * Static members
     *
     * @param savedInstanceState
     */
    private static final String LOG_TAG = MainActivity.class.getCanonicalName();
    public static final String NULL_EMAIL = "##";
    public static String type;

    public static final String GOOGLE="googlyy";
    public static final String EMAIL="emiliya";
    private static final String blood_REQUEST_URL = "https://data.gov.in/api/datastore/resource.json?" +
            "resource_id=e16c75b6-7ee6-4ade-8e1f-2cd3043ff4c9&api-key=2575dbfedd0d64318f30910fcfe1facc";

    private static final String hospital_REQUEST_URL = "https://data.gov.in/api/datastore/resource.json?" +
            "resource_id=37670b6f-c236-49a7-8cd7-cc2dc610e32d&api-key=2575dbfedd0d64318f30910fcfe1facc";
    public static boolean visible=true;

    private static final int bloodBank_LOADER_ID = 1;
    private static final int bloodData_LOADER_ID = 2;
    private static final int REQUEST_LOADER_ID_2= 7;
    private static final int REQUEST_LOADER_ID_1= 8;
    private static final int REQUEST_LOADER_ID= 3;
    private static final int hospital_LOADER_ID = 5;
    private static final int hospital_DATA_LOADER_ID=6;


    private BloodBankAdapter mAdapter;
    public CursorAdapter mRequestAdapter, mRequestAdapter1, mRequestAdapter2;
    private TextView mEmptyStateTextView;
    private boolean firstTime=true;
    private int count= 0;
    public static boolean history= false;

    public Boolean searchSelected=false;

    public CursorAdapter todoAdapter, hospitalDataAdapter;
   // BottomNavigationView navigation
    SharedPreferences prefs = null;

    String selection;
    String[] projection, selectionArgs;

    DatabaseReference mRef;
    private ViewPager f;
    TabLayout tl;
    FirebaseAuth mAuth;

    Toolbar toolbar;
    RelativeLayout rl;
    EditText et;
    ImageView back;
    static MainActivity activityA;
    BottomNavigationView navigation;
    ListView lv;



    private BottomNavigationView.OnNavigationItemSelectedListener
            mOnNavigationItemSelectedListener
            = navigationListener();

    private BottomNavigationView
            .OnNavigationItemSelectedListener
            navigationListener() {
        return new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                (findViewById(R.id.list_view_once))
                        .setVisibility(View.GONE);
                switch (item.getItemId()) {

                    case R.id.navigation_home:
                     //   search.setVisible(false);
                        history= false;
                        visible= true;
                        tl.setVisibility(View.GONE);
                        //stopRequestsLoader();
//                        RequestsAdapter r;
////                        if (firstTime) {
//                            r = new RequestsAdapter(
//                                    getSupportFragmentManager(), 1);
//                            firstTime=false;
//                            Log.e(LOG_TAG, " main activity present");
//                        }
//                        else{
//                            stopRequestsLoader();
//                            r= new RequestsAdapter(getSupportFragmentManager(), 1);
//                            Log.e(LOG_TAG, " main activity not present");
//                        }
                        @SuppressLint("RestrictedApi") List<Fragment> e1 =
                                getSupportFragmentManager()
                                        .getFragments();
                        if(e1==null)
                            Log.e(LOG_TAG, " no fragments");
                        if (e1 != null) {
                            FirstFragment ff1 = (FirstFragment) e1.get(0);
                            ff1.setText("Requests");
                            ff1.setFragmentIdentifier(1);
                            Log.e(LOG_TAG, " in navigation");
                            if (mRequestAdapter==null)
                                Log.e(LOG_TAG, " in navigation adapter is null");
                            else Log.e(LOG_TAG, " in navigation adapter not null");
                            ff1.setList(mRequestAdapter);
                            //ff1.setListener(listener);
                        }
                        RequestsAdapter r = new RequestsAdapter(
                                getSupportFragmentManager(), 1);
                        f.setAdapter(r);
                        return true;

                    case R.id.navigation_dashboard:
                        history=true;
                   //     search.setVisible(false);
                        visible= false;

                       // stopRequestsLoader();
                        @SuppressLint("RestrictedApi") List<Fragment> e =
                                getSupportFragmentManager()
                                        .getFragments();

                        if (e != null) {
                            FirstFragment ff = (FirstFragment) e.get(0);
                            ff.setText("Donates");
                            ff.setFragmentIdentifier(2);
                            ff.setList(mRequestAdapter1);
                            ff.setListener(null);
                            if (e.size() == 2) {
                                SecondFragment sf = (SecondFragment) e.get(1);
                                sf.setList(mRequestAdapter2);
                                sf.setText("Recieved");
                                sf.setFragmentIdentifier(2);

                            }
                        }
                        tl.setVisibility(View.VISIBLE);
                        HistoryAdapter h = new HistoryAdapter(
                                getSupportFragmentManager());
                        f.setAdapter(h);
                        tl.setupWithViewPager(f);
                        return true;

                    case R.id.navigation_notifications:
                        history= false;
                        visible= false;
                      //  search.setVisible(true);
                      //  stopRequestsLoader();
                        @SuppressLint("RestrictedApi") List<Fragment> e2 =
                                getSupportFragmentManager()
                                        .getFragments();
                        if (e2 != null) {
                            FirstFragment ff = (FirstFragment) e2.get(0);
                            ff.setText("Blood Banks");
                            ff.setList(todoAdapter);
                            ff.setListener(null);
                            ff.setFragmentIdentifier(3);
                            if (e2.size() == 2) {
                                SecondFragment sf = (SecondFragment) e2.get(1);
                                sf.setText("Hospitals");
                                sf.setList(hospitalDataAdapter);
                                sf.setFragmentIdentifier(3);
                            }
                        }


                        tl.setVisibility(View.VISIBLE);

                        CentreAdapter c = new CentreAdapter(
                                getSupportFragmentManager());

                        f.setAdapter(c);

                        tl.setupWithViewPager(f);

                        return true;
                }
                return false;
            }

        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.getAllUsers(this);

        mAuth= FirebaseAuth.getInstance();

        mRef= FirebaseDatabase.getInstance().getReference().child("users");
        Log.e(LOG_TAG, "in on create "+ mRef.child("users")  );


//        mRef.child(mAuth.getCurrentUser().getUid())
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Log.e("MAIN ACTIVITY_", dataSnapshot.getValue().toString());
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                        Log.e("MAIN ACTIVITY_", dataSnapshot.getValue().toString());
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });


//        String k= mRef.push().getKey();
//        mRef.child(k).setValue("My test");

//        if (getIntent().getExtras() != null)
//        addAsNewUserIfSignedInForFirstTime(
//                UserProfile.getUserProfileFromBundle(getIntent().getExtras())
//        );

        //mRef.child("pizzzza").setValue("userId is urjaswit");
      //  mRef.push().setValue(UserProfile.getUserProfileFromBundle(getIntent().getExtras()));
//        addAsNewUserIfSignedInForFirstTime(
//                new UserProfile("hdush83whdjd",
//                        "Urjaswit", "uks@gmail.com",
//                        "7890451616", "B+"));

        setViews();

//        initializeMembers(UserProfile
//                .getUserProfileFromBundle(getIntent().getExtras()));

        Utils.loadAllRequestsFromInternetIntoProvider(
                this, mRef.getRoot().child(Utils.REQUESTS_KEY));
        Log.e(LOG_TAG, "in oncreate "+ mRef.getRoot().child(Utils.REQUESTS_KEY) );

    }

    private void setViews(){


        f = (ViewPager) findViewById(R.id.content);
        tl = (TabLayout) findViewById(R.id.scrolling_tab);
        navigation = (BottomNavigationView)
                findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(
                mOnNavigationItemSelectedListener);

        setFloatingActionBar();
        activityA = this;
        // Find the toolbar view inside the activity layout

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        rl = (RelativeLayout) findViewById(R.id.filter);
        back = (ImageView) findViewById(R.id.back);
        et = (EditText) findViewById(R.id.et);
        mEmptyStateTextView = (TextView)findViewById(R.id.empty_view);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        setSupportActionBar(toolbar);
        toolbar.setBackgroundColor(Color.parseColor("#cc0000"));
        lv = (ListView) findViewById(R.id.lv);


        initializeBloodBankAdapters();

        tl.setVisibility(View.GONE);

        prefs = getSharedPreferences("com.example.android.bloodbook", MODE_PRIVATE);
        setRequestAdapterAsStartingPoint();
//        FirstFragment ff = new FirstFragment();
//                ff.setList(mRequestAdapter);
//        if (mRequestAdapter==null)
//            Log.e(LOG_TAG, "adapter is null");
//        else Log.e(LOG_TAG, "adapter not null");
//        navigation.requestFocus();
//        navigation.setSelectedItemId(R.id.navigation_home);
       // startRequestCursorLoader(1);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        if (mRequestAdapter==null)
//            Log.e(LOG_TAG, "adapter is null");
//        else Log.e(LOG_TAG, "adapter not null");
//        navigation.requestFocus();
//        navigation.setSelectedItemId(R.id.navigation_home);
    }

//    private void initializeMembers(UserProfile user){
//        addAsNewUserIfSignedInForFirstTime(user);
//
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.profile, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        Toast.makeText(MainActivity.this, "Profile Clicked",
//                Toast.LENGTH_SHORT).show();
//        return super.onOptionsItemSelected(item);
//    }

    private void addAsNewUserIfSignedInForFirstTime(final UserProfile user){
//        Log.e(LOG_TAG, "in sign up "+ mRef
//                .child(mAuth.getCurrentUser().getUid()));
        mRef.child(mAuth.getCurrentUser().getUid()).setValue(user);
        return;
//        mRef.child(user.getUserId())
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
////                        if (dataSnapshot.hasChild(user.getUserId())) {
////                            // run some code
////                            Log.e(LOG_TAG, "in if statement");
////                        }
////                        else {
////                            Log.e(LOG_TAG, "in else statement "+ mRef.child("users"));
////                            mRef.child("users").push().setValue(user);
////                          //  mRef.child("users").child(key).setValue(user);
////                        }
//                        if (dataSnapshot.exists()) {
//                            Log.e(LOG_TAG, "in if statement");
//                        }else {
//                            Log.e(LOG_TAG, "in else statement "+ mRef.child("users"));
//                            mRef.child(user.getUserId()).setValue(user);
//                          //  mRef.child("users").child(key).setValue(user);
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                });
//        Log.e(LOG_TAG, "after sign up");
    }



    @Override
    protected void onResume() {
        super.onResume();


        finishAll();
        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            startHospitalLoader();

            if (prefs.getBoolean("firstrun", true)) {
                MainActivity.getInstance().startBloodLoader();
                // Do first run stuff here then set 'firstrun' as false
                // using the following line to edit/commit prefs

            }
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            Log.e(LOG_TAG, "Error");}
//            Update empty state with no connection error message
//            mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
//
//            mEmptyStateTextView.setText("no_internet_connection");}

//        if (mRequestAdapter==null)
//            Log.e(LOG_TAG, "adapter is null");
//        else Log.e(LOG_TAG, "adapter not null");
//        navigation.requestFocus();
//        navigation.setSelectedItemId(R.id.navigation_home);
       // danger();
    }

    private void danger(){
        if (mRequestAdapter==null)
            Log.e(LOG_TAG, "adapter is null");
        else Log.e(LOG_TAG, "adapter not null");
        navigation.requestFocus();
        navigation.setSelectedItemId(R.id.navigation_home);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile, menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case (R.id.profile):

                startActivity(
                        (new Intent(MainActivity.this, ProfileActivity.class)
                        .putExtra(UserProfile.USER_ID, mAuth.getCurrentUser().getUid())));
               // Toast.makeText(MainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                return true;
            case (R.id.action_search):
                searchSelected = true;
                if(navigation.getSelectedItemId()!=R.id.navigation_notifications)
                    navigation.setSelectedItemId(R.id.navigation_notifications);
                tl.setVisibility(View.GONE);
                toolbar.setVisibility(View.GONE);
                rl.setVisibility(View.VISIBLE);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        loader.initLoader(bloodData_LOADER_ID, null, BloodDataLoaderListener);
                        searchSelected=false;
                        startHospitalDataLoader();
                        startBloodDataLoader();
                        et.setText("");
                        rl.setVisibility(View.GONE);
                        tl.setVisibility(View.VISIBLE);
                        toolbar.setVisibility(View.VISIBLE);
                    }
                });
                return true;

            case R.id.action_logout:
                mAuth.signOut();
                startActivity(SplashActivity.makeIntent(this));
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public static MainActivity getInstance(){
        return   activityA;
    }

    public void startBloodLoader()
    {
        LoaderManager loaderManager = getLoaderManager();
        if(loaderManager.getLoader(bloodBank_LOADER_ID)!=null)
            loaderManager.restartLoader(bloodBank_LOADER_ID, null , BloodApiLoaderListener);
        else
            loaderManager.initLoader(bloodBank_LOADER_ID,null, BloodApiLoaderListener);
    }

    public void startBloodDataLoader()
    {
        LoaderManager loaderManager = getLoaderManager();
        if(loaderManager.getLoader(bloodData_LOADER_ID)!=null)
            loaderManager.restartLoader(bloodData_LOADER_ID, null , BloodDataLoaderListener);
        else
            loaderManager.initLoader(bloodData_LOADER_ID,null, BloodDataLoaderListener);
    }

    public void startHospitalLoader() {
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager.getLoader(hospital_LOADER_ID) != null)
            loaderManager.restartLoader(hospital_LOADER_ID, null, HospitalApiLoaderListener);
        else
            loaderManager.initLoader(hospital_LOADER_ID, null, HospitalApiLoaderListener);
    }

    public void startHospitalDataLoader() {
        LoaderManager loaderManager = getLoaderManager();
        if (loaderManager.getLoader(hospital_DATA_LOADER_ID) != null)
            loaderManager.restartLoader(hospital_DATA_LOADER_ID, null, HospitalDataLoaderListener);
        else
            loaderManager.initLoader(hospital_DATA_LOADER_ID, null, HospitalDataLoaderListener);
    }


    public void startRequestCursorLoader(int choice)
    {

        Log.e("%%%%%%%%%%%%  ",  String.valueOf(choice));


        LoaderManager loaderManager = getLoaderManager();
        if (choice==1){
//            projection= new String[]{
//                    CentreContract.RequestEntry._ID,
//                    Requests.Columns.COLUMN_SENT,
//                    Requests.Columns.COLUMN_REQID,
//                    Requests.Columns.COLUMN_RECV,
//                    Requests.Columns.COLUMN_MSG,
//                    Requests.Columns.COLUMN_TIME
//            };
            projection= null;
            selection= null;
            selection= null;
            if(loaderManager.getLoader(REQUEST_LOADER_ID)!=null)
                loaderManager.restartLoader(REQUEST_LOADER_ID,
                        null , requestsLoaderListener);
            else
                loaderManager.initLoader(REQUEST_LOADER_ID,
                        null, requestsLoaderListener);
//            selection= Requests.Columns.COLUMN_RECV + "!=?";
//            selectionArgs= new String[]{mAuth.getCurrentUser().getUid()};
        }else if (choice==2){
            projection= null;
            selection= Requests.Columns.COLUMN_RECV + "=?";
            selectionArgs= new String[]{mAuth.getCurrentUser().getUid()};
            if(loaderManager.getLoader(REQUEST_LOADER_ID_1)!=null)
                loaderManager.restartLoader(REQUEST_LOADER_ID_1,
                        null , requestsLoaderListener1);
            else
                loaderManager.initLoader(REQUEST_LOADER_ID_1,
                        null, requestsLoaderListener1);
        }else if (choice==3){
            projection= null;
            selection= Requests.Columns.COLUMN_SENT + "=?";
            selectionArgs= new String[]{mAuth.getCurrentUser().getUid()};
            if(loaderManager.getLoader(REQUEST_LOADER_ID_2)!=null)
                loaderManager.restartLoader(REQUEST_LOADER_ID_2,
                        null , requestsLoaderListener2);
            else
                loaderManager.initLoader(REQUEST_LOADER_ID_2,
                        null, requestsLoaderListener2);
        }


//
//        if(loaderManager.getLoader(REQUEST_LOADER_ID)!=null)
//            loaderManager.restartLoader(REQUEST_LOADER_ID,
//                    null , requestsLoaderListener);
//        else
//            loaderManager.initLoader(REQUEST_LOADER_ID,
//                    null, requestsLoaderListener);
    }

//    public void stopRequestsLoader(){
//        if (getLoaderManager().getLoader(REQUEST_LOADER_ID)!= null){
//            getLoaderManager().destroyLoader(REQUEST_LOADER_ID);
//        }
//    }


    private LoaderManager.LoaderCallbacks<List<BloodBanks>> BloodApiLoaderListener
            = new LoaderManager.LoaderCallbacks<List<BloodBanks>>() {

        @Override
        public Loader<List<BloodBanks>> onCreateLoader(int i, Bundle bundle) {
            // Hide loading indicator because the data has been loaded
//            View loadingIndicator = findViewById(R.id.loading_indicator);
//            loadingIndicator.setVisibility(View.VISIBLE);

            Uri baseUri = Uri.parse(blood_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            getContentResolver().delete(CentreContract.bloodEntry.CONTENT_URI, null, null);

            return new BloodBankLoader(MainActivity.this, uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<BloodBanks>> loader, List<BloodBanks> blood) {

            // Hide loading indicator because the data has been loaded
//            View loadingIndicator = findViewById(R.id.loading_indicator);
//            loadingIndicator.setVisibility(View.GONE);

            mAdapter.clear();

            startBloodDataLoader();

            //                mAdapter.addAll(blood);
            // Set empty state text to display "No BloodBank found."


        }

        @Override
        public void onLoaderReset(Loader<List<BloodBanks>> loader) {

            mAdapter.clear();
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> BloodDataLoaderListener
            = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = {CentreContract.bloodEntry._ID,
                    CentreContract.bloodEntry.COLUMN_BANK_NAME,
                    CentreContract.bloodEntry.COLUMN_BANK_ADDRESS,
                    CentreContract.bloodEntry.COLUMN_BANK_CITY,
                    CentreContract.bloodEntry.COLUMN_BANK_DISTRICT,
                    CentreContract.bloodEntry.COLUMN_BANK_STATE,
                    CentreContract.bloodEntry.COLUMN_BANK_PIN,
                    CentreContract.bloodEntry.COLUMN_BANK_PHONE
            };

            return new CursorLoader(MainActivity.this, CentreContract.bloodEntry.CONTENT_URI,
                    projection, null, null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            todoAdapter.swapCursor(cursor);

//            Toast.makeText(MainActivity.this, "NO" + Integer.toString(todoAdapter.getCount()), Toast.LENGTH_SHORT).show();


            if (todoAdapter.getCount() >= 2900) {
                Log.e(LOG_TAG, "Less than 2900");

                prefs.edit().putBoolean("firstrun", false).commit();
            }


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            todoAdapter.swapCursor(null);

        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> requestsLoaderListener=
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    Log.e(LOG_TAG, "in on create loader "+count );

                    return new android.content.CursorLoader(MainActivity.this,
                            CentreContract.RequestEntry.CONTENT_URI,
                            projection, null, null, null);
                }

                @Override
                public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
                    Log.e(LOG_TAG, "in on loader finished counting "+count++ );

                    mRequestAdapter.swapCursor(data);
//                    if (firstTime){
//                        if (mRequestAdapter==null)
//                            Log.e(LOG_TAG, "adapter is null");
//                        else Log.e(LOG_TAG, "adapter not null");
//                        navigation.requestFocus();
//                        navigation.setSelectedItemId(R.id.navigation_home);
////                        firstTime= false;
//                    }
                    Log.e(LOG_TAG, "in on loader finished 2"+ data.moveToFirst() );
                    if (data.moveToFirst())
                        Log.e(LOG_TAG, "data is: "+data.getString(data.getColumnIndex(Requests.Columns.COLUMN_REQID)));

                }

                @Override
                public void onLoaderReset(android.content.Loader<Cursor> loader) {
                    Log.e(LOG_TAG, "in on loader reset" );
                    mRequestAdapter.swapCursor(null);
                }
            };

    private LoaderManager.LoaderCallbacks<Cursor> requestsLoaderListener1=
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    Log.e(LOG_TAG, "in on create loader "+count );

                    return new android.content.CursorLoader(MainActivity.this,
                            CentreContract.RequestEntry.CONTENT_URI,
                            projection, selection, selectionArgs, null);
                }

                @Override
                public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
                    Log.e(LOG_TAG, "in on loader finished counting "+count++ );

                    mRequestAdapter1.swapCursor(data);
//                    if (firstTime){
//                        if (mRequestAdapter==null)
//                            Log.e(LOG_TAG, "adapter is null");
//                        else Log.e(LOG_TAG, "adapter not null");
//                        navigation.requestFocus();
//                        navigation.setSelectedItemId(R.id.navigation_home);
////                        firstTime= false;
//                    }
                    Log.e(LOG_TAG, "in on loader finished 2"+ data.moveToFirst() );
                    if (data.moveToFirst())
                        Log.e(LOG_TAG, "data is: "+data.getString(data.getColumnIndex(Requests.Columns.COLUMN_REQID)));

                }

                @Override
                public void onLoaderReset(android.content.Loader<Cursor> loader) {
                    Log.e(LOG_TAG, "in on loader reset" );
                    mRequestAdapter.swapCursor(null);
                }
            };


    private LoaderManager.LoaderCallbacks<Cursor> requestsLoaderListener2=
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                public android.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
                    Log.e(LOG_TAG, "in on create loader "+count );

                    return new android.content.CursorLoader(MainActivity.this,
                            CentreContract.RequestEntry.CONTENT_URI,
                            projection, selection, selectionArgs, null);
                }

                @Override
                public void onLoadFinished(android.content.Loader<Cursor> loader, Cursor data) {
                    Log.e(LOG_TAG, "in on loader finished counting "+count++ );

                    mRequestAdapter2.swapCursor(data);
//                    if (firstTime){
//                        if (mRequestAdapter==null)
//                            Log.e(LOG_TAG, "adapter is null");
//                        else Log.e(LOG_TAG, "adapter not null");
//                        navigation.requestFocus();
//                        navigation.setSelectedItemId(R.id.navigation_home);
////                        firstTime= false;
//                    }
                    Log.e(LOG_TAG, "in on loader finished 2"+ data.moveToFirst() );
                    if (data.moveToFirst())
                        Log.e(LOG_TAG, "data is: "+data.getString(data.getColumnIndex(Requests.Columns.COLUMN_REQID)));

                }

                @Override
                public void onLoaderReset(android.content.Loader<Cursor> loader) {
                    Log.e(LOG_TAG, "in on loader reset" );
                    mRequestAdapter.swapCursor(null);
                }
            };


    private LoaderManager.LoaderCallbacks<List<Hospitals>> HospitalApiLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Hospitals>>() {

        @Override
        public Loader<List<Hospitals>> onCreateLoader(int i, Bundle bundle) {
            // Hide loading indicator because the data has been loaded
//            View loadingIndicator = findViewById(R.id.loading_indicator);
//            loadingIndicator.setVisibility(View.VISIBLE);

            Uri baseUri = Uri.parse(hospital_REQUEST_URL);
            Uri.Builder uriBuilder = baseUri.buildUpon();
            getContentResolver().delete(CentreContract.HospitalsEntry.CONTENT_URI, null, null);

            return new HospitalLoader(MainActivity.this, uriBuilder.toString());
        }

        @Override
        public void onLoadFinished(Loader<List<Hospitals>> loader, List<Hospitals> hospital) {

            // Hide loading indicator because the data has been loaded
//            View loadingIndicator = findViewById(R.id.loading_indicator);
//            loadingIndicator.setVisibility(View.GONE);

            mAdapter.clear();

            startHospitalDataLoader();

            //                mAdapter.addAll(blood);
            // Set empty state text to display "No BloodBank found."


        }

        @Override
        public void onLoaderReset(Loader<List<Hospitals>> loader) {

            mAdapter.clear();
        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> HospitalDataLoaderListener
            = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            String[] projection = {CentreContract.HospitalsEntry._ID,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_NAME,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_ADDRESS,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_DISTRICT,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_STATE,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PIN,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PHONE,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_MOB
            };

            return new CursorLoader(MainActivity.this, CentreContract.HospitalsEntry.CONTENT_URI,
                    projection, null, null,
                    null);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

            hospitalDataAdapter.swapCursor(cursor);

      //      Toast.makeText(MainActivity.this, "NO" + Integer.toString(hospitalDataAdapter.getCount()), Toast.LENGTH_SHORT).show();


        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

            hospitalDataAdapter.swapCursor(null);

        }
    };



    public Cursor getBloodList(CharSequence constraint) {
        String[] projection = {CentreContract.bloodEntry._ID,
                CentreContract.bloodEntry.COLUMN_BANK_NAME,
                CentreContract.bloodEntry.COLUMN_BANK_ADDRESS,
                CentreContract.bloodEntry.COLUMN_BANK_CITY,
                CentreContract.bloodEntry.COLUMN_BANK_DISTRICT,
                CentreContract.bloodEntry.COLUMN_BANK_STATE,
                CentreContract.bloodEntry.COLUMN_BANK_PIN,
                CentreContract.bloodEntry.COLUMN_BANK_PHONE
        };

        if (constraint == null || constraint.length() == 0) {
            //  Return the full list

            return getContentResolver().query(CentreContract.bloodEntry.CONTENT_URI,
                    projection, null, null,
                    null);
        } else {
            String value = "%" + constraint.toString() + "%";
            return getContentResolver().query(CentreContract.bloodEntry.CONTENT_URI, projection,
                    CentreContract.bloodEntry.COLUMN_BANK_NAME + " like ? " + "OR " +
                            CentreContract.bloodEntry.COLUMN_BANK_ADDRESS + " like ? " + "OR " +
                            CentreContract.bloodEntry.COLUMN_BANK_CITY + " like ? " + "OR " +
                            CentreContract.bloodEntry.COLUMN_BANK_DISTRICT + " like ? " + "OR " +
                            CentreContract.bloodEntry.COLUMN_BANK_STATE + " like ? " + "OR " +
                            CentreContract.bloodEntry.COLUMN_BANK_PIN + " like ? ", new String[]{value, value,
                            value, value, value, value}, null, null);
        }
    }



    private void setFloatingActionBar(){
        FloatingActionButton fab = (FloatingActionButton)
                findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action",
//                        Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();

                final Dialog newReq= new Dialog(MainActivity.this);
                newReq.setCancelable(true);
                newReq.setContentView(R.layout.dialog_for_requests);
//                ((EditText)newReq.findViewById(R.id.req_name_text))
//                        .setText(getUser().getFirstName());

                (newReq.findViewById(R.id.req_ok_btn))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                final String name=
                                        ((EditText)newReq.findViewById(R.id.req_name_text))
                                        .getText().toString().trim();
                                EditText e= (EditText)newReq.findViewById(R.id.req_date_text);
                                                e.getText().toString().trim();


                                String value= e.getText().toString().trim();
                                Date date = null;
                                try {
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                    date = sdf.parse(value);
                                    if (!value.equals(sdf.format(date))) {
                                        e.setError("Invalid format enter as dd/MM/yyyy");
                                        return;
                                    }
                                } catch (ParseException ex) {
                                    ex.printStackTrace();
                                }



                                final String blood=
                                        ((EditText)newReq.findViewById(R.id.req_group_text))
                                                .getText().toString().trim();
//                                final AsyncTask requestTask= new AsyncTask<Void, Void, Void>(){
//
//                                    @Override
//                                    protected Void doInBackground(Void... voids) {
////                        Utils.uploadNewRequest(
////                                new Requests("",
////                                        mAuth.getCurrentUser().getUid(),
////                                        null),
////                                mRef);
//                                        Utils.uploadNewRequest(
//                                                new Requests(name,
//                                                        getUser().getUserId(),
//                                                        date, blood),
//                                                mRef.getRoot().child("requests"));
//                                        return null;
//                                    }
//
//                                };
//                                requestTask.execute();
                                Log.e("  ", mRef.child("users").toString());

                            //    Log.e(LOG_TAG, "HHHHHHHHHHHHHHHHH "+ getUser().getUserId());
                                Utils.uploadNewRequest(
                                                new Requests(name,
                                                        mAuth.getCurrentUser().getUid(),
                                                        e.getText().toString().trim(), blood),
                                                mRef.getRoot().child("requests"));


                                newReq.dismiss();
                            }
                        });
                (newReq.findViewById(R.id.req_cancel_btn))
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                newReq.dismiss();
                            }
                        });

                newReq.setTitle("New Blood Request");
                newReq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                newReq.show();

            }
        });
    }



    private void setRequestAdapterAsStartingPoint(){
        RequestsAdapter r1 = new RequestsAdapter(
                getSupportFragmentManager(), 1);
        f.setAdapter(r1);

        mRequestAdapter = new RequestsCursorAdapter(
                MainActivity.this, null);

        mRequestAdapter1 = new RequestsCursorAdapter(
                MainActivity.this, null);

        mRequestAdapter2 = new RequestsCursorAdapter(
                MainActivity.this, null);


        startRequestCursorLoader(1);

        ((ListView)findViewById(R.id.list_view_once))
                .setAdapter(mRequestAdapter);
    }

    private void initializeBloodBankAdapters(){
        // Create a new {@link ArrayAdapter} of bloodbanks
        mAdapter = new BloodBankAdapter(
                MainActivity.this, new ArrayList<BloodBanks>());

        todoAdapter = new BloodCursorAdapter(this, null);
        hospitalDataAdapter = new HospitalsCursorAdapter(this, null);


        todoAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return getBloodList(charSequence);
            }
        });
        hospitalDataAdapter.setFilterQueryProvider(new FilterQueryProvider() {
            @Override
            public Cursor runQuery(CharSequence charSequence) {
                return getHospitalList(charSequence);
            }
        });
        et.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                todoAdapter.getFilter().filter(editable.toString());
                hospitalDataAdapter.getFilter().filter(editable.toString());
            }
        });

    }

    public static UserProfile getUser(){
//        if (getIntent().getExtras() != null)
//            return UserProfile.getUserProfileFromBundle(
//                    getIntent().getExtras()
//        );

        Cursor cursor= MainActivity.getInstance()
                .getContentResolver().query(
                        CentreContract.UsersEntry.CONTENT_URI,
                        null, UserProfile.USER_ID+"=?",
                        new String[]{FirebaseAuth.getInstance()
                                .getCurrentUser().getUid().toString()},
                        null
                );
        cursor.moveToFirst();
        return UserProfile.getUserProfileFromCursor(cursor);



//       return UserProfile.getUserProfileFromFirebaseUser(
//               FirebaseAuth.getInstance().getCurrentUser());

//        return new UserProfile("gdwgdwhgwdw",
//                "Urjaswit",
//                "uks@gmail.co.in",
//                5555, "B+");

//        return new UserProfile(
//                "CHPJiDyCRqf0Z5POd5hX5GzsjD73",
//                "",
//                "toto@a.com",
//                "",
//                "",
//                ""
//        );
    }

    public Cursor getHospitalList(CharSequence constraint) {
        String[] projection = {CentreContract.HospitalsEntry._ID,
                CentreContract.HospitalsEntry.COLUMN_HOSPITAL_NAME,
                CentreContract.HospitalsEntry.COLUMN_HOSPITAL_ADDRESS,
                CentreContract.HospitalsEntry.COLUMN_HOSPITAL_DISTRICT,
                CentreContract.HospitalsEntry.COLUMN_HOSPITAL_STATE,
                CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PIN,
                CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PHONE,
                CentreContract.HospitalsEntry.COLUMN_HOSPITAL_MOB
        };

        if (constraint == null || constraint.length() == 0) {
            //  Return the full list

            return getContentResolver().query(CentreContract.HospitalsEntry.CONTENT_URI,
                    projection, null, null,
                    null);
        } else {
            String value = "%" + constraint.toString() + "%";
            return getContentResolver().query(CentreContract.HospitalsEntry.CONTENT_URI, projection,
                    CentreContract.HospitalsEntry.COLUMN_HOSPITAL_NAME + " like ? " + "OR " +
                            CentreContract.HospitalsEntry.COLUMN_HOSPITAL_ADDRESS + " like ? " + "OR " +
                            CentreContract.HospitalsEntry.COLUMN_HOSPITAL_DISTRICT + " like ? " + "OR " +
                            CentreContract.HospitalsEntry.COLUMN_HOSPITAL_STATE + " like ? " + "OR " +
                            CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PIN + " like ? "
                    , new String[]{value, value, value, value, value}, null, null);
        }
    }

    public static Intent makeIntent(Context context,
                                    UserProfile user){
        return (new Intent(context, MainActivity.class))
                .putExtras(UserProfile.getBundleFromUserProfile(user));
    }

    public static Intent makeIntent(Context c){
        return new Intent(c, MainActivity.class);
    }

    private void finishAll(){
        if (ChooserActivity.getInstance() != null)
            ChooserActivity.getInstance().finish();
        else if (EmailPasswordActivity.getInstance() != null)
            EmailPasswordActivity.getInstance().finish();
        else if (RegistrationActivity.getInstance() != null)
            RegistrationActivity.getInstance().finish();
    }

}



