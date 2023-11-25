package com.phoenix.phoenixNest.util;

import android.content.Context;

import java.io.InputStream;
import java.util.Properties;

public class Env {
    private static final Properties PROPERTIES = new Properties();

    public static String get(Context context, String key) {
        try {
            InputStream inputStream = context.getAssets().open("app.properties");
            PROPERTIES.load(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PROPERTIES.getProperty(key);
    }

    public static void setProperty(Context context, String key, String value) {
        PROPERTIES.setProperty(key, value);
    }
}
