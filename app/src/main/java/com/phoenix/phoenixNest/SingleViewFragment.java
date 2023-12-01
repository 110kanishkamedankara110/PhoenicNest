package com.phoenix.phoenixNest;

import android.Manifest;
import android.app.DownloadManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.tabs.TabLayout;
import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.util.Env;
import com.squareup.picasso.Picasso;


import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import firebase.com.protolitewrapper.BuildConfig;


public class SingleViewFragment extends Fragment {

    Bundle extra;
    String url;

    long downloadId;

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


        Button dwb = view.findViewById(R.id.download);
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


        PackageManager pm = getContext().getPackageManager();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            try {

                ApplicationInfo pi = pm.getApplicationInfo(packageName, PackageManager.ApplicationInfoFlags.of(0));
                PackageInfo pii=pm.getPackageInfo(packageName,PackageManager.PackageInfoFlags.of(0));
                long vc = pii.getLongVersionCode();
                long apkVc = Long.parseLong(versionCode);
                System.out.println(apkVc);
                if (vc < apkVc) {
                    dwb.setText("Update");
                    dwb.setEnabled(true);
                    dwb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            url = (Env.get(getContext(), "app.url") + "app/download/" + packageName + "/" + versionCode + "/" + apk);
                            Uri uri = Uri.parse(url);


                            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                            } else {
                                dwb.setText("Downloading...");
                                dwb.setEnabled(false);
                                downloadId = download(uri, appTitle, apk);
                            }
                        }
                    });
                } else {
                    dwb.setText("Open");
                    dwb.setEnabled(true);

                    dwb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);

                            getContext().startActivity(launchIntent);
                        }
                    });
                }


            } catch (PackageManager.NameNotFoundException e) {
                dwb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url = (Env.get(getContext(), "app.url") + "app/download/" + packageName + "/" + versionCode + "/" + apk);
                        Uri uri = Uri.parse(url);


                        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                        } else {
                            dwb.setText("Downloading...");
                            dwb.setEnabled(false);
                            downloadId = download(uri, appTitle, apk);
                        }
                    }
                });
            }
        } else {
            try {

                PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
                long vc = pi.getLongVersionCode();
                long apkVc = Long.parseLong(versionCode);
                System.out.println(apkVc);
                if (vc < apkVc) {
                    dwb.setText("Update");
                    dwb.setEnabled(true);
                    dwb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            url = (Env.get(getContext(), "app.url") + "app/download/" + packageName + "/" + versionCode + "/" + apk);
                            Uri uri = Uri.parse(url);


                            if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                            } else {
                                dwb.setText("Downloading...");
                                dwb.setEnabled(false);
                                downloadId = download(uri, appTitle, apk);
                            }
                        }
                    });
                } else {
                    dwb.setText("Open");
                    dwb.setEnabled(true);

                    dwb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent launchIntent = getContext().getPackageManager().getLaunchIntentForPackage(packageName);

                            getContext().startActivity(launchIntent);
                        }
                    });
                }


            } catch (PackageManager.NameNotFoundException e) {
                dwb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        url = (Env.get(getContext(), "app.url") + "app/download/" + packageName + "/" + versionCode + "/" + apk);
                        Uri uri = Uri.parse(url);


                        if (ContextCompat.checkSelfPermission(getContext(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);

                        } else {
                            dwb.setText("Downloading...");
                            dwb.setEnabled(false);
                            downloadId = download(uri, appTitle, apk);
                        }
                    }
                });
            }
        }


        BroadcastReceiver onDownloadComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
                System.out.println(id + " " + downloadId);
                if (downloadId == id) {
                    DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                    if (dm != null) {
                        try (Cursor c = dm.query(new DownloadManager.Query().setFilterById(downloadId))) {
                            if (c.moveToFirst()) {
                                int i = c.getColumnIndex(DownloadManager.COLUMN_STATUS);
                                int status = c.getInt(i);
                                if (status == DownloadManager.STATUS_SUCCESSFUL) {

                                    dwb.setText("Installing...");

                                    int uriIndex = c.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI);
                                    String downloadedUriString = c.getString(uriIndex);

                                    // Now you have the URI of the downloaded file
                                    Uri downloadedUri = Uri.parse(downloadedUriString);


                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        if (!getActivity().getPackageManager().canRequestPackageInstalls()) {


                                            startActivityForResult(new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES).setData(Uri.parse(String.format("package:%s", getContext().getPackageName()))), 1234);
                                        } else {
                                        }
                                    }

                                    //Storage Permission

                                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                                    }

                                    if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                                    }

                                    String PATH = downloadedUri.getPath();
                                    File file = new File(PATH);
                                    Uri uri = null;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                        uri = FileProvider.getUriForFile(context, "com.phoenix.phoenixNest", file);
                                    } else {
                                        uri = Uri.fromFile(file);
                                    }


                                    if (file.exists()) {
                                        Intent inten = new Intent(Intent.ACTION_INSTALL_PACKAGE);
                                        inten.setDataAndType(uri, "application/vnd.android.package-archive");
                                        inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        inten.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                        try {
                                            getActivity().getApplicationContext().startActivity(inten);
                                        } catch (ActivityNotFoundException e) {
                                            dwb.setText("Installing...");
                                            dwb.setEnabled(true);
                                            e.printStackTrace();
                                            Log.e("TAG", "Error in opening the file!");
                                        }
                                    } else {

                                    }


                                } else if (status == DownloadManager.STATUS_FAILED) {
                                    dwb.setText("Download");
                                    dwb.setEnabled(true);
                                }
                            }
                        }
                    }
                }
            }
        };

        BroadcastReceiver onInstallComplete = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println("installed.......................");
                String action = intent.getAction();
                if (Intent.ACTION_PACKAGE_ADDED.equals(action)) {
                    String packageName = intent.getData().getEncodedSchemeSpecificPart();


                    String InstpackageName = intent.getData().getEncodedSchemeSpecificPart();

                    if (packageName.equals(InstpackageName)) {
                        dwb.setText("Open");
                        dwb.setEnabled(true);

                        dwb.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);

                                context.startActivity(launchIntent);
                            }
                        });
                    }
                }
            }
        };


        getContext().registerReceiver(onDownloadComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");

        getContext().registerReceiver(onInstallComplete, intentFilter);


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


    }


    private long download(Uri uri, String appTitle, String apk) {


        DownloadManager.Request request = new DownloadManager.Request(uri)
                .setTitle(appTitle)
                .setDescription("Downloading")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, apk)
                .setAllowedOverMetered(true)
                .setMimeType("application/vnd.android.package-archive")
                .setAllowedOverRoaming(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            request.setRequiresCharging(false);
        }

        // Enqueue the download and save the reference ID
        DownloadManager downloadManager = (DownloadManager) getContext().getSystemService(Context.DOWNLOAD_SERVICE);

        return downloadManager.enqueue(request);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}