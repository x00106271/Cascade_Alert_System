package models;

public class GPS{

public String Label;

public double Lat;

public double Lng;

public double SeaLevel;

public double Radius;

public boolean TrackingPoint;

public GPS()
        {

        }

public GPS(String itemId, String label, double lat, double lng, double radius, boolean trackingPoint, double seaLevel)
        {
        // this.Id = itemId;
        this.Label = label;
        this.Lat = lat;
        this.Lng = lng;
        this.Radius = radius;
        this.TrackingPoint = trackingPoint;
        this.SeaLevel = seaLevel;
        }

        }

