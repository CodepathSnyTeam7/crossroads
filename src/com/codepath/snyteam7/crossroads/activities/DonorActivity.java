package com.codepath.snyteam7.crossroads.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.adapters.DonorHomeListAdapter;
import com.codepath.snyteam7.crossroads.fragments.DonorHomeFragment;
import com.codepath.snyteam7.crossroads.listeners.FragmentTabListener;
import com.parse.ParseUser;

public class DonorActivity extends FragmentActivity {
	
	private ListView lvItems;
	public  DonorHomeListAdapter aItems;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.donate_button, menu);
        return true;
    }
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donor);
	    
		// Assign view references
		/*lvItems = (ListView) findViewById(R.id.lvDonorList);
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
		lvItems.setAdapter(aItems);*/
		
		//fetchDonorList();
		setupReviewerTabs();

	}
	 
	// Setting Donor Home and Lists
		private void setupReviewerTabs() {
			ActionBar actionBar = getActionBar();
			actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
			actionBar.setDisplayShowTitleEnabled(true);

			ActionBar.Tab tab1 = actionBar
				.newTab()
				.setText("Home")
				//.setIcon(R.drawable.ic_login)
				.setTag("DonorHomeFragment")
				.setTabListener(
					new FragmentTabListener<DonorHomeFragment>(R.id.flDonorContainer, this, "DonorHometab",
							DonorHomeFragment.class));

			actionBar.addTab(tab1);
			actionBar.selectTab(tab1);

			ActionBar.Tab tab2 = actionBar
				.newTab()
				.setText("Pending")
				//.setIcon(R.drawable.ic_signup)
				.setTag("DonorPendingFragment")
				.setTabListener(
				    new FragmentTabListener<DonorHomeFragment>(R.id.flDonorContainer, this, "DonorPendingTab",
				    		DonorHomeFragment.class));

			actionBar.addTab(tab2);
			
		}
	
	// TBD: Fetch the list items from Parse
    private void fetchDonorList() {
    	// TBD: Fetch the list items from Parse with pagination
    	aItems.loadObjects();
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.miDonate:
            	onDonate();
                return true;
            case R.id.action_signout:
        		ParseUser.logOut();
        		Intent i = new Intent(this, LoginActivity.class);
        		startActivity(i);
        		return true; 
            default:
                return super.onOptionsItemSelected(item);
        }
    }
	
	public void onDonate() {
		Intent i = new Intent(this, DonateActivity.class);
		startActivity(i);
	}

}
