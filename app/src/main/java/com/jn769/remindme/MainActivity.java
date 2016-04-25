package com.jn769.remindme;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

/**
 * @author Jorge Nieves
 * @version 1.0
 */

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final DatabaseHandler db = new DatabaseHandler(this);
    ArrayList<Reminder> reminderArrayList = new ArrayList<>();
    ListView reminderListView;
    Reminder reminder;
    CustomAdapter listAdapter;
    ShareActionProvider mShareActionProvider;

    int clickedItemID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        frameLayout.getBackground().setAlpha(0);
        assert fabMenu != null;
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton fabAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
        assert fabAdd != null;
        fabAdd.setVisibility(View.VISIBLE);

        com.getbase.floatingactionbutton.FloatingActionButton fabErase = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDelete);
        assert fabErase != null;
        fabErase.setVisibility(View.GONE);

        com.getbase.floatingactionbutton.FloatingActionButton fabEdit = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabEdit);
        assert fabEdit != null;
        fabEdit.setVisibility(View.GONE);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fabIntent = new Intent(view.getContext(), AddReminder.class);
                fabMenu.collapse();
                startActivity(fabIntent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        toggle.syncState();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            fabMenu.setVisibility(View.INVISIBLE);
        } else {
            fabMenu.setVisibility(View.VISIBLE);
        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        displayListView();


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
        displayListView();
    }

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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        switch (item.getItemId()) {
//            case R.id.action_delete:
//                MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
//                        .content("Discard reminder?")
//                        .positiveText("Delete")
//                        .negativeText("Cancel")
//                        .onPositive(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                deleteReminder(reminder);
//                                reloadAllData();
//                            }
//                        })
//                        .onNegative(new MaterialDialog.SingleButtonCallback() {
//                            @Override
//                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                                dialog.dismiss();
//                            }
//                        })
//                        .show();
//
//                reminder.setSelected(false);
//                com.getbase.floatingactionbutton.FloatingActionButton fabAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
//                assert fabAdd != null;
//                fabAdd.setVisibility(View.VISIBLE);
//                com.getbase.floatingactionbutton.FloatingActionButton fabErase = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDelete);
//                assert fabErase != null;
//                fabErase.setVisibility(View.GONE);
//                com.getbase.floatingactionbutton.FloatingActionButton fabEdit = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabEdit);
//                assert fabEdit != null;
//                fabEdit.setVisibility(View.GONE);
//
//                return true;
//            default:
//    }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
//        String className;
        Intent menuIntent;

        if (id == R.id.nav_reminders) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            assert drawer != null;
            drawer.closeDrawer(GravityCompat.START);
        } else if (id == R.id.nav_settings) {
            menuIntent = new Intent(this, SettingsActivity.class);
            startActivity(menuIntent);
        } else if (id == R.id.nav_rate) {
            Uri uri = Uri.parse("market://details?id=" + "com.jn769.remindme");
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            // To count with Play market backstack, After pressing back button,
            // to taken back to our application, we need to add following flags to intent.
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + "com.jn769.remindme")));
            }

        } else if (id == R.id.nav_feedback) {
            sendEmail();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void displayListView() {

        reminderArrayList = db.getAllReminders();
        reminderListView = (ListView) findViewById(R.id.mainListView);
        listAdapter = new CustomAdapter(this, R.layout.reminder_custom_listview, reminderArrayList);
        reminderListView.setAdapter(listAdapter);


        reminderListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                clickedItemID = reminderArrayList.get(position).getID();

                return false;
            }
        });

        reminderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            int idFromDB;
            final int selected_item = -1;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, final long id) {
                Log.d("Clicked item id", " " + id);
                reminder = (Reminder) parent.getItemAtPosition(position);
                idFromDB = reminder.getID();
                final CheckBox cb = (CheckBox) view.findViewById(R.id.listCheckBox);

                final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
                com.getbase.floatingactionbutton.FloatingActionButton fabEdit = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabEdit);
                assert fabEdit != null;
                fabEdit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editIntent = new Intent(MainActivity.this, EditReminder.class);
                        editIntent.putExtra("id", idFromDB);
                        startActivity(editIntent);

                        reminder.setSelected(false);
                        com.getbase.floatingactionbutton.FloatingActionButton fabAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
                        assert fabAdd != null;
                        fabAdd.setVisibility(View.VISIBLE);
                        com.getbase.floatingactionbutton.FloatingActionButton fabErase = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDelete);
                        assert fabErase != null;
                        fabErase.setVisibility(View.GONE);
                        com.getbase.floatingactionbutton.FloatingActionButton fabEdit = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabEdit);
                        assert fabEdit != null;
                        fabEdit.setVisibility(View.GONE);
                        fabMenu.collapse();
                    }
                });

                if (position == selected_item || !reminder.isSelected()) {

                    reminder.setSelected(true);
                    assert cb != null;
                    cb.setChecked(true);
                    com.getbase.floatingactionbutton.FloatingActionButton fabAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
                    assert fabAdd != null;
                    fabAdd.setVisibility(View.GONE);
                    com.getbase.floatingactionbutton.FloatingActionButton fabErase = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDelete);
                    assert fabErase != null;
                    fabErase.setVisibility(View.VISIBLE);
                    fabEdit.setVisibility(View.VISIBLE);

                    fabErase.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            fabMenu.collapse();

                            MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                                    .content("Discard reminder?")
                                    .positiveText("Delete")
                                    .negativeText("Cancel")
                                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            deleteReminder(reminder);
                                            reloadAllData();
                                        }
                                    })
                                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                                        @Override
                                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();

                            reminder.setSelected(false);
                            cb.setChecked(false);
                            com.getbase.floatingactionbutton.FloatingActionButton fabAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
                            assert fabAdd != null;
                            fabAdd.setVisibility(View.VISIBLE);
                            com.getbase.floatingactionbutton.FloatingActionButton fabErase = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDelete);
                            assert fabErase != null;
                            fabErase.setVisibility(View.GONE);
                            com.getbase.floatingactionbutton.FloatingActionButton fabEdit = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabEdit);
                            assert fabEdit != null;
                            fabEdit.setVisibility(View.GONE);
                        }
                    });


                } else if (reminder.isSelected()) {

                    reminder.setSelected(false);
                    assert cb != null;
                    cb.setChecked(false);
                    com.getbase.floatingactionbutton.FloatingActionButton fabAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
                    assert fabAdd != null;
                    fabAdd.setVisibility(View.VISIBLE);
                    com.getbase.floatingactionbutton.FloatingActionButton fabErase = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDelete);
                    assert fabErase != null;
                    fabErase.setVisibility(View.GONE);
                    fabEdit.setVisibility(View.GONE);

                }
            }
        });
        registerForContextMenu(reminderListView);
    }

    class CustomAdapter extends ArrayAdapter<Reminder> {

        private final Context context;
        private ArrayList<Reminder> reminderArrayList = new ArrayList<>();

        public CustomAdapter(Context context, int textViewResourceId, ArrayList<Reminder> reminderArrayList) {
            super(context, textViewResourceId, reminderArrayList);

            this.context = context;
            this.reminderArrayList = reminderArrayList;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Reminder listItems = reminderArrayList.get(position);

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.reminder_custom_listview, null);

            }

            TextView titleCode = (TextView) convertView.findViewById(R.id.titleCode);
            TextView dateCode = (TextView) convertView.findViewById(R.id.dateCode);
            TextView timeCode = (TextView) convertView.findViewById(R.id.timeCode);

            titleCode.setText(listItems.getTitle());
            dateCode.setText(listItems.getDate());
            timeCode.setText(listItems.getTime());

            return convertView;

        }
    }

    public void deleteReminder(Reminder reminder) {
        db.deleteReminder(reminder);
    }

    private void reloadAllData() {
        // get new modified random data
        listAdapter.clear();
        displayListView();
        // fire the event
        listAdapter.notifyDataSetChanged();
    }

    @SuppressLint("LongLogTag")
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"jorge.e.nieves@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

//        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback for Remind Me App");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(MainActivity.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}

