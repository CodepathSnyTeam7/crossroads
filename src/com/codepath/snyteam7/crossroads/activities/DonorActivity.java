package com.codepath.snyteam7.crossroads.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.codepath.snyteam7.crossroads.DonorItemDetailsActivity;
import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.adapters.DonorHomeListAdapter;
import com.codepath.snyteam7.crossroads.model.Item;

public class DonorActivity extends Activity {
	
	private ListView lvItems;
	public  DonorHomeListAdapter aItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donor);
		
		// Assign view references
		lvItems = (ListView) findViewById(R.id.lvDonorList);
		lvItems.setClickable(true);
		aItems = new DonorHomeListAdapter(this);
		lvItems.setAdapter(aItems);
        // Set up on click listener for detail view
	    lvItems.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// Launch the item display activity
				Intent i = new Intent(DonorActivity.this, DonorItemDetailsActivity.class);
				Item item = aItems.getItem(position);
				i.putExtra("item_objid", item.getObjectId());
				startActivity(i);				
			}		
		});		
		fetchDonorList();
	}
	
	// TBD: Fetch the list items from Parse
    private void fetchDonorList() {
    	// TBD: Fetch the list items from Parse with pagination
    	aItems.loadObjects();
    }
    
	
	public void onDonate(View v) {
		Intent i = new Intent(this, DonateActivity.class);
		startActivity(i);
	}
}
