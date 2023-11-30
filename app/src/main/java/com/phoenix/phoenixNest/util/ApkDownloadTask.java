//package com.phoenix.phoenixNest.util;
//
//import android.content.ContentResolver;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Environment;
//
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//
//public class ApkDownloadTask extends AsyncTask<String, Void, String> {
//
//    @Override
//    protected String doInBackground(String... params) {
//        String fileUrl = params[0];
//        String fileName = "app.apk"; // Change this to the desired file name
//
//        try {
//            URL url = new URL(fileUrl);
//            ContentResolver contentResolver = .getContentResolver();
//
//
//            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//
//            urlConnection.setRequestMethod("GET");
//            urlConnection.setDoOutput(true);
//
//            urlConnection.connect();
//
//            // Create a file to save the downloaded APK
//            FileOutputStream fileOutput = new FileOutputStream(
//                    new java.io.File(Environment.getExternalStoragePublicDirectory(
//                            Environment.DIRECTORY_DOWNLOADS), fileName));
//
//            InputStream inputStream = urlConnection.getInputStream();
//
//            byte[] buffer = new byte[1024];
//            int bufferLength;
//
//            while ((bufferLength = inputStream.read(buffer)) > 0) {
//                fileOutput.write(buffer, 0, bufferLength);
//            }
//            fileOutput.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return fileName;
//    }
//
//    @Override
//    protected void onPostExecute(String result) {
//        if (result != null) {
//            // The APK file has been successfully downloaded
//            // You can now install it or perform any other necessary actions
//        } else {
//            // Handle the case where the download failed
//        }
//    }
//}
