package com.codepath.snyteam7.crossroads.fragments;

import com.codepath.snyteam7.crossroads.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ReviewerViewsFragment extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout not attach it yet
		View v = inflater.inflate(R.layout.fragment_reviewer_home, container, false);
		
		// Assign view references
		
		//Set up view listeners
		//setupViewListeners(v);
		
		// Return layout view
		return v;
	}

}
