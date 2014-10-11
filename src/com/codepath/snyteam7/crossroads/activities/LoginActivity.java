package com.codepath.snyteam7.crossroads.activities;

import android.app.ActionBar;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.snyteam7.crossroads.fragments.HowitworksDialogFragment;
import com.codepath.snyteam7.crossroads.fragments.LoginFragment;
import com.codepath.snyteam7.crossroads.fragments.SignupFragment;
import com.codepath.snyteam7.crossroads.listeners.FragmentTabListener;
import com.example.crossroads.R;

public class LoginActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        Drawable login_activity_background = getResources().getDrawable(R.drawable.hkstreet);
        login_activity_background.setAlpha(127);
        
		// Set up login tabs
		setupLoginTabs();
    }
    
    // Set up two tabs. One for Login and another for Sign up
	private void setupLoginTabs() {
		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(true);

		ActionBar.Tab tab1 = actionBar
			.newTab()
			.setText("LOGIN")
			//.setIcon(R.drawable.ic_login)
			.setTag("LoginFragment")
			.setTabListener(
				new FragmentTabListener<LoginFragment>(R.id.flContainer, this, "Logintab",
														LoginFragment.class));

		actionBar.addTab(tab1);
		actionBar.selectTab(tab1);

		ActionBar.Tab tab2 = actionBar
			.newTab()
			.setText("SIGN UP")
			//.setIcon(R.drawable.ic_signup)
			.setTag("SingnupFragment")
			.setTabListener(
			    new FragmentTabListener<SignupFragment>(R.id.flContainer, this, "Signuptab",
														SignupFragment.class));

		actionBar.addTab(tab2);
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
        FragmentManager fm = getSupportFragmentManager();
        HowitworksDialogFragment filterItem = new HowitworksDialogFragment();
        filterItem.show(fm, "fragment_howitworks");
	}
}
