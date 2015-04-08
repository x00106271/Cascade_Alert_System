package services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.util.Log;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchCordinatesIntentService extends IntentService {

    private final String TAG = "co-ordinates service";
    protected ResultReceiver mReceiver;

    public FetchCordinatesIntentService() {
        super("address_gps intent");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        mReceiver=intent.getParcelableExtra("receiver");
        String errorMessage = "";
        String location = intent.getStringExtra("address");
        List<Address> addresses = null;

        try {
            addresses = geocoder.getFromLocationName(location,1);
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = "service_not_available";
            Log.e(TAG, errorMessage, ioException);
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            errorMessage = "invalid_lat_long_used";
            Log.e(TAG, errorMessage);
        }

        // Handle case where no gps was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = "no_gps_found";
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(Constants.FAILURE_RESULT_GPS, errorMessage);
        } else {
            Log.i(TAG, "gps_found");
            Constants.ADDRESS_GPS=addresses.get(0);
            deliverResultToReceiver(Constants.SUCCESS_RESULT_GPS,"address_found");
        }
    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString("returning",message);
        mReceiver.send(resultCode, bundle);
    }
}


