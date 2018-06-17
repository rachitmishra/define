package `in`.ceeq.define.firebase


import `in`.ceeq.define.BuildConfig
import `in`.ceeq.define.R
import `in`.ceeq.define.firebase.MyFirebaseMessageService.Intents.FORCE_UPDATE
import `in`.ceeq.define.firebase.MyFirebaseMessageService.Intents.NOTIFY
import `in`.ceeq.define.firebase.MyFirebaseMessageService.Intents.TRACKING
import `in`.ceeq.define.firebase.MyFirebaseMessageService.Intents.UPDATE
import `in`.ceeq.define.ui.home.HomeActivity
import `in`.ceeq.define.utils.LogUtils
import `in`.ceeq.define.utils.PreferenceUtils
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import javax.inject.Inject

class MyFirebaseMessageService : FirebaseMessagingService() {

    private var mNotificationManager: NotificationManager? = null

    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    private val randomNotificationId: Int
        get() = Random().nextInt(
                MAX_LIMIT_RANDOM_NOTIFICATION_ID - MIN_LIMIT_RANDOM_NOTIFICATION_ID + 1) + MIN_LIMIT_RANDOM_NOTIFICATION_ID

    object Intents {
        val TRACKING = "t"
        val UPDATE = "u"
        val FORCE_UPDATE = "f"
        val NOTIFY = "n"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        mNotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = remoteMessage!!.data[INTENT]

        if (intent.isNullOrBlank()) {
            val payload = remoteMessage.data[PAYLOAD]
            when (intent) {
                TRACKING -> {
                }
                UPDATE -> buildUpdateNotification(false)
                FORCE_UPDATE -> {
                    val updateVersion = remoteMessage.data[UPDATE_VERSION]
                    if (!preferenceUtils.isUpdateAvailable && preferenceUtils.updateVersion == 0) {
                        val appVersionCode = BuildConfig.VERSION_CODE
                        if (appVersionCode <= Integer.valueOf(updateVersion)) {
                            preferenceUtils.setForceUpgradeStatus(true, Integer.valueOf(updateVersion)!!)
                            buildUpdateNotification(true)
                        }
                    }
                }
                NOTIFY -> {
                    val myFcmMessage = Gson().fromJson(payload, MyFirebaseMessage::class.java)
                    if (null != myFcmMessage) {
                        buildCustomNotification(myFcmMessage)
                    }
                }
            }
        }
    }

    private fun buildUpdateNotification(isForced: Boolean) {
        val message = getString(R.string.update_available_notification,
                getString(R.string.app_name))
        val intentMarket = Intent(Intent.ACTION_VIEW)
        val packageName = packageName
        val appName = getString(R.string.app_name)
        intentMarket.data = Uri.parse(GOOGLE_PLAY_URL + packageName)
        val intent = PendingIntent.getActivity(this, 0, intentMarket, 0)

        val notification = NotificationCompat.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentTitle(appName)
                .setContentText(message)
                .setAutoCancel(!isForced)
                .setSmallIcon(R.drawable.ic_status)
                .setContentIntent(intent).build()

        mNotificationManager!!.notify(NOTIFICATION_ID, notification)
    }

    private fun buildCustomNotification(myFcmMessage: MyFirebaseMessage?) {
        var intentApp = Intent(this, HomeActivity::class.java)

        if (myFcmMessage!!.isUrlNotification) {
            intentApp = Intent(Intent.ACTION_VIEW)
            intentApp.data = Uri.parse(myFcmMessage.url)
        }

        val localNotificationId = randomNotificationId

        intentApp.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val intent = PendingIntent.getActivity(this, 0, intentApp, PendingIntent.FLAG_UPDATE_CURRENT)
        val builder = NotificationCompat.Builder(this)
                .setColor(ContextCompat.getColor(this, R.color.colorAccent))
                .setContentText(myFcmMessage.message)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_status)
                .setContentIntent(intent)

        var title = myFcmMessage.title
        if (!title.isBlank()) {
            title = getString(R.string.app_name)
        }
        builder.setContentTitle(title)

        if (!myFcmMessage.subText.isBlank()) {
            builder.setSubText(myFcmMessage.subText)
        }

        if (myFcmMessage.imageUrl.isBlank()) {
            val notificationStyle = NotificationCompat.BigTextStyle().bigText(myFcmMessage.message)
            if (myFcmMessage.summary.isNotEmpty()) {
                notificationStyle.setSummaryText(myFcmMessage.summary)
            }
            builder.setStyle(notificationStyle)
        } else {
            val notificationStyle = NotificationCompat.BigPictureStyle()
            notificationStyle.setBigContentTitle(myFcmMessage.title)
            val notificationPicUrl = myFcmMessage.imageUrl
            var notificationBigPicture: Bitmap? = null
            try {
                notificationBigPicture = BitmapFactory.decodeStream(URL(notificationPicUrl).content as InputStream)
            } catch (e: IOException) {
                LogUtils.log(e)
            }

            if (null != notificationBigPicture) {
                notificationStyle.bigPicture(notificationBigPicture)
                if (myFcmMessage.summary.isNotEmpty()) {
                    notificationStyle.setSummaryText(myFcmMessage.summary)
                }
            }
            builder.setStyle(notificationStyle)
        }

        mNotificationManager!!.notify(localNotificationId, builder.build())
    }

    companion object {

        private val GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id="
        val PAYLOAD = "p"
        val INTENT = "i"
        val UPDATE_VERSION = "uv"

        val MAX_LIMIT_RANDOM_NOTIFICATION_ID = 10000
        val MIN_LIMIT_RANDOM_NOTIFICATION_ID = 0
        val NOTIFICATION_ID = 7001
    }
}
