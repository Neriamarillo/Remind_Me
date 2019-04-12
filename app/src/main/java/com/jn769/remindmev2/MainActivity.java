package com.jn769.remindmev2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jorge Nieves
 * @version 2.0
 * Change to Material Design, move to Room Database and major app overhaul.
 */

public class MainActivity extends AppCompatActivity {

    private ReminderViewModel reminderViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // Old fragment code
//        if (savedInstanceState == null) {
//            getSupportFragmentManager()
//                    .beginTransaction()
//                    .add(R.id.container, new ReminderFragment())
//                    .commit();
//        }

        // Swt up RecyclerView

        // RecyclerView initialization
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        final ReminderViewAdapter adapter = new ReminderViewAdapter(new ArrayList<Reminder>());
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
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(this, recyclerView,
                new RecyclerTouchListener.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                    }

                    @Override
                    public void onLongClick(View view, int position) {
                        reminderViewModel.deleteReminder(position);
                    }
                }));

        // FAB code
        FloatingActionButton fab = findViewById(R.id.main_add_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddReminder.class);
                startActivity(intent);
            }
        });


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

    }

    // NavigationHost code for fragments
//    @Override
//    public void navigateTo(Fragment fragment, boolean addToBackstack) {
//        FragmentTransaction transaction =
//                getSupportFragmentManager()
//                        .beginTransaction()
//                        .replace(R.id.container, fragment);
//
//        if (addToBackstack) {
//            transaction.addToBackStack(null);
//        }
//
//        transaction.commit();
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
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        dataList.clear();
//        dataList = db.getAllReminders();
//        adapter = new ReminderViewAdapter(dataList);
//        recyclerView.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        assert drawer != null;
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//
//        adapter.notifyDataSetChanged();
//    }

//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
////        String className;
//        Intent menuIntent;
//
//        if (id == R.id.nav_reminders) {
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//            assert drawer != null;
//            drawer.closeDrawer(GravityCompat.START);
//        } else if (id == R.id.nav_settings) {
//            menuIntent = new Intent(this, SettingsActivity.class);
//            startActivity(menuIntent);
//        } else if (id == R.id.nav_rate) {
//            Uri uri = Uri.parse("market://details?id=" + "com.jn769.remindme");
//            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
//            // To count with Play market backstack, After pressing back button,
//            // to taken back to our application, we need to add following flags to intent.
//            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
//                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
//                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//            try {
//                startActivity(goToMarket);
//            } catch (ActivityNotFoundException e) {
//                startActivity(new Intent(Intent.ACTION_VIEW,
//                        Uri.parse("http://play.google.com/store/apps/details?id=" + "com.jn769.remindme")));
//            }
//
//        } else if (id == R.id.nav_feedback) {
//            sendEmail();
//        }
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


}

