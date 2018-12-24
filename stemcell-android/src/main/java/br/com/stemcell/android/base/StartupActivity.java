package br.com.stemcell.android.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import br.com.stemcell.android.util.Constants;
import br.com.stemcell.android.util.PrefUtils;

public class StartupActivity extends Activity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "StartupActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String token = PrefUtils.getFromPrefs(this, Constants.TOKEN_REGISTER_ID, null);

        if (token == null || token.isEmpty()) {
            if (checkPlayServices()) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, BaseApp.getInstance().getRegisterGCMServiceClass());
                startService(intent);
            }
        }

        Intent intent = new Intent(this, BaseApp.getInstance().getMainActivityClass());
        startActivity(intent);
        finish();
    }

    @Override
    public void onPause() {
        super.onPause();
        ProgressApp.dismiss();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

}
