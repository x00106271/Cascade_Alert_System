package models;

import java.util.ArrayList;

public class Area{
    @com.google.gson.annotations.SerializedName("Id")
    private String Id;
    @com.google.gson.annotations.SerializedName("label")
    private String label;
    @com.google.gson.annotations.SerializedName("gps")
    private String gps;
    @com.google.gson.annotations.SerializedName("ordering")
    private int ordering;
    @com.google.gson.annotations.SerializedName("parent")
    private String parent;
    public ArrayList<String> Users;

    public ArrayList<String> Addresses;

    public ArrayList<String> Alerts;

    public Area()
        {

        }

public Area(String itemId, String label, String gps,int ordering, String parent)
        {
        this.Id = itemId;
        this.label = label;
        this.gps=gps;
        this.ordering = ordering;
        this.parent=parent;

       }

    public void setId(String id) {
        Id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public void setOrdering(int ordering) {
        this.ordering = ordering;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getId() {
        return Id;
    }

    public String getLabel() {
        return label;
    }

    public String getGps() {
        return gps;
    }

    public int getOrdering() {
        return ordering;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Area && ((Area) o).Id == Id;
    }
}
