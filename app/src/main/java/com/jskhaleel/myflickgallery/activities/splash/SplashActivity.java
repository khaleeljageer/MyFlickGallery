package com.jskhaleel.myflickgallery.activities.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.jskhaleel.myflickgallery.R;
import com.jskhaleel.myflickgallery.activities.home.FlickrGalleryActivity;

import java.lang.ref.WeakReference;


public class SplashActivity extends AppCompatActivity {

    private static final long SLEEP_DURATION = 3000;
    private boolean isDestroyed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AppHandler mHandler = new AppHandler(SplashActivity.this);
        mHandler.sendEmptyMessageDelayed(1, SLEEP_DURATION);
    }

    private void launchNextScreen() {
        startActivity(new Intent(this, FlickrGalleryActivity.class));
        finish();
    }

    private static class AppHandler extends Handler {
        WeakReference<SplashActivity> splash;

        AppHandler(SplashActivity splashScreen) {
            splash = new WeakReference<>(splashScreen);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = splash.get();
            if (activity != null && msg.what == 1 && !activity.isDestroyed) {
                activity.launchNextScreen();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isDestroyed = true;
    }
}
