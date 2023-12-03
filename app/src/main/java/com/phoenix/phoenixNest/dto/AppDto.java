package com.phoenix.phoenixNest.dto;

import java.util.List;
import java.util.Map;

public class AppDto {
    private String packageName;
    private String appTitle;
    private String mainActivity;
    private String description;
    private List<String> categoryies;

    private String versionCode;
    private String version;
    private List<String> screenShots;
    private String apk;
    private String appIcon;

    private String  maxColor;
    private String  minColor;

    private int downloads;

    public int getDownloads() {
        return downloads;
    }

    public String getMinColor() {
        return minColor;
    }

    public void setMinColor(String minColor) {
        this.minColor = minColor;
    }

    public String getMaxColor() {
        return maxColor;
    }

    public void setMaxColor(String maxColor) {
        this.maxColor = maxColor;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public List<String> getScreenShots() {
        return screenShots;
    }

    public void setScreenShots(List<String> screenShots) {
        this.screenShots = screenShots;
    }

    public String getApk() {
        return apk;
    }

    public void setApk(String apk) {
        this.apk = apk;
    }

    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    private String appBanner;
    public List<String> getCategoryies() {
        return categoryies;
    }
    public void setCategoryies(List<String> categoryies) {
        this.categoryies = categoryies;
    }
    public String getAppIcon() {
        return appIcon;
    }
    public void setAppIcon(String appIcon) {
        this.appIcon = appIcon;
    }
    public String getAppBanner() {
        return appBanner;
    }
    public void setAppBanner(String appBanner) {
        this.appBanner = appBanner;
    }
    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
    public String getAppTitle() {
        return appTitle;
    }
    public void setAppTitle(String appTitle) {
        this.appTitle = appTitle;
    }
    public String getMainActivity() {
        return mainActivity;
    }
    public void setMainActivity(String mainActivity) {
        this.mainActivity = mainActivity;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
