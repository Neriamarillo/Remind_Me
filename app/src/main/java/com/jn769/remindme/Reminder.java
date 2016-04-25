package com.jn769.remindme;

/**
 * @author Jorge Nieves
 * @version 1.0
 */
class Reminder {

    private int _id;
    private String _title;
    private String _time;
    private String _date;
    private String _description;
    private boolean selected = false;

    public Reminder() {

    }

    public Reminder(int _id, String _title, String _time, String _date, String _description) {
        this._id = _id;
        this._title = _title;
        this._time = _time;
        this._date = _date;
        this._description = _description;
    }


    public Reminder(String _title, String _time, String _date, String _description) {
        this._title = _title;
        this._time = _time;
        this._date = _date;
        this._description = _description;
    }

    public int getID() {
        return _id;
    }

    public void setID(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return _title;
    }

    public void setTitle(String _title) {
        this._title = _title;
    }

    public String getTime() {
        return _time;
    }

    public void setTime(String _time) {
        this._time = _time;
    }

    public String getDate() {
        return _date;
    }

    public void setDate(String _date) {
        this._date = _date;
    }

    public String getDescription() {
        return _description;
    }

    public void setDescription(String _description) {
        this._description = _description;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public void toggleChecked() {
        selected = !selected;
    }
}
