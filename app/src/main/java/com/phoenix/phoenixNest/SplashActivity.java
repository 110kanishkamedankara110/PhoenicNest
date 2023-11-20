package com.phoenix.phoenixNest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import com.phoenix.phoenixNest.util.StatusBar;
import com.phoenix.phoenicnest.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.phoenix.phoenicnest.R.layout.activity_splash);
        StatusBar.hideStatusBar(this);
        loaderLoad();
    }

    private void loaderLoad(){
        ProgressBar p=findViewById(R.id.progressBar);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100; i++) {
                    int ii = i;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            p.setProgress(ii, true);
                        }
                    });
                    try {
                        Thread.sleep(20);
                    } catch (Exception e) {

                    }
                    if (i == 99) {
                        startActivity(new Intent(SplashActivity.this,HomeActivity.class));
                    }
                }
            }
        }).start();
    }
}