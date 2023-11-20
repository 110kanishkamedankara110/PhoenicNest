package com.phoenix.phoenixNest.util;

import android.app.Activity;
import android.view.View;

public class StatusBar {
    public static void hideStatusBar(Activity activity){
        View decorView = activity.getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }
}
