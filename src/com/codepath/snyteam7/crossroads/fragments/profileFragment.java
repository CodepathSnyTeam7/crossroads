package com.codepath.snyteam7.crossroads.fragments;


import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class profileFragment extends DialogFragment {
	public Context thisContext = null;
	private static EditText ProfileFirstname;
	private static EditText ProfileLastname;
    private static TextView ProfileUsername;
    private static EditText ProfilePhone;
    private static EditText ProfileEmail;
    private static ImageView ivDonorpic;
    private Spinner spinState;
    private Spinner spinDistrict;
    List<String> allDistlist;
    List<String> HKDistlist;
    List<String> KowloonDistlist;
    List<String> NTDistlist;
    ArrayAdapter<String> spinStatedataAdapter;
    ArrayAdapter<String> spinDistdataAdapter;
 	private View customView;
	ProgressBar pbProfileEdit;


    public profileFragment() {
        // Empty constructor required for DialogFragment
    }

    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        customView = inflater.inflate(R.layout.fragment_edit_profile, null, false);
		// Assign view references
		ProfileFirstname = (EditText) customView.findViewById(R.id.etProfileFirstname);
		ProfileLastname = (EditText) customView.findViewById(R.id.etProfileLastname);
		ProfileUsername = (TextView) customView.findViewById(R.id.tvProfileUsername);
		ProfilePhone = (EditText) customView.findViewById(R.id.etProfilePhone);
		ProfileEmail = (EditText) customView.findViewById(R.id.etProfileEmail);
		spinState = (Spinner) customView.findViewById(R.id.spinnerProfileState);
		spinDistrict = (Spinner) customView.findViewById(R.id.spinnerProfileDistrict);
		pbProfileEdit = (ProgressBar)customView.findViewById(R.id.pbProfileEdit);
		ivDonorpic = (ImageView) customView.findViewById(R.id.ivProfileIcon);

		// Create the ParseUser
		ParseUser user = ParseUser.getCurrentUser();
		
		// Set core properties
		String ufirst = user.getString("firstname");
		if ((ufirst == null) || ufirst.isEmpty()) {
			ufirst = "";
		}
		ProfileFirstname.setText(ufirst);
		String ulast = user.getString("lastname");
		if ((ulast == null) || ulast.isEmpty()) {
			ulast = "";
		}
		ProfileLastname.setText(ulast);
	
		
		// Set username
		ProfileUsername.setText("@" + user.getUsername());
		
		if (user.getUsername().equalsIgnoreCase("v1")) {
			ivDonorpic.setImageDrawable(getResources().getDrawable(R.drawable.cr_d3));
		}
		if (user.getUsername().equalsIgnoreCase("jean")) {
			ivDonorpic.setImageDrawable(getResources().getDrawable(R.drawable.cr_d4));
		}
		
		// Set district spinner
		setupDistlists();
		addItemsOnSpinnerState();
		addItemsOnSpinnerDistrict();
		// Set the default value
		String udistrict = user.getString("district");
		if ((udistrict != null) && !udistrict.isEmpty()) {
			int spinnerPosition = spinDistdataAdapter.getPosition(udistrict);
			//set the default according to value
			spinDistrict.setSelection(spinnerPosition);
		}
	    // Setup District listener
	    spinState.setOnItemSelectedListener(spinStateitemSelectedListener);
		
		String uemail = user.getEmail();
		if ((uemail == null) || uemail.isEmpty()) {
			uemail = "";
		}
		ProfileEmail.setText(uemail);
		
		String uphone = user.getString("phonenumber");
		if ((uphone == null) || uphone.isEmpty()) {
			uphone = " ";
		}
		ProfilePhone.setText(uphone);

		
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(customView)
        // Add action buttons
               .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int id) {
                       // save
						// Extract content from alert dialog
						final String firstname = ProfileFirstname.getText().toString();
						final String lastname = ProfileLastname.getText().toString();
						final String phone = ProfilePhone.getText().toString();
						final String email = ProfileEmail.getText().toString();
						final String dist = (String)spinDistrict.getSelectedItem();
						final ParseUser user = ParseUser.getCurrentUser();
						user.put("firstname", firstname);
						user.put("lastname", lastname);
						user.put("phonenumber", phone);
						user.put("email", email);
						user.put("district", dist);
						startProgressBar();
						user.saveInBackground(new SaveCallback(){

							@Override
							public void done(ParseException arg0) {
								stopProgressBar();
								if(arg0 != null) {
									Toast.makeText(getActivity(), "Profile changes not saved",
											Toast.LENGTH_LONG).show();
								}
							}
							
						});
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
    
	private OnItemSelectedListener spinStateitemSelectedListener = new OnItemSelectedListener() {

	    @Override
	    public void onItemSelected(AdapterView<?> arg0, View v, int position,
	            long arg3) {

	        String statename = String.valueOf(spinState.getSelectedItem());
	        if (statename.equalsIgnoreCase("HK Island")) {
	        	spinDistdataAdapter.clear();
	        	spinDistdataAdapter.addAll(HKDistlist);
	        	spinDistdataAdapter.notifyDataSetChanged();
	        } else if (statename.equalsIgnoreCase("Kowloon")) {
	        	spinDistdataAdapter.clear();
	        	spinDistdataAdapter.addAll(KowloonDistlist);
	        	spinDistdataAdapter.notifyDataSetChanged();
	        } else if (statename.equalsIgnoreCase("NT")) {
	        	spinDistdataAdapter.clear();
	        	spinDistdataAdapter.addAll(NTDistlist);
	        	spinDistdataAdapter.notifyDataSetChanged();
	        } else if (statename.equalsIgnoreCase("All")) {
	        	spinDistdataAdapter.clear();
	        	spinDistdataAdapter.addAll(allDistlist);
	        	spinDistdataAdapter.notifyDataSetChanged();
	        } 
	    }

	    @Override
	    public void onNothingSelected(AdapterView<?> arg0) {
	        // TODO Auto-generated method stub

	    }
	};
	  // add items into spinner dynamically
	  public void addItemsOnSpinnerState() {
		List<String> list = new ArrayList<String>();
		list.add("All");
		list.add("HK Island");
		list.add("Kowloon");
		list.add("NT");
		spinStatedataAdapter = new ArrayAdapter<String>(getActivity(),
			android.R.layout.simple_spinner_item, list);
		spinStatedataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinState.setAdapter(spinStatedataAdapter);
	  }
	  
	  // add items into spinner dynamically
	  public void addItemsOnSpinnerDistrict() {
		List<String> list = new ArrayList<String>();
		  list = new ArrayList<String>();
		  list.add("HKDist1");
		  list.add("HKDist2");
		  list.add("HKDist3");
		  list.add("KLDist1");
		  list.add("KLDist2");
		  list.add("NTDist1");
		spinDistdataAdapter = new ArrayAdapter<String>(getActivity(),
			android.R.layout.simple_spinner_item, list);
		spinDistdataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinDistrict.setAdapter(spinDistdataAdapter);
	  }
	  
	  private void setupDistlists() {
		  // Set up all dists
		  allDistlist = new ArrayList<String>();
		  allDistlist.add("HKDist1");
		  allDistlist.add("HKDist2");
		  allDistlist.add("HKDist3");
		  allDistlist.add("KLDist1");
		  allDistlist.add("KLDist2");
		  allDistlist.add("NTDist1");
		  // Set up HK districts
		  HKDistlist = new ArrayList<String>();
		  HKDistlist.add("HKDist1");
		  HKDistlist.add("HKDist2");
		  HKDistlist.add("HKDist3");
		  //Set up Kowloon districts
		  KowloonDistlist = new ArrayList<String>();
		  KowloonDistlist.add("KLDist1");
		  KowloonDistlist.add("KLDist2");
		  //Set up NT districts
		  NTDistlist = new ArrayList<String>();
		  NTDistlist.add("NTDist1");
	  }
	  
		public void startProgressBar() {
			pbProfileEdit.setVisibility(View.VISIBLE);
		}
		
		public void stopProgressBar() {
			pbProfileEdit.setVisibility(View.GONE);
		}

}
