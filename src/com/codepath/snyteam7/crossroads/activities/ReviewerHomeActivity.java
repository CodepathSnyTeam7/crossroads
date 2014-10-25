package com.codepath.snyteam7.crossroads.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.ReviewerHomeFragment;
import com.codepath.snyteam7.crossroads.fragments.ReviewerReviewedItemsFragment;
import com.codepath.snyteam7.crossroads.listeners.FragmentTabListener;
import com.parse.ParseUser;

public class ReviewerHomeActivity extends FragmentActivity {

	private FragmentTabListener<ReviewerHomeFragment> tab1Listener;
	private FragmentTabListener<ReviewerReviewedItemsFragment> tab2Listener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviewer_home);
		getActionBar().setTitle("");
		// Setup reviewer tabs
		setupReviewerTabs();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if(tab1Listener != null && tab1Listener.getFragment() != null) {
			ReviewerHomeFragment tab1Frag = (ReviewerHomeFragment)tab1Listener.getFragment();
			tab1Frag.fetchReviewerList();
		}
		if(tab2Listener != null && tab2Listener.getFragment() != null) {
			ReviewerReviewedItemsFragment tab2Frag = (ReviewerReviewedItemsFragment)tab2Listener.getFragment();
			tab2Frag.fetchDonorList();  //it's actually fetching list for reviewer
		}
	}

	
    // Set up two tabs. One for Login and another for Sign up
	private void setupReviewerTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);
		
		tab1Listener = new FragmentTabListener<ReviewerHomeFragment>(R.id.flReviewerContainer, this, "ReviewerHomeFragment",
				ReviewerHomeFragment.class);
		
		ActionBar.Tab tab1 = actionBar
			.newTab()
			.setText("Pending")
			//.setIcon(R.drawable.ic_login)
			.setTag("ReviewerHomeFragment")
			.setTabListener(tab1Listener);

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);
		
		tab2Listener = new FragmentTabListener<ReviewerReviewedItemsFragment>(R.id.flReviewerContainer, this, "ReviewerReviewedItemsFragment",
				ReviewerReviewedItemsFragment.class);

		ActionBar.Tab tab2 = actionBar
			.newTab()
			.setText("Reviewed")
			//.setIcon(R.drawable.ic_signup)
			.setTag("ReviewerReviewedItemsFragment")
			.setTabListener(tab2Listener);

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
		switch (item.getItemId()) {
			case R.id.action_settings:
			return true;
			case R.id.action_signout:
				ParseUser.logOut();
				Intent i = new Intent(this, LoginActivity.class);
        		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(i);
				finish();
				return true;
			case R.id.miRProfile:
				ProfileAction();
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void ProfileAction () {
		Intent i = new Intent(this, ProfileActivity.class);
		startActivity(i);	
	}
}
