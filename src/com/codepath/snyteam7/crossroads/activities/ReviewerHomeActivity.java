package com.codepath.snyteam7.crossroads.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.DonorHomeFragment;
import com.codepath.snyteam7.crossroads.fragments.ReviewerHomeFragment;
import com.codepath.snyteam7.crossroads.fragments.ReviewerViewsFragment;
import com.codepath.snyteam7.crossroads.listeners.FragmentTabListener;
import com.parse.ParseUser;

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
			.setText("Pending")
			//.setIcon(R.drawable.ic_login)
			.setTag("ReviewerHomeFragment")
			.setTabListener(
				new FragmentTabListener<ReviewerHomeFragment>(R.id.flReviewerContainer, this, "ReviewerHometab",
														ReviewerHomeFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		ActionBar.Tab tab2 = actionBar
			.newTab()
			.setText("Accepted")
			//.setIcon(R.drawable.ic_signup)
			.setTag("ReviewerAcceptedFragment")
			.setTabListener(
			    new FragmentTabListener<ReviewerViewsFragment>(R.id.flReviewerContainer, this, "ReviewerAcceptedtab",
			    		ReviewerViewsFragment.class));
		actionBar.addTab(tab2);
		
		ActionBar.Tab tab3 = actionBar
				.newTab()
				.setText("Rejected")
				//.setIcon(R.drawable.ic_signup)
				.setTag("ReviewerRejectedFragment")
				.setTabListener(
				    new FragmentTabListener<ReviewerViewsFragment>(R.id.flReviewerContainer, this, "ReviewerRejectedtab",
				    		ReviewerViewsFragment.class));

			actionBar.addTab(tab3);
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
		if (id == R.id.action_signout) {
			ParseUser.logOut();
			Intent i = new Intent(this, LoginActivity.class);
			startActivity(i);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
