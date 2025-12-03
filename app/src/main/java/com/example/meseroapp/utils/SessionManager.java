package com.example.meseroapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "MeseroAppPrefs";
    private static final String KEY_BAR_ID = "barId";
    private static final String KEY_IS_LOGGED = "isLogged";

    private static SessionManager instance;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // getInstance thread-safe
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    public void saveSession(int barId) {
        editor.putInt(KEY_BAR_ID, barId);
        editor.putBoolean(KEY_IS_LOGGED, true);
        editor.apply();
    }

    public int getBarId() {
        return prefs.getInt(KEY_BAR_ID, -1);
    }

    public boolean isLogged() {
        return prefs.getBoolean(KEY_IS_LOGGED, false);
    }

    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}