package com.codepath.snyteam7.crossroads.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.ItemDetailFragment;

public class ReceiverItemDetailsActivity extends FragmentActivity {
	
	ItemDetailFragment detailsFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver_item_details);
		getActionBar().setTitle("");
		setupDetailsFragment();

	}

	private void setupDetailsFragment() {
		String itemObjIdStr = getIntent().getStringExtra("item_objid");
		detailsFragment = ItemDetailFragment.newInstance(itemObjIdStr);
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		ft.replace(R.id.detail_fragment_placeholder, detailsFragment);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.receiver_item_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_accept) {
			// open the dialog fragment
			openAcceptFormDialog();
			return true;
		} else if (id == R.id.action_reject) {

		} else if (id == R.id.action_viewmessages) {
			
		}
		return super.onOptionsItemSelected(item);
	}

	private void openAcceptFormDialog() {
		// inflate message_item.xml view
		View formView = LayoutInflater.from(ReceiverItemDetailsActivity.this).inflate(
				R.layout.accept_form, null);
		// Create alert dialog builder
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setView(formView);

		// Create alert dialog
		final AlertDialog alertDialog = alertDialogBuilder.create();

		// Configure dialog button (OK)
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "Accept",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {						
						// Extract content from alert dialog
						String notes = ((EditText) alertDialog
								.findViewById(R.id.etNotes)).getText()
								.toString();
						Spinner categorySpinner = (Spinner)alertDialog
								.findViewById(R.id.spinnerCategory);
						int selectedPosition = categorySpinner.getSelectedItemPosition();
						
					}
				});

		// Configure dialog button (Cancel)
		alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});

		// Display the dialog
		alertDialog.show();
	}

}
