package com.codepath.snyteam7.crossroads.activities;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.profileFragment;
import com.parse.ParseUser;

public class ProfileActivity extends FragmentActivity {
	private static TextView ProfileName;
    private static TextView ProfileUsername;
    private static TextView ProfilePhone;
    private static TextView ProfileEmail;
    private static TextView ProfileDistrict;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		ActionBar bar = getActionBar();
		bar.setTitle("Profile");
		populateProfileInfo();
	}

	private void populateProfileInfo() {

		ProfileName = (TextView)findViewById(R.id.tvProfNames);
		ProfileUsername = (TextView)findViewById(R.id.tvProfuname);
		ProfilePhone = (TextView)findViewById(R.id.tvProfPhone);
		ProfileEmail = (TextView)findViewById(R.id.tvProfEmail);
		ProfileDistrict = (TextView) findViewById(R.id.tvProfDist);

		// Create the ParseUser
		ParseUser user = ParseUser.getCurrentUser();
		
		// Set core properties
		String ufirst = user.getString("firstname");
		if ((ufirst == null) || ufirst.isEmpty()) {
			ufirst = " ";
		}	
		String ulast = user.getString("lastname");
		if ((ulast == null) || ulast.isEmpty()) {
			ulast = " ";
		}
		ProfileName.setText(ufirst + " " + ulast);
	
		

		ProfileUsername.setText("@" + user.getUsername());
		
		String udistrict = user.getString("district");
		if ((udistrict == null) || udistrict.isEmpty()) {
			udistrict = " ";
		}
		ProfileDistrict.setText(udistrict);
		
		
		String uemail = user.getEmail();
		if ((uemail == null) || uemail.isEmpty()) {
			uemail = " ";
		}
		ProfileEmail.setText(uemail);
		
		String uphone = user.getString("phonenumber");
		if ((uphone == null) || uphone.isEmpty()) {
			uphone = " ";
		}
		ProfilePhone.setText(uphone);
	    
	    //ImageLoader imageLoader = ImageLoader.getInstance();
	    //imageLoader.displayImage(user.getProfileImageUrl(), ivProfileImage);
		
		
	}
	
	public void onClickProfEdit (View v) {
		FragmentManager fm = getSupportFragmentManager();
        profileFragment pFrag = new profileFragment();
        pFrag.show(fm, "fragment_profile");
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
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
	}
		return super.onOptionsItemSelected(item);
	}
}
