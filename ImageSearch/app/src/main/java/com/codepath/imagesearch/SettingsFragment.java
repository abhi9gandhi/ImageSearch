package com.codepath.imagesearch;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.imagesearch.Activity.ImageSettings;
import com.codepath.imagesearch.Activity.MainActivity;
import com.codepath.imagesearch.Model.ImageSetting;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends DialogFragment implements TextView.OnEditorActionListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static ImageSetting settings;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SettingsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(ImageSetting psettings) {
        SettingsFragment fragment = new SettingsFragment();
        settings = new ImageSetting();
        settings = psettings;
        return fragment;
    }

    public SettingsFragment() {
        // Required empty public constructor
    }

    public interface EditNameDialogListener {
        void onFinishEditDialog(ImageSetting setting);
    }

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_settings, container);
        EditText type = (EditText) view.findViewById(R.id.EtvType);
        EditText colour = (EditText) view.findViewById(R.id.Etvcolour);
        EditText size = (EditText) view.findViewById(R.id.EtvSize);
        EditText site = (EditText) view.findViewById(R.id.EtvSite);
            if (settings != null) {
                type.setText(settings.getImageType());
                colour.setText(settings.getColour());
                size.setText(settings.getSize());
                site.setText(settings.getSite());
            }
            Button b=(Button)view.findViewById(R.id.BSettings);
            b.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    saveSetting(view);
                    return;
                }

            });

        String title = "Settings";
        getDialog().setTitle(title);
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        return view;
    }

    public void saveSetting(View view) {
        EditText type = (EditText) view.findViewById(R.id.EtvType);
        EditText colour = (EditText) view.findViewById(R.id.Etvcolour);
        EditText size = (EditText) view.findViewById(R.id.EtvSize);
        EditText site = (EditText) view.findViewById(R.id.EtvSite);

        if (type.getText() != null && !type.getText().toString().isEmpty()) {
            settings.setImageType(type.getText().toString());
        }
        if (colour.getText() != null && !colour.getText().toString().isEmpty()) {
            settings.setColour(colour.getText().toString());
        }
        if (size.getText() != null && !size.getText().toString().isEmpty()) {
            settings.setSize(size.getText().toString());
        }
        if (site.getText() != null && !site.getText().toString().isEmpty()) {
           settings.setSite(site.getText().toString());
        }
        EditNameDialogListener listener = (EditNameDialogListener) getActivity();
        listener.onFinishEditDialog(settings);
        dismiss();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (EditorInfo.IME_ACTION_DONE == actionId) {
            // Return input text to activity
            EditNameDialogListener listener = (EditNameDialogListener) getActivity();
            listener.onFinishEditDialog(settings);
            dismiss();
            return true;
        }
        return false;
    }

}
