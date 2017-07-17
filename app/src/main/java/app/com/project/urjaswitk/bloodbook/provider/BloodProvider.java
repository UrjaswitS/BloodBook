package app.com.project.urjaswitk.bloodbook.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.util.Log;

import app.com.project.urjaswitk.bloodbook.activities.MainActivity;

public class BloodProvider extends ContentProvider {

    private static final UriMatcher mUriMatcher =
            CentreContract.URI_MATCHER;
    public static final String LOG_TAG = BloodProvider.class.getSimpleName();

    private CentreDbHelper mDbHelper;

    public BloodProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsDeleted=0;
        getContext().getContentResolver().notifyChange(uri, null);

        switch (mUriMatcher.match(uri)) {

            case CentreContract.bloodEntry.TOKEN_LIST_TYPE:
                rowsDeleted = database.delete(CentreContract.bloodEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;

            case CentreContract.RequestEntry.TOKEN_LIST_TYPE:
                rowsDeleted= database.delete(CentreContract.RequestEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;
            case CentreContract.UsersEntry.TOKEN_LIST_TYPE:
                rowsDeleted= database.delete(CentreContract.UsersEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;

            case CentreContract.HospitalsEntry.TOKEN_LIST_TYPE:
                rowsDeleted = database.delete(CentreContract.HospitalsEntry.TABLE_NAME,
                        selection, selectionArgs);
                break;

            default: throw new
                    UnsupportedOperationException("Not yet implemented");
        }
        if (rowsDeleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        switch (mUriMatcher.match(uri)) {

            case CentreContract.bloodEntry.TOKEN_LIST_TYPE:
               return CentreContract.bloodEntry.CONTENT_LIST_TYPE;

            case CentreContract.RequestEntry.TOKEN_LIST_TYPE:
                return CentreContract.RequestEntry.CONTENT_LIST_TYPE;

            case CentreContract.HospitalsEntry.TOKEN_LIST_TYPE:
                return CentreContract.HospitalsEntry.CONTENT_LIST_TYPE;

            default: throw new
                    UnsupportedOperationException("Not yet implemented");
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values){

        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        long id=-1;
        switch (mUriMatcher.match(uri)) {

            case CentreContract.bloodEntry.TOKEN_LIST_TYPE:
                id = database.insert(CentreContract.bloodEntry.TABLE_NAME,
                        null, values);
                break;

            case CentreContract.RequestEntry.TOKEN_LIST_TYPE:
                try {
                    id= database.insert(CentreContract.RequestEntry.TABLE_NAME,
                            null, values);
                }catch (SQLiteException e){
                    Log.e("provider","do nothing");
                }
                Log.e(LOG_TAG, "row inserted ");
                break;

            case CentreContract.HospitalsEntry.TOKEN_LIST_TYPE:
                id = database.insert(CentreContract.HospitalsEntry.TABLE_NAME,
                        null, values);
                break;

            case CentreContract.UsersEntry.TOKEN_LIST_TYPE:
                try {
                    id= database.insert(CentreContract.UsersEntry.TABLE_NAME,
                            null, values);
                }catch (SQLiteException e){
                    Log.e("provider","do nothing");
                }
                break;

            default: throw new
                    UnsupportedOperationException("Not yet implemented");
        }
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }

        //getContext().getContentResolver().notifyChange(uri, null);
        if(!MainActivity.getInstance().searchSelected) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new CentreDbHelper(
                getContext(), "bloodbank.db");
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();
        Cursor cursor=null;
        switch (mUriMatcher.match(uri)) {

            case CentreContract.bloodEntry.TOKEN_LIST_TYPE:
                cursor = database.query(CentreContract.bloodEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case CentreContract.RequestEntry.TOKEN_LIST_TYPE:
                cursor= database.query(CentreContract.RequestEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case CentreContract.UsersEntry.TOKEN_LIST_TYPE:
                cursor= database.query(CentreContract.UsersEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            case CentreContract.HospitalsEntry.TOKEN_LIST_TYPE:
                cursor = database.query(CentreContract.HospitalsEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;

            default: throw new
                    UnsupportedOperationException("Not yet implemented");
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        int rowsUpdated=0;
        getContext().getContentResolver().notifyChange(uri, null);

        switch (mUriMatcher.match(uri)) {

            case CentreContract.bloodEntry.TOKEN_LIST_TYPE:
                rowsUpdated = database.update(CentreContract.bloodEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;

            case CentreContract.RequestEntry.TOKEN_LIST_TYPE:
                rowsUpdated= database.update(CentreContract.RequestEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;
            case CentreContract.UsersEntry.TOKEN_LIST_TYPE:
                rowsUpdated= database.update(CentreContract.UsersEntry.TABLE_NAME,
                        values, selection, selectionArgs);
                break;

            default: throw new
                    UnsupportedOperationException("Not yet implemented");
        }
        if (rowsUpdated != 0)
            getContext().getContentResolver().notifyChange(uri, null);

        return rowsUpdated;
    }
}
