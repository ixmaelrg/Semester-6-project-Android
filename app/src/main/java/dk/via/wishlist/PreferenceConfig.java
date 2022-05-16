package dk.via.wishlist;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceConfig {

    private static final String PREF_NAME = "dk.via.wishlist";
    public static final String PREF_LOGIN_STATE = "pref_login_state";
    public static final String PREF_USERNAME = "pref_username";

    public static void updateLoginState(Context context, String username, boolean isLoggedIn) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(PREF_LOGIN_STATE, isLoggedIn);
        editor.putString(PREF_USERNAME, username);
        editor.apply();
    }

    public static boolean getLoginState(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getBoolean(PREF_LOGIN_STATE, false);
    }

    public static String getUsername(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return preferences.getString(PREF_USERNAME, "test");
    }

    public static void registerPreference(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public static void unregisterPreference(Context context, SharedPreferences.OnSharedPreferenceChangeListener listener) {
        SharedPreferences preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        preferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

}
