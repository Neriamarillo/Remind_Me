package com.jn769.remindmev2;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class AddReminderFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private ReminderViewModel reminderViewModel;

    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar;
    private Date dateSet;

    private TextInputLayout titleInput;
    private TextInputEditText titleEditText;
    private TextInputEditText timeEditText;
    private TextInputEditText dateEditText;
    private TextInputEditText descEditText;

    private MaterialButton saveButton;
    private MaterialButton cancelButton;

    public AddReminderFragment() {
//        // Required empty public constructor
    }

//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddReminderFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AddReminderFragment newInstance(String param1, String param2) {
//        AddReminderFragment fragment = new AddReminderFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullscreenDialog);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fullscreen_add_reminder_fragment, container, false);

        reminderViewModel = ViewModelProviders.of(this).get(ReminderViewModel.class);

        Toolbar toolbar = view.findViewById(R.id.app_bar_fragment);
        toolbar.setTitle(R.string.title_activity_add_reminder);

        // Date and time
        dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.US);
        timeFormatter = new SimpleDateFormat("h:mm a", Locale.US);
        calendar = Calendar.getInstance();


        titleInput = view.findViewById(R.id.titleInput);
        titleEditText = view.findViewById(R.id.titleEditText);
        timeEditText = view.findViewById(R.id.timeEditText);
        dateEditText = view.findViewById(R.id.dateEditText);
        descEditText = view.findViewById(R.id.descEditText);

        saveButton = view.findViewById(R.id.save_button);
        cancelButton = view.findViewById(R.id.cancel_button);

        dateListener();
        timeListener();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isTitleValid(titleEditText.getText())) {
                    titleInput.setError(getString(R.string.titleError));
                    titleEditText.setError(getString(R.string.titleError));
                } else {
//                    setReminderInfo();
                    dismiss();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
//        Dialog dialog = getDialog();
//        if (dialog != null) {
//            int width = ViewGroup.LayoutParams.MATCH_PARENT;
//            int height = ViewGroup.LayoutParams.MATCH_PARENT;
//            dialog.getWindow().setLayout(width, height);
//            dialog.getWindow().setWindowAnimations(R.style.RemindMeSlide);
////            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
////            dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
////                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        }
    }

    private boolean isTitleValid(@Nullable Editable text) {
        return text != null && text.length() >= 1;
    }

//    private void setReminderInfo() {
//        reminderViewModel.insert(new Reminder(
//                String.valueOf(titleEditText.getText()),
//                String.valueOf(timeEditText.getText()),
//                dateSet,
//                String.valueOf(descEditText.getText())));
//    }

    // Time Selection Listener
    public void timeListener() {
//        final TextInputEditText setTime = findViewById(R.id.timeEditText);
        timeEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpTime();

            }
        });
        timeEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                }
            }
        });
    }

    // Time setup
    public void setUpTime() {
//        final TextInputEditText getTime = findViewById(R.id.timeEditText);
        if (Objects.requireNonNull(timeEditText.getText()).length() > 0) {
            timePickerDialog.show();
        } else {
            timePickerDialog = new TimePickerDialog(
                    getContext(),
                    AddReminderFragment.this,
                    calendar.get(Calendar.HOUR),
                    calendar.get(Calendar.MINUTE),
                    false);
            timePickerDialog.show();
        }
    }

    public void onTimeSet(TimePicker timePicker,
                          int selectedHour,
                          int selectedMinute) {
        final Date time;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR, selectedHour);
        calendar.set(Calendar.MINUTE, selectedMinute);
        time = calendar.getTime();
//        final TextInputEditText timeEditText = findViewById(R.id.timeEditText);
        String timeString = timeFormatter.format(time);
        timeEditText.setText(timeString);
        timeEditText.setShowSoftInputOnFocus(false);
    }

    // Date Selection Listener
    public void dateListener() {
//        final TextInputEditText setDate = findViewById(R.id.dateEditText);
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpDate();
            }
        });
        dateEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                }
            }
        });
    }

    // Date setup
    public void setUpDate() {
//        final TextInputEditText getDate = findViewById(R.id.dateEditText);
        if (Objects.requireNonNull(dateEditText.getText()).length() > 0) {
            datePickerDialog.show();
        } else {
            datePickerDialog = new DatePickerDialog(
                    getContext(),
                    AddReminderFragment.this,
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    }

    public void onDateSet(DatePicker datepicker,
                          int selectedYear,
                          int selectedMonth,
                          int selectedDay) {
        final Date date;
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, selectedYear);
        calendar.set(Calendar.MONTH, selectedMonth);
        calendar.set(Calendar.DAY_OF_MONTH, selectedDay);
        date = calendar.getTime();
//        final TextInputEditText dateEditText = findViewById(R.id.dateEditText);
        String dateString = dateFormatter.format(date);
        dateEditText.setText(dateString);
        dateSet = date;
    }

}


//
//import android.net.Uri;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.design.button.MaterialButton;
//import android.support.design.widget.TextInputEditText;
//import android.support.design.widget.TextInputLayout;
//import android.support.v4.app.Fragment;
//import android.text.Editable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//
///**
// * A simple {@link Fragment} subclass.
// * Activities that contain this fragment must implement the
// * {@link AddReminderFragment.OnFragmentInteractionListener} interface
// * to handle interaction events.
// * Use the {@link AddReminderFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class AddReminderFragment extends Fragment {
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    private OnFragmentInteractionListener mListener;
//
//    public AddReminderFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment AddReminderFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static AddReminderFragment newInstance(String param1, String param2) {
//        AddReminderFragment fragment = new AddReminderFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//
//
//    }
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.add_reminder, container, false);
//        final ReminderManager reminderManager = new ReminderManager();
////        final TextInputEditText titleInput = view.findViewById(R.id.titleEditText);
//        final TextInputLayout titleInput = view.findViewById(R.id.titleInput);
//        final TextInputLayout timeInput = view.findViewById(R.id.timeEditText);
//        final TextInputLayout dateInput = view.findViewById(R.id.dateEditText);
//        final TextInputLayout descInput = view.findViewById(R.id.descEditText);
//        final TextInputEditText titleEditText = view.findViewById(R.id.titleEditText);
////        final TextInputLayout passwordTextInput = view.findViewById(R.id.password_text_input);
////        final TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
//        final MaterialButton saveButton = view.findViewById(R.id.save_button);
//        MaterialButton cancelButton = view.findViewById(R.id.cancel_button);
//
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                assert titleEditText != null;
//                if (!isTitleValid(titleEditText.getText())) {
//                    titleInput.setError(getString(R.string.titleError));
//                    titleEditText.setError(getString(R.string.titleError));
//                } else {
////                    reminderManager.selectTime();
////                    ((NavigationHost) getActivity()).navigateTo(new ReminderFragment(), false); // Navigate to the next Fragment
//                }
//            }
//        });
//
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((NavigationHost) getActivity()).navigateTo(new ReminderFragment(), false); // Navigate to the next Fragment
//            }
//        });
//
//        return view;
//    }
//
//    // TODO: Rename method, update argument and hook method into UI event
//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }
//
////    @Override
////    public void onAttach(Context context) {
////        super.onAttach(context);
////        if (context instanceof OnFragmentInteractionListener) {
////            mListener = (OnFragmentInteractionListener) context;
////        } else {
////            throw new RuntimeException(context.toString()
////                    + " must implement OnFragmentInteractionListener");
////        }
////    }
//
//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }
//
//    /**
//     * This interface must be implemented by activities that contain this
//     * fragment to allow an interaction in this fragment to be communicated
//     * to the activity and potentially other fragments contained in that
//     * activity.
//     * <p>
//     * See the Android Training lesson <a href=
//     * "http://developer.android.com/training/basics/fragments/communicating.html"
//     * >Communicating with Other Fragments</a> for more information.
//     */
//    public interface OnFragmentInteractionListener {
//        // TODO: Update argument type and name
//        void onFragmentInteraction(Uri uri);
//    }
//
//
//    private boolean isTitleValid(@Nullable Editable text) {
//        return text != null && text.length() >= 1;
//    }
//
//}
