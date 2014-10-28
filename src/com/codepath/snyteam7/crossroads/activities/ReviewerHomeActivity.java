package com.codepath.snyteam7.crossroads.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.ReviewerHomeFragment;
import com.codepath.snyteam7.crossroads.fragments.ReviewerReviewedItemsFragment;
import com.parse.ParseUser;

public class ReviewerHomeActivity extends FragmentActivity {
	ViewPager vpPager;
	MyPagerAdapter adapterViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reviewer_home);
		getActionBar().setTitle("");

		// Setup Viewpager
		setupViewpager();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		// Check for pager items
		if (vpPager.getCurrentItem() == 0) {
			ReviewerHomeFragment tab1Frag = (ReviewerHomeFragment)adapterViewPager.getRegisteredFragment(vpPager.getCurrentItem());
			tab1Frag.fetchReviewerList();
		}
		if (vpPager.getCurrentItem() == 1) {
			ReviewerReviewedItemsFragment tab2Frag = (ReviewerReviewedItemsFragment)adapterViewPager.getRegisteredFragment(vpPager.getCurrentItem());
			tab2Frag.fetchDonorList();
		}
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
	
	// Setup viewpager adapter
	private void setupViewpager() {
		vpPager = (ViewPager) findViewById(R.id.vpPager);
		adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
		vpPager.setAdapter(adapterViewPager);
	}
	
	// Viewpager adapter
    public static class MyPagerAdapter extends FragmentPagerAdapter {
    	private static int NUM_ITEMS = 2;
    	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();
		
        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }
        
        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
 
        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return new ReviewerHomeFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new ReviewerReviewedItemsFragment();
            default:
            	return null;
            }
        }
        
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
        	switch (position) {
            case 0:
            	return "Pending";
            case 1:
            	return "Reviewed";
            default:
            	return null;
            }	
            	
        }
        
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Fragment fragment = (Fragment) super.instantiateItem(container, position);
            registeredFragments.put(position, fragment);
            return fragment;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            registeredFragments.remove(position);
            super.destroyItem(container, position, object);
        }

        public Fragment getRegisteredFragment(int position) {
            return registeredFragments.get(position);
        }
        
    }

}
