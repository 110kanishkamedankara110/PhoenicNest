package com.phoenix.phoenixNest.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class AppDetails {
    public static PackageInfo getPackageInfo(Uri uri, Context c) {


        File outputFile = new File(c.getExternalCacheDir(), "myApp.apk");

        try {
            InputStream inputStream = c.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                // Copy the content to the temporary file
                OutputStream outputStream = new FileOutputStream(outputFile);
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.close();
                inputStream.close();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        PackageManager packageManager = c.getPackageManager();





        // Specify flags to include additional information
        int flags = PackageManager.GET_ACTIVITIES | PackageManager.GET_SIGNING_CERTIFICATES;

        // Retrieve package information using PackageManager
        PackageInfo packageInfo = packageManager.getPackageArchiveInfo(outputFile.getAbsolutePath(), flags);
        outputFile.delete();
        return packageInfo;

    }
}
