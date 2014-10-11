package com.codepath.snyteam7.crossroads.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.example.crossroads.R;

public class HowitworksDialogFragment extends DialogFragment {
 	private View customView;


    public HowitworksDialogFragment() {
        // Empty constructor required for DialogFragment
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        customView = inflater.inflate(R.layout.fragment_howitworks, null, false);

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(customView)
        // Add action buttons
               .setPositiveButton("save", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // save
                       // Return input text to activity
                   		dismiss();
                   }
               })
               .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       // Cancel
                   }
               }); 

        return builder.create();
    }
    
}
