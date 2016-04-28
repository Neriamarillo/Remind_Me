package com.jn769.remindme;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
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

    public RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static RecyclerView recyclerView;
    private static ArrayList<Reminder> dataList = new ArrayList<>();
    private int idFromDB;

    static com.getbase.floatingactionbutton.FloatingActionButton fabAdd;
    static com.getbase.floatingactionbutton.FloatingActionButton fabErase;
    static com.getbase.floatingactionbutton.FloatingActionButton fabEdit;
    private int checked = 0;
    private int getposition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        assert recyclerView != null;
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        dataList = db.getAllReminders();

        adapter = new RecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);

        //ITEM CLICK
        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {

                    @Override
                    public void onItemClick(View view, int position) {
                        getposition = position;
                        idFromDB = dataList.get(position).getID();
                        Log.d("Clicked and Position is ", String.valueOf(position));
                        Log.d("Clicked and dbID is ", String.valueOf(idFromDB));

                        ViewHolder holder = new ViewHolder(view);
                        if (!holder.checkBox.isChecked() || checked == 0) {
                            holder.checkBox.setChecked(true);
                            fabAdd.setVisibility(View.GONE);
                            fabErase.setVisibility(View.VISIBLE);
                            fabEdit.setVisibility(View.VISIBLE);
                            checked = 1;
                        } else {
                            holder.checkBox.setChecked(false);
                            fabAdd.setVisibility(View.VISIBLE);
                            fabErase.setVisibility(View.GONE);
                            fabEdit.setVisibility(View.GONE);
                            checked = 0;
                        }

                        holder.checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fabAdd.setVisibility(View.VISIBLE);
                                fabErase.setVisibility(View.GONE);
                                fabEdit.setVisibility(View.GONE);
                            }
                        });

                    }

                    @Override
                    public void onItemLongClick(View view, int position) {
                        MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                                .title(dataList.get(position).getTitle())
                                .content(dataList.get(position).getTime() + "    " + dataList.get(position).getDate() + '\n' + dataList.get(position).getDescription())
                                .positiveText("Ok")
                                .onPositive(new MaterialDialog.SingleButtonCallback() {
                                    @Override
                                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                        dialog.dismiss();
                                    }
                                })
                                .show();
                    }

                })
        );

//      FAB
        final FloatingActionsMenu fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        assert frameLayout != null;
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

        fabAdd = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
        fabErase = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabDelete);
        fabEdit = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabEdit);

        fabAdd.setVisibility(View.VISIBLE);
        fabErase.setVisibility(View.GONE);
        fabEdit.setVisibility(View.GONE);

        fabAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fabIntent = new Intent(view.getContext(), AddReminder.class);
                fabMenu.collapse();
                startActivity(fabIntent);
            }
        });

        fabEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checked = 0;

                Intent editIntent = new Intent(MainActivity.this, EditReminder.class);
                editIntent.putExtra("id", idFromDB);
                startActivity(editIntent);

                dataList.clear();
                dataList = db.getAllReminders();
                adapter.notifyItemChanged(getposition);
                adapter.notifyDataSetChanged();

                fabMenu.collapse();
                fabAdd.setVisibility(View.VISIBLE);
                fabErase.setVisibility(View.GONE);
                fabEdit.setVisibility(View.GONE);

            }
        });

        fabErase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MaterialDialog dialog = new MaterialDialog.Builder(MainActivity.this)
                        .content("Discard reminder?")
                        .positiveText("Delete")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                db.deleteReminder(idFromDB);
                                dataList.remove(getposition);
                                adapter.notifyItemRemoved(getposition);
                                fabMenu.collapse();
                                fabAdd.setVisibility(View.VISIBLE);
                                fabErase.setVisibility(View.GONE);
                                fabEdit.setVisibility(View.GONE);
                                checked = 0;
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();


            }
        });

//      DRAWER
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

//      NAV
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

//      FIRST TIME DIALOG

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

        return super.onOptionsItemSelected(item);
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
        dataList.clear();
        dataList = db.getAllReminders();
        adapter = new RecyclerViewAdapter(dataList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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

        adapter.notifyDataSetChanged();
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

    @SuppressLint("LongLogTag")
    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"jorge.e.nieves@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
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

