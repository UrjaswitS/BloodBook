package app.com.project.urjaswitk.bloodbook.provider;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by UrJasWitK on 11-Jul-17.
 */

public class CentreContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.urjaswitk.bloodbook";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_BLOOD = "bloodbank";
    public static final String PATH_REQUEST= "request_path";
    public static final String PATH_HOSPITAL = "hospital";
    public static final String PATH_USERS= "users";

    private CentreContract()
    {}

    public static final UriMatcher URI_MATCHER =
            buildUriMatcher();

    protected static UriMatcher buildUriMatcher()
    {
        final UriMatcher matcher = new UriMatcher(
                UriMatcher.NO_MATCH);

        matcher.addURI(CONTENT_AUTHORITY,
                PATH_REQUEST,
                RequestEntry.TOKEN_LIST_TYPE);
        matcher.addURI(CONTENT_AUTHORITY,
                PATH_USERS,
                UsersEntry.TOKEN_LIST_TYPE);
        matcher.addURI(CONTENT_AUTHORITY,
                PATH_BLOOD,
                bloodEntry.TOKEN_LIST_TYPE);
        matcher.addURI(CONTENT_AUTHORITY,
                PATH_HOSPITAL,
                HospitalsEntry.TOKEN_LIST_TYPE);

        return matcher;
    }

    public static final class bloodEntry implements BaseColumns {

        public static final String TABLE_NAME = "bloodbank";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_BANK_NAME = "name";
        public static final String COLUMN_BANK_ADDRESS = "address";
        public static final String COLUMN_BANK_CITY = "city";
        public static final String COLUMN_BANK_DISTRICT = "district";
        public static final String COLUMN_BANK_STATE = "state";
        public static final String COLUMN_BANK_PIN = "pin";
        public static final String COLUMN_BANK_PHONE = "phone";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BLOOD);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/" + PATH_BLOOD;

        public static final int TOKEN_LIST_TYPE= 11111;

    }

    public static final class RequestEntry implements BaseColumns{
        public static final String TABLE_NAME= "request";

        public static final String _ID = BaseColumns._ID;

        public static final Uri CONTENT_URI=
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_REQUEST);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/" + PATH_REQUEST;
        public static final int TOKEN_LIST_TYPE= 11112;
    }

    public static final class UsersEntry implements BaseColumns{
        public static final String TABLE_NAME= "users_table";

        public static final String _ID = BaseColumns._ID;

        public static final Uri CONTENT_URI=
                Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USERS);

        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/"
                        + CONTENT_AUTHORITY + "/" + PATH_USERS;
        public static final int TOKEN_LIST_TYPE= 11115;
    }

    public static final class HospitalsEntry implements BaseColumns {

        public static final String TABLE_NAME = "hospital";

        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_HOSPITAL_NAME = "name";
        public static final String COLUMN_HOSPITAL_ADDRESS = "address";
        public static final String COLUMN_HOSPITAL_DISTRICT = "district";
        public static final String COLUMN_HOSPITAL_STATE = "state";
        public static final String COLUMN_HOSPITAL_PIN = "pin";
        public static final String COLUMN_HOSPITAL_PHONE = "phone";
        public static final String COLUMN_HOSPITAL_MOB = "mobile";

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_HOSPITAL);

        /**
         * The MIME type of the {@link #CONTENT_URI} for a list of pets.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_HOSPITAL;

        public static final int TOKEN_LIST_TYPE= 11113;

    }

}
