package com.jn769.remindme;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {
    protected CardView cardView;
    protected TextView title;
    protected TextView time;
    protected TextView date;
    protected TextView description;
    protected CheckBox checkBox;


    public ViewHolder(View v) {
        super(v);
        cardView = (CardView) v.findViewById(R.id.cardView);
        title = (TextView) v.findViewById(R.id.titleCode);
        time = (TextView) v.findViewById(R.id.timeCode);
        date = (TextView) v.findViewById(R.id.dateCode);
        description = (TextView) v.findViewById(R.id.descEditText);
        checkBox = (CheckBox) v.findViewById(R.id.checkBox);

    }

}