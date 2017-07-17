package app.com.project.urjaswitk.bloodbook.adapters;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.Utils;
import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.models.Requests;
import app.com.project.urjaswitk.bloodbook.models.UserProfile;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract;

/**
 * Created by UrJasWitK on 12-Jul-17.
 */

public class RequestsCursorAdapter extends CursorAdapter {

    public RequestsCursorAdapter(Context context, Cursor c) {
        super(context, c);

    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.list_item_requests, parent, false);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final Requests req= Requests.getRequestsFromCursor(cursor);

        TextView name= (TextView)view.findViewById(R.id.name),
        time= (TextView)view.findViewById(R.id.time);


        if (MainActivity.history){
            if (FirebaseAuth.getInstance().getCurrentUser().getUid().trim()
                    .equals(req.getReceiverId().trim())){
                name.setText("Donated " + req.getMessage());
                time.setText(req.getBlood());
                return;
            }else if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(
                    req.getSenderId())){
                if (req.getReceiverId() != null || !req.getReceiverId().equals("")){
//                    Cursor c= context.getContentResolver().query(
//                            CentreContract.UsersEntry.CONTENT_URI,
//                           new String[] {UserProfile.FIRST_NAME},
//                            UserProfile.USER_ID+"=?",
//                            new String[]{req.getReceiverId()}, null);
//                    c.moveToFirst();
                    name.setText(req.getReceiverName()

                            + "  accepted your request");
                    time.setText(req.getBlood().toString());
                }else
                {
                    name.setText("Requested for " + req.getBlood());
                    time.setText(req.getTime());
                    return;
                }
                return;

            }

        }


        Log.e("Adapter", "mesage: "+cursor.getString(
                cursor.getColumnIndex(
                        Requests.Columns.COLUMN_MSG)));
        Log.e("Adapter", "time: "+cursor.getString(
                cursor.getColumnIndex(
                        Requests.Columns.COLUMN_TIME)));
       // ((TextView)view.findViewById(R.id.name))
//        name.setText(String.format(cursor.getString(
//                cursor.getColumnIndex(
//                        Requests.Columns.COLUMN_MSG))+" needs "+
//        String.valueOf(cursor.getString(cursor.getColumnIndex(Requests.Columns.COLUMN_BLOOD)))
//         + " blood group"));
//                + " @## "+
//        cursor.getString(
//                cursor.getColumnIndex(Requests.Columns.COLUMN_RECV)))+ "#");

        name.setText(req.getMessage()+ " needs  "+ req.getBlood() + " blood group");
        time.setText(req.getTime());

//        ((TextView)view.findViewById(R.id.time))
//                .setText(cursor.getString(
//                        cursor.getColumnIndex(
//                                Requests.Columns.COLUMN_TIME)));



        View accept= view.findViewById(R.id.accept);

//        if (req.getReceiverId()!="" || req.getReceiverId()!= null){
//            accept.setVisibility(View.INVISIBLE);
//            return;
//        }
        Log.e("Cursor adapter", "just check");
        if (MainActivity.visible==false)return;

        if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(req.getSenderId())) {
            Log.e("77777777777777777", "888888888888888");
            return;
        }


        accept.setVisibility(View.VISIBLE);
        accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(context, req.getRequestId(),
//                                Toast.LENGTH_LONG).show();
//                        Utils.acceptedRequestsUpdation(
//                                cursor.getString(
//                                cursor.getColumnIndex(
//                                Requests.Columns.COLUMN_REQID)), req
//                                );
                        MainActivity.getInstance().showProgressDialog();
                        Utils.acceptedRequestsUpdation(context,
                                FirebaseAuth.getInstance().getCurrentUser().toString(), req);
                        Log.e("in cursor adapter", "updated requests");
                    }
                });
    }

}
