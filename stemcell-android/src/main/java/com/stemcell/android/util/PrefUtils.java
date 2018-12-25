package com.stemcell.android.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.stemcell.android.beans.LocalCredentialsBean;


/**
 *
 *
 * Saving user credentials on successful login case
 * PrefUtils.saveToPrefs(YourActivity.this, PREFS_LOGIN_USERNAME_KEY, username);
 * PrefUtils.saveToPrefs(YourActivity.this, PREFS_LOGIN_PASSWORD_KEY, password);
 *
 * To retrieve values back
 * String loggedInUserName = PrefUtils.getFromPrefs(YourActivity.this, PREFS_LOGIN_USERNAME_KEY);
 * String loggedInUserPassword = PrefUtils.getFromPrefs(YourActivity.this, PREFS_LOGIN_PASSWORD_KEY);
 *
 */
public class PrefUtils {
    private static final String PREFS_CREDENTIALS_ID_KEY = "__ID__" ;
    private static final String PREFS_CREDENTIALS_KEY_KEY = "__KEY__" ;
    private static final String PREFS_CREDENTIALS_PASSWORD_KEY = "__PASSWORD__" ;
    private static final String PREFS_CREDENTIALS_EMAIL_KEY = "__EMAIL__" ;
    private static final String PREFS_CREDENTIALS_NAME_KEY = "__NAME__" ;
    private static final String PREFS_CREDENTIALS_CONFIRM_KEY = "__CONFIRM__" ;
    private static final String PREFS_CONFIRMED_EMAILS_KEY = "__CONFIRMED_EMAILS__" ;
    private static final String PREFS_LAST_LOGGED_EMAI_KEY = "__LAST_LOGGED_EMAI__" ;


    /**
     * Called to save supplied value in shared preferences against given key.
     * @param context Context of caller activity
     * @param key Key of value to save against
     * @param value Value to save
     */
    public static void saveToPrefs(Context context, String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void saveLongToPrefs(Context context, String key, Long value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    /**
     * Called to retrieve required value from shared preferences, identified by given key.
     * Default value will be returned of no value found or error occurred.
     * @param context Context of caller activity
     * @param key Key to find value against
     * @param defaultValue Value to return if no data found against given key
     * @return Return the value found against given key, default if not found or any error occurs
     */
    public static String getFromPrefs(Context context, String key, String defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getString(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static Long getLongFromPrefs(Context context, String key, Long defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getLong(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


    public static void removeFromPrefs(Context context, String key) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = sharedPrefs.edit();
        editor.remove(key).apply();
    }

    public static void saveCredentials(Context context, LocalCredentialsBean credentials) {
        saveLongToPrefs(context, PREFS_CREDENTIALS_ID_KEY, credentials.getId());
        saveToPrefs(context, PREFS_CREDENTIALS_KEY_KEY, credentials.getLogon());
        saveToPrefs(context, PREFS_CREDENTIALS_PASSWORD_KEY, credentials.getPassword());
        saveToPrefs(context, PREFS_CREDENTIALS_EMAIL_KEY, credentials.getEmail());
        saveToPrefs(context, PREFS_CREDENTIALS_NAME_KEY, credentials.getUsername());
        saveToPrefs(context, PREFS_CREDENTIALS_CONFIRM_KEY, credentials.isConfirmed() ? "Y" : "N");
    }

    public static void removeCredentials(Context context) {
        removeFromPrefs(context, PREFS_CREDENTIALS_ID_KEY);
        removeFromPrefs(context, PREFS_CREDENTIALS_KEY_KEY);
        removeFromPrefs(context, PREFS_CREDENTIALS_PASSWORD_KEY);
        removeFromPrefs(context, PREFS_CREDENTIALS_EMAIL_KEY);
        removeFromPrefs(context, PREFS_CREDENTIALS_NAME_KEY);
        removeFromPrefs(context, PREFS_CREDENTIALS_CONFIRM_KEY);
    }




    public static LocalCredentialsBean getCredentials(Context context) {
        LocalCredentialsBean credentials = new LocalCredentialsBean();

        Long id = getLongFromPrefs(context, PREFS_CREDENTIALS_ID_KEY, null);
        String email = getFromPrefs(context, PREFS_CREDENTIALS_EMAIL_KEY, null);
        String senha = getFromPrefs(context, PREFS_CREDENTIALS_PASSWORD_KEY, null);
        String chave = getFromPrefs(context, PREFS_CREDENTIALS_KEY_KEY, null);
        String nome =  getFromPrefs(context, PREFS_CREDENTIALS_NAME_KEY, null);
        boolean confirmado = getFromPrefs(context, PREFS_CREDENTIALS_NAME_KEY, null) == "Y" ? true : false;

        email = email != null && !email.isEmpty()? email : null;
        senha = senha != null && !senha.isEmpty()? senha : null;
        chave = chave != null && !chave.isEmpty()? chave : null;
        nome = nome != null && !nome.isEmpty()? nome : null;

        credentials.setId(id);
        credentials.setEmail(email);
        credentials.setPassword(senha);
        credentials.setLogon(chave);
        credentials.setUsername(nome);
        credentials.setConfirmed(confirmado);

        return credentials;
    }

    public static boolean isConfirmed(Context context, String email) {
        Set<String> emails = getConfirmedEmails(context);
        return emails.contains(email);
    }

    public static void addConfirmedEmail(Context context, String email) {
        Set<String> emails = getConfirmedEmails(context);
        emails.add(email);
        saveToPrefs(context, PREFS_CONFIRMED_EMAILS_KEY, join(emails, ","));
    }

    private static Set<String> getConfirmedEmails(Context context) {
        List<String> emails = new ArrayList<String>();
        String emailsLogadosString = getFromPrefs(context, PREFS_CONFIRMED_EMAILS_KEY, "");
        if (emailsLogadosString != null && !emailsLogadosString.isEmpty()) {
            emails = Arrays.asList(emailsLogadosString.split(","));
        }
        return new HashSet<>(emails);

    }

    public static void setLastLoggedEmail(Context context, String email) {
        saveToPrefs(context, PREFS_LAST_LOGGED_EMAI_KEY, email);
    }

    public static String  getLastLoggedEmail(Context context) {
        return getFromPrefs(context, PREFS_LAST_LOGGED_EMAI_KEY, null);
    }

    public static void  removeLastLoggedEmail(Context context) {
        removeFromPrefs(context, PREFS_LAST_LOGGED_EMAI_KEY);
    }


    public static String getDeviceToken(Context context) {
        return getFromPrefs(context, Constants.TOKEN_REGISTER_ID, null);
    }

    private static String join(Iterable<?> elements, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (Object e : elements) {
            if (sb.length() > 0)
                sb.append(delimiter);
            sb.append(e);
        }
        return sb.toString();
    }

    public static void logoff(Context context) {
        removeCredentials(context);
        removeLastLoggedEmail(context);
    }
}
