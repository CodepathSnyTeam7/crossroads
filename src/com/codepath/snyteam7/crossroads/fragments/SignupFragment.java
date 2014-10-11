package com.codepath.snyteam7.crossroads.fragments;

import com.example.crossroads.R;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SignupFragment extends Fragment {
    public Context thisContext = null;
	
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
		//lvTweets = (ListView) v.findViewById(R.id.lvTweets);
		//lvTweets.setClickable(true);
		//lvTweets.setAdapter(aTweets);
		
		//Set up view listeners
		//setupViewListeners(v);
		
		// Return layout view
		return v;
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
}
