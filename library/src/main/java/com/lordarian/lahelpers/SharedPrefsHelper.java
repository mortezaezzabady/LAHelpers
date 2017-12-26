package com.lordarian.lahelpers;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Map;

public class SharedPrefsHelper {
    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;
    private static Context context;
    private static String TAG = "PREF-HELPER";

    public SharedPrefsHelper(Context context, String PREFS_NAME){
        preferences = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        editor = preferences.edit();
        SharedPrefsHelper.context = context;
    }

    public static String getString(String key, String defValue){
        return preferences.getString(key, defValue);
    }

    public static boolean setString(String key, String value) {
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean getBolean(String key){
        return preferences.getBoolean(key, false);
    }

    public static boolean setBoolean(String key, boolean value) {
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static int getInt(String key){
        return preferences.getInt(key, -1);
    }

    public static boolean setInt(String key, int value) {
        editor.putInt(key, value);
        return editor.commit();
    }

    public static void getAllKeys(){
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            Log.d(TAG, entry.getKey() + ": " + entry.getValue().toString());
        }
    }

}