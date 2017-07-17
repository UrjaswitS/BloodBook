package app.com.project.urjaswitk.bloodbook.activities;

import android.database.Cursor;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.models.UserProfile;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract;

public class ProfileActivity extends BaseActivity
        implements View.OnClickListener{

    private EditText name, email ,phone, city, blood;

    private Button cancel, ok;

    private UserProfile user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        name= (EditText)findViewById(R.id.name_prof);
        email= (EditText)findViewById(R.id.email_prof);
        city= (EditText)findViewById(R.id.city_prof);
        phone= (EditText)findViewById(R.id.phone_prof);
        blood= (EditText)findViewById(R.id.blood_prof);

        ok= (Button) findViewById(R.id.ok_prof);
        cancel= (Button)findViewById(R.id.cancel_prof);

        cancel.setOnClickListener(this);
        ok.setOnClickListener(this);

        String uId= getIntent().getStringExtra(UserProfile.USER_ID);

        Cursor cursor= getContentResolver().query(CentreContract.UsersEntry.CONTENT_URI,
                null,
                UserProfile.USER_ID+"=?",
                new String[]{uId}, null);

        if (cursor.moveToFirst()){
           // name.setText(cursor.getString());
            user= UserProfile.getUserProfileFromCursor(cursor);
            name.setText(cursor.getString(cursor.getColumnIndex(UserProfile.FIRST_NAME)));
            email.setText(cursor.getString(cursor.getColumnIndex(UserProfile.EMAIL_ADDRESS)));
            phone.setText(cursor.getString(cursor.getColumnIndex(UserProfile.PHONE_NUMBER)));
            blood.setText(cursor.getString(cursor.getColumnIndex(UserProfile.BLOOD_GROUP)));
            city.setText(cursor.getString(cursor.getColumnIndex(UserProfile.CITY)));
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ok_prof:
                //FirebaseAuth.getInstance().getCurrentUser().getUid();
                upadateUserDetails();
                finish();
                break;
            case R.id.cancel_prof:
                finish();
                break;
            default:
        }
    }

    private void upadateUserDetails(){

        user.setFirstName(name.getText().toString().trim());
        user.setEmailAddress(email.getText().toString().trim());
        user.setPhone(phone.getText().toString().trim());
        user.setCity(city.getText().toString().trim());
        user.setBloodGroup(blood.getText().toString().trim());


        FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUserId())
                .setValue(user);
    }
}
