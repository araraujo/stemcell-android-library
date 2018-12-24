package com.stemcell.android.service.gcm.http;

import android.content.Intent;
import com.google.android.gms.iid.InstanceIDListenerService;

import com.stemcell.android.base.BaseApp;

/**
 * Created by spassu on 09/08/15.
 */
public class LocalInstanceIDListenerService extends InstanceIDListenerService {

    private static final String TAG = "InstanceIDLS";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This call is initiated by the
     * InstanceID provider.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
        Intent intent = new Intent(this, BaseApp.getInstance().getRegisterGCMServiceClass());
        startService(intent);
    }
    // [END refresh_token]

}
