package com.phoenix.phoenixNest.util;

import android.content.Context;
import android.widget.TextView;

import com.phoenix.phoenicnest.R;

public class Success {
    public static void removeSuccessText(TextView textView, Context context){
        textView.setText("");
        textView.setBackgroundColor(context.getColor(R.color.transpoarent));
    }
    public static void displaySuccessMessage(TextView textView,Context context,String message){
        textView.setText(message);
        textView.setBackgroundColor(context.getColor(R.color.buttonBlueCOl));
    }
}
