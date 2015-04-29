package services;


import android.location.Address;
import android.location.Location;
import java.util.List;

public final class Constants {

    public static String MOBILE_SERVICE_URL = "https://cascade.azure-mobile.net/";
    public static String MOBILE_SERVICE_APPLICATION_KEY = "TVkGQDWdaeNNbNKirTHAknOUmHqWgu76";
    public static String GOOGLE_PLACES_API="AIzaSyD0PMnTgVwCCofclYlklu4uxvW9eAZjiWU";

    //for geocoder
    public static final int SUCCESS_RESULT = 0;
    public static final int FAILURE_RESULT = 1;
    public static final int SUCCESS_RESULT_GPS = 2;
    public static final int FAILURE_RESULT_GPS = 3;
    public static Location LOCATION=null;
    public static List<Address> ADDRESSLIST=null;
    public static Address ADDRESS_GPS=null;

}
