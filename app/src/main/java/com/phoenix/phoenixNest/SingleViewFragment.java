package com.phoenix.phoenixNest;

import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.util.Env;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Request;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.Permission;
import java.security.Permissions;
import java.util.List;


public class SingleViewFragment extends Fragment {

    Bundle extra;
    String url ;

    public SingleViewFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(com.phoenix.phoenicnest.R.layout.fragment_single_view, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        extra = getArguments();
        String appTitle = extra.getString("appTitle");
        String packageName = extra.getString("packageName");
        List<String> categoryies = extra.getStringArrayList("categoryies");
        List<String> screenshots = extra.getStringArrayList("screenshots");
        String appBanner = extra.getString("appBanner");
        String appIcon = extra.getString("appIcon");
        String appDescription = extra.getString("appDescription");
        String version = extra.getString("version");
        String versionCode = extra.getString("versionCode");
        String apk = extra.getString("apk");
        int width = extra.getInt("width");
        int height = extra.getInt("height");
        String mainActivity = extra.getString("mainActivity");


        TextView appTitleView = view.findViewById(R.id.appTitle);
        TextView descriptionView = view.findViewById(R.id.description);
        ImageView banner = view.findViewById(R.id.banner);
        ImageView icon = view.findViewById(R.id.icon);
        icon.setClipToOutline(true);
        appTitleView.setText(appTitle);
        descriptionView.setText("(" + packageName + " " + version + " (" + versionCode + ")" + ") - " + appDescription);
        Picasso.get()
                .load(Env.get(getContext(), "app.url") + "image/appBanner/" + packageName + "/" + appBanner)
                .into(banner);

        Picasso.get()
                .load(Env.get(getContext(), "app.url") + "image/appIcon/" + packageName + "/" + appIcon)
                .into(icon);


        LinearLayout ssLayput = view.findViewById(R.id.ssLayout);
        ssLayput.removeAllViews();
        screenshots.forEach(ss -> {
            View ssView = LayoutInflater
                    .from(getContext())
                    .inflate(R.layout.screen_shots, ssLayput, false);
            Picasso.get()
                    .load(Env.get(getContext(), "app.url") + "image/screenShot/" + packageName + "/" + versionCode + "/" + ss)
                    .into((ImageView) ssView.findViewById(R.id.ssImage1));

            ssLayput.addView(ssView);

        });


        view.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
                }
//                String url=(Env.get(getContext(), "app.url") + "app/download/" + packageName + "/" + versionCode + "/" + apk);
                url = "http://10.0.2.2:8080/PhoenixNest/api/app/download/" + packageName + "/" + versionCode + "/" + apk;
                Uri uri = Uri.parse(url);
                String fileName = "app.apk";

                try {

                    DownloadManager manager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);
                    DownloadManager.Request request = new DownloadManager.Request(uri);
                    request.setTitle(fileName + ".apk");
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setAllowedOverRoaming(true);
                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
                    request.setAllowedOverMetered(true);
                    request.setVisibleInDownloadsUi(true);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName + ".apk");
                    request.setMimeType("application/vnd.android.package-archive");

                    // Enqueue the download and save the reference ID
                    long downloadId = manager.enqueue(request);




                            Cursor c =
                                    manager.query(new DownloadManager.Query().setFilterById(downloadId));

                            if (c == null) {
                                System.out.println("download not found.......");
                            } else {
                                c.moveToFirst();
                                int q1 = c.getColumnIndex(DownloadManager.COLUMN_ID);
                                int q2 = c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR);
                                int q3 = c.getColumnIndex(DownloadManager.COLUMN_LAST_MODIFIED_TIMESTAMP);
                                int q4 = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                                int q5 = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int q6 = c.getColumnIndex(DownloadManager.COLUMN_REASON);

                                Log.d(getClass().getName(),
                                        "COLUMN_ID: "
                                                + c.getLong(q1));
                                Log.d(getClass().getName(),
                                        "COLUMN_BYTES_DOWNLOADED_SO_FAR: "
                                                + c.getLong(q2));
                                Log.d(getClass().getName(),
                                        "COLUMN_LAST_MODIFIED_TIMESTAMP: "
                                                + c.getLong(q3));
                                Log.d(getClass().getName(),
                                        "COLUMN_LOCAL_URI: "
                                                + c.getString(q4));
                                Log.d(getClass().getName(),
                                        "COLUMN_STATUS: "
                                                + c.getInt(q5));
                                Log.d(getClass().getName(),
                                        "COLUMN_REASON: "
                                                + c.getInt(q6));


                                c.close();

                            }

            } catch(
            Exception e)

            {
                e.printStackTrace();
            }


        }


    });


}

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}