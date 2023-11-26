package com.phoenix.phoenixNest.util;

import android.content.Context;
import android.graphics.Color;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.phoenix.phoenicnest.R;

public class Error {
   public static void setErrorFiled(EditText textFiled, Context context, TextView textView,String message){
       textFiled.setBackground(context.getDrawable(R.drawable.error_dorder));
       textFiled.setHintTextColor(context.getColor(R.color.errorColor));
       textView.setText(message);
       textView.setBackgroundColor(context.getColor(R.color.errorColor));
       textFiled.startAnimation(AnimationUtils.loadAnimation(context,R.anim.shkae));
   }
    public static void setErrorFiled(EditText textFiled, Context context){
        textFiled.setBackground(context.getDrawable(R.drawable.error_dorder));
        textFiled.setHintTextColor(context.getColor(R.color.errorColor));
    }

    public static void removeErrorFiled(EditText textFiled, Context context){
        textFiled.setBackground(context.getDrawable(R.drawable.border));
        textFiled.setHintTextColor(context.getColor(R.color.textHint));

    }
    public static void removeErrorText(TextView textView,Context context){
        textView.setText("");
        textView.setBackgroundColor(context.getColor(R.color.transpoarent));
    }
    public static void displayErrorMessage(TextView textView,Context context,String message){
        textView.setText(message);
        textView.setBackgroundColor(context.getColor(R.color.errorColor));
    }
}
