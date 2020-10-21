package com.thegistapp.thegistapp.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import androidx.core.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.thegistapp.thegistapp.R;
import com.thegistapp.thegistapp.activity.StoryListActivity;


public class FCMMessagingService extends FirebaseMessagingService {
    String TAG  =   "PUSH_NOTIFICATION";
    String notificationType =   "";
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
       super.onMessageReceived(remoteMessage);

        final String BAYGPS_EVENTS = "baygps_events";

        Log.e(TAG, "From: " + remoteMessage.getFrom());
        String[] eventData = new String[1];

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Message data payload: " + remoteMessage.getData());
            Log.e(TAG, "Message notification: " + remoteMessage.getNotification().getBody());


            String type = remoteMessage.getData().get("notification_type");
            String message = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            eventData[0]    =   remoteMessage.getData().get("notification_type");
            Intent intentEvent = new Intent(this, StoryListActivity.class);
            intentEvent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intentEvent.putExtra("event_data", eventData);
            PendingIntent customerRebatePendingIntent = PendingIntent.getActivity(this, 0, intentEvent,
                    PendingIntent.FLAG_UPDATE_CURRENT );
            NotificationCompat.Builder customerRebateBuilder = new NotificationCompat.Builder(this )
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(message)
                    .setContentText(text)
                    .setSound(defaultSoundUri)
                    .setAutoCancel(true)
                    .setGroup(BAYGPS_EVENTS)
                    .setContentIntent(customerRebatePendingIntent);


            NotificationManager customerRebateManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            customerRebateManager.notify(0, customerRebateBuilder.build());


            /*switch (type) {
                case "ignitionOn":
                    customerRebateDetails[0]    =   remoteMessage.getData().get("notification_type");
                    Intent customerRebate = new Intent(this, NotificationActivity.class);
                    customerRebate.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    customerRebate.putExtra("customer_points_rebate_confirmation", customerRebateDetails);
                    PendingIntent customerRebatePendingIntent = PendingIntent.getActivity(this, 0, customerRebate,
                            PendingIntent.FLAG_ONE_SHOT);
                    NotificationCompat.Builder customerRebateBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(message)
                            .setContentText(text)
                            .setSound(defaultSoundUri)
                            .setAutoCancel(true)
                            .setContentIntent(customerRebatePendingIntent);
                    NotificationManager customerRebateManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    customerRebateManager.notify(0, customerRebateBuilder.build());
                    break;
            }*/
        }


    }




}
