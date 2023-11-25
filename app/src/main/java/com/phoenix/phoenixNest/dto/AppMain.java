package com.phoenix.phoenixNest.dto;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import okhttp3.RequestBody;

public class AppMain {
    private String appName;
    private String pakgeName;
    private String mainActivity;
    private String user= FirebaseAuth.getInstance().getCurrentUser().getUid();

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPakgeName() {
        return pakgeName;
    }

    public void setPakgeName(String pakgeName) {
        this.pakgeName = pakgeName;
    }

    public String getMainActivity() {
        return mainActivity;
    }

    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }


}
