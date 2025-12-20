package com.example.meseroapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SessionManager {

    private static final String PREF_NAME = "MeseroAppPrefs";
    private static final String KEY_BAR_ID = "barId";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_IS_LOGGED = "isLogged";
    private static final String KEY_USER_ROLE = "userRole";

    private static SessionManager instance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private SessionManager(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    // Singleton
    public static synchronized SessionManager getInstance(Context context) {
        if (instance == null) {
            instance = new SessionManager(context);
        }
        return instance;
    }

    // Guarda sesión completa
    public void saveSession(int userId, int barId, String role) {
        editor.putInt(KEY_USER_ID, userId);
        editor.putInt(KEY_BAR_ID, barId);
        editor.putString(KEY_USER_ROLE, role);
        editor.putBoolean(KEY_IS_LOGGED, true);
        editor.apply();
    }

    // Get usuario logeado
    public int getUserId() {
        return prefs.getInt(KEY_USER_ID, -1);
    }

    // Guarda solo el rol
    public void saveUserRole(String role) {
        editor.putString(KEY_USER_ROLE, role);
        editor.apply();
    }

    // Guarda solo el barId
    public void saveBarId(int barId) {
        editor.putInt(KEY_BAR_ID, barId);
        editor.apply();
    }

    // Devuelve el rol
    public String getUserRole() {
        return prefs.getString(KEY_USER_ROLE, "guest");
    }

    // Devuelve barId
    public int getBarId() {
        return prefs.getInt(KEY_BAR_ID, -1);
    }

    // Estado del login
    public boolean isLogged() {
        return prefs.getBoolean(KEY_IS_LOGGED, false);
    }

    // Limpia todo
    public void clearSession() {
        editor.clear();
        editor.apply();
    }
}