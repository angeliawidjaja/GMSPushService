package com.example.pushnotifangelia;
import static android.app.NotificationManager.IMPORTANCE_HIGH;
import static android.app.PendingIntent.FLAG_ONE_SHOT;

import static com.example.pushnotifangelia.Constants.CHANNEL_ID;
import static com.example.pushnotifangelia.Constants.TOKEN;
import static com.google.firebase.messaging.Constants.TAG;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

/**
 * Created by Angelia Widjaja on 21-Jan-22 11:00.
 */
public class FCMService extends FirebaseMessagingService {
    public static SharedPreferences sharedPref = null;
    public String token;

    public String getToken() {
        return sharedPref != null ? sharedPref.getString(TOKEN, "") : "";
    }

    public static void setToken(String token) {
        if(sharedPref != null)
            sharedPref.edit().putString(TOKEN, token).apply();
        // apply()  -> asynchronous
        // commit() -> synchronous
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(Constants.TAG, "From: " + remoteMessage.getFrom());
        if(remoteMessage.getNotification() != null) {
            Log.d(Constants.TAG, "Message body:" + remoteMessage.getNotification().getBody());
            Log.d(Constants.TAG, "Message title:" + remoteMessage.getNotification().getTitle());
        }

        Intent intent = new Intent(this, MainActivity.class);
        NotificationManager notifManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Integer notifId = new Random().nextInt();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createNotificationChannel(notifManager);
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        //FLAG_ONE_SHOT = Sekali pakai, setelah diclick, notification hilang
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, FLAG_ONE_SHOT);
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle(remoteMessage.getData().get("title"))
                .setContentText(remoteMessage.getData().get("message"))
                .setSmallIcon(R.drawable.ic_baseline_notifications_24)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();

        notifManager.notify(notifId, notification);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createNotificationChannel(NotificationManager notificationManager) {
        String channelName = "AngeliaChannel";
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channelName, IMPORTANCE_HIGH);
        channel.setDescription("Angelia's Channel Description");
        channel.enableLights(true);
        channel.setLightColor(Color.GREEN);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public void onNewToken(@NonNull String newToken) {
        setToken(newToken);
    }
}
