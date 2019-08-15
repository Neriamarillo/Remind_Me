package com.jn769.remindmev2;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultNightMode = preferences.getString("DEFAULT_NIGHT_MODE", "");
        assert defaultNightMode != null;
        if (defaultNightMode.equalsIgnoreCase("dark_theme")) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
