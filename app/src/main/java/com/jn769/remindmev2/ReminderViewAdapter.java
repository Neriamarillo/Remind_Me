package com.jn769.remindmev2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/*
 * View Adapter for setting the items in the RecyclerView
 **/

public class ReminderViewAdapter extends RecyclerView.Adapter<ReminderViewAdapter.ReminderViewHolder> {

    class ReminderViewHolder extends RecyclerView.ViewHolder {
        private final TextView titleItemView;
        private final TextView timeItemView;
        private final TextView dateItemView;
        private final TextView descItemView;

        ReminderViewHolder(View itemView) {
            super(itemView);
            titleItemView = itemView.findViewById(R.id.titleTextView);
            timeItemView = itemView.findViewById(R.id.timeTextView);
            dateItemView = itemView.findViewById(R.id.dateTextView);
            descItemView = itemView.findViewById(R.id.descTextView);
        }
    }

    private List<Reminder> reminderList; // Cached copy of words

    private Reminder singleReminder; // Single reminder

    DateFormat output = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);

    ReminderViewAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    ReminderViewAdapter(Reminder reminder) {
        this.singleReminder = reminder;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ReminderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item_card, parent, false));
    }

    // Added check for null input before setting text
    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {

        if (reminderList != null) {
            holder.titleItemView.setText(reminderList.get(position).getTitle());
            if (reminderList.get(position).getTime() != null) {
                holder.timeItemView.setText(reminderList.get(position).getTime());
            }
//            holder.dateItemView.setText(reminderList.get(position).getDate() != null ?
//                    reminderList.get(position).getDate().toString().substring(0, 12) : null);
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
        singleReminder = reminder;
        notifyDataSetChanged();
    }

}
