package info.camposha.mrchatview;

import android.graphics.Bitmap;

/**
 * Create a class that represents ChatModel. it will have time,id,message
 * message type and Bitmap
 */
public class ChatModel {
    String time;
    int id;
    String message;
    String messageType;
    Bitmap bitmap;

    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getMessageType() {
        return messageType;
    }
    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
