package activities;

        import java.util.HashSet;
        import java.util.Set;

        import android.content.Context;
        import android.content.SharedPreferences;
        import android.os.AsyncTask;
        import android.util.Log;

        import com.google.android.gms.gcm.GoogleCloudMessaging;
        import com.microsoft.windowsazure.messaging.NotificationHub;

public class Notifications2 {
    private static final String PREFS_NAME = "BreakingNewsCategories";
    private GoogleCloudMessaging gcm;
    private NotificationHub hub;
    private Context context;
    private String senderId;

    public Notifications2(Context context, String senderId) {
        this.context = context;
        this.senderId = senderId;

        gcm = GoogleCloudMessaging.getInstance(context);
        String endpoint = "Endpoint=sb://cascadehub-ns.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=F3KuM7FPDIZFdv17uen1i7unt3b+bC2JTXwQ9n3rHY4=";
        hub = new NotificationHub("cascadehub", endpoint, context);
    }

    public void storeCategoriesAndSubscribe(Set<String> categories)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putStringSet("categories", categories).commit();
        subscribeToCategories(categories);
    }

    public void subscribeToCategories(final Set<String> categories) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {
                    String regid = gcm.register(senderId);
                    hub.register(regid, categories.toArray(new String[categories.size()]));
                } catch (Exception e) {
                    Log.e("MainActivity", "Failed to register - " + e.getMessage());
                    return e;
                }
                return null;
            }

            protected void onPostExecute(Object result) {
                //String message = "Subscribed for categories: " + categories.toString();
                //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
            }
        }.execute(null, null, null);
    }

    public Set<String> retrieveCategories() {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        return settings.getStringSet("categories", new HashSet<String>());
    }
}
