package com.piticlistudio.playednext.utils;


import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import java.util.Hashtable;

public class FontCache {

    private static final String TAG = "FontCache";

    private static Hashtable<String, Typeface> fontCache = new Hashtable<String, Typeface>();

    public static Typeface get(String name, Context context) {
        Typeface tf = fontCache.get(name);
        if(tf == null) {
            try {
                tf = Typeface.createFromAsset(context.getAssets(), name);
            }
            catch (Exception e) {
                Log.e(TAG, "Failed loading font: " + e.getLocalizedMessage());
                return null;
            }
            fontCache.put(name, tf);
        }
        return tf;
    }
}
