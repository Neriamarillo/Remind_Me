package com.jn769.remindmev2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

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

    ReminderViewAdapter(List<Reminder> reminderList) {
        this.reminderList = reminderList;
    }

    @Override
    public ReminderViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        return new ReminderViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_item, parent, false));
    }

    // Added check for null input before setting text
    @Override
    public void onBindViewHolder(ReminderViewHolder holder, int position) {
        if (reminderList != null) {
            holder.titleItemView.setText(reminderList.get(position).getTitle());
            if (reminderList.get(position).getTime() != null) {
                holder.timeItemView.setText(reminderList.get(position).getTime());
            }
            holder.dateItemView.setText(reminderList.get(position).getDate() != null ?
                    reminderList.get(position).getDate().toString().substring(0, 11) : null);
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

}
