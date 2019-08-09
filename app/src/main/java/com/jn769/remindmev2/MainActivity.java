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


    private ReminderViewModel reminderViewModel;
    private ReminderViewAdapter adapter;

    private ExtendedFloatingActionButton fab;

    private DrawerLayout dl;
    private ActionBarDrawerToggle t;
    private NavigationView nv;

    private LinearLayout cardButtons;
    private MaterialButton editButton;
    private MaterialButton deleteButton;

    protected SharedPreferences mPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager.setDefaultValues(this, R.xml.root_preferences, false);
        setContentView(R.layout.main_activity);

        final Toolbar toolbar = findViewById(R.id.app_bar);
        toolbar.setTitle(R.string.app_title);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black_24dp);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Init Notification Channel
        createNotificationChannel();

        editButton = findViewById(R.id.card_edit_button);
        deleteButton = findViewById(R.id.card_delete_button);

        // Drawer
        dl = findViewById(R.id.activity_main);
        t = new ActionBarDrawerToggle(this, dl, R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        dl.addDrawerListener(t);
        t.syncState();


        nv = findViewById(R.id.navigation_view);
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
        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);
        reminderViewModel.getReminderList().observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable final List<Reminder> reminders) {
                // Update the cached copy of the words in the adapter.
                adapter.setReminders(reminders);
            }
        });

        // RecyclerView ItemOnTouchListeners

//        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
//                new RecyclerTouchListener.ClickListener() {
//                    @Override
//                    public void onClick(View view, final int position) {
//
//                        Log.d("Item ID", String.valueOf(position));
//
//
//
//                    }
//
//                    @Override
//                    public void onLongClick(View view, final int position) {
////                        MaterialAlertDialogBuilder deleteDialog = new MaterialAlertDialogBuilder(MainActivity.this, R.style.RemindMe_AlertDialog);
////                        deleteDialog
////                                .setTitle(R.string.confirm_delete)
////                                .setMessage("Are you sure you want to delete this reminder?")
////                                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
////                                    @Override
////                                    public void onClick(DialogInterface dialog, int which) {
////                                        reminderViewModel.deleteReminder(position);
////                                    }
////                                })
////                                .setNegativeButton("Cancel", null)
////                                .show();
//                    }
//                }));

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

//        if (deleteButton)
//        deleteButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                reminderViewModel.deleteReminder(recyclerView.getChildAdapterPosition(v));
//            }
//        });

//      DRAWER
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        assert drawer != null;
//        toggle.syncState();
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            fabMenu.setVisibility(View.INVISIBLE);
//        } else {
//            fabMenu.setVisibility(View.VISIBLE);
//        }

//      NAV
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
//        assert navigationView != null;
//        navigationView.setNavigationItemSelectedListener(this);

//      FIRST TIME DIALOG

//        // DRAWER
//        DrawerLayout drawer = findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        assert drawer != null;
//        toggle.syncState();
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            fab.hide();
//        } else {
//            fab.show();
//        }
//
////      NAV
//        NavigationView navigationView = findViewById(R.id.navigation_view);
//        assert navigationView != null;
//        navigationView.setNavigationItemSelectedListener(this);


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

    private void startRevealEdit(View v, int id) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, EditReminder.class);
        intent.putExtra("REMINDER_ID", id);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

    private void startRevealSettings(View v) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(this, intent, null);

        //to prevent strange behaviours override the pending transitions
        overridePendingTransition(0, 0);
    }

//    private void editReminder(final int position) {
////        LayoutInflater inflater = LayoutInflater.from(this);
////        final View addView = inflater.inflate(R.layout.edit_reminder_dialog, null);
//
//        /*Hidden for testing*/
////        TextInputEditText titleEditText = addView.findViewById(R.id.titleEditText);
////        TextInputEditText timeEditText = addView.findViewById(R.id.timeEditText);
////        TextInputEditText dateEditText = addView.findViewById(R.id.dateEditText);
////        TextInputEditText descEditText = addView.findViewById(R.id.descEditText);
//
//
////        new MaterialAlertDialogBuilder(MainActivity.this, R.style.ReminderDialog)
////                .setView(addView)
////                .setPositiveButton("Edit", new DialogInterface.OnClickListener() {
////                    @Override
////                    public void onClick(DialogInterface dialog, int which) {
////
//////                        Intent editIntent = new Intent(this, EditReminder.class);
////////                        Intent editIntent = new Intent(this, EditReminder.class)
//////
////////                        Reminder reminder = new Reminder();
////////                        reminder = adapter.getReminder(position);
//////                        // 3. put person in intent data
//////                        editIntent.putExtra("reminder", position);
//////                        // 4. start the activity
//////                        startActivity(editIntent);
////                        sendReminder(addView, position);
////                    }
////                }) // change listener
////                .setNegativeButton("Cancel", null)
////                .show();
//
//
////        titleEditText.setText(adapter.getReminder(position).getTitle());
////        timeEditText.setText(adapter.getReminder(position).getTime());
////        dateEditText.setText(adapter.getReminder(position).getDate() != null ?
////                adapter.getReminder(position).getDate().toString().substring(0, 11) : null);
////        descEditText.setText(adapter.getReminder(position).getDescription());
//        /*Hidden for testing*/
//
//        String title = adapter.getReminder(position).getTitle();
//        String time = adapter.getReminder(position).getTime();
//        String date = adapter.getReminder(position).getDate() != null ?
//                adapter.getReminder(position).getDate().toString().substring(0, 11) : null;
//        String descript = adapter.getReminder(position).getDescription();
//
//        Bundle extras = new Bundle();
//        extras.putString("TITLE", title);
//        extras.putString("TIME", time);
//        extras.putString("DATE", date);
//        extras.putString("DESC", descript);
//
//        Intent intent = new Intent(this, EditReminder.class);
//        intent.putExtras(extras);
//        startActivity(intent);
//
//    }

    //WORKING
//    public void sendReminder(View view, int position) {
//        Intent editIntent = new Intent(this, EditReminder.class);
//        Bundle bundle = new Bundle();
//        bundle.putInt(EDIT_MESSAGE, position);
//        editIntent.putExtras(bundle);
////        editIntent.putExtra(EDIT_MESSAGE, position);
//        startActivity(editIntent);
//    }

    // Send Id of the item clicked to EditReminder Class
//    public void sendReminder(int id, View view) {
//        Intent intent = new Intent(this, EditReminder.class);
//        intent.putExtra("REMINDER_ID", id);
//        startRevealEdit(view);
////        startActivity(intent);
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        return super.onOptionsItemSelected(item);
//    }

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


//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
////        String className;
//        Intent menuIntent;
//
////        if (id == R.id.nav_reminders) {
////            DrawerLayout drawer = findViewById(R.id.drawer_layout);
////            assert drawer != null;
////            drawer.closeDrawer(GravityCompat.START);
////        } else if (id == R.id.nav_settings) {
////            menuIntent = new Intent(this, SettingsActivity.class);
////            startActivity(menuIntent);
////        }
////        } else if (id == R.id.nav_rate) {
////            Uri uri = Uri.parse("market://details?id=" + "com.jn769.remindme");
////            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
////            // To count with Play market backstack, After pressing back button,
////            // to taken back to our application, we need to add following flags to intent.
////            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
////                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
////            try {
////                startActivity(goToMarket);
////            } catch (ActivityNotFoundException e) {
////                startActivity(new Intent(Intent.ACTION_VIEW,
////                        Uri.parse("http://play.google.com/store/apps/details?id=" + "com.jn769.remindme")));
////            }
////
////        }
////        } else if (id == R.id.nav_feedback) {
////            sendEmail();
////        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        assert drawer != null;
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }

//    @SuppressLint("LongLogTag")
//    protected void sendEmail() {
//        Log.i("Send email", "");
//        String[] TO = {"jorge.e.nieves@gmail.com"};
//        Intent emailIntent = new Intent(Intent.ACTION_SEND);
//
//        emailIntent.setData(Uri.parse("mailto:"));
//        emailIntent.setType("text/plain");
//        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
//        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Remind Me App");
//
//        try {
//            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//            finish();
//            Log.i("Finished sending email...", "");
//        } catch (android.content.ActivityNotFoundException ex) {
//            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
//        }
//    }



/*
* CODE FOR NOTIFICATION SETTINGS INSIDE SETTINGS APP -> INSIDE SETTINGS APP
* //            INTENT FOR NOTIFICATION SETTINGS

//            Intent notifIntent = null;
//            notifIntent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
//            notifIntent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
//            notifIntent.putExtra(Settings.EXTRA_CHANNEL_ID, groupId);
//            startActivity(notifIntent);
*
* */


//
}

