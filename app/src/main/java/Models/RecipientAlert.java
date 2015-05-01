package models;

public class RecipientAlert{
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("alertId")
    private String alertId;
    @com.google.gson.annotations.SerializedName("recipientType")
    private int rType;
    @com.google.gson.annotations.SerializedName("recipientId")
    private String rId;

    // RecipientType user = 0, group = 1, area = 2, all = 3
    // recipientId = null then broadcast

public RecipientAlert()
        {

        }

public RecipientAlert(String itemId, String alert, int recipientType, String recipientId)
        {
            this.Id=itemId;
            this.alertId=alert;
            this.rType=recipientType;
            this.rId=recipientId;
        }

    public RecipientAlert(String alert, int recipientType, String recipientId)
    {
        this.alertId=alert;
        this.rType=recipientType;
        this.rId=recipientId;
    }

    public String getId() {
        return Id;
    }

    public String getAlertId() {
        return alertId;
    }

    public int getrType() {
        return rType;
    }

    public String getrId() {
        return rId;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public void setrType(int rType) {
        this.rType = rType;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof RecipientAlert && ((RecipientAlert) o).Id == Id;
    }
}
