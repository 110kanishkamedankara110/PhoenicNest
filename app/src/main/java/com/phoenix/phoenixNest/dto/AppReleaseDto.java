package com.phoenix.phoenixNest.dto;

import java.io.InputStream;
import java.util.List;

public class AppReleaseDto {
    private String packageName;
    private String version;
    private String versionCode;




    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(String versionCode) {
        this.versionCode = versionCode;
    }
}
