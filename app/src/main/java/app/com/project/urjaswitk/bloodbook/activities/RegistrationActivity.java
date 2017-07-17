package app.com.project.urjaswitk.bloodbook.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.models.Requests;
import app.com.project.urjaswitk.bloodbook.models.UserProfile;

public class RegistrationActivity extends BaseActivity {

    private static final String TAG = RegistrationActivity.class.getSimpleName();
    private EditText name, email, pass, pass_c, blood, city;
    private Button register;

    private String phone;

    private FirebaseAuth mAuth;
    private static RegistrationActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        mAuth= FirebaseAuth.getInstance();
        activity= this;

        email = (EditText) findViewById(R.id.email_reg);
        name = (EditText) findViewById(R.id.name_reg);
        pass = (EditText) findViewById(R.id.password_reg);
        pass_c = (EditText) findViewById(R.id.password_reg_confirm);
        blood = (EditText) findViewById(R.id.blood_reg);
        city = (EditText) findViewById(R.id.city_reg);

        register= (Button)findViewById(R.id.register_user);
        phone= getIntent().getStringExtra(UserProfile.PHONE_NUMBER);

       // setDummy();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateViews(email, name, pass, pass_c, blood, city)) {
                    return;
                }
                if (!pass.getText().toString()
                        .equals(pass_c.getText().toString())){
                    Log.e(TAG, "rr " +pass.getText().toString().trim()+ " rr");
                    Log.e(TAG, "rr "+ pass_c.getText().toString().trim()+ " rr");
                    pass_c.setText("Password don't match");
                    return;
                }
                createAccount(email.getText().toString().trim(),
                        pass.getText().toString().trim());


            }
        });
    }

    private void setDummy(){
        email.setText("uks@yahoo.com");
        name.setText(getIntent().getStringExtra(UserProfile.PHONE_NUMBER).toString());
        blood.setText("B+");
        city.setText("Kolkata");
        pass.setText("Urjas@9999");
        pass_c.setText("Urjas@9999");
    }

    private void createAccount(final String email_, String password) {
        Log.e(TAG, "createAccount mmmmmmm:" + email_);
        showProgressDialog();

        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email_, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegistrationActivity.this, "Authenticated "+user.getUid(),
                                    Toast.LENGTH_SHORT).show();


                            UserProfile user_= new UserProfile(
                                    mAuth.getCurrentUser().getUid(),
                                    name.getText().toString().trim(),
                                    email.getText().toString().trim(),
                                    phone.trim(),
                                    blood.getText().toString().trim(),
                                    city.getText().toString().trim()
                            );
                            registerNewUser(user_);
                            Log.e(TAG, " &&&& "+
                                            mAuth.getCurrentUser().getUid()+
                                    name.getText().toString().trim()+
                                    email.getText().toString().trim()+
                                    phone.trim()+
                                    blood.getText().toString().trim()+
                                    city.getText().toString().trim());
                            afterSignIn(user_);
                            //
                            // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegistrationActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                           // updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }


    private void registerNewUser(UserProfile user){
        FirebaseDatabase.getInstance().getReference()
                .child("users").child(user.getUserId()).setValue(user);


        Log.e(TAG, "done all registeration");
    }

    private boolean validateViews(EditText... views) {
        boolean result=true;
        for (EditText v : views) {
            if (TextUtils.isEmpty(v.getText().toString().trim())) {
                v.setError("Invalid");
                result=false;
            }
        }
        return result;
    }

    public static RegistrationActivity getInstance(){
        return   activity;
    }

    private void afterSignIn(UserProfile user){
//        startActivity(new Intent(RegistrationActivity.this,
//                MainActivity.class));
        startActivity(MainActivity.makeIntent(RegistrationActivity.this, user));
     //   finish();
    }
}
