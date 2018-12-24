package br.com.stemcell.android.base;

import android.app.Application;
import android.content.Context;
import android.telephony.TelephonyManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import br.com.stemcell.android.beans.LocalCredentialsBean;
import br.com.stemcell.android.util.PrefUtils;

public abstract class BaseApp extends Application {

    private static BaseApp instance;

    private static LocalCredentialsBean credentials;

    private Properties properties = null;

    private String imei = null;

    public BaseApp() {
    	instance = this;
    }

    public static final BaseApp getInstance() {
    	return instance;
    }

    private Properties getProperties() {
        if (properties == null) {
            properties = new Properties();
            try {
                InputStream inputStream = this.getAssets().open(getDefaultBundleProperties());
                properties.load(inputStream);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return properties;
    }

    public String getAppUrl() {
        return getProperties().getProperty("appurl");
    }

    public String getSenderId() {
        return getProperties().getProperty("sender.id");
    }

    protected abstract String getDefaultBundleProperties();

    public abstract Class getMainActivityClass();

    public abstract Class getRegisterGCMServiceClass();

    public abstract Class getGcmXmppServiceClass();

    public static LocalCredentialsBean getCredentials() {
        return credentials;
    }

    public static void setCredentials(LocalCredentialsBean credentials) {
        BaseApp.credentials = credentials;
    }

    public String getImei() {
        if (imei==null) {
            imei = ((TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
        }
        return imei;
    }

    public String getDeviceToken() {
        return PrefUtils.getDeviceToken(getApplicationContext());
    }

    public String getUsername() {
        return getCredentials().getUsername();
    }

    public String getUserEmail() {
        return getCredentials().getEmail();
    }

    public Long getUserId() {
        return getCredentials().getId();
    }

}
