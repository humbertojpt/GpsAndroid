package com.humbertojpt.androidgps.gps;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseCrashReporting;

/**
 * Created by Client on 10/26/2015.
 */
public class ParseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();

        ParseCrashReporting.enable(this);
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "ZNn0MYRoAOcsxpGFUnVnbPjT8ACBjeOvktB0DWSu", "73Y3DkfJZ0bY8cgwpwbIgVnFT7bKCvDTCSVYoU2a");
    }
}
