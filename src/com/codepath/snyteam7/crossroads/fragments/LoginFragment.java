package com.codepath.snyteam7.crossroads.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.activities.ReviewerHomeActivity;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginFragment extends Fragment {
    public Context thisContext = null;
    private static EditText loginUsername;
    private static EditText loginPassword;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout not attach it yet
		View v = inflater.inflate(R.layout.fragment_login, container, false);
		
		// Assign view references
		loginUsername = (EditText) v.findViewById(R.id.etLoginUsername);
		loginPassword = (EditText) v.findViewById(R.id.etLoginPassword);
		
		//Set up view listeners
		setupViewListeners(v);
		
		// Return layout view
		return v;
	}
	
	public void setupViewListeners(View v) {
		final Button button = 
                (Button) v.findViewById(R.id.btLogin);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                loginButtonClicked(v);
	            }
	        });
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

	public void loginButtonClicked(View view) {
		String uname = loginUsername.getText().toString();
		String upasswd = loginPassword.getText().toString();
		
		// Check for boundary conditions
		if (uname.isEmpty() || upasswd.isEmpty()) {
			Log.d("debug", "Username or Password is null");
			return;
		}
		
		// Login User using ParseUser
		ParseUser.logInInBackground(uname, upasswd, new LogInCallback() {
			  public void done(ParseUser user, ParseException e) {
			    if (user != null) {
			    	// Hooray! The user is logged in.
			    	// TBD: Check for the User type and call the corresponding Activity
			    	// Donor - call Donor Home activity
			    	// Reviewer - call Reviewer Home activity
			    	onLoginSuccessCallActivity(user);
			    	Log.d("debug", "Login Successful!!!!");
			    } else {
			      // Signup failed. Look at the ParseException to see what happened.
			    	Log.d("debug", "Login failed!!");
			    }
			  }
			});
	}
	
	public void onLoginSuccessCallActivity(ParseUser user) {
    	// TBD: VV Call the Donor Home Activity if successful
		if (user.getString("usertype").equalsIgnoreCase("reviewer")) {
			Intent intent = new Intent().setClass(getActivity(), ReviewerHomeActivity.class);
			startActivity(intent);
		}
		return;
	}
}
