package com.oneness.fdxmerchant.Utils.PushNotification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.oneness.fdxmerchant.Activity.EntryPoint.SplashActivity;
import com.oneness.fdxmerchant.R;
import com.oneness.fdxmerchant.Utils.DialogView;
import com.oneness.fdxmerchant.Utils.Prefs;


@RequiresApi(api = Build.VERSION_CODES.M)
public class FirebaseMessageReceiver extends FirebaseMessagingService {

    final static String CHANNEL_ID = "1";
    static String TAG = "pushdata";
    Prefs prefs;
    DialogView dialogView;

    //NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NotificationManager.class); // If you are writting code in fragment


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        prefs = new Prefs(getApplicationContext());
        prefs.saveData("hasNoti", "y");
        dialogView = new DialogView();
        //Log.d("type>>",remoteMessage.getData().get("type"));
        Log.d("notification>>", remoteMessage.getNotification().toString());
        String extra = "";
        //createNotificationChannel();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            if (remoteMessage.getData().get("extra").equals("")){
//                extra = "";
//            }else{
//                extra = remoteMessage.getData().get("extra");
//            }
            issueNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
            //dialogView.errorButtonDialog(this, getResources().getString(R.string.app_name), remoteMessage.getNotification().getBody());
        } else {
            addNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("message"));
        }
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void addNotification2aa(String id, String name, int importance) {
        Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.tone_test);
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes. CONTENT_TYPE_SONIFICATION )
                .setUsage(AudioAttributes. USAGE_ALARM )
                .build() ;
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true);
        channel.setSound(sound, audioAttributes);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
        Log.d("callo>>","callo");
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    void issueNotification(String title, String msg) {
        Log.d("In>>", "in issue notification");
        Log.d("msg>>", msg);
        //Log.d("extra>>",extra);
        int notificationId = 001;
        Intent viewIntent = new Intent(this, SplashActivity.class);
        //PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(getApplicationContext(),
                0,
                viewIntent, PendingIntent.FLAG_MUTABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addNotification2aa("CHANNEL_1", "Echannel", NotificationManager.IMPORTANCE_DEFAULT);
        }
//        NotificationCompat.Builder notification =
//                new NotificationCompat.Builder(this, "CHANNEL_1");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
//        notification
//                //.setSmallIcon(R.drawable.ic_stat_name)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setLargeIcon(bitmap)
//                .setLargeIcon(bitmap)
//                .setAutoCancel(true)
//                .setContentIntent(viewPendingIntent)
//                .setNumber(3);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//        assert notificationManager != null;
//        notificationManager.notify(1, notification.build());
        // Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/tone_test");
        //Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.tone_test);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.tone_test2);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "CHANNEL_1");
//        notificationBuilder.setAutoCancel(true)
//                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
//                //.setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.fdxlogo)
//                .setTicker(title)
//                .setPriority(Notification.PRIORITY_MAX)
//                .setSound(sound)
//                .setContentTitle(title)
//                .setContentText(msg);
//
//        notificationManager.notify(1, notificationBuilder.build());

        NotificationManager notificationManager1 = (NotificationManager) getApplicationContext().getSystemService(NotificationManager.class); // If you are writting code in fragment

        CharSequence name = "fdxAgent";
        String description = "FDX-Rider";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("cn_fdx_agent_id", name, importance);
        channel.setDescription(description);
        channel.enableLights(true); channel.enableVibration(true);
        channel.setSound(sound, audioAttributes);
        notificationManager.createNotificationChannel(channel);

        Log.d("fire1>>","fire1");
        //Toast.makeText(getApplicationContext(), "test", Toast.LENGTH_SHORT).show();

        final MediaPlayer mp = MediaPlayer.create(this, R.raw.tone_test);
        mp.start();


        //Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
        //Toast toast = Toast.makeText(this, "Toast", Toast.LENGTH_SHORT);
        //toast.show();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(String msg, String title) {
        Log.d("In>>", "in add notification");
        int notificationId = 001;
        Intent viewIntent = new Intent(this, SplashActivity.class);
        PendingIntent viewPendingIntent = PendingIntent.getActivity(this, 0, viewIntent, PendingIntent.FLAG_IMMUTABLE);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        //Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/raw/tone_test");
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        //Uri sound = Uri.parse("android.resource://" + getApplicationContext().getPackageName() + "/" + R.raw.tone_test);
        int myColor = getResources().getColor(R.color.colorAccent);
        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.tone_test2);
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_ALARM)
                .build();
//        Notification mNotification = new NotificationCompat.Builder(this)
//                //.setSmallIcon(R.drawable.ic_stat_name)
//                //.setColor(myColor)
//                .setContentTitle(title)
//                .setContentText(msg)
//                .setLargeIcon(bitmap)
//                .setSound(sound)
//                //.setSound(defaultSoundUri)
//                .setContentIntent(viewPendingIntent).build();
//
//        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(notificationId, mNotification);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(NotificationManager.class); // If you are writting code in fragment

        CharSequence name = "mychannel";
        String description = "testing";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("cnid", name, importance);
        channel.setDescription(description);
        channel.enableLights(true); channel.enableVibration(true);
        channel.setSound(sound, audioAttributes);
        notificationManager.createNotificationChannel(channel);

        Log.d("fire2>>","fire2");
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FIRE-TOKEN", token);
        //sendTokenToTheAppServer(token);
    }

//    private void createNotificationChannel() {
//        Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getApplicationContext().getPackageName() + "/" + R.raw.tone_test); //Here is FILE_NAME is the name of file that you want to play
//// Create the NotificationChannel, but only on API 26+ because
//// the NotificationChannel class is new and not in the support library if
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
//        {
//            CharSequence name = "mychannel";
//            String description = "testing";
//            int importance = NotificationManager.IMPORTANCE_DEFAULT;
//            AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                    .setUsage(AudioAttributes.USAGE_ALARM)
//                    .build();
//            NotificationChannel channel = new NotificationChannel("cnid", name, importance);
//            channel.setDescription(description);
//            channel.enableLights(true); channel.enableVibration(true);
//            channel.setSound(sound, audioAttributes);
//            notificationManager.createNotificationChannel(channel);
//        }
//    };



    /*final  static String CHANNEL_ID="1";
    static String TAG="pushdata";
    Prefs prefs;
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage)
    {
        prefs = new Prefs(getApplicationContext());
        prefs.saveData("hasNoti", "y");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            issueNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }
        else
        {
            addNotification(remoteMessage.getData().get("title"),remoteMessage.getData().get("message"));
        }
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void addNotification2aa(String id, String name, int importance)
    {
        NotificationChannel channel = new NotificationChannel(id, name, importance);
        channel.setShowBadge(true);
        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        assert notificationManager != null;
        notificationManager.createNotificationChannel(channel);
    }


    void issueNotification(String title,String msg)
    {
        int notificationId = 001;
        Intent viewIntent = new Intent(this, SplashActivity.class);
        PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            addNotification2aa("CHANNEL_1", "Echannel", NotificationManager.IMPORTANCE_DEFAULT);
        }
        NotificationCompat.Builder notification =
                new NotificationCompat.Builder(this, "CHANNEL_1");
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        notification
                //.setSmallIcon(R.drawable.ic_stat_name)
                .setContentTitle(title)
                .setContentText(msg)
                .setLargeIcon(bitmap)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentIntent(viewPendingIntent)
                .setNumber(3);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        assert notificationManager != null;
        notificationManager.notify(1, notification.build());

    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addNotification(String msg,String title)
    {
        int notificationId = 001;
        Intent viewIntent = new Intent(this, SplashActivity.class);
        PendingIntent viewPendingIntent =PendingIntent.getActivity(this, 0, viewIntent, 0);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        int myColor =getResources().getColor(R.color.colorAccent);
        Notification mNotification =new NotificationCompat.Builder(this)
                //.setSmallIcon(R.drawable.ic_stat_name)
                //.setColor(myColor)
                .setContentTitle(title)
                .setContentText(msg)
                .setLargeIcon(bitmap)
                .setContentIntent(viewPendingIntent).build();

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, mNotification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("FIRE-TOKEN", token);
        //sendTokenToTheAppServer(token);
    }*/
}
