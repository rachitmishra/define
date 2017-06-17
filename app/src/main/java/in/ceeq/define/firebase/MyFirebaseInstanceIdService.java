package in.ceeq.define.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import in.ceeq.define.utils.PreferenceUtils;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        PreferenceUtils.newInstance(this).setUniqueId(refreshedToken);
    }
}
