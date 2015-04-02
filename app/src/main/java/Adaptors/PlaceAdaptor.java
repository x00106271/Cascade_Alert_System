// displays autocomplete address from google API
package adaptors;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import services.Constants;

public class PlaceAdaptor extends ArrayAdapter<String> implements Filterable{

    private ArrayList<String> resultList;
    private static final String TAG = "Place Address";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private Context context;

    public PlaceAdaptor(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    @Override
    public int getCount() {
        return resultList.size();}

    @Override
    public String getItem(int position) {
        return resultList.get(position);}

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (constraint != null) {
                    resultList = autocomplete(constraint.toString());
                }
                else{
                    resultList = new ArrayList<String>();
                }
                filterResults.values = resultList;
                filterResults.count = resultList.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint,FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

// get the array list of addresses
private ArrayList<String> autocomplete(String input) {
    ArrayList<String> resultList = null;

    HttpURLConnection conn = null;
    StringBuilder jsonResults = new StringBuilder();
    try {
        StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
        sb.append("?key=" + Constants.GOOGLE_PLACES_API);
        sb.append("&components=country:ie");
        sb.append("&input=" + URLEncoder.encode(input, "utf8"));

        URL url = new URL(sb.toString());
        conn = (HttpURLConnection) url.openConnection();
        InputStreamReader in = new InputStreamReader(conn.getInputStream());

        // Load the results into a StringBuilder
        int read;
        char[] buff = new char[1024];
        while ((read = in.read(buff)) != -1) {
            jsonResults.append(buff, 0, read);
        }
    } catch (MalformedURLException e) {
        Log.e(TAG, "Error processing Places API URL", e);
        return resultList;
    } catch (IOException e) {
        Log.e(TAG, "Error connecting to Places API", e);
        return resultList;
    } finally {
        if (conn != null) {
            conn.disconnect();
        }
    }

    try {
        // Create a JSON object hierarchy from the results
        JSONObject jsonObj = new JSONObject(jsonResults.toString());
        JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

        // Extract the Place descriptions from the results
        resultList = new ArrayList<String>(predsJsonArray.length());
        for (int i = 0; i < predsJsonArray.length(); i++) {
            resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
        }
    } catch (JSONException e) {
        Log.e(TAG, "Cannot process JSON results", e);
    }

    return resultList;
}

}
