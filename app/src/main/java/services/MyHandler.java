package services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.cascadealertsystem.R;
import com.microsoft.windowsazure.notifications.NotificationsHandler;

import activities.MainActivity;

public class MyHandler extends NotificationsHandler{

    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    Context ctx;

    @Override
    public void onReceive(Context context, Bundle bundle) {
        ctx = context;
        String nhMessage = bundle.getString("msg");
        //System.out.println("********************** " + nhMessage);
        sendNotification(nhMessage, context);
    }

    private void sendNotification(String msg, Context context) {
        mNotificationManager = (NotificationManager)
                ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0,
                new Intent(ctx, MainActivity.class), 0);
//Demo
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(ctx)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Cascade Alert System")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg))
                        .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRegistered(Context context,  final String gcmRegistrationId) {
        super.onRegistered(context, gcmRegistrationId);

        new AsyncTask<Void, Void, Void>() {

            protected Void doInBackground(Void... params) {
                try {
                    MobileService.mClient.getPush().register(gcmRegistrationId, null);
                    return null;
                }
                catch(Exception e) {
                    // handle error
                }
                return null;
            }
        }.execute();
    }
}
