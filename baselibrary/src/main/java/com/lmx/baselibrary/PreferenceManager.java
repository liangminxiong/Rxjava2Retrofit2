package com.lmx.baselibrary;

import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferenceManager {

    private static PreferenceManager sInstance;
    private static SharedPreferences mSharedPreferences;

    private PreferenceManager() {
        mSharedPreferences = BaseApplication.context.getSharedPreferences("com.lmx.rxjava2retrofit2", MODE_PRIVATE);
    }

    public static PreferenceManager getInstance() {
        if (sInstance == null) {
            synchronized (PreferenceManager.class) {
                if (sInstance == null) {
                    sInstance = new PreferenceManager();
                }
            }
        }
        return sInstance;
    }

    public static void setUserCookieId(String value) {
        mSharedPreferences.edit().putString("CookieId", value).apply();
    }

    public static String getUserCookieId(String defaultValue) {
        return mSharedPreferences.getString("CookieId", defaultValue);
    }

    public void putString(String key, String value) {
        mSharedPreferences.edit().putString(key, value).apply();
    }

    public String getString(String key, String defaultValue) {
        return mSharedPreferences.getString(key, defaultValue);
    }

    public void removeString(String key) {
        mSharedPreferences.edit().remove(key).apply();
    }

    public void clearAll() {
        mSharedPreferences.edit().clear().apply();
    }

    public void putBoolean(String key, boolean value) {
        mSharedPreferences.edit().putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public void putInt(String key, int value) {
        mSharedPreferences.edit().putInt(key, value).apply();
    }

    public int getInt(String key, int defaultValue) {
        return mSharedPreferences.getInt(key, defaultValue);
    }

    public void putLong(String key, long value) {
        mSharedPreferences.edit().putLong(key, value).apply();
    }

    public long getLong(String key, long defaultValue) {
        return mSharedPreferences.getLong(key, defaultValue);
    }

}
