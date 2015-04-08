package models;

public class GPS{

    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("label")
    private String Label;
    @com.google.gson.annotations.SerializedName("lat")
    private double Lat;
    @com.google.gson.annotations.SerializedName("lng")
    private double Lng;
    @com.google.gson.annotations.SerializedName("radius")
    private double Radius;
    @com.google.gson.annotations.SerializedName("trackingpoint")
    private boolean TrackingPoint;
    @com.google.gson.annotations.SerializedName("address")
    private String Address;
    @com.google.gson.annotations.SerializedName("sealevel")
    private double SeaLevel;

    public GPS()
    {

    }

public GPS(String itemId, String label, double lat, double lng, double radius, boolean trackingPoint,String address, double seaLevel)
        {
        this.Id = itemId;
        this.Label = label;
        this.Lat = lat;
        this.Lng = lng;
        this.Radius = radius;
        this.TrackingPoint = trackingPoint;
            this.Address=address;
        this.SeaLevel = seaLevel;
        }

    public GPS(String label, double lat, double lng, double radius, boolean trackingPoint,String address, double seaLevel)
    {
        this.Label = label;
        this.Lat = lat;
        this.Lng = lng;
        this.Radius = radius;
        this.TrackingPoint = trackingPoint;
        this.Address=address;
        this.SeaLevel = seaLevel;
    }

    public void setId(String id) {
        Id = id;
    }

    public void setLabel(String label) {
        Label = label;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public void setLng(double lng) {
        Lng = lng;
    }

    public void setRadius(double radius) {
        Radius = radius;
    }

    public void setTrackingPoint(boolean trackingPoint) {
        TrackingPoint = trackingPoint;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public void setSeaLevel(double seaLevel) {
        SeaLevel = seaLevel;
    }

    public String getId() {
        return Id;
    }

    public String getLabel() {
        return Label;
    }

    public double getLat() {
        return Lat;
    }

    public double getLng() {
        return Lng;
    }

    public double getRadius() {
        return Radius;
    }

    public boolean isTrackingPoint() {
        return TrackingPoint;
    }

    public String getAddress() {
        return Address;
    }

    public double getSeaLevel() {
        return SeaLevel;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GPS && ((GPS) o).Id == Id;
    }
}

