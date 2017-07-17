package app.com.project.urjaswitk.bloodbook.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract;

/**
 * Created by UrJasWitK on 14-Jul-17.
 */


public class HospitalsCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link HospitalsCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public HospitalsCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // TODO: Fill out this method and return the list item view (instead of null)
        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
    }

    /**
     * This method binds the hospital data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current hospital can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // TODO: Fill out this method
        // Find fields to populate in inflated template
        TextView hospitalName = (TextView) view.findViewById(R.id.name);
        TextView hospitalAddress = (TextView) view.findViewById(R.id.address);
        // Extract properties from cursor
        String name = cursor.getString(cursor.getColumnIndex(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_NAME));
        String add = (cursor.getString(cursor.getColumnIndex(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_ADDRESS)) +
                " , " + cursor.getString(cursor.getColumnIndex(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_DISTRICT)) +
                " , " + cursor.getString(cursor.getColumnIndex(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_STATE)) +
                " , " + cursor.getString(cursor.getColumnIndex(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PIN)));

        Log.e("Hospital Adapter", add);

        final String phone = cursor.getString(cursor.getColumnIndex(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_PHONE)) +
                " , " + cursor.getString(cursor.getColumnIndex(CentreContract.HospitalsEntry.COLUMN_HOSPITAL_MOB));
        ImageView call = (ImageView) view.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = "tel:" + phone.trim() ;
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(uri));
                if (intent.resolveActivity(MainActivity.getInstance().getPackageManager()) != null) {
                    MainActivity.getInstance().startActivity(intent);
                }
            }
        });
        // Populate fields with extracted properties
        hospitalName.setText(name);
        hospitalAddress.setText(add);
    }
}
