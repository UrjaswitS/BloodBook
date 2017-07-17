package app.com.project.urjaswitk.bloodbook.models;

/**
 * Created by UrJasWitK on 14-Jul-17.
 */


public class Hospitals {

    private String mName;
    private String mAdd;
    private String mDistrict;
    private String mState;
    private String mPin;
    private String mPhone;
    private String mMob;

    public Hospitals(String mName, String mAdd, String mDistrict, String mState, String mPin, String mPhone, String mMob) {
        this.mName = mName;
        this.mAdd = mAdd;
        this.mDistrict = mDistrict;
        this.mState = mState;
        this.mPin = mPin;
        this.mPhone = mPhone;
        this.mMob = mMob;
    }

    public String getName() {
        return mName;
    }

    public String getAdd() {
        return mAdd;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public String getState() {
        return mState;
    }

    public String getPin() {
        return mPin;
    }

    public String getPhone() {
        return mPhone;
    }

    public String getMob() {
        return mMob;
    }
}
