package app.com.project.urjaswitk.bloodbook.models;

/**
 * Created by UrJasWitK on 11-Jul-17.
 */

public class BloodBanks {

    private String mName;
    private String mAdd;
    private String mCity;
    private String mDistrict;
    private String mState;
    private String mPin;
    private String mPhone;

    public BloodBanks(String mName, String mAdd, String mCity, String mDistrict, String mState, String mPin, String mPhone) {
        this.mName = mName;
        this.mAdd = mAdd;
        this.mCity = mCity;
        this.mDistrict = mDistrict;
        this.mState = mState;
        this.mPin = mPin;
        this.mPhone = mPhone;
    }

    public String getName() {
        return mName;
    }

    public String getAdd() {
        return mAdd;
    }

    public String getCity() {
        return mCity;
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
}
