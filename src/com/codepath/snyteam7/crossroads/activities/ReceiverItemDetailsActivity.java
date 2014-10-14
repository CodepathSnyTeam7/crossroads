package com.codepath.snyteam7.crossroads.activities;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.FindCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

public class ReceiverItemDetailsActivity extends Activity {

	private ParseImageView ivPhoto;
	private TextView tvDonorName;
	private TextView tvDescription;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver_item_details);
		ivPhoto = (ParseImageView) findViewById(R.id.ivItemPhoto);
		tvDonorName = (TextView) findViewById(R.id.tvDonorName);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
		query.findInBackground(new FindCallback<Item>() {

			@Override
			public void done(List<Item> arg0, ParseException e) {
				if (e == null) {
					if (arg0 != null && arg0.size() > 0) {
						Item item0 = arg0.get(0);
						tvDescription.setText(item0.getDescription());
						ParseFile photoFile = item0.getPhotoFile();
						if (photoFile != null) {
							ivPhoto.setParseFile(photoFile);
							ivPhoto.loadInBackground(new GetDataCallback() {

								@Override
								public void done(byte[] arg0, ParseException e) {
									if (e == null) {

									} else {
										e.printStackTrace();
										Toast.makeText(
												ReceiverItemDetailsActivity.this,
												"error fetching photo",
												Toast.LENGTH_LONG).show();
									}

								}
							});
						}
					}
				} else {
					e.printStackTrace();
					Toast.makeText(ReceiverItemDetailsActivity.this,
							"error finding items", Toast.LENGTH_LONG).show();
				}

			}
		});
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
