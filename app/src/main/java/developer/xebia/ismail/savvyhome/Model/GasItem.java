package developer.xebia.ismail.savvyhome.Model;

/**
 * Created by Arrival Sentosa on 5/20/2016.
 */
public class GasItem {
    String device_id;
    String usage;
    String progress;
    String status;

    public GasItem(String device_id, String usage, String progress, String status) {
        this.device_id = device_id;
        this.usage = usage;
        this.progress = progress;
        this.status = status;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

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
