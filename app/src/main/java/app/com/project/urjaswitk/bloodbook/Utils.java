package app.com.project.urjaswitk.bloodbook;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import app.com.project.urjaswitk.bloodbook.activities.MainActivity;
import app.com.project.urjaswitk.bloodbook.models.Requests;
import app.com.project.urjaswitk.bloodbook.models.UserProfile;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract;


/**
 * Created by UrJasWitK on 09-Jul-17.
 */

public class Utils {

    public static String REQUESTS_KEY= "requests";
    public static String USERS_KEY= "users";

  //  public static final UserProfile user= getUser();


    /**
     * Loads all requests from the realtime database and
     * stores them in the provider of the app
     * @param ref
     */
    public static void loadAllRequestsFromInternetIntoProvider(
            Context context, DatabaseReference ref){//this reference is of the requests
        ref.addChildEventListener(makeChildEventListener(context));
    }

    private static void addToContentProvider(Context context,
            List<Requests> reqs){
            context.getContentResolver()
                    .bulkInsert(Uri.parse(CentreContract.RequestEntry.CONTENT_LIST_TYPE),
                            Requests.addAllRequests(reqs));
    }

    public static void uploadNewRequest(Requests r,
            DatabaseReference ref){
        Log.e("MMMMMMMMMMMMMMMM", r.getSenderId());



        String key= ref.push().getKey();
        r.setRequestId(key.trim());
        ref.child(key).setValue(r);

       // ref.push().setValue(r);

        Log.e("Utils new request again", ref.toString());
    }

    private static ChildEventListener makeChildEventListener(
            final Context context){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Requests reqs= dataSnapshot.getValue(Requests.class);
                //reqs.setRequestId(dataSnapshot.getKey());
                Log.e("UtilOOOO on child added", reqs.getSenderId()+ "     "+ reqs.getReceiverId());

                addRequestsToProviderUsingAsyncTask(context, reqs);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Requests r= dataSnapshot.getValue(Requests.class);
                Log.e("TO BE UPDATED", "");
                addRequestsToProviderUsingAsyncTask(context, r);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Requests req= dataSnapshot.getValue(Requests.class);
                Log.e("utils", "out to insert ");
                deleteFromProviderUsingAsyncTask(context, req);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @SuppressLint("StaticFieldLeak")
    private static void deleteFromProviderUsingAsyncTask(
            final Context context, final Requests req){
                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... voids) {
                        context.getContentResolver().delete(
                                CentreContract.RequestEntry.CONTENT_URI,
                                Requests.Columns.COLUMN_REQID+"=?",
                                new String[]{req.getRequestId()});
                        Log.e("utils", "done with inserting ");
                        return null;
                    }
                }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    private static void addRequestsToProviderUsingAsyncTask(
            final Context context, final Requests reqs){
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                Cursor cursor = context.getContentResolver().query(
                        CentreContract.RequestEntry.CONTENT_URI,
                        null, Requests.Columns.COLUMN_REQID + "=?",
                        new String[]{reqs.getRequestId()}, null);

                if(cursor.moveToFirst())
                {

                    String recv= cursor.getString(
                            cursor.getColumnIndex(Requests.Columns.COLUMN_RECV));
//                    if( (recv  == null || recv == "#") &&
//                            (reqs.getReceiverId()!=null) || (reqs.getReceiverId()!="#")) {
                if (!Requests.getRequestsFromCursor(cursor).equals(reqs)) {
                    context.getContentResolver()
                            .update(CentreContract.RequestEntry.CONTENT_URI,
                                    Requests.getContentValuesFromRequests(reqs),
                                    Requests.Columns.COLUMN_REQID + "=?",
                                    new String[]{reqs.getRequestId()});

//                        UserProfile acceptor= FirebaseDatabase.getInstance().getReference()
//                                .child("users")
                    //showAlertForRequestReceived(context, reqs.getReceiverId());
                    Log.e("Utils----------", " here again we are 1");
                    Log.e("Utils---------- ", reqs.getSenderId()+"-------------"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
//                    if (reqs.getSenderId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
//                        if (reqs.getReceiverId().trim().equals("") || reqs.getReceiverId() != null)
//                            FirebaseDatabase.getInstance().getReference()
//                                    .child("users").addChildEventListener(
//                                    makeChildEventListenerForMyRequest(context,
//                                            reqs.getReceiverId(), reqs, 2) );
                    Log.e("Utils----------", " here again we are 2");
                }


                    Log.e("Utils", cursor.getString(cursor.getColumnIndex(Requests.Columns.COLUMN_RECV)));
                    Log.e("Utils", "no insertion required");
                    return null;
                }
                context.getContentResolver().
                        insert(
                                CentreContract.RequestEntry.CONTENT_URI,
                                Requests.getContentValuesFromRequests(reqs));
                return null;
            }
        }.execute();
    }

    public static void acceptedRequestsUpdation(Context c, String id, Requests r){
                r.setReceiverId(id);
//        Log.e("Utils",  FirebaseDatabase.getInstance()
//                .getReference().child("requests").child(r.getRequestId()).toString());

        FirebaseDatabase.getInstance().getReference()
                .child("users").addChildEventListener(
                        makeChildEventListenerForMyRequest(c, r.getSenderId(), r, 1));
        Log.e("utils", "@@@@@@@@@@@@@@@@@@@@@wassa");
        Log.e("utils", r.getReceiverId());
//        FirebaseDatabase.getInstance()
//                .getReference().child("requests").child(r.getRequestId())
//                .setValue(r);
    }

    private static void showAlertForRequest(
            Context context, final UserProfile user, final Requests r){
        MainActivity.getInstance().hideProgressDialog();

        Log.e("Utils----------", " here again we are ");
        final Dialog newReq= new Dialog(context);
        newReq.setCancelable(true);
        newReq.setContentView(R.layout.dialog_received_request);

        ((TextView)newReq.findViewById(R.id.acc_name_text))
                .setText(r.getMessage());
        ((TextView)newReq.findViewById(R.id.acc_blood_text))
                .setText(r.getBlood());
        ((TextView)newReq.findViewById(R.id.acc_email_text))
                .setText(user.getEmailAddress());
        TextView t= (TextView)newReq.findViewById(R.id.acc_phone_text);
                t.setText(user.getPhone());

        ((Button)newReq.findViewById(R.id.acc_ok_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance()
                .getReference().child("requests").child(r.getRequestId())
                .child("receiverId").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        FirebaseDatabase.getInstance()
                                .getReference().child("requests").child(r.getRequestId())
                                .child("receiverName").setValue(MainActivity.getUser().getFirstName());
                        newReq.dismiss();
                    }
                });
        (newReq.findViewById(R.id.acc_cancel_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        FirebaseDatabase.getInstance().getReference()
//                                .child("requests").child(r.getRequestId().trim())
//                                .child("receiverId").setValue("#");
//                        FirebaseDatabase.getInstance()
//                                .getReference().child("requests").child(r.getRequestId())
//                                .setValue(r);
                        newReq.dismiss();
                    }
                });
        final String phone= t.getText().toString().trim();
        if (phone != null || !phone.equals("")) {
            (newReq.findViewById(R.id.call_to_acceptor_btn))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String uri = "tel:" + phone.trim();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(uri));
                            if (intent.resolveActivity(MainActivity.getInstance().getPackageManager()) != null) {
                                MainActivity.getInstance().startActivity(intent);
                            }
                        }
                    });
        }else Toast.makeText(context, "No Phone no.", Toast.LENGTH_SHORT).show();

        newReq.setTitle("Your request is accepted !!");
        newReq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        newReq.show();

    }

    private static void showAlertForRequestReceived(
            Context context, final UserProfile user, final Requests r){

        Log.e("Utils----------", " here again we are ");
        final Dialog newReq= new Dialog(context);
        newReq.setCancelable(true);
        newReq.setContentView(R.layout.dialog_received_request);

        ((TextView)newReq.findViewById(R.id.acc_name_text))
                .setText(user.getFirstName());
        ((TextView)newReq.findViewById(R.id.acc_blood_text))
                .setText(user.getBloodGroup());
        ((TextView)newReq.findViewById(R.id.acc_email_text))
                .setText(user.getEmailAddress());

        TextView t= (TextView)newReq.findViewById(R.id.acc_phone_text);
        t.setText(user.getPhone());

        ((Button)newReq.findViewById(R.id.acc_ok_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        newReq.dismiss();
                    }
                });
        (newReq.findViewById(R.id.acc_cancel_btn))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FirebaseDatabase.getInstance().getReference()
                                .child("requests").child(r.getRequestId().trim())
                                .child("receiverId").setValue("");
                        newReq.dismiss();
                    }
                });

        final String phone= t.getText().toString().trim();
        if (phone != null || !phone.equals("")) {
            (newReq.findViewById(R.id.call_to_acceptor_btn))
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String uri = "tel:" + phone.trim();
                            Intent intent = new Intent(Intent.ACTION_DIAL);
                            intent.setData(Uri.parse(uri));
                            if (intent.resolveActivity(MainActivity.getInstance().getPackageManager()) != null) {
                                MainActivity.getInstance().startActivity(intent);
                            }
                        }
                    });
        }else Toast.makeText(context, "No Phone no.", Toast.LENGTH_SHORT).show();

        newReq.setTitle("Your request is accepted !!");
        newReq.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        newReq.show();

    }

    private static ChildEventListener makeChildEventListenerForMyRequest(
            final Context c, final String id, final Requests r, final int ch){
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("utils", "@@@@@@@@ "+dataSnapshot.getValue());
                UserProfile user= dataSnapshot.getValue(UserProfile.class);

                if (id.trim().equals(user.getUserId().trim())){
                    Log.e("utils", "@@@@@@@@@@@@@@@@@@@@@burger1");
                    if(ch==1)showAlertForRequest(c, user, r);
                   // else showAlertForRequestReceived(c, user, r);
                }
                Log.e("utils", "@@@@@@@@@@@@@@@@@@@@@burger2  "+ user.getUserId()+"  &&  "+id+"  $$");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }




    @SuppressLint("StaticFieldLeak")
    public static void getAllUsers(final Context c) {

                Log.d("UUUUUUUUUUUUUUUUU", "tttttttttttttttttttt kk");

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                Log.d("UUUUUUUUUUUUUUUUU", "tttttttttttttttttttt");

                final UserProfile user = new UserProfile();
                final ChildEventListener listener = new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //UserProfile user= new UserProfile();
//                        if (dataSnapshot.getKey().equals("bloodGroup"))
//                            user.setBloodGroup(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("city"))
//                            user.setCity(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("emailAddress"))
//                            user.setEmailAddress(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("firstName"))
//                            user.setFirstName(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("phone"))
//                            user.setPhone(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("userId"))
//                            user.setUserId(dataSnapshot.getValue().toString());

                        UserProfile user_= dataSnapshot.getValue(UserProfile.class);
//                        new AsyncTask<Void, Void, Void>(){
//
//                            @Override
//                            protected Void doInBackground(Void... voids) {

                        user_.setPhone(dataSnapshot.child("phone").getValue().toString());


                        if (c.getContentResolver()
                                .query(CentreContract.UsersEntry.CONTENT_URI,
                                        null, UserProfile.USER_ID + "=?",
                                        new String[]{user_.getUserId()}, null).moveToFirst()) {

                            Log.d("UUUUUUUUUUUUUUUUU", "tttttttttttttttttttt already");

                        } else{
                            c.getContentResolver()
                                    .insert(CentreContract.UsersEntry.CONTENT_URI,
                                            UserProfile.getContentValuesFromUserProfile(user_));
                            Log.d("UUUUUUUUUUUUUUUUU", "tttttttttttttttttttt inerted");

                        }
//                                return null;
//                            }
//                        }.execute();


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        // UserProfile user= new UserProfile();
//                        if (dataSnapshot.getKey().equals("bloodGroup"))
//                            user.setBloodGroup(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("city"))
//                            user.setCity(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("emailAddress"))
//                            user.setEmailAddress(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("firstName"))
//                            user.setFirstName(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("phone"))
//                            user.setPhone(dataSnapshot.getValue().toString());
//                        else if (dataSnapshot.getKey().equals("userId"))
//                            user.setUserId(dataSnapshot.getValue().toString());

//                        new AsyncTask<Void, Void, Void>(){
//
//                            @Override
//                            protected Void doInBackground(Void... voids) {
                        UserProfile user_= dataSnapshot.getValue(UserProfile.class);

                        user_.setPhone(dataSnapshot.child("phone").getValue().toString());
                        c.getContentResolver()
                                .update(CentreContract.UsersEntry.CONTENT_URI,
                                        UserProfile.getContentValuesFromUserProfile(user_),
                                        UserProfile.USER_ID + "=?",
                                        new String[]{user_.getUserId()});

//                                return null;
//                            }
//                        }.execute();


                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                };


                FirebaseDatabase.getInstance().getReference()
                        .child("users")
                      //  .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .addChildEventListener(listener);
                return null;
            }



//        Query query= FirebaseDatabase.getInstance().getReference()
//                .child("users").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
//        //UserProfile user_= query;

        }.execute();
    }

    public static UserProfile getUsers(){
        final UserProfile user = new UserProfile();
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getKey().equals("bloodGroup"))
                            user.setBloodGroup(dataSnapshot.getValue().toString());
                        else if (dataSnapshot.getKey().equals("city"))
                            user.setCity(dataSnapshot.getValue().toString());
                        else if (dataSnapshot.getKey().equals("emailAddress"))
                            user.setEmailAddress(dataSnapshot.getValue().toString());
                        else if (dataSnapshot.getKey().equals("firstName"))
                            user.setFirstName(dataSnapshot.getValue().toString());
                        else if (dataSnapshot.getKey().equals("phone"))
                            user.setPhone(dataSnapshot.getValue().toString());
                        else if (dataSnapshot.getKey().equals("userId"))
                            user.setUserId(dataSnapshot.getValue().toString());
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        return user;
    }
}
