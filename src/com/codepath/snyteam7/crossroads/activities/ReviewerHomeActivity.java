package com.codepath.snyteam7.crossroads.activities;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.adapters.ReviewerHomeListAdapter;
import com.codepath.snyteam7.crossroads.listeners.EndlessScrollListener;
import com.codepath.snyteam7.crossroads.model.Item;

public class ReviewerHomeActivity extends Activity {
    private SwipeRefreshLayout reviewerSwipeContainer;
	private ArrayList<Item> items;
	public ReviewerHomeListAdapter aItems;
	private ListView lvItems;
	private int itemsTotalCount = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviewer_home);
		
		// Assign view references
		lvItems = (ListView) findViewById(R.id.lvReviewerHome);
		lvItems.setClickable(true);
		lvItems.setAdapter(aItems);
		
		// Setup swipe for refresh
		setupViewListeners();
        // Fetch the review items
        fetchReviewerList();
	}

	// Setup swipe refresh
	private void setupViewListeners() {
	    reviewerSwipeContainer = (SwipeRefreshLayout) findViewById(R.id.reviewerSwipeContainer);
	    // Setup refresh listener which triggers new data loading
	    reviewerSwipeContainer.setOnRefreshListener(new OnRefreshListener() {
	        @Override
	        public void onRefresh() {
	            fetchReviewerList();
	        } 
	    });
	        
	    // Configure the refreshing colors
	    reviewerSwipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright, 
	            android.R.color.holo_green_light, 
	            android.R.color.holo_orange_light, 
	            android.R.color.holo_red_light);
	    
        // Attach the on scroll listener
	    lvItems.setOnScrollListener(new EndlessScrollListener() {
        	@Override
        	public void onLoadMore(int page, int totalItemsCount) {
        		// Triggered only when new data needs to be appended to the list
        		if (totalItemsCount > 0) {
        			itemsTotalCount = totalItemsCount;
        			fetchReviewerList();
        		}
        	}
        });
        
        // Set up on click listener for detail view
	    lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*
				// TBD Launch the item display activity
				Intent i = new Intent(getActivity(), ItemDisplayActivity.class);
				Tweet displaytweet = tweets.get(position);
				i.putExtra("tweet", displaytweet);
				// Start the new activity
				startActivity(i);
				*/			
			}		
		});
	}
	
	// TBD: Fetch the list items from Parse
    private void fetchReviewerList() {
    	// TBD: Fetch the list items from Parse
    }
    
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reviewer_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
