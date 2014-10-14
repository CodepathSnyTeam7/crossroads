package com.codepath.snyteam7.crossroads.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.adapters.DonorHomeListAdapter;

public class DonorActivity extends Activity {
	
	private ListView lvItems;
	public  ArrayAdapter<DonorHomeListAdapter> aItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donor);
		
		// Assign view references
		lvItems = (ListView) findViewById(R.id.lvReviewerHome);
		lvItems.setClickable(true);
		lvItems.setAdapter(aItems);
	}
	
	public void onDonate() {
		Intent i = new Intent(this, DonateActivity.class);
		startActivity(i);
	}
}
