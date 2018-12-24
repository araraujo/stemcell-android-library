package com.stemcell.android.service.gcm.xmpp;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import java.io.IOException;
import com.stemcell.android.base.BaseApp;
import de.greenrobot.event.EventBus;

public abstract class GcmIntentService extends IntentService {

    private static final String TAG = "GcmListenerService";
    public static final String KEY_MSG_ID = "keyMsgId";
    public static final long GCM_DEFAULT_TTL = 2 * 24 * 60 * 60 * 1000; // two days
    public static final String ACTION_REGISTER = "register";
    public static final String ACTION_UNREGISTER = "unregister";
    public static final String ACTION_ECHO = "echo";
    public static final String KEY_EVENT_TYPE = "keyEventbusType";
    public static final String KEY_ACCOUNT = "keyAccount";
    public static final String KEY_REG_ID = "keyRegId";
    public static final String KEY_STATE = "keyState";
    public static final String ACTION = "action";
    public static final String KEY_MESSAGE_TXT = "keyMessageTxt";

    private NotificationManager mNotificationManager;
    private String mSenderId = null;

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mSenderId = BaseApp.getInstance().getSenderId();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        // action handling for actions of the activity
        String action = intent.getAction();
        Log.v(TAG, "action: " + action);
        if (action.equals(ACTION_REGISTER)) {
            register(gcm, intent);
        } else if (action.equals(ACTION_UNREGISTER)) {
            unregister(gcm, intent);
        } else if (action.equals(ACTION_ECHO)) {
            sendMessage(gcm, intent);
        }

        // handling of stuff as described on http://developer.android.com/google/gcm/client.html
        try {
            Bundle extras = intent.getExtras();
            // The getMessageType() intent parameter must be the intent you received in your BroadcastReceiver.
            String messageType = gcm.getMessageType(intent);

            if (extras != null && !extras.isEmpty()) { // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that
             * GCM will be extended in the future with new message types, just
             * ignore any message types you're not interested in, or that you
             * don't recognize.
             */
                if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                    Log.e(TAG, "Send error: " + extras.toString());
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                    Log.e(TAG, "Deleted messages on server: " + extras.toString());
                    // If it's a regular GCM message, do some work.
                } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                    // Post notification of received message.
                    messageHandler(intent);
                    Log.i(TAG, "Received: " + extras.toString());
                }
            }
        } finally {
            // Release the wake lock provided by the WakefulBroadcastReceiver.
            GcmXmppReceiver.completeWakefulIntent(intent);
        }
    }

    private void unregister(GoogleCloudMessaging gcm, Intent intent) {
        try {
            Log.v(TAG, "about to unregister...");
            gcm.unregister();
            Log.v(TAG, "device unregistered");

            // Persist the regID - no need to register again.
            removeRegistrationId();
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_EVENT_TYPE, EventbusMessageType.UNREGISTRATION_SUCCEEDED.ordinal());
            EventBus.getDefault().post(bundle);
        } catch (IOException e) {
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.

            // I simply notify the user:
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_EVENT_TYPE,
                    EventbusMessageType.UNREGISTRATION_FAILED.ordinal());
            EventBus.getDefault().post(bundle);
            Log.e(TAG, "Unregistration failed", e);
        }
    }

    private void register(GoogleCloudMessaging gcm, Intent intent) {
        try {
            Log.v(TAG, "about to register...");
            String regid = gcm.register(mSenderId);
            Log.v(TAG, "device registered: " + regid);

            String account = intent.getStringExtra(KEY_ACCOUNT);
            sendRegistrationIdToBackend(gcm, regid, account);

            // Persist the regID - no need to register again.
            storeRegistrationId(regid);
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_EVENT_TYPE,
                    EventbusMessageType.REGISTRATION_SUCCEEDED.ordinal());
            bundle.putString(KEY_REG_ID, regid);
            EventBus.getDefault().post(bundle);
        } catch (IOException e) {
            // If there is an error, don't just keep trying to register.
            // Require the user to click a button again, or perform
            // exponential back-off.

            // I simply notify the user:
            Bundle bundle = new Bundle();
            bundle.putInt(KEY_EVENT_TYPE,
                    EventbusMessageType.REGISTRATION_FAILED.ordinal());
            EventBus.getDefault().post(bundle);
            Log.e(TAG, "Registration failed", e);
        }
    }

    private void storeRegistrationId(String regId) {
        final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.i(TAG, "Saving regId to prefs: " + regId);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(KEY_REG_ID, regId);
        editor.putInt(KEY_STATE, State.REGISTERED.ordinal());
        editor.apply();
    }

    private void removeRegistrationId() {
        final SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        Log.i(TAG, "Removing regId from prefs");
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(KEY_REG_ID);
        editor.putInt(KEY_STATE, State.UNREGISTERED.ordinal());
        editor.apply();
    }

    private void sendRegistrationIdToBackend(GoogleCloudMessaging gcm, String regId, String account) {
        try {
            Bundle data = new Bundle();
            // the name is used for keeping track of user notifications
            // if you use the same name everywhere, the notifications will
            // be cancelled
            data.putString("account", account);
            data.putString("action", ACTION_REGISTER);
            String msgId = Integer.toString(getNextMsgId());
            gcm.send(mSenderId + "@gcm.googleapis.com", msgId, GCM_DEFAULT_TTL, data);
            Log.v(TAG, "regId sent: " + regId);
        } catch (IOException e) {
            Log.e(TAG, "IOException while sending registration to backend...", e);
        }
    }

    private void sendMessage(GoogleCloudMessaging gcm, Intent intent) {
        try {
            String msg = intent.getStringExtra(KEY_MESSAGE_TXT);
            Bundle data = new Bundle();
            data.putString(ACTION, ACTION_ECHO);
            data.putString("message", msg);
            String id = intent.getStringExtra(KEY_MSG_ID);
            gcm.send(mSenderId + "@gcm.googleapis.com", id, data);
            Log.v(TAG, "Mensagem enviada: " + msg);
        } catch (IOException e) {
            Log.e(TAG, "Error while sending a message", e);
        }
    }

    public abstract void sendNotification(String msg);

    public abstract void messageHandler(Intent intent);


    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with a GCM message.
    /*private void sendNotification(String msg) {
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, GCMDemoActivity.class);
        notificationIntent.setAction(Constants.NOTIFICATION_ACTION);
        notificationIntent.putExtra(Constants.KEY_MESSAGE_TXT, msg);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_collections_cloud)
                .setContentTitle("GCM Notification")
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setContentText(msg);

        mBuilder.setContentIntent(contentIntent);
        mNotificationManager.notify(Constants.NOTIFICATION_NR, mBuilder.build());
    }*/

    private int getNextMsgId() {
        SharedPreferences prefs = getPrefs();
        int id = prefs.getInt(KEY_MSG_ID, 0);
        Editor editor = prefs.edit();
        editor.putInt(KEY_MSG_ID, ++id);
        editor.apply();
        return id;
    }

    private SharedPreferences getPrefs() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }

    enum EventbusMessageType {
        REGISTRATION_FAILED, REGISTRATION_SUCCEEDED, UNREGISTRATION_SUCCEEDED, UNREGISTRATION_FAILED;
    }

    enum State {
        REGISTERED, UNREGISTERED;
    }

}