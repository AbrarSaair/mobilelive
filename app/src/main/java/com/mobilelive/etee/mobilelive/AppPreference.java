package com.mobilelive.etee.mobilelive;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * The type App preference.
 */
public class AppPreference {

    private static String TAG = AppPreference.class.getName();

    private static SharedPreferences usrPrefrence = null;

    /**
     * Init prefrence.
     *
     * @param mContext the m context
     */
    protected static void initPrefrence(final Context mContext) {
        try {
            AppPreference.usrPrefrence = PreferenceManager.getDefaultSharedPreferences(mContext);
        } catch (final Exception exception) {
        }
    }

    /**
     * Save string.
     *
     * @param context the context
     * @param value   the value
     * @param key     the key
     */
    public static void saveString(final Context context, final String value, final String key) {

        try {
            if (AppPreference.usrPrefrence == null) {
                AppPreference.initPrefrence(context);
            }
            if (AppPreference.usrPrefrence != null) {
                editStringPrefrenceValue(value, key);
            }
        } catch (final Exception exception) {
        }
    }

    /**
     * Edit string prefrence value.
     *
     * @param value the value
     * @param key   the key
     */
    protected static void editStringPrefrenceValue(final String value, final String key) {
        final Editor editing = AppPreference.usrPrefrence.edit();
        try {
            editing.remove(key);
        } catch (final Exception exception) {
        }
        editing.putString(key, value);
        editing.commit();
    }

    /**
     * Gets string.
     *
     * @param context the context
     * @param key     the key
     * @return the string
     */
    public static String getString(final Context context, final String key) {
        return getString(context, key, null);
    }

    /**
     * Gets string.
     *
     * @param context the context
     * @param key     the key
     * @param defult  the defult
     * @return the string
     */
    public static String getString(final Context context, final String key, String defult) {

        try {
            if (AppPreference.usrPrefrence == null) {
                AppPreference.initPrefrence(context);
            }
            return AppPreference.usrPrefrence.getString(key, defult);
        } catch (final Exception exception) {
        }
        return null;
    }

}
