package com.example.loginapplication;

import android.app.Application;
import android.util.Log;

public class LoginApplication extends Application {
    
    private static final String TAG = "LoginApplication";
    
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application created");
    }
} 