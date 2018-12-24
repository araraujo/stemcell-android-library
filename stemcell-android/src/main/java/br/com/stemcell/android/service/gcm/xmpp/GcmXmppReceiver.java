package br.com.stemcell.android.service.gcm.xmpp;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import br.com.stemcell.android.base.BaseApp;

/**
 * See http://developer.android.com/google/gcm/client.html
 */
public class GcmXmppReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Explicitly specify that UpStreamService will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                BaseApp.getInstance().getGcmXmppServiceClass().getName());
        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}