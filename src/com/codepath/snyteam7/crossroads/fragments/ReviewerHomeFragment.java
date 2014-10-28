package com.codepath.snyteam7.crossroads.fragments;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.activities.ReceiverItemDetailsActivity;
import com.codepath.snyteam7.crossroads.adapters.ReviewerHomeListAdapter;
import com.codepath.snyteam7.crossroads.listeners.EndlessScrollListener;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.ParseQueryAdapter.OnQueryLoadListener;

public class ReviewerHomeFragment extends Fragment {
    private SwipeRefreshLayout reviewerSwipeContainer;
	private ArrayList<Item> items;
	public ReviewerHomeListAdapter aItems;
	private ListView lvItems;
	private int itemsTotalCount = 0;
	private ProgressBar pbReviewerHomeList;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// create instances
		aItems = new ReviewerHomeListAdapter(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout not attach it yet
		View v = inflater.inflate(R.layout.fragment_reviewer_home, container, false);
		
		// Assign view references
		// Inflate the layout not attach it yet
		lvItems = (ListView) v.findViewById(R.id.lvReviewerHome);
		lvItems.setClickable(true);
		lvItems.setAdapter(aItems);
		
		pbReviewerHomeList = (ProgressBar)v.findViewById(R.id.pbReviewerHomeList);
		
		//Set up view listeners
		setupViewListeners(v);

		// Return layout view
		return v;
	}
	
	// Setup swipe refresh
	private void setupViewListeners(View v) {
	    reviewerSwipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeReviewerContainer);
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
				// Launch the item display activity
				Intent i = new Intent(getActivity(), ReceiverItemDetailsActivity.class);
				Item item = aItems.getItem(position);
				i.putExtra("item_objid", item.getObjectId());
				startActivity(i);
				
			}		
		});
	    
		// Set a callback to be fired upon successful loading of a new set of ParseObjects.
		 aItems.addOnQueryLoadListener(new OnQueryLoadListener<Item>() {
			 @Override
			 public void onLoading() {
		     // Trigger any "loading" UI
				 startProgressBar();
		   }

			@Override
			public void onLoaded(List<Item> arg0, Exception arg1) {
				stopProgressBar();
			}
		 });

	}
	
	// Fetch the list items from Parse
    public void fetchReviewerList() {
    	// Fetch the list items from Parse with pagination
    	aItems.loadObjects();
    }
    
    private void startProgressBar() {
    	pbReviewerHomeList.setVisibility(View.VISIBLE);
    }
    
    private void stopProgressBar() {
    	pbReviewerHomeList.setVisibility(View.GONE);
    }


}
