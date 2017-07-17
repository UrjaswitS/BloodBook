package app.com.project.urjaswitk.bloodbook.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import app.com.project.urjaswitk.bloodbook.models.Requests;
import app.com.project.urjaswitk.bloodbook.models.UserProfile;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract.HospitalsEntry;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract.bloodEntry;

/**
 * Created by UrJasWitK on 11-Jul-17.
 */

public class CentreDbHelper extends SQLiteOpenHelper {

    static final String SQL_CREATE_BLOOD_TABLE = "CREATE TABLE " + bloodEntry.TABLE_NAME +
            " (" + bloodEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + bloodEntry.COLUMN_BANK_NAME + " TEXT NOT NULL, "
            + bloodEntry.COLUMN_BANK_ADDRESS + " TEXT NOT NULL, "
            + bloodEntry.COLUMN_BANK_CITY + " TEXT NOT NULL, "
            + bloodEntry.COLUMN_BANK_DISTRICT + " TEXT NOT NULL, "
            + bloodEntry.COLUMN_BANK_STATE +" TEXT NOT NULL, "
            + bloodEntry.COLUMN_BANK_PIN + " TEXT NOT NULL, "
            + bloodEntry.COLUMN_BANK_PHONE + " TEXT NOT NULL);" ;

    static final String SQL_CREATE_REQUEST_TABLE=
            "CREATE TABLE "+ CentreContract.RequestEntry.TABLE_NAME+
            "("+ CentreContract.RequestEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + Requests.Columns.COLUMN_REQID + " TEXT NOT NULL UNIQUE, "
            + Requests.Columns.COLUMN_RECV + " TEXT, "
                    + Requests.Columns.COLUMN_RECV_NAME + " TEXT, "
                    + Requests.Columns.COLUMN_MSG + " TEXT, "
                    + Requests.Columns.COLUMN_TIME + " TEXT , "
                    + Requests.Columns.COLUMN_BLOOD + " TEXT , "
            + Requests.Columns.COLUMN_SENT+ " TEXT NOT NULL);";
    static final String SQL_CREATE_USERS_TABLE=
            "CREATE TABLE "+ CentreContract.UsersEntry.TABLE_NAME+
                    "("+ CentreContract.UsersEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + UserProfile.FIRST_NAME + " TEXT NOT NULL, "
                    + UserProfile.USER_ID + " TEXT NOT NULL, "
                    + UserProfile.CITY + " TEXT, "
                    + UserProfile.PHONE_NUMBER + " TEXT , "
                    + UserProfile.EMAIL_ADDRESS + " TEXT, "
                    + UserProfile.BLOOD_GROUP + " TEXT); ";
                   // + UserProfile.USER_ID + " TEXT NOT NULL UNIQUE); ";

    static final String SQL_CREATE_HOSPITALS_TABLE = "CREATE TABLE " + HospitalsEntry.TABLE_NAME +
            " (" + HospitalsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HospitalsEntry.COLUMN_HOSPITAL_NAME + " TEXT NOT NULL, "
            + HospitalsEntry.COLUMN_HOSPITAL_ADDRESS + " TEXT NOT NULL, "
            + HospitalsEntry.COLUMN_HOSPITAL_DISTRICT + " TEXT NOT NULL, "
            + HospitalsEntry.COLUMN_HOSPITAL_STATE + " TEXT NOT NULL, "
            + HospitalsEntry.COLUMN_HOSPITAL_PIN +" TEXT NOT NULL, "
            + HospitalsEntry.COLUMN_HOSPITAL_PHONE + " TEXT NOT NULL, "
            + HospitalsEntry.COLUMN_HOSPITAL_MOB + " TEXT NOT NULL);" ;


    public static final String LOG_TAG = CentreDbHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;

    public CentreDbHelper(Context context, String DATABASE_NAME) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.v(LOG_TAG,SQL_CREATE_BLOOD_TABLE);
        Log.e("in helper", "in here "+ CentreContract.RequestEntry._ID);
        sqLiteDatabase.execSQL(SQL_CREATE_BLOOD_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REQUEST_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_HOSPITALS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
