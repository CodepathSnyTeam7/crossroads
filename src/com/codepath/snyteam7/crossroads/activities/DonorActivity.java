package com.codepath.snyteam7.crossroads.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.adapters.DonorHomeListAdapter;
import com.codepath.snyteam7.crossroads.adapters.ReviewerHomeListAdapter;

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
		
		fetchDonorList();
	}
	
	// TBD: Fetch the list items from Parse
    private void fetchDonorList() {
    	// TBD: Fetch the list items from Parse with pagination
    	aItems.loadObjects();
    }
    
	
	public void onDonate() {
		Intent i = new Intent(this, DonateActivity.class);
		startActivity(i);
	}
}
