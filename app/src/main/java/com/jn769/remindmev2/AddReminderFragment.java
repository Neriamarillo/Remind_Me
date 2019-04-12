//package com.jn769.remindmev2;
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
