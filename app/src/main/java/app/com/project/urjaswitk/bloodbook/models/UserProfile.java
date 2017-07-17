package app.com.project.urjaswitk.bloodbook.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;

import com.google.firebase.auth.FirebaseUser;

import app.com.project.urjaswitk.bloodbook.activities.MainActivity;

/**
 * Created by UrJasWitK on 04-Jul-17.
 */

public class UserProfile {

    public static String FIRST_NAME= "first_name";
    public static String CITY= "city";
    public static String EMAIL_ADDRESS= "email_address";
    public static String PHONE_NUMBER= "phone_number";
    public static String USER_ID= "user_id";
    public static String BLOOD_GROUP= "blood_group";

    private String userId, firstName, city;
    private String bloodGroup, emailAddress, phone;
  //  private long phoneNumber;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public UserProfile(){
        this.userId = "";
        this.firstName = "";
        this.bloodGroup = "";
        this.emailAddress = "";
        this.phone="";
     //   this.phoneNumber = 0;
        this.city= "";
    }

    public UserProfile(String userId, String firstName,
                       String emailAddress, String phone,
                       String bloodGroup, String city) {
        this();
        this.userId = userId;
        this.firstName = firstName;
        this.bloodGroup = bloodGroup;
        this.emailAddress = emailAddress;
        this.phone = phone;
        this.city= city;
    }

    public UserProfile(String userId, String firstName,
                       String emailAddress, String phone) {
        this();
        this.userId = userId;
        this.firstName = firstName;

        this.emailAddress = emailAddress;
        this.phone = phone;
    }


//    public UserProfile(String userId, String firstName,
//                       String emailAddress, long phoneNumber,
//                       String bloodGroup, String city) {
//        this();
//        this.userId = userId;
//        this.firstName = firstName;
//        this.bloodGroup = bloodGroup;
//        this.emailAddress = emailAddress;
//        this.phoneNumber = phoneNumber;
//        this.phone= String.valueOf(phoneNumber);
//        this.city= city;
//    }



    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

//    public long getPhoneNumber() {
//        return phoneNumber;
//    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

//    public void setPhoneNumber(long phoneNumber) {
//        this.phone= String.valueOf(phoneNumber);
//        this.phoneNumber = phoneNumber;
//    }

    public static Bundle getBundleFromUserProfile(UserProfile user){
        Bundle data= new Bundle();
        data.putString(UserProfile.USER_ID, user.getUserId());
        data.putString(UserProfile.FIRST_NAME, user.getFirstName());
        //data.putString(UserProfile.LAST_NAME, user.getLastName());
        data.putString(UserProfile.EMAIL_ADDRESS, user.getEmailAddress()) ;
        data.putString(UserProfile.PHONE_NUMBER, user.getPhone());
       // data.putString(UserProfile.PHONE_NUMBER, String.valueOf(user.getPhoneNumber()));
        data.putString(UserProfile.BLOOD_GROUP, user.getBloodGroup());
        data.putString(UserProfile.CITY, user.getCity());
        return data;
    }

    public static UserProfile getUserProfileFromBundle(Bundle b){
        return new UserProfile(
                b.getString(UserProfile.USER_ID),
                b.getString(UserProfile.FIRST_NAME),
               // b.getString(UserProfile.LAST_NAME),
                b.getString(UserProfile.EMAIL_ADDRESS),
                b.getString(UserProfile.PHONE_NUMBER),
                b.getString(UserProfile.BLOOD_GROUP),
                b.getString(UserProfile.CITY));
    }

    public static ContentValues getContentValuesFromUserProfile(UserProfile user){
        ContentValues data= new ContentValues();
        data.put(UserProfile.USER_ID, user.getUserId());
        data.put(UserProfile.FIRST_NAME, user.getFirstName());
     //   data.put(UserProfile.LAST_NAME, user.getLastName());
        data.put(UserProfile.EMAIL_ADDRESS, user.getEmailAddress()) ;
        data.put(UserProfile.PHONE_NUMBER, user.getPhone());
        data.put(UserProfile.BLOOD_GROUP, user.getBloodGroup());
        data.put(UserProfile.CITY, user.getCity());
        return data;
    }

    public static UserProfile getUserProfileFromCOntentValues(
            ContentValues b){
        return new UserProfile(
                b.getAsString(UserProfile.USER_ID),
                b.getAsString(UserProfile.FIRST_NAME),
                //b.getAsString(UserProfile.LAST_NAME),
                b.getAsString(UserProfile.EMAIL_ADDRESS),
                b.getAsString(UserProfile.PHONE_NUMBER),
                b.getAsString(UserProfile.BLOOD_GROUP),
                b.getAsString(UserProfile.CITY));
    }

    public static UserProfile getUserProfileFromFirebaseUser(
            FirebaseUser authUser){
        return new UserProfile(authUser.getUid(), authUser.getDisplayName(), authUser.getEmail(),
                authUser.getPhoneNumber());
    }

    public static UserProfile getUserProfileFromCursor(
            Cursor cursor){
//                if (!cursor.moveToFirst())
//                    return null;
        return new UserProfile(
                cursor.getString(cursor.getColumnIndex(UserProfile.USER_ID)),
                cursor.getString(cursor.getColumnIndex(UserProfile.FIRST_NAME)),
                cursor.getString(cursor.getColumnIndex(UserProfile.EMAIL_ADDRESS)),
                cursor.getString(cursor.getColumnIndex(UserProfile.PHONE_NUMBER)),
                cursor.getString(cursor.getColumnIndex(UserProfile.BLOOD_GROUP)),
                cursor.getString(cursor.getColumnIndex(UserProfile.CITY))
        );
    }

}
