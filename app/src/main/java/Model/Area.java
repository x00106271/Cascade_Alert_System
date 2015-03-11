package Model;

import java.util.ArrayList;

public class Area{

public String Label;

public String GPS;

public int Ordering;

public ArrayList<String> Users;

public ArrayList<String> Addresses;

public ArrayList<String> Alerts;

public Area()
        {

        }

public Area(String itemId, String label, int ordering, String gps, ArrayList<String> users,
            ArrayList<String> addresses, ArrayList<String> alerts)
        {
        // this.Id = itemId;
        this.Label = label;
        this.Ordering = ordering;
        this.GPS = gps;

        Users = new ArrayList<String>(users);
        Addresses = new ArrayList<String>(addresses);
        Alerts = new ArrayList<String>(alerts);
        }
        }
