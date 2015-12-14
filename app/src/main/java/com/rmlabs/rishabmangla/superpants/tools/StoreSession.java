package com.rmlabs.rishabmangla.superpants.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by rishabmangla on 13/12/15.
 */
public class StoreSession {

    public static final String SUPER_PREF = "SuperPref";

    public final static String SSNTOKEN = "token";
    public final static String TOPS_URI = "tops_uri";
    public final static String PANTS_URI = "pants_uri";
    public final static String BOOKMARKS_TOPS_URI = "bookmarks_tops_uri";
    public final static String BOOKMARKS_PANTS_URI = "bookmarks_pants_uri";
    public final static String DEFAULT_DELIMITER = ",,/,";

    SharedPreferences mSharedPreference;
    SharedPreferences.Editor mEditor;

    public StoreSession(Context context) {
        mSharedPreference = context.getSharedPreferences(SUPER_PREF, Context.MODE_PRIVATE); // 0 - for private mode
    }

    public StoreSession(Context context, String sessioToken) {
        mSharedPreference = context.getSharedPreferences(SUPER_PREF, Context.MODE_PRIVATE); // 0 - for private mode
        mEditor = mSharedPreference.edit();
        mEditor.putString(SSNTOKEN, sessioToken);
        mEditor.apply();
    }

    public void storeVariable(String key, String value) {
        mEditor = mSharedPreference.edit();
        mEditor.putString(key, value);
        mEditor.apply();
    }

    public void storeVariable(String key, boolean value) {
        mEditor = mSharedPreference.edit();
        mEditor.putBoolean(key, value);
        mEditor.apply();
    }

    public void storeVariable(String key, Set<String> value) {
        mEditor = mSharedPreference.edit();
        mEditor.putStringSet(key, value);
        mEditor.apply();
    }

    public void storeArray(String key, String value) {
        mEditor = mSharedPreference.edit();
        String default_val = mSharedPreference.getString(key, null);
        if (default_val != null) {
            mEditor.putString(key, default_val + DEFAULT_DELIMITER + value);
        } else {
            mEditor.putString(key, value);
        }
        mEditor.apply();
    }

    public void setListString(String key, List<String> stringList) {
        String[] myStringList = stringList.toArray(new String[stringList.size()]);
        mSharedPreference.edit().putString(key, TextUtils.join("‚‗‚", myStringList)).apply();
    }

    public String[] retrieveArray(String key) {
        String val = mSharedPreference.getString(key, null);
        if (val == null) return new String[]{};
        else return val.split(DEFAULT_DELIMITER);
    }

    public ArrayList<String> getListString(String key) {
        return new ArrayList<String>(Arrays.asList(TextUtils.split(mSharedPreference.getString(key, ""), "‚‗‚")));
    }


    public String retrieveVariable(String key) {
        return mSharedPreference.getString(key, null);
    }

    public String retrieveVariable(String key, String defaultValue) {
        return mSharedPreference.getString(key, defaultValue);
    }

    public boolean retrieveBoolean(String key, boolean defaultValue) {
        return mSharedPreference.getBoolean(key, defaultValue);
    }

    public String retrieveSessiontoken() {
        return mSharedPreference.getString(SSNTOKEN, null);
    }


    public void clearData() {
        mEditor = mSharedPreference.edit();
        mEditor.clear();
        mEditor.apply();
    }


}
