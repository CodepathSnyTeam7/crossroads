package com.codepath.snyteam7.crossroads.activities;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
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
		ivPhoto = (ParseImageView)findViewById(R.id.ivItemPhoto);
		tvDonorName = (TextView)findViewById(R.id.tvDonorName);
		tvDescription = (TextView)findViewById(R.id.tvDescription);
		ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
		query.findInBackground(new FindCallback<Item>() {
			
			@Override
			public void done(List<Item> arg0, ParseException e) {
				if(e == null) {
					if(arg0 != null && arg0.size() > 0) {
						Item item0 = arg0.get(0);
						tvDescription.setText(item0.getDescription());
						ParseFile photoFile = item0.getPhotoFile();
						if(photoFile != null) {
							ivPhoto.setParseFile(photoFile);
							ivPhoto.loadInBackground(new GetDataCallback() {
								
								@Override
								public void done(byte[] arg0, ParseException e) {
									if(e == null) {
										
									}else{
										e.printStackTrace();
										Toast.makeText(ReceiverItemDetailsActivity.this, "error fetching photo", Toast.LENGTH_LONG).show();
									}
									
								}
							});
						}
					}
				}else{
					e.printStackTrace();
					Toast.makeText(ReceiverItemDetailsActivity.this, "error finding items", Toast.LENGTH_LONG).show();
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
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
