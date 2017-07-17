package app.com.project.urjaswitk.bloodbook.models;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;

import java.sql.Date;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by UrJasWitK on 09-Jul-17.
 */

public class Requests {



    public interface Columns{
        String COLUMN_REQID= "reqid_col";
        String COLUMN_MSG= "msg_col";//this is replaced to the sender name
        String COLUMN_RECV= "recv_col";
        String COLUMN_SENT= "sent_col";
        String COLUMN_TIME= "time_col";
        String COLUMN_BLOOD= "blood_col";
        String COLUMN_RECV_NAME= "recv_name_col";
    }

    String message, senderId, receiverId, requestId;
    String time, blood, receiverName;

    public Requests(){
        this.message = "";
        this.senderId = "";
        this.receiverId = "";
        this.requestId = "";
        this.receiverName= "";
        time= "";
        blood= "";
    }

    public Requests(String message, String senderId,
                    String receiverId, String requestId,
                    String time, String blood, String recv) {
        this();
        this.message = message;
        this.senderId = senderId;
        if (receiverId!=null){
            this.receiverId = receiverId;
            receiverName= recv;
        }
        this.requestId = requestId;
        this.time= time;
        this.blood= blood;

    }

    public Requests(String message, String senderId,
                    String requestId, String time,
                    String blood, String receiverName) {
        this();
        this.message = message;
        this.senderId = senderId;
        this.requestId = requestId;
        this.time= time;
        this.blood= blood;
        this.receiverName= receiverName;
    }

    public Requests(String message, String senderId,
                     String time, String blood) {
        this.message = message;
        this.senderId = senderId;
        this.time= time;
        this.blood= blood;

    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getBlood() {
        return blood;
    }

    public void setBlood(String blood) {
        this.blood = blood;
    }

    public String getRequestId() {
        Log.e("Utils", "rqId: "+requestId);
        return requestId;
    }

    public String  getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getMessage() {
        Log.e("Utils", "msg: "+message);
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        Log.e("Utils", "senderId: "+senderId);
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        Log.e("Utils", "recvId: "+receiverId);
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;

    }

    public static Bundle getBundleFromRequests(Requests request){
        Bundle data= new Bundle();
        data.putString(Columns.COLUMN_REQID, request.getRequestId());
        data.putString(Columns.COLUMN_RECV, request.getReceiverId());
        data.putString(Columns.COLUMN_SENT, request.getSenderId());
        data.putString(Columns.COLUMN_MSG, request.getMessage());
        data.putString(Columns.COLUMN_TIME, request.getTime());
        data.putString(Columns.COLUMN_BLOOD, request.getBlood());
        data.putString(Columns.COLUMN_RECV_NAME, request.getReceiverName());
        return data;
    }

    public static Requests getRequestsFromBundle(Bundle b){
        return new Requests(
                b.getString(Columns.COLUMN_MSG),
                b.getString(Columns.COLUMN_REQID),
                b.getString(Columns.COLUMN_SENT),
                b.getString(Columns.COLUMN_RECV),
                b.getString(Columns.COLUMN_TIME),
                b.getString(Columns.COLUMN_BLOOD),
                b.getString(Columns.COLUMN_RECV_NAME)
        );
    }

    public static ContentValues getContentValuesFromRequests(
            Requests request){

        ContentValues data= new ContentValues();
        data.put(Columns.COLUMN_RECV, request.getReceiverId());
        data.put(Columns.COLUMN_REQID, request.getRequestId());
        data.put(Columns.COLUMN_SENT, request.getSenderId());
        data.put(Columns.COLUMN_TIME, request.getTime());
        data.put(Columns.COLUMN_MSG, request.getMessage());
        data.put(Columns.COLUMN_BLOOD, request.getBlood());
        data.put(Columns.COLUMN_RECV_NAME, request.getReceiverName());
        return data;
    }

    public static Requests getRequestsFromCursor(
            Cursor cursor){
                return new Requests(
                        cursor.getString(cursor.getColumnIndex(Columns.COLUMN_MSG)),
                        cursor.getString(cursor.getColumnIndex(Columns.COLUMN_SENT)),
                        cursor.getString(cursor.getColumnIndex(Columns.COLUMN_RECV)),
                        cursor.getString(cursor.getColumnIndex(Columns.COLUMN_REQID)),
                        cursor.getString(cursor.getColumnIndex(Columns.COLUMN_TIME)),
                        cursor.getString(cursor.getColumnIndex(Columns.COLUMN_BLOOD)),
                        cursor.getString(cursor.getColumnIndex(Columns.COLUMN_RECV_NAME))
                        );
    }

    public static Requests getRequestsFromContentValues(
                    ContentValues values){
        return new Requests(
                values.getAsString(Columns.COLUMN_MSG),
                values.getAsString(Columns.COLUMN_REQID),
                values.getAsString(Columns.COLUMN_SENT),
                values.getAsString(Columns.COLUMN_RECV),
                values.getAsString(Columns.COLUMN_TIME),
                values.getAsString(Columns.COLUMN_BLOOD),
                values.getAsString(Columns.COLUMN_RECV_NAME)
                );
    }

    public static ContentValues[] addAllRequests(
            List<Requests> list){
        ContentValues[] values= new ContentValues[list.size()];
        int i=0;
        for (Requests req:list)
            values[i++]= getContentValuesFromRequests(req);
        return values;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Requests)) return false;

        Requests requests = (Requests) o;

        if (getMessage() != null ? !getMessage().equals(requests.getMessage()) : requests.getMessage() != null)
            return false;
        if (getSenderId() != null ? !getSenderId().equals(requests.getSenderId()) : requests.getSenderId() != null)
            return false;
        if (getReceiverId() != null ? !getReceiverId().equals(requests.getReceiverId()) : requests.getReceiverId() != null)
            return false;
        if (getRequestId() != null ? !getRequestId().equals(requests.getRequestId()) : requests.getRequestId() != null)
            return false;
        if (getTime() != null ? !getTime().equals(requests.getTime()) : requests.getTime() != null)
            return false;
        if (getBlood() != null ? !getBlood().equals(requests.getBlood()) : requests.getBlood() != null)
            return false;
        return getReceiverName() != null ? getReceiverName().equals(requests.getReceiverName()) : requests.getReceiverName() == null;
    }

    @Override
    public int hashCode() {
        int result = getMessage() != null ? getMessage().hashCode() : 0;
        result = 31 * result + (getSenderId() != null ? getSenderId().hashCode() : 0);
        result = 31 * result + (getReceiverId() != null ? getReceiverId().hashCode() : 0);
        result = 31 * result + (getRequestId() != null ? getRequestId().hashCode() : 0);
        result = 31 * result + (getTime() != null ? getTime().hashCode() : 0);
        result = 31 * result + (getBlood() != null ? getBlood().hashCode() : 0);
        result = 31 * result + (getReceiverName() != null ? getReceiverName().hashCode() : 0);
        return result;
    }
}
