package models;

public class MediaAsset{
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("data")
    private String data;
    @com.google.gson.annotations.SerializedName("alertId")
    private String alertId;
    @com.google.gson.annotations.SerializedName("extension")
    private String ext;

public MediaAsset()
        {

        }

public MediaAsset(String data, String alert, String ex)
        {
        this.data=data;
            this.alertId=alert;
            this.ext=ex;
        }


    public void setId(String id) {
        Id = id;
    }

    public void setData(String data) {
        this.data = data;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getId() {
        return Id;
    }

    public String getData() {
        return data;
    }

    public String getAlertId() {
        return alertId;
    }

    public String getExt() {
        return ext;
    }
}
