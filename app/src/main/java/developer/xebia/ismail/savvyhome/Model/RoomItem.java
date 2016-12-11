package developer.xebia.ismail.savvyhome.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by Arrival Sentosa on 5/19/2016.
 */
public class RoomItem {
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public RoomItem(String device_id, String room_name, int image) {
        this.device_id = device_id;
        this.room_name = room_name;
        this.image = image;
    }

    String device_id;
    String room_name;

    int image;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
