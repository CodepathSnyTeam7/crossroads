package com.codepath.snyteam7.crossroads.fragments;

import java.util.ArrayList;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.activities.DonorItemDetailsActivity;
import com.codepath.snyteam7.crossroads.adapters.DonorHomeListAdapter;
import com.codepath.snyteam7.crossroads.listeners.EndlessScrollListener;
import com.codepath.snyteam7.crossroads.model.Item;

public class ItemListFragment extends Fragment {
    private SwipeRefreshLayout donorSwipeContainer;
	private ArrayList<Item> items;
	public DonorHomeListAdapter aItems;
	private ListView lvItems;
	//private Button emptyListButton;
	private ProgressBar pbItemList;
	private int itemsTotalCount = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {

		// Inflate the layout not attach it yet
		View v = inflater.inflate(R.layout.fragment_donor_list, container, false);
		
		// Assign view references
		// Inflate the layout not attach it yet
		lvItems = (ListView) v.findViewById(R.id.lvDonorList);
		lvItems.setClickable(true);
		lvItems.setAdapter(aItems);
		//emptyListButton = (Button) v.findViewById(android.R.id.empty);
		lvItems.setEmptyView((ImageView)v.findViewById(android.R.id.empty));
		//Set up view listeners
		setupViewListeners(v);
		
		// Return layout view
		return v;
	}
	
	// Setup swipe refresh
	private void setupViewListeners(View v) {
	    donorSwipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeDonorContainer);
	    // Setup refresh listener which triggers new data loading
	    donorSwipeContainer.setOnRefreshListener(new OnRefreshListener() {
	        @Override
	        public void onRefresh() {
	        	fetchDonorList();
	        } 
	    });
	        
	    // Configure the refreshing colors
	    donorSwipeContainer.setColorSchemeColors(android.R.color.holo_blue_bright, 
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
        			fetchDonorList();
        		}
        	}
        });
        
        // Set up on click listener for detail view
	    lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Launch the item display activity
				Intent i = new Intent(getActivity(), DonorItemDetailsActivity.class);
				Item item = aItems.getItem(position);
				i.putExtra("item_objid", item.getObjectId());
				startActivity(i);
				
			}		
		});
	    /*
	    emptyListButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent i = new Intent(getActivity(), DonateActivity.class);
				startActivity(i);
			}
		});
		*/
	}
	
	// Fetch the list items from Parse
    private void fetchDonorList() {
    	// Fetch the list items from Parse with pagination
    	aItems.loadObjects();
    }
    
    public static DonorHomeFragment getInstance() {
    	return new DonorHomeFragment();
    }
    
    private void startProgressBar() {
    	pbItemList.setVisibility(View.VISIBLE);
    }
    
    private void stopProgressBar() {
    	pbItemList.setVisibility(View.GONE);
    }

}
