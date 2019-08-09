package com.jn769.remindmev2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

public class SettingsActivity extends AppCompatActivity {
    private static final String PREFS_NAME = "themes";
    public static final String PREF_DARK_THEME = "themesPref";
    public static final int RESULT_CODE_THEME_UPDATED = 1;
    public static final int DEFAULT_NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, new SettingsFragment())
                .commit();
        Toolbar toolbar = findViewById(R.id.settingsToolbar);
        toolbar.setTitle(R.string.settings_title);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


    }


    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference notificationPref = findPreference("key_notificationPref");
            final ListPreference themePref = findPreference("themesPref");

            assert notificationPref != null;
            notificationPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                public boolean onPreferenceClick(Preference preference) {
                    Intent notifIntent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    notifIntent.putExtra(Settings.EXTRA_APP_PACKAGE, BuildConfig.APPLICATION_ID);
                    notifIntent.putExtra(Settings.EXTRA_CHANNEL_ID, "Reminders");
                    startActivity(notifIntent);
                    return true;
                }
            });

            assert themePref != null;
            themePref.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
                @Override
                public boolean onPreferenceChange(Preference preference, Object newValue) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
                    SharedPreferences.Editor editor = preferences.edit();
                    if (newValue.equals("dark_theme")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        Toast.makeText(preference.getContext(), "NIGHT MODE ON", Toast.LENGTH_SHORT).show();
                        editor.putString("DEFAULT_NIGHT_MODE", "dark_theme");
                        editor.apply();
                        Log.d("NEW VALUE", newValue.toString());

                    } else if (newValue.equals("light_theme")) {
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        Toast.makeText(preference.getContext(), "NIGHT MODE OFF", Toast.LENGTH_SHORT).show();
                        editor.putString("DEFAULT_NIGHT_MODE", "light_theme");
                        editor.apply();
                        Log.d("NEW VALUE", newValue.toString());
                    }
                    return true;
                }

            });

        }
    }

    @Override
    protected void onRestart() {
        super.onResume();
    }

}