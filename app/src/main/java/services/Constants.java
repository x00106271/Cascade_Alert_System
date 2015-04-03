package services;


import android.location.Address;
import android.location.Location;

import java.util.List;

public final class Constants {

    public static String MOBILE_SERVICE_URL = "";
    public static String MOBILE_SERVICE_APPLICATION_KEY = "";
    public static String GOOGLE_PLACES_API="";

    //for geocoder
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static Location LOCATION=null;
    public static List<Address> ADDRESSLIST=null;
}
