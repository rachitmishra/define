package in.ceeq.define.firebase;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Random;

import in.ceeq.define.BuildConfig;
import in.ceeq.define.MainActivity;
import in.ceeq.define.R;
import in.ceeq.define.utils.LogUtils;
import in.ceeq.define.utils.PreferenceUtils;
import in.ceeq.define.utils.Utils;

import static in.ceeq.define.firebase.MyFirebaseMessageService.Intents.FORCE_UPDATE;
import static in.ceeq.define.firebase.MyFirebaseMessageService.Intents.NOTIFY;
import static in.ceeq.define.firebase.MyFirebaseMessageService.Intents.TRACKING;
import static in.ceeq.define.firebase.MyFirebaseMessageService.Intents.UPDATE;

public class MyFirebaseMessageService extends FirebaseMessagingService {

    private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";
    public static final String PAYLOAD = "p";
    public static final String INTENT = "i";
    public static final String UPDATE_VERSION = "uv";

    public class Intents {
        public static final String TRACKING = "t";
        public static final String UPDATE = "u";
        public static final String FORCE_UPDATE = "f";
        public static final String NOTIFY = "n";
    }

    public static final int MAX_LIMIT_RANDOM_NOTIFICATION_ID = 10000;
    public static final int MIN_LIMIT_RANDOM_NOTIFICATION_ID = 0;
    public static final int NOTIFICATION_ID = 7001;

    private NotificationManager mNotificationManager;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String intent = remoteMessage.getData().get(INTENT);

        if (!Utils.isEmptyString(intent)) {
            String payload = remoteMessage.getData().get(PAYLOAD);
            switch (intent) {
                case TRACKING:
                    break;
                case UPDATE:
                    buildUpdateNotification(false);
                    break;
                case FORCE_UPDATE:
                    String updateVersion = remoteMessage.getData().get(UPDATE_VERSION);
                    PreferenceUtils preferenceUtils = PreferenceUtils.newInstance(this);
                    if (!preferenceUtils.isUpdateAvailable() &&
                            preferenceUtils.getUpdateVersion() == 0) {
                        int appVersionCode = BuildConfig.VERSION_CODE;
                        if (appVersionCode <= Integer.valueOf(updateVersion)) {
                            preferenceUtils.setForceUpgradeStatus(true, Integer.valueOf(updateVersion));
                            buildUpdateNotification(true);
                        }
                    }
                    break;
                case NOTIFY:
                    MyFirebaseMessage myFcmMessage = new Gson().fromJson(payload, MyFirebaseMessage.class);
                    if (null != myFcmMessage) {
                        buildCustomNotification(myFcmMessage);
                    }
                    break;
            }
        }
    }

    private void buildUpdateNotification(boolean isForced) {
        String message = getString(R.string.update_available_notification,
                getString(R.string.app_name));
        Intent intentMarket = new Intent(Intent.ACTION_VIEW);
        String packageName = getPackageName();
        String appName = getString(R.string.app_name);
        intentMarket.setData(Uri.parse(GOOGLE_PLAY_URL + packageName));
        PendingIntent intent = PendingIntent.getActivity(this, 0, intentMarket, 0);

        Notification notification = new NotificationCompat.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentTitle(appName)
                .setContentText(message)
                .setAutoCancel(!isForced)
                .setSmallIcon(R.drawable.ic_status)
                .setContentIntent(intent).build();

        mNotificationManager.notify(NOTIFICATION_ID, notification);
    }

    private int getRandomNotificationId() {
        return new Random().nextInt(
                (MAX_LIMIT_RANDOM_NOTIFICATION_ID
                        - MIN_LIMIT_RANDOM_NOTIFICATION_ID) + 1)
                + MIN_LIMIT_RANDOM_NOTIFICATION_ID;
    }

    private void buildCustomNotification(MyFirebaseMessage myFcmMessage) {
        Intent intentApp = new Intent(this, MainActivity.class);

        if (myFcmMessage.isUrlNotification()) {
            intentApp = new Intent(Intent.ACTION_VIEW);
            intentApp.setData(Uri.parse(myFcmMessage.url));
        }

        int localNotificationId = getRandomNotificationId();

        intentApp.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, intentApp, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                        .setContentText(myFcmMessage.message)
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_status)
                        .setContentIntent(intent);

        String title = myFcmMessage.title;
        if (Utils.isEmptyString(title)) {
            title = getString(R.string.app_name);
        }
        builder.setContentTitle(title);

        if (!Utils.isEmptyString(myFcmMessage.subText)) {
            builder.setSubText(myFcmMessage.subText);
        }

        if (Utils.isEmptyString(myFcmMessage.imageUrl)) {
            NotificationCompat.BigTextStyle notificationStyle =
                    new NotificationCompat.BigTextStyle().bigText(myFcmMessage.message);
            if (!Utils.isEmptyString(myFcmMessage.summary)) {
                notificationStyle.setSummaryText(myFcmMessage.summary);
            }
            builder.setStyle(notificationStyle);
        } else {
            NotificationCompat.BigPictureStyle notificationStyle = new NotificationCompat.BigPictureStyle();
            notificationStyle.setBigContentTitle(myFcmMessage.title);
            String notificationPicUrl = myFcmMessage.imageUrl;
            Bitmap notificationBigPicture = null;
            try {
                notificationBigPicture =
                        BitmapFactory.decodeStream((InputStream) new URL(notificationPicUrl).getContent());
            } catch (IOException e) {
                LogUtils.LOG(e);
            }

            if (null != notificationBigPicture) {
                notificationStyle.bigPicture(notificationBigPicture);
                if (!Utils.isEmptyString(myFcmMessage.summary)) {
                    notificationStyle.setSummaryText(myFcmMessage.summary);
                }
            }
            builder.setStyle(notificationStyle);
        }

        mNotificationManager.notify(localNotificationId, builder.build());
    }
}
