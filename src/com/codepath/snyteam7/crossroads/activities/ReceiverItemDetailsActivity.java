package com.codepath.snyteam7.crossroads.activities;

import java.util.Date;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.ChatRoomFragment;
import com.codepath.snyteam7.crossroads.fragments.ItemDetailFragment;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class ReceiverItemDetailsActivity extends FragmentActivity {
	
	ItemDetailFragment detailsFragment;
	String itemObjIdStr;
	ProgressBar pbReceiverDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver_item_details);
		getActionBar().setTitle("");
		setupDetailsFragment();
		pbReceiverDetails = (ProgressBar)findViewById(R.id.pbReceiverDetails);
	}

	private void setupDetailsFragment() {
		itemObjIdStr = getIntent().getStringExtra("item_objid");
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
			ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
			query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or
																				// CACHE_ONLY
			// Execute the query to find the object with ID
			query.getInBackground(itemObjIdStr, new GetCallback<Item>() {
				public void done(Item item0, ParseException e) {
					startProgressBar();
					item0.put("rejecteddate", new Date());
					item0.setReviewer(ParseUser.getCurrentUser());
					item0.saveInBackground(new SaveCallback() {
						
						@Override
						public void done(ParseException arg0) {
							stopProgressBar();
							if(arg0 == null) {
								// Send reject push
								pushItemReviewResult("Item not accepted");
								Toast.makeText(ReceiverItemDetailsActivity.this, "Item Saved!", Toast.LENGTH_LONG).show();
								startReviewerHomeActivity();
							}
							
						}
					});
				}
			});
			return true;
		} else if (id == R.id.action_viewmessages) {
			showChatRoomDialog();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	private void showChatRoomDialog() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if(prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);
		
		ParseUser loggedInUser = ParseUser.getCurrentUser();
		DialogFragment chatFragment = ChatRoomFragment.getInstance(loggedInUser.getObjectId(), itemObjIdStr, loggedInUser.getUsername());
		chatFragment.show(ft, "dialog");
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
						startProgressBar();
						// Extract content from alert dialog
						String notes = ((EditText) alertDialog
								.findViewById(R.id.etNotes)).getText()
								.toString();
						Spinner categorySpinner = (Spinner)alertDialog
								.findViewById(R.id.spinnerCategory);
						final String category = (String)categorySpinner.getSelectedItem();
						final ParseUser reviewer = ParseUser.getCurrentUser();
						
						ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
						query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
						// Execute the query to find the object with ID

						query.getInBackground(itemObjIdStr, new GetCallback<Item>() {
						  public void done(Item item0, ParseException e) {
							  item0.put("accepteddate", new Date());
							  item0.put("category", category);
							  item0.setReviewer(reviewer);
							  item0.saveInBackground(new SaveCallback(){

								@Override
								public void done(ParseException arg0) {
									stopProgressBar();
									if(arg0 == null) {
										// Send accept push
										pushItemReviewResult("Item accepted");
										Toast.makeText(ReceiverItemDetailsActivity.this, "Item Saved!", Toast.LENGTH_LONG).show();
										startReviewerHomeActivity();
									}
								}
								
							});
						  }
						});
						
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
	
	public void startProgressBar() {
		pbReceiverDetails.setVisibility(View.VISIBLE);
	}
	
	public void stopProgressBar() {
		pbReceiverDetails.setVisibility(View.GONE);
	}
	
	public void startReviewerHomeActivity() {
		Intent i = new Intent(this, ReviewerHomeActivity.class);
		startActivity(i);
	}

	private void pushItemReviewResult(String result) {
		Toast.makeText(ReceiverItemDetailsActivity.this,
				"Sending Push notification", Toast.LENGTH_LONG).show();
		ParsePush push = new ParsePush();
		ParseQuery pQuery = ParseInstallation.getQuery(); 
		pQuery.whereEqualTo("username", detailsFragment.getDonorName());
		push.setQuery(pQuery);
		push.setMessage(result);
		push.sendInBackground();
		
	}
}
