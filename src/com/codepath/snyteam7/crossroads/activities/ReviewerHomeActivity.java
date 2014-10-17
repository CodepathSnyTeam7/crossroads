package com.codepath.snyteam7.crossroads.activities;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.ReviewerHomeFragment;
import com.codepath.snyteam7.crossroads.fragments.ReviewerViewsFragment;
import com.codepath.snyteam7.crossroads.listeners.FragmentTabListener;

public class ReviewerHomeActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviewer_home);

		// Setup reviewer tabs
		setupReviewerTabs();
	}

	
    // Set up two tabs. One for Login and another for Sign up
	private void setupReviewerTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		ActionBar.Tab tab1 = actionBar
			.newTab()
			.setText("HOME")
			//.setIcon(R.drawable.ic_login)
			.setTag("ReviewerHomeFragment")
			.setTabListener(
				new FragmentTabListener<ReviewerHomeFragment>(R.id.flReviewerContainer, this, "ReviewerHometab",
														ReviewerHomeFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		ActionBar.Tab tab2 = actionBar
			.newTab()
			.setText("MY VIEW")
			//.setIcon(R.drawable.ic_signup)
			.setTag("ReviewerViewsFragment")
			.setTabListener(
			    new FragmentTabListener<ReviewerViewsFragment>(R.id.flReviewerContainer, this, "ReviewerViewstab",
														ReviewerViewsFragment.class));

		actionBar.addTab(tab2);
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
