package com.codepath.snyteam7.crossroads.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.codepath.snyteam7.crossroads.R;
import com.parse.ParseUser;

public class profileFragment extends DialogFragment {
	public Context thisContext = null;
	private static TextView ProfileFirstname;
	private static TextView ProfileLastname;
    private static TextView ProfileUsername;
    private static TextView ProfilePhone;
    private static TextView ProfileEmail;
    private static TextView ProfileDistrict;
 	private View customView;


    public profileFragment() {
        // Empty constructor required for DialogFragment
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        customView = inflater.inflate(R.layout.user_profile, null, false);
		// Assign view references
		ProfileFirstname = (TextView) customView.findViewById(R.id.tvProfileFirstname);
		ProfileLastname = (TextView) customView.findViewById(R.id.tvProfileLastname);
		ProfileUsername = (TextView) customView.findViewById(R.id.tvProfileUsername);
		ProfilePhone = (TextView) customView.findViewById(R.id.etProfilePhone);
		ProfileEmail = (TextView) customView.findViewById(R.id.etProfileEmail);
		ProfileDistrict = (TextView) customView.findViewById(R.id.etProfileDistrict);

		// Create the ParseUser
		ParseUser user = ParseUser.getCurrentUser();
		
		// Set core properties
		String ufirst = user.getString("firstname");
		if ((ufirst == null) || ufirst.isEmpty()) {
			ufirst = " ";
		}
		ProfileFirstname.setText(Html.fromHtml("<font color=\"#206199\"><b>" + "FirstName" +
	                "<br>" + "</b></font>" + "<font color=\"#206199\">" + ufirst + "</font>"));
	
		String ulast = user.getString("lastname");
		if ((ulast == null) || ulast.isEmpty()) {
			ulast = " ";
		}
		ProfileLastname.setText(Html.fromHtml("<font color=\"#206199\"><b>" + "LastName" +
	                "<br>" + "</b></font>" + "<font color=\"#206199\">" + ulast + "</font>"));
	
		
		ProfileUsername.setText(Html.fromHtml("<font color=\"#206199\"><b>" + "UserName" +
                "<br>" + "</b></font>" + "<font color=\"#206199\">" + user.getUsername() + "</font>"));
		
		String udistrict = user.getString("district");
		if ((udistrict == null) || udistrict.isEmpty()) {
			udistrict = " ";
		}
		ProfileDistrict.setText(Html.fromHtml("<font color=\"#206199\"><b>" + "District" +
	                "<br>" + "</b></font>" + "<font color=\"#206199\">" + udistrict + "</font>"));
		
		
		String uemail = user.getEmail();
		if ((uemail == null) || uemail.isEmpty()) {
			uemail = " ";
		}
		ProfileEmail.setText(Html.fromHtml("<font color=\"#206199\"><b>" + "Email" +
	                "<br>" + "</b></font>" + "<font color=\"#206199\">" + uemail + "</font>"));
		
		String uphone = user.getString("phonenumber");
		if ((uphone == null) || uphone.isEmpty()) {
			uphone = " ";
		}
		ProfilePhone.setText(Html.fromHtml("<font color=\"#206199\"><b>" + "Phone" +
	                "<br>" + "</b></font>" + "<font color=\"#206199\">" + uphone + "</font>"));

		
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(customView)
        // Add action buttons
               .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // save
                       // Return input text to activity
                   		dismiss();
                   }
               }); 

        return builder.create();
    }

}
