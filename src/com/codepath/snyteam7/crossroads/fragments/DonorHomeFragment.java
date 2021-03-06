package com.codepath.snyteam7.crossroads.fragments;

import android.os.Bundle;

import com.codepath.snyteam7.crossroads.adapters.DonorHomeListAdapter;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class DonorHomeFragment extends ItemListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// create instances
		ParseQueryAdapter.QueryFactory<Item> queryFactory = new ParseQueryAdapter.QueryFactory<Item>() {
			public ParseQuery<Item> create() {
				// Here we can configure a ParseQuery to display
				// items for review.
				ParseQuery<Item> query = new ParseQuery<Item>("Item");
				// Query Donor items
				ParseUser loggedInUser = ParseUser.getCurrentUser();
				query.whereContains("donorusername", loggedInUser.getUsername());
				//query.orderByDescending("rating");
				return query;
			}
		};
		aItems = new DonorHomeListAdapter(getActivity(), queryFactory);
	}
}

