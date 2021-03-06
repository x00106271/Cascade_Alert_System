package models;

import java.util.Date;

public class Alert{
    @com.google.gson.annotations.SerializedName("Id")
private String Id;
    @com.google.gson.annotations.SerializedName("alertType")
    private int alertType;
    @com.google.gson.annotations.SerializedName("title")
private String title;
    @com.google.gson.annotations.SerializedName("body")
private String body;
    @com.google.gson.annotations.SerializedName("complete")
private boolean complete;
    @com.google.gson.annotations.SerializedName("active")
private boolean active;
    @com.google.gson.annotations.SerializedName("createdBy")
private String createdBy;
    @com.google.gson.annotations.SerializedName("broadcast")
private boolean broadcast;
    @com.google.gson.annotations.SerializedName("priority")
private int priority;
    @com.google.gson.annotations.SerializedName("gpsId")
private String gpsId;
    @com.google.gson.annotations.SerializedName("startDateTime")
private Date startDateTime;
    @com.google.gson.annotations.SerializedName("endDateTime")
private Date endDateTime;

public Alert() {

        }

public Alert(String itemId,int alerttype, String createdBy, String title, String body,
        Date startDateTime, Date endDateTime,
        boolean broadcast, int priority,String gps, boolean active, boolean complete)
        {
            this.Id = itemId;
        this.alertType=alerttype;
        this.createdBy = createdBy;
        this.title = title;
        this.body = body;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.broadcast = broadcast;
        this.priority = priority;
        this.active = active;
        this.complete = complete;
        this.gpsId=gps;
        }

    public Alert(int alerttype, String createdBy, String title, String body,
                 Date startDateTime, Date endDateTime,
                 boolean broadcast, int priority,String gps, boolean active, boolean complete) {
        this.alertType = alerttype;
        this.createdBy = createdBy;
        this.title = title;
        this.body = body;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.broadcast = broadcast;
        this.priority = priority;
        this.active = active;
        this.complete = complete;
        this.gpsId = gps;
    }

    public void setId(String id) {
        Id = id;
    }

    public int getAlertType() {
        return alertType;
    }

    public void setAlertType(int alertType) {
        this.alertType = alertType;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setBroadcast(boolean broadcast) {
        this.broadcast = broadcast;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setGpsId(String gpsId) {
        this.gpsId = gpsId;
    }

    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    public String getId() {
        return Id;
    }



    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public boolean isComplete() {
        return complete;
    }

    public boolean isActive() {
        return active;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public boolean isBroadcast() {
        return broadcast;
    }

    public int getPriority() {
        return priority;
    }

    public String getGpsId() {
        return gpsId;
    }

    public Date getStartDateTime() {
        return startDateTime;
    }

    public Date getEndDateTime() {
        return endDateTime;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Alert && ((Alert) o).Id == Id;
    }

}
