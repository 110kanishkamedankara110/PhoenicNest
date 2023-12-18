package com.phoenix.phoenixNest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.phoenix.phoenicnest.R;
import com.phoenix.phoenixNest.dto.AppDto;
import com.phoenix.phoenixNest.dto.User;
import com.phoenix.phoenixNest.util.Env;
import com.phoenix.phoenixNest.util.GetAppService;
import com.phoenix.phoenixNest.util.StatusBar;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();
        String action = i.getAction();
        if (((Intent.ACTION_SEND.equals(action)&& i.getType() != null)|| (Intent.ACTION_VIEW.equals(action))) ) {
            Uri link;


            if (Intent.ACTION_SEND.equals(action)) {
                link = Uri.parse(i.getStringExtra("android.intent.extra.TEXT"));
            } else {
                link = i.getData();
            }
            String id = link.getQueryParameter("id");

            Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(Env.get(getApplicationContext(), "app.url")).build();
            GetAppService getAppService = retrofit.create(GetAppService.class);
            Call<List<AppDto>> call = getAppService.getAllApps(id);

            call.enqueue(new Callback<List<AppDto>>() {
                @Override
                public void onResponse(Call<List<AppDto>> call, Response<List<AppDto>> response) {
                    if (response.isSuccessful()) {
                        if (response.body() != null && response.body().size() > 0) {
                            AppDto app = response.body().get(0);
                            Bundle b = new Bundle();
                            b.putString("packageName", app.getPackageName());
                            b.putString("MaxColor", app.getMaxColor());
                            b.putString("MinColor", app.getMinColor());
                            b.putStringArrayList("categoryies", (ArrayList<String>) app.getCategoryies());
                            b.putStringArrayList("screenshots", (ArrayList<String>) app.getScreenShots());
                            b.putString("appBanner", app.getAppBanner());
                            b.putString("apk", app.getApk());
                            b.putString("appIcon", app.getAppIcon());
                            b.putString("appTitle", app.getAppTitle());
                            b.putString("appDescription", app.getDescription());
                            b.putString("version", app.getVersion());
                            b.putString("versionCode", app.getVersionCode());
                            b.putInt("width", app.getWidth());
                            b.putInt("height", app.getHeight());
                            b.putString("mainActivity", app.getMainActivity());


                            getSupportFragmentManager().beginTransaction()
                                    .setReorderingAllowed(true).addToBackStack("SingleAppView")
                                    .replace(R.id.fragmentContainer, SingleViewFragment.class, b)
                                    .commit();

                        } else {
                            Toast.makeText(getApplicationContext(), "App Cannot be found", Toast.LENGTH_SHORT);
                        }
                    }

                }

                @Override
                public void onFailure(Call<List<AppDto>> call, Throwable t) {

                }
            });


        }


        Bundle ex = i.getExtras();
        if (ex != null) {
            if (ex.getString("fragment") != null) {

                if (ex.getString("fragment").equals("AddDetailsFragment")) {

                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("AppDetails")
                            .replace(R.id.fragmentContainer, AppDetailsFragment.class, ex)
                            .commit();
                } else if (ex.getString("fragment").equals("AppRelease")) {
                    getSupportFragmentManager().beginTransaction()
                            .setReorderingAllowed(true).addToBackStack("Release")
                            .replace(R.id.fragmentContainer, AppRelseaseFragment.class, ex)
                            .commit();
                }
            }
        }
        setContentView(R.layout.activity_home);
        StatusBar.hideStatusBar(this);

        LoadingFragment loader = LoadingFragment.getLoader();


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    loader.show(getSupportFragmentManager(), "Loader");
                    db.collection("user").document(user.getUid())
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    if (documentSnapshot.exists()) {
                                        User user = documentSnapshot.toObject(User.class);
                                        System.out.println(user.getName());

                                    }
                                    loader.dismiss();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loader.dismiss();


                                }
                            });
                    Snackbar sb = null;
                    user = mAuth.getCurrentUser();
                    System.out.println("........" + user.isEmailVerified());
                    if (!user.isEmailVerified()) {
                        getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragmentDrawer)).commit();
                        sb = Snackbar.make(findViewById(R.id.cont), "Email Not Verified Please Verify Email", Snackbar.LENGTH_INDEFINITE);
                        sb.setAction("Open Emails", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);

                                    startActivity(intent);
                                } catch (Exception e) {

                                }
                            }
                        }).show();
                    } else {
                        if (sb != null) {
                            sb.dismiss();
                        }

                    }
                }
            });

        }

    }

    @Override
    protected void onResume() {

        super.onResume();
        System.out.println("resumed...............");

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();
        if (user != null) {
            user.reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    user = mAuth.getCurrentUser();
                    user.getIdToken(true);

                    Snackbar sb = null;
                    if (!user.isEmailVerified()) {

                        sb = Snackbar.make(findViewById(R.id.cont), "Email Not Verified Please Verify Email", Snackbar.LENGTH_INDEFINITE);
                        sb.setAction("Open Emails", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent intent = new Intent(Intent.ACTION_MAIN);
                                    intent.addCategory(Intent.CATEGORY_APP_EMAIL);

                                    startActivity(intent);
                                } catch (Exception e) {

                                }
                            }
                        }).show();
                    } else {
                        if (sb != null) {
                            sb.dismiss();
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.fragmentDrawer, DrawerFragmernt.class, null)
                                    .commit();

                        }

                    }
                }
            });

        }

    }

}