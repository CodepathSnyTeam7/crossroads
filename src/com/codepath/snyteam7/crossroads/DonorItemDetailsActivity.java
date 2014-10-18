package com.codepath.snyteam7.crossroads;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.codepath.snyteam7.crossroads.fragments.ItemDetailFragment;

public class DonorItemDetailsActivity extends FragmentActivity {
	ItemDetailFragment detailsFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donor_item_details);
		getActionBar().setTitle("");
		setupDetailsFragment();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.donor_item_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_viewmessages) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private void setupDetailsFragment() {
		String itemObjIdStr = getIntent().getStringExtra("item_objid");
		detailsFragment = ItemDetailFragment.newInstance(itemObjIdStr);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.detail_fragment_placeholder, detailsFragment);
		ft.commit();
	}
}
