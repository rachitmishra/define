package `in`.ceeq.define.firebase

import `in`.ceeq.define.utils.PreferenceUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token!!
        PreferenceUtils.newInstance(this).uniqueId = refreshedToken
    }
}
