/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package app.com.project.urjaswitk.bloodbook.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import app.com.project.urjaswitk.bloodbook.R;
import app.com.project.urjaswitk.bloodbook.Utils;
import app.com.project.urjaswitk.bloodbook.models.UserProfile;
import app.com.project.urjaswitk.bloodbook.provider.CentreContract;

/**
 * Simple list-based Activity to redirect to one of the other Activities. This Activity does not
 * contain any useful code related to Firebase Authentication. You may want to start with
 * one of the following Files:
 *     {@link GoogleSignInActivity}
 *     {@link EmailPasswordActivity}
 *     {@link PhoneAuthActivity}
 */
public class ChooserActivity extends AppCompatActivity
        implements
        View.OnClickListener {

    private static final String TAG = "PhoneAuthActivity";

    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private ViewGroup mPhoneNumberViews, mLoginAndSignUpFields;
    private ViewGroup mSignedInViews;

    private TextView mStatusText;
    private TextView mDetailText;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;
    private EditText mPasswordField;

    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button mSignOutButton, mSignInButton, mRegisterButton;
    private static ChooserActivity activity;

   // private LinearLayout mPhoneAuthFields;

    private boolean mReadyTOSignIn= false;
    private String mNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_auth);

        // Restore instance state
        if (savedInstanceState != null) {
            onRestoreInstanceState(savedInstanceState);
        }

        activity= this;

        // Assign views
        mPhoneNumberViews = (ViewGroup) findViewById(R.id.phone_auth_fields);
       // mSignedInViews = (ViewGroup) findViewById(R.id.signed_in_buttons);

//        mStatusText = (TextView) findViewById(R.id.status);
//        mDetailText = (TextView) findViewById(R.id.detail);

        mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);
        mVerificationField = (EditText) findViewById(R.id.field_verification_code);
       // mPasswordField= (EditText)findViewById(R.id.password_edit_text);

        mStartButton = (Button) findViewById(R.id.button_start_verification);
        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);
        mResendButton = (Button) findViewById(R.id.button_resend);
       // mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mSignInButton= (Button)findViewById(R.id.button_signin);
        mRegisterButton= (Button)findViewById(R.id.button_register);

      //  mPhoneAuthFields= (LinearLayout)findViewById(R.id.phone_auth_fields);
        mLoginAndSignUpFields= (LinearLayout)findViewById(R.id.login_and_signup_fields);


        // Assign click listeners
        mStartButton.setOnClickListener(this);
        mVerifyButton.setOnClickListener(this);
        mResendButton.setOnClickListener(this);
       // mSignOutButton.setOnClickListener(this);
        mSignInButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            mReadyTOSignIn= true;
            mNumber= mAuth.getCurrentUser().getPhoneNumber();
        }
        // [END initialize_auth]

        // Initialize phone auth callbacks
        // [START phone_auth_callbacks]
//        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
//
//            @Override
//            public void onVerificationCompleted(PhoneAuthCredential credential) {
//                // This callback will be invoked in two situations:
//                // 1 - Instant verification. In some cases the phone number can be instantly
//                //     verified without needing to send or enter a verification code.
//                // 2 - Auto-retrieval. On some devices Google Play services can automatically
//                //     detect the incoming verification SMS and perform verificaiton without
//                //     user action.
//                Log.d(TAG, "onVerificationCompleted:" + credential);
//                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
//                // [END_EXCLUDE]
//
//                // [START_EXCLUDE silent]
//                // Update the UI and attempt sign in with the phone credential
//                updateUI(STATE_VERIFY_SUCCESS, credential);
//
//                afterVerification(mPhoneNumberField.getText().toString().trim());
//
//                // [END_EXCLUDE]
//                // signInWithPhoneAuthCredential(credential);
//            }
//
//            @Override
//            public void onVerificationFailed(FirebaseException e) {
//                // This callback is invoked in an invalid request for verification is made,
//                // for instance if the the phone number format is not valid.
//                Log.w(TAG, "onVerificationFailed", e);
//                // [START_EXCLUDE silent]
//                mVerificationInProgress = false;
//                // [END_EXCLUDE]
//
//                if (e instanceof FirebaseAuthInvalidCredentialsException) {
//                    // Invalid request
//                    // [START_EXCLUDE]
//                    mPhoneNumberField.setError("Invalid phone number.");
//                    // [END_EXCLUDE]
//                } else if (e instanceof FirebaseTooManyRequestsException) {
//                    // The SMS quota for the project has been exceeded
//                    // [START_EXCLUDE]
//                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
//                            Snackbar.LENGTH_SHORT).show();
//                    // [END_EXCLUDE]
//                }
//
//                // Show a message and update the UI
//                // [START_EXCLUDE]
//                updateUI(STATE_VERIFY_FAILED);
//                // [END_EXCLUDE]
//            }
//
//            @Override
//            public void onCodeSent(String verificationId,
//                                   PhoneAuthProvider.ForceResendingToken token) {
//                // The SMS verification code has been sent to the provided phone number, we
//                // now need to ask the user to enter the code and then construct a credential
//                // by combining the code with a verification ID.
//                Log.d(TAG, "onCodeSent:" + verificationId);
//
//                // Save verification ID and resending token so we can use them later
//                mVerificationId = verificationId;
//                mResendToken = token;
//
//                // [START_EXCLUDE]
//                // Update UI
//                updateUI(STATE_CODE_SENT);
//                // [END_EXCLUDE]
//            }
//        };
        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verificaiton without
                //     user action.
                Log.d(TAG, "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                mResendButton.setOnClickListener(null);
                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                updateUI(STATE_VERIFY_SUCCESS, credential);

                Log.e("CHOOSER", mPhoneNumberField.getText().toString().trim());
                //String phone= new String(mPhoneNumberField.getText().toString().trim());
               // signInWithPhoneAuthCredential(credential);
                afterVerification(mPhoneNumberField.getText().toString().trim());

                //startActivity(new Intent(ChooserActivity.this, MainActivity.class));
                // [END_EXCLUDE]
              //  signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.w(TAG, "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.d(TAG, "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                mVerificationId = verificationId;
                mResendToken = token;

                // [START_EXCLUDE]
                // Update UI
                updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };
        // [END phone_auth_callbacks]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
        if (mAuth.getCurrentUser() != null){
            afterSignIn();
        }

        // [START_EXCLUDE]
        if (mVerificationInProgress && validatePhoneNumber()) {
            startPhoneNumberVerification(mPhoneNumberField.getText().toString());
        }
        // [END_EXCLUDE]
    }
    // [END on_start_check_user]

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);
    }


    private void startPhoneNumberVerification(String phoneNumber) {
        mNumber= phoneNumber;
        // [START start_phone_auth]
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
        // [END start_phone_auth]

        mVerificationInProgress = true;
        Log.e(TAG, "verification");
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider
                .getCredential(verificationId, code);
        // [END verify_with_code]
          signInWithPhoneAuthCredential(credential);
    }

    // [START resend_verification]
    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
      //  enableViews(mVerifyButton);
    }
    // [END resend_verification]

     //[START sign_in_with_phone]
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                          //  disableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField);
                            // [START_EXCLUDE]
                            updateUI(STATE_SIGNIN_SUCCESS, user);
                            afterVerification(mPhoneNumberField.getText().toString().trim());
                         //   afterSignIn();
//                            startActivity((new Intent(
//                                    ChooserActivity.this, MainActivity.class)
//                                    .putExtras(UserProfile.getBundleFromUserProfile(
//                                            UserProfile.getUserProfileFromFirebaseUser(user)
//                                    ))));
//                                    .putExtras(UserProfile.getBundleFromUserProfile(
//                                            new UserProfile(
//                                                    user.getUid(), user.getDisplayName(),
//                                                    user.getEmail(), user.getPhoneNumber(), "B+", )))));

                            mReadyTOSignIn= true;

                            // [END_EXCLUDE]
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.e(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                // [START_EXCLUDE silent]
                                mVerificationField.setError("Invalid code.");
                                // [END_EXCLUDE]
                            }
                            // [START_EXCLUDE silent]
                            // Update UI
                            updateUI(STATE_SIGNIN_FAILED);
                            // [END_EXCLUDE]
                        }
                    }
                });
    }
    // [END sign_in_with_phone]

    private void signOut() {
        mAuth.signOut();
        updateUI(STATE_INITIALIZED);
        mReadyTOSignIn= false;
    }

    private void updateUI(int uiState) {
        updateUI(uiState, mAuth.getCurrentUser(), null);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            updateUI(STATE_SIGNIN_SUCCESS, user);
        } else {
            updateUI(STATE_INITIALIZED);
        }
    }

    private void updateUI(int uiState, FirebaseUser user) {
        updateUI(uiState, user, null);
    }

    private void updateUI(int uiState, PhoneAuthCredential cred) {
        updateUI(uiState, null, cred);
    }

    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {
        switch (uiState) {
            case STATE_INITIALIZED:
                // Initialized state, show only the phone number field and start button
//                enableViews(mStartButton, mPhoneNumberField);
//                disableViews(mVerifyButton, mResendButton, mVerificationField);
////                mDetailText.setText(null);
                break;
            case STATE_CODE_SENT:
                // Code sent state, show the verification field, the
//                enableViews(mVerifyButton, mResendButton, mPhoneNumberField, mVerificationField);
//             //   disableViews(mStartButton);
                //mDetailText.setText(R.string.status_code_sent);
                Toast.makeText(this, getString(R.string.status_code_sent),
                        Toast.LENGTH_LONG).show();
                break;
            case STATE_VERIFY_FAILED:
                // Verification has failed, show all options
             //   enableViews(mStartButton, mVerifyButton, mResendButton, mPhoneNumberField,
                     //   mVerificationField);
                //mDetailText.setText(R.string.status_verification_failed);
                Toast.makeText(this, getString(R.string.status_verification_failed),
                        Toast.LENGTH_LONG).show();
                break;
            case STATE_VERIFY_SUCCESS:
                // Verification has succeeded, proceed to firebase sign in
               // disableViews(mStartButton, mVerifyButton, mResendButton,
                       // mVerificationField);
                //mDetailText.setText(R.string.status_verification_succeeded);
                Toast.makeText(this, getString(R.string.status_verification_succeeded),
                        Toast.LENGTH_LONG).show();

                // Set the verification text based on the credential
                if (cred != null) {
                    if (cred.getSmsCode() != null) {
                        mVerificationField.setText(cred.getSmsCode());
                    } else {
                        mVerificationField.setText(R.string.instant_validation);
                    }
                }

                break;
            case STATE_SIGNIN_FAILED:
                // No-op, handled by sign-in check
             //   mDetailText.setText(R.string.status_sign_in_failed);
                Toast.makeText(this, getString(R.string.status_sign_in_failed),
                        Toast.LENGTH_LONG).show();
                break;
            case STATE_SIGNIN_SUCCESS:
                Toast.makeText(this, "successfull",
                        Toast.LENGTH_LONG).show();
                // Np-op, handled by sign-in check
                break;
        }

//        if (user == null) {
//            // Signed out
//            mPhoneNumberViews.setVisibility(View.VISIBLE);
////            mSignedInViews.setVisibility(View.GONE);
//
//            mStatusText.setText(R.string.signed_out);;
//        } else {
//            // Signed in
//            mPhoneNumberViews.setVisibility(View.GONE);
//        //    mSignedInViews.setVisibility(View.VISIBLE);
//
//            enableViews(mPhoneNumberField, mVerificationField);
//            // mPhoneNumberField.setText(null);
//            mVerificationField.setText(null);
//
//            mStatusText.setText(R.string.signed_in);
//            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//        }
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        if (TextUtils.isEmpty(phoneNumber)) {
            mPhoneNumberField.setError("Invalid phone number.");
            return false;
        }

        return true;
    }

    private boolean validatePassword() {
        String password = mPasswordField.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mPasswordField.setError("Invalid password.");
            return false;
        }

        return true;
    }

    private void enableViews(View... views) {
        for (View v : views) {
            v.setEnabled(true);
        }
    }

    private void disableViews(View... views) {
        for (View v : views) {
            v.setEnabled(false);
        }
    }

    private void signIn(String phone, String password) {
        Log.d(TAG, "signIn:" + phone);
        if (!validatePassword() || !mReadyTOSignIn) {
            return;
        }
        String email= phone + "@uksmail.com";
        // showProgressDialog();

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            afterSignIn();
//                            startActivity(new Intent(
//                                    ChooserActivity.this,
//                                    MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(ChooserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

//                        // [START_EXCLUDE]
//                        if (!task.isSuccessful()) {
//                            mStatusTextView.setText(R.string.auth_failed);
//                        }
//                        hideProgressDialog();
//                        // [END_EXCLUDE]
                    }
                });
        // [END sign_in_with_email]
    }

    private void register(String phone, String password){

        if(!validatePassword() || !mReadyTOSignIn) {
            // show that there is some error in username or password
            return;
        }
        String email= phone + "@uksmail.com";
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(ChooserActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //  hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });

//        mAuth.getCurrentUser().updatePassword(mPasswordField.getText().toString())
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Log.d(TAG, "User password updated.");
//                            Toast.makeText(getApplicationContext(),
//                                    "Account created", Toast.LENGTH_LONG);
//                        }
//                        Log.d(TAG, "User password not updated.");
//                    }
//                });
        //add it to the firebase realtime database
        //get user here and do the necessary

    }

    private void goForPhoneNumberVerificationAndRegistration(){
        mLoginAndSignUpFields.setVisibility(View.GONE);
        mPhoneNumberViews.setVisibility(View.VISIBLE);
        //disableViews(mVerificationField, mResendButton, mVerifyButton);

        //start registration activity
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_start_verification:
                if (!validatePhoneNumber()) {
                    return;
                }
                startPhoneNumberVerification(mPhoneNumberField.getText().toString());
                break;
            case R.id.button_verify_phone:
                String code = mVerificationField.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mVerificationField.setError("Cannot be empty.");
                    return;
                }
               // disableViews(mResendButton);

                verifyPhoneNumberWithCode(mVerificationId, code);
                break;
            case R.id.button_resend:
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
//            case R.id.sign_out_button:
//                signOut();
//                break;
            case R.id.button_signin:
                startActivity(new Intent(
                        ChooserActivity.this,
                        EmailPasswordActivity.class));
//                signIn(mNumber,
//                        mPasswordField.getText().toString());
                //start an activity in which all sign in options are given
                break;
            case R.id.button_register:
            //    startActivity(new Intent(ChooserActivity.this, PhoneAuthActivity.class));
                goForPhoneNumberVerificationAndRegistration();
//                register(mPhoneNumberField.getText().toString(),
//                        mPasswordField.getText().toString());
        }
    }

    private void checkPhoneNumber(){
        new AsyncTask<Void, Void, Void>(){

            @Override
            protected Void doInBackground(Void... voids) {

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {

            }
        }.execute();
    }
    private void afterSignIn(){

        Log.e("MOOOOOOOOOOOOO", mAuth.getCurrentUser().getUid().toString());

        //final UserProfile user= new UserProfile();

       // Log.e("111111111111111111", "22222222222222222");
//        FirebaseDatabase.getInstance().getReference()
//                .child("users").child(mAuth.getCurrentUser().getUid())
//                .addChildEventListener(new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        //UserProfile user= new UserProfile();
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
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//                       // UserProfile user= new UserProfile();
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

      //  MainActivity.type= MainActivity.EMAIL;
        startActivity(MainActivity.makeIntent(ChooserActivity.this));
        //finish();
   //     Log.e("rukkkkkk", Utils.user.getUserId() +"9999" +mAuth.getCurrentUser().getUid());
//        FirebaseDatabase.getInstance().getReference()
//                .child("users").addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                if (dataSnapshot.getKey().equals(mAuth.getCurrentUser().getUid())){
//
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        })
//
//        startActivity((new Intent(
//                ChooserActivity.this, MainActivity.class)
//                .putExtras((UserProfile.getBundleFromUserProfile(
//                        UserProfile.getUserProfileFromFirebaseUser(mAuth.getCurrentUser()))
//
//                )).putExtra(UserProfile.PHONE_NUMBER, mAuth.getCurrentUser().getPhoneNumber())));
       // finish();
    }
    public static ChooserActivity getInstance(){
        return   activity;
    }
    private void afterVerification(String phone){
        startActivity((new Intent(
                ChooserActivity.this,
                RegistrationActivity.class).putExtra(UserProfile.PHONE_NUMBER, phone)));
    }
}