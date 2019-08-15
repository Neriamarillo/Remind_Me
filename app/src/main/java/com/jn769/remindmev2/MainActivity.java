package com.jn769.remindmev2;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jorge Nieves
 * @version 2.0
 * Change to Material Design, move to Room Database and major app overhaul.
 */

public class MainActivity extends AppCompatActivity {
    private static final String CHANNEL_ID = "Reminders";
    public static final int DEFAULT_NIGHT_MODE = AppCompatDelegate.MODE_NIGHT_NO;


    private ReminderViewAdapter adapter;

    private ExtendedFloatingActionButton fab;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;

    private LinearLayout cardButtons;

    protected SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        setContentView(R.layout.main_activity);

        final Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.app_title);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init Notification Channel
        createNotificationChannel();

        MaterialButton editButton = findViewById(R.id.card_edit_button);
        MaterialButton deleteButton = findViewById(R.id.card_delete_button);

        // Drawer
        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();


        NavigationView nv = findViewById(R.id.navigation_view);
        nv.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                // TODO: Add future settings
                switch (id) {
//                    case R.id.account:
//                        Toast.makeText(MainActivity.this, "My Account", Toast.LENGTH_SHORT).show();
//                        break;
                    case R.id.settings:
                        Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(settingsIntent);
//                        Toast.makeText(MainActivity.this, "Settings", Toast.LENGTH_SHORT).show();
                        break;
//                    case R.id.mycart:
//
//                        Toast.makeText(MainActivity.this, "My Cart", Toast.LENGTH_SHORT).show();
//                        break;
                    default:
                        return true;
                }
                return true;
            }
        });

        // FAB code
        fab = findViewById(R.id.main_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startRevealAdd(view);
            }
        });

        // RecyclerView initialization
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        adapter = new ReminderViewAdapter(new ArrayList<Reminder>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // LiveData Observer
        ReminderViewModel reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);
        reminderViewModel.getReminderList().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable final List<Reminder> reminders) {
                // Update the cached copy of the words in the adapter.
                adapter.setReminders(reminders);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    fab.hide();
                } else {
                    fab.show();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });

    }

    private void startRevealAdd(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, AddReminder.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    @Override
    public void onRestart() {
        super.onRestart();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        fab.show();

    }

    @Override
    public void onPause() {
        super.onPause();
        fab.hide();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
//        DrawerLayout drawer = findViewById(R.id.navigation_view);
        assert dl != null;
        if (dl.isDrawerOpen(GravityCompat.START)) {
            dl.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (t.onOptionsItemSelected(item))
            return true;

        return super.onOptionsItemSelected(item);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Reminders";
            String description = "Reminder notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}

