package com.codepath.snyteam7.crossroads.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.activities.ReviewerHomeActivity.MyPagerAdapter;
import com.codepath.snyteam7.crossroads.fragments.LoginFragment;
import com.codepath.snyteam7.crossroads.fragments.ReviewerHomeFragment;
import com.codepath.snyteam7.crossroads.fragments.ReviewerReviewedItemsFragment;
import com.codepath.snyteam7.crossroads.fragments.LoginFragment.OnLoginSuccessListener;
import com.codepath.snyteam7.crossroads.fragments.SignupFragment;
import com.codepath.snyteam7.crossroads.fragments.SignupFragment.OnSignupSuccessListener;
import com.codepath.snyteam7.crossroads.listeners.FragmentTabListener;
import com.parse.ParseUser;

public class LoginActivity extends FragmentActivity 
	implements OnSignupSuccessListener, OnLoginSuccessListener {
	
	ViewPager vpPager;
	MyPagerAdapter adapterViewPager;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Drawable login_activity_background = getResources().getDrawable(R.drawable.hkstreet);
        login_activity_background.setAlpha(127);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        
		PagerTabStrip pagerTabStrip = (PagerTabStrip)findViewById(R.id.pager_header_login);
		pagerTabStrip.setDrawFullUnderline(true);
		pagerTabStrip.setTabIndicatorColor(Color.parseColor("#367588"));
        
		// Check for Parse user
        // Get current user data from Parse.com
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
    		String usertype = currentUser.getString("usertype");
    		Intent intent;
    		
    		if (usertype == null) {
    			Toast.makeText(this, "Login usertype unknown", Toast.LENGTH_LONG).show();
    		} else {
	            if (usertype.equalsIgnoreCase("reviewer")) {
	    			intent = new Intent(LoginActivity.this, ReviewerHomeActivity.class);
		            startActivity(intent);
	    		} else if (usertype.equalsIgnoreCase("donor")) {
	    			intent = new Intent(LoginActivity.this, DonorActivity.class);
		            startActivity(intent);
	    		}
	            finish();
    		}
        } else {
            // Send user to Login Signup Activity
        	Toast.makeText(this, "Login or Sign up", Toast.LENGTH_LONG).show();
        }
        
		// Setup Viewpager
		setupViewpager();
    }
	
	// Inflate the menu; this adds items to the action bar if it is present.
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        
            case R.id.miHowitworks:
            	onHowitworksAction();
                return true;
        }
        return false;
    }

	// OnCLick HowitWorks action
	public void onHowitworksAction() {
		Intent i = new Intent(this, HowtoActivity.class);
		startActivity(i);
	}

	@Override
	public void OnLoginSuccess() {
		finish();		
	}

	@Override
	public void OnSignupSuccess() {
		finish();		
	}
	
	// Setup viewpager adapter
	private void setupViewpager() {
		vpPager = (ViewPager) findViewById(R.id.vpPagerLogin);
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
                return new LoginFragment();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return new SignupFragment();
            default:
            	return null;
            }
        }
        
        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
        	switch (position) {
            case 0:
            	return "LOGIN";
            case 1:
            	return "SIGN UP";
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
