package developer.xebia.ismail.savvyhome.Model;

/**
 * Created by Arrival Sentosa on 5/19/2016.
 */
public class DeviceItem {
    String device_name;
    int image;

    public DeviceItem(String device_name, int image) {
        this.device_name = device_name;
        this.image = image;
    }

    public String getDevice_name() {
        return device_name;
    }

    public void setDevice_name(String device_name) {
        this.device_name = device_name;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
