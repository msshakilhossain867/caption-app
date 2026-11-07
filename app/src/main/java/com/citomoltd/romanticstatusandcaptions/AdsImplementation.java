package com.citomoltd.romanticstatusandcaptions;

import android.app.Application;

import com.google.android.gms.ads.MobileAds;


public class AdsImplementation extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Initialize AdMob once for the entire app
        MobileAds.initialize(this, initializationStatus -> {
        });

    }

}
