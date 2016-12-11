package developer.xebia.ismail.savvyhome.Model;

/**
 * Created by Arrival Sentosa on 5/19/2016.
 */
public class GalloonItem {
    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public GalloonItem(String device_id) {
        this.device_id = device_id;
    }

    String device_id;
    String usage;
    String progress;
    String status;

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
