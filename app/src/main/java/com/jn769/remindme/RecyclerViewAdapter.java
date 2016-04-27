package com.jn769.remindme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Collections;
import java.util.List;

class RecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {

    List<Reminder> list = Collections.emptyList();
    Context context;
    private Reminder data;

    public RecyclerViewAdapter(List<Reminder> list) {
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.reminder_custom_listview, parent, false);
        ViewHolder holder = new ViewHolder(v);

        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        assert holder.title != null;
        holder.title.setText(list.get(position).getTitle());
        assert holder.time != null;
        holder.time.setText(list.get(position).getTime());
        assert holder.date != null;
        holder.date.setText(list.get(position).getDate());

        //animate(holder);

    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Reminder data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Reminder data) {
        this.data = data;
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


}