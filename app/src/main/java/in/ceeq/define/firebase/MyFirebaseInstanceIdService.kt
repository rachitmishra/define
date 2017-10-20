package `in`.ceeq.define.firebase

import `in`.ceeq.define.utils.PreferenceUtils
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import javax.inject.Inject

class MyFirebaseInstanceIdService : FirebaseInstanceIdService() {

    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token!!
        preferenceUtils.uniqueId = refreshedToken
    }
}
