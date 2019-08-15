package com.jn769.remindmev2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

/*
 * View Adapter for setting the items in the RecyclerView
 **/

public class ReminderViewAdapter extends RecyclerView.Adapter<ReminderViewAdapter.ReminderViewHolder> {

    private List<Reminder> reminderList; // Cached copy of words


    private ReminderViewModel reminderViewModel;
    private ReminderRepository reminderRepository;

    private Context context;
    private int mExpandedPosition = -1;


    class ReminderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final TextView titleItemView;
        private final TextView timeItemView;
        private final TextView dateItemView;
        private final TextView descItemView;
        private final ConstraintLayout cardButtons;
        private final MaterialButton editButton;
        private final MaterialButton deleteButton;

        ReminderViewHolder(View itemView) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.titleTextView);
            timeItemView = itemView.findViewById(R.id.timeTextView);
            dateItemView = itemView.findViewById(R.id.dateTextView);
            descItemView = itemView.findViewById(R.id.descTextView);
            cardButtons = itemView.findViewById(R.id.cardButtons);
            editButton = itemView.findViewById(R.id.card_edit_button);
            deleteButton = itemView.findViewById(R.id.card_delete_button);
            itemView.setOnClickListener(this);
            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            final int position = getAdapterPosition();

            if (view == editButton) {
                mExpandedPosition = -1;
                notifyItemChanged(position);
                Intent intent = new Intent(context, EditReminder.class);
                intent.putExtra("REMINDER_ID", reminderList.get(position).getId());
                ActivityCompat.startActivity(context, intent, null);

            }

            if (view == deleteButton) {
                MaterialAlertDialogBuilder deleteDialog = new MaterialAlertDialogBuilder(context, R.style.RemindMe_AlertDialog);
                deleteDialog
                        .setTitle(R.string.confirm_delete)
                        .setMessage("Are you sure you want to delete this reminder?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteReminder(position);
                                mExpandedPosition = -1;
//                                notifyItemRemoved(position);
                                Toast.makeText(context, "Reminder Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mExpandedPosition = -1;
                                notifyItemChanged(position);

                            }
                        })
                        .show();
            }
        }
    }

    ReminderViewAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int position) {
        context = parent.getContext();
        reminderViewModel = ViewModelProviders.of((FragmentActivity) parent.getContext()).get(ReminderViewModel.class);
        return new ReminderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_card, parent, false));

    }

    // Added check for null input before setting text
    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, final int position) {
        holder.itemView.setTag(position);

        final boolean isExpanded = position == mExpandedPosition;
        holder.cardButtons.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        holder.itemView.setActivated(isExpanded);
        int previousExpandedPosition = -1;
        if (isExpanded)
            previousExpandedPosition = position;

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandedPosition = isExpanded ? -1 : position;
                notifyDataSetChanged();
            }
        });

        if (reminderList != null) {
            holder.titleItemView.setText(reminderList.get(position).getTitle());
            if (reminderList.get(position).getTime() != null) {
                holder.timeItemView.setText(reminderList.get(position).getTime());
            }
            holder.dateItemView.setText(reminderList.get(position).getDate());
            if (reminderList.get(position).getDescription() != null) {
                holder.descItemView.setText(reminderList.get(position).getDescription());
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.titleItemView.setText(R.string.empty_recyclerlist);
        }

    }

    @Override
    public int getItemCount() {
        if (reminderList != null)
            return reminderList.size();
        else return 0;
    }

    void setReminders(List<Reminder> reminders) {
        reminderList = reminders;
        notifyDataSetChanged();
    }

    Reminder getReminder(int position) {
        if (reminderList != null)
            return reminderList.get(position);
        else return null;
    }

    void setSingleReminder(Reminder reminder) {
        // Single reminder
        Reminder singleReminder = reminder;
        notifyDataSetChanged();
    }

    private void deleteReminder(final int position) {
        Thread thread = new Thread(new Runnable() {
            public void run() {
                reminderViewModel.deleteReminder(position);
                App application = (App) context.getApplicationContext();
                AlarmHandler alarmHandler = new AlarmHandler(application);

                if (alarmHandler.getAlarms().size() != 0) {
                    alarmHandler.deleteAlarm((int) reminderList.get(position).getAlarmId());
                }
                Log.d("Postion at Delete Reminder", String.valueOf(position));
                Log.d("DELETE ALARM AT ADAPTER: ", String.valueOf(alarmHandler.getAlarms().size()));
                Log.d("Postion at Delete Reminder", String.valueOf(reminderList.get(position).getId()));
            }
        });
        thread.start();
        notifyItemRemoved(position);
    }

    private void startRevealEdit(View v, int id) {
        //calculates the center of the View v you are passing
        int revealX = (int) (v.getX() + v.getWidth() / 2);
        int revealY = (int) (v.getY() + v.getHeight() / 2);

        //create an intent, that launches the second activity and pass the x and y coordinates
        Intent intent = new Intent(context, EditReminder.class);
        intent.putExtra("REMINDER_ID", id);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(RevealAnimation.EXTRA_CIRCULAR_REVEAL_Y, revealY);

        //just start the activity as an shared transition, but set the options bundle to null
        ActivityCompat.startActivity(context, intent, null);

        //to prevent strange behaviours override the pending transitions
//        overridePendingTransition(0, 0);
    }

}
