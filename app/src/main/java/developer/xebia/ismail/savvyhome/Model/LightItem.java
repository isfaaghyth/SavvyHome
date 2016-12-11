package developer.xebia.ismail.savvyhome.Model;

/**
 * Created by Arrival Sentosa on 5/19/2016.
 */
public class LightItem {
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public LightItem(String device_id, String location, String kwh, String status) {
        this.device_id = device_id;
        this.location = location;
        this.kwh = kwh;
        this.status = status;
    }

    String device_id;
    String location;
    String kwh;
    String status;

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getKwh() {
        return kwh;
    }

    public void setKwh(String kwh) {
        this.kwh = kwh;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
