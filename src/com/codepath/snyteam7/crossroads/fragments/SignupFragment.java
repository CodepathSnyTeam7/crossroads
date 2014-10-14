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
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.activities.LoginActivity;
import com.codepath.snyteam7.crossroads.activities.DonorActivity;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignupFragment extends Fragment {
    public Context thisContext = null;
    private static EditText signupUsername;
    private static EditText signupPassword;
    private static EditText signupPhone;
    private static EditText signupEmail;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

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
		
		//Set up view listeners
		setupViewListeners(v);
		
		// Return layout view
		return v;
	}
	
	public void setupViewListeners(View v) {
		final Button button = 
                (Button) v.findViewById(R.id.btSignup);
	        button.setOnClickListener(new View.OnClickListener() {
	            public void onClick(View v) {
	                signupButtonClicked(v);
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
		// Invoke signUpInBackground
		user.signUpInBackground(new SignUpCallback() {
		  public void done(ParseException e) {
		    if (e == null) {
		    	// Hooray! Let them use the app now.
		    	Log.d("debug", "Sign up Successful");
		    	Intent i = new Intent(getActivity(), DonorActivity.class);
		    	startActivity(i);
		    } else {
		      // Sign up didn't succeed. Look at the ParseException
		      // to figure out what went wrong
		    	e.printStackTrace();
		    	Log.d("debug", "Sign up didn't succeed");
		    	Toast.makeText(getActivity(), "signup didn't work", Toast.LENGTH_LONG).show();
		    }
	    	// TBD: VV Call the Donor Home Activity if successful
	    	Intent intent = new Intent().setClass(getActivity(), LoginActivity.class);
			startActivity(intent);
		  }
		});
	}
}
