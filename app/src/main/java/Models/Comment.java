package models;



public class Comment {
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("alertId")
    private String alertId;
    @com.google.gson.annotations.SerializedName("createdBy")
    private String createdBy;
    @com.google.gson.annotations.SerializedName("body")
    private String comBody;

    public Comment(String i,String a,String c,String b){
        this.Id=i;
        this.alertId=a;
        this.createdBy=c;
        this.comBody=b;
    }

    public Comment(String a,String c,String b){
        this.alertId=a;
        this.createdBy=c;
        this.comBody=b;
    }

    public String getId() {
        return Id;
    }

    public String getAlertId() {
        return alertId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getBody() {
        return comBody;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setBody(String body) {
        this.comBody = body;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Comment && ((Comment) o).Id == Id;
    }
}
