package com.codepath.snyteam7.crossroads.fragments;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;

import com.codepath.snyteam7.crossroads.adapters.DonorHomeListAdapter;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class ReviewerReviewedItemsFragment extends ItemListFragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// create instances
		ParseQueryAdapter.QueryFactory<Item> queryFactory = new ParseQueryAdapter.QueryFactory<Item>() {
			public ParseQuery<Item> create() {
				// Here we can configure a ParseQuery to display
				// items for review.
				ParseQuery<Item> query1 = new ParseQuery<Item>("Item");
				// Query items that have been rejected or accepted
				query1.whereExists("accepteddate");
				ParseQuery<Item> query2 = new ParseQuery<Item>("Item");
				// Query items that have been rejected or accepted
				query2.whereExists("rejecteddate");
				List<ParseQuery<Item>> queryList = new ArrayList<ParseQuery<Item>>();
				queryList.add(query1);
				queryList.add(query2);
				ParseQuery<Item> query = ParseQuery.or(queryList);
				query.orderByDescending("updatedAt");
				return query;
			}
		};
		aItems = new DonorHomeListAdapter(getActivity(), queryFactory);
	}

}
