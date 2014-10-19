package com.codepath.snyteam7.crossroads.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.DonorHomeFragment;
import com.parse.ParseUser;

public class DonorActivity extends FragmentActivity {
	
	private DonorHomeFragment itemsListFragment;

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
		getActionBar().setTitle(getResources().getString(R.string.my_donations));
		setupItemListFragment();
	}
	 
    
	private void setupItemListFragment() {
		itemsListFragment = DonorHomeFragment.getInstance();
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.flDonorContainer, itemsListFragment);
		ft.commit();
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
