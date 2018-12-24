package com.stemcell.android.service.gcm.http;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import java.io.IOException;
import com.stemcell.android.base.BaseApp;
import com.stemcell.android.exception.GcmRegistrationException;
import com.stemcell.android.util.Constants;

public abstract class AbstractRegisterGCMService extends IntentService {

    private static final String TAG = "RegisterGCMService";
    private static final String[] TOPICS = {"global"};

    public AbstractRegisterGCMService(){
        super(TAG);
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            // In the (unlikely) event that multiple refresh operations occur simultaneously, Ensure that they are processed sequentially.
            synchronized (TAG) {
                InstanceID instanceId = InstanceID.getInstance(getApplicationContext());
                String token = instanceId.getToken(BaseApp.getInstance().getSenderId(), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                String imei = BaseApp.getInstance().getImei();

                sendRegistrationToServer(token, imei);

                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, true).apply();
                sharedPreferences.edit().putString(Constants.TOKEN_REGISTER_ID, token).apply();
            }

        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(Constants.SENT_TOKEN_TO_SERVER, false).apply();
            sharedPreferences.edit().remove(Constants.TOKEN_REGISTER_ID);

            Intent intentClose = new Intent(Constants.ACTION_CLOSE_ALL);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intentClose);

            throw new GcmRegistrationException(e);
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Constants.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     *
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    protected abstract void sendRegistrationToServer(String token, String imei);

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]


}
