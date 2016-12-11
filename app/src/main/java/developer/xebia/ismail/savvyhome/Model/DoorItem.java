package developer.xebia.ismail.savvyhome.Model;

/**
 * Created by Arrival Sentosa on 5/20/2016.
 */
public class DoorItem {
    String device_id;
    String location;
    String status;

    public DoorItem(String device_id, String location, String status) {
        this.device_id = device_id;
        this.location = location;
        this.status = status;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
