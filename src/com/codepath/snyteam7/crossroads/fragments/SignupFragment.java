package com.codepath.snyteam7.crossroads.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.activities.DonorActivity;
import com.codepath.snyteam7.crossroads.activities.LoginActivity;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupFragment extends Fragment {
    public Context thisContext = null;
    private static EditText signupUsername;
    private static EditText signupPassword;
    private static EditText signupPhone;
    private static EditText signupEmail;
    private Spinner spinState;
    private Spinner spinDistrict;
    List<String> allDistlist;
    List<String> HKDistlist;
    List<String> KowloonDistlist;
    List<String> NTDistlist;
    ArrayAdapter<String> spinStatedataAdapter;
    ArrayAdapter<String> spinDistdataAdapter;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setupDistlists();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout not attach it yet
		View v = inflater.inflate(R.layout.fragment_signup, container, false);
		
		// Assign view references
		signupUsername = (EditText) v.findViewById(R.id.etSignupUsername);
		signupPassword = (EditText) v.findViewById(R.id.etSignupPassword);
		signupPhone = (EditText) v.findViewById(R.id.etSignupPhone);
		signupEmail = (EditText) v.findViewById(R.id.etSignupEmail);	
		addItemsOnSpinnerState(v);
		addItemsOnSpinnerDistrict(v);
		
		//Set up view listeners
		setupViewListeners(v);
		
		// Return layout view
		return v;
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
	
	public void setupViewListeners(View v) {
		// Signup button listener
		final Button button = 
                (Button) v.findViewById(R.id.btSignup);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                signupButtonClicked(v);
	            }
	    }); 
	    // Setup District listener
	    spinState.setOnItemSelectedListener(spinStateitemSelectedListener);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override
	public void onAttach(Activity activity) {
	    super.onAttach(activity);
	    thisContext = activity;
	}
	
	public void signupButtonClicked(View view) {
		String uname = signupUsername.getText().toString();
		String upasswd = signupPassword.getText().toString();
		String uphone = signupPhone.getText().toString();
		String uemail = signupEmail.getText().toString();
		
		// Check for boundary conditions
		if (uname.isEmpty() || upasswd.isEmpty()) {
			Log.d("debug", "Username or Password is null");
			return;
		}
		
		// Create the ParseUser
		ParseUser user = new ParseUser();
		// Set core properties
		user.setUsername(uname);
		user.setPassword(upasswd);
		user.setEmail(uemail);
		// Set custom properties
		user.put("phonenumber", uphone);
		user.put("usertype", "donor");
		String dist = String.valueOf(spinDistrict.getSelectedItem());
		if (!dist.isEmpty()) {
			user.put("district", dist);
		}
		
		// Invoke signUpInBackground
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
		    if (e == null) {
		    	// Hooray! Let them use the app now.
		    	Log.d("debug", "Sign up Successful");
		    	
				// Store the username in the installation object for receiving push notifications
				ParseInstallation installation = ParseInstallation.getCurrentInstallation();
				ParseUser currentUser = ParseUser.getCurrentUser();
				installation.put("username", currentUser.getUsername());
				installation.saveInBackground();
				
				// Start the Donor activity
		    	Intent i = new Intent(getActivity(), DonorActivity.class);
		    	startActivity(i);
		    } else {
		      // Sign up didn't succeed. Look at the ParseException
		      // to figure out what went wrong
		    	e.printStackTrace();
		    	Log.d("debug", "Sign up didn't succeed");
		    	Toast.makeText(getActivity(), "Signup didn't succeed. Please try again!", Toast.LENGTH_LONG).show();
		    }

		  }
		});
	}
	
	  // add items into spinner dynamically
	  public void addItemsOnSpinnerState(View v) {
		spinState = (Spinner) v.findViewById(R.id.spinnerSignupState);
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
	  public void addItemsOnSpinnerDistrict(View v) {
		spinDistrict = (Spinner) v.findViewById(R.id.spinnerSignupDistrict);
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
}
