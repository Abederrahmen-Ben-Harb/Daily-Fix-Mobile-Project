package com.example.dailyfix;

import android.content.Context;
import android.content.SharedPreferences;

public final class SessionManager {
    private static final String PREFS_NAME = "DailyFixPrefs";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_AUTH_TOKEN = "auth_token";
    private static final String KEY_USER_ROLE = "user_role";
    private static final String KEY_NIGHT_MODE = "night_mode";

    private SessionManager() {
        // Utility class
    }

    private static SharedPreferences prefs(Context context) {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static void saveUserSession(Context context, String userName, String token, String role) {
        prefs(context).edit()
                .putString(KEY_USER_NAME, userName)
                .putString(KEY_AUTH_TOKEN, token)
                .putString(KEY_USER_ROLE, role)
                .apply();
    }

    public static void clearSession(Context context) {
        prefs(context).edit().clear().apply();
    }

    public static String getUserName(Context context) {
        return prefs(context).getString(KEY_USER_NAME, null);
    }

    public static String getUserRole(Context context) {
        return prefs(context).getString(KEY_USER_ROLE, null);
    }

    public static int getNightMode(Context context, int fallback) {
        return prefs(context).getInt(KEY_NIGHT_MODE, fallback);
    }

    public static void setNightMode(Context context, int nightMode) {
        prefs(context).edit().putInt(KEY_NIGHT_MODE, nightMode).apply();
    }
}
