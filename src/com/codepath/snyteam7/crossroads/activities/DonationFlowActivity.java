package com.codepath.snyteam7.crossroads.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.PickItemConditionFragment;
import com.codepath.snyteam7.crossroads.fragments.PickItemConditionFragment.OnPickedItemConditionListener;
import com.codepath.snyteam7.crossroads.fragments.PickPhotoSourceFragment;
import com.codepath.snyteam7.crossroads.fragments.PickPhotoSourceFragment.OnPhotoSourcePickedListener;
import com.codepath.snyteam7.crossroads.fragments.WriteItemDescriptionFragment;
import com.codepath.snyteam7.crossroads.fragments.WriteItemDescriptionFragment.OnWroteItemDescriptionListener;
import com.codepath.snyteam7.crossroads.helper.PhotoScalerHelper;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class DonationFlowActivity extends FragmentActivity 
	implements OnPhotoSourcePickedListener, OnWroteItemDescriptionListener, OnPickedItemConditionListener {
 
	private Uri photoUri;
	private Bitmap photoBitmap;
	ParseFile parsePhotoFile;
	
	private ProgressBar pbPhoto;
	private ImageView ivDonatePhoto;
	private CheckBox cbDonatePhoto;
	
	private TextView tvItemDescription;
	private CheckBox cbDescription;
	
	private TextView tvPlanDelivery;
	private CheckBox cbDelivery;
	
	private TextView tvPickCondition;
	private CheckBox cbCondition;
	
	private Button btSend;
	
	private static final int TAKE_PHOTO_CODE = 1;
	private static final int PICK_PHOTO_CODE = 2;
	private static final int DROPOFF_LOCATION_CODE = 3;
	
	public static String APP_TAG = "crossroadssny";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donation_flow);
		pbPhoto = (ProgressBar)findViewById(R.id.pbItemPhoto);
		ivDonatePhoto = (ImageView)findViewById(R.id.ivDonatePhoto);
		cbDonatePhoto = (CheckBox)findViewById(R.id.cbDonatePhoto);
		tvItemDescription = (TextView)findViewById(R.id.tvItemDescription);
		cbDescription = (CheckBox)findViewById(R.id.cbDescription);
		tvPlanDelivery = (TextView)findViewById(R.id.tvPlanDelivery);
		cbDelivery = (CheckBox)findViewById(R.id.cbDelivery);
		tvPickCondition = (TextView)findViewById(R.id.tvPickCondition);
		cbCondition = (CheckBox)findViewById(R.id.cbCondition);
		btSend = (Button)findViewById(R.id.btSend);
	}
	
	public void onDonatePhotoClicked(View v) {
		showPickPhotoSourceDialog();
	}
	
	public void onItemDescriptionClicked(View v) {
		showWriteDescriptionDialog();
	}
	
	public void onPlanDeliveryClicked(View v) {
		Intent intent = new Intent(this, MapActivity.class);
		startActivityForResult(intent, DROPOFF_LOCATION_CODE);		
	}
	
	public void onPickConditionClicked(View v) {
		showPickConditionDialog();
	}
	
	public void onSendDonationClicked(View v) {
		saveDonation();
	}

	@Override
	public void onPhotoSourcePicked(String type) {
		if(PickPhotoSourceFragment.SOURCE_TYPE_CAMERA.equalsIgnoreCase(type)) {
			onCameraClicked();
		}else if(PickPhotoSourceFragment.SOURCE_TYPE_MEDIA.equalsIgnoreCase(type)) {
			onMediaClicked();
		}
	}
	public void onCameraClicked() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		File outputFile = getOutputMediaFile();
		if(outputFile != null) {
		photoUri = Uri.fromFile(outputFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(intent, TAKE_PHOTO_CODE);	
		}else{
			Toast.makeText(this, "problem creating media storage", Toast.LENGTH_LONG).show();
		}
	}
	
	public void onMediaClicked() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, PICK_PHOTO_CODE);
	}
	
	private  File getOutputMediaFile() {
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), DonationFlowActivity.APP_TAG);
	    if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
	    	Toast.makeText(this, "failed to open media storage", Toast.LENGTH_LONG).show();
	        return null;
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
	    File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
		        "IMG_"+ timeStamp + ".jpg");

	    return mediaFile;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == TAKE_PHOTO_CODE) {
				new ProcessPhotoAsyncTask().execute(PickPhotoSourceFragment.SOURCE_TYPE_CAMERA);
			} else if (requestCode == PICK_PHOTO_CODE) {
				if(data != null) {
					// Extract the photo that was just picked from the gallery
					photoUri = data.getData();
					try {
						photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
						new ProcessPhotoAsyncTask().execute(PickPhotoSourceFragment.SOURCE_TYPE_MEDIA);
					} catch (FileNotFoundException e) {
						Toast.makeText(this, "Error retrieving photo: "+e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} catch (IOException e) {
						Toast.makeText(this, "Error retrieving photo: "+e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
				}
			}else if(requestCode == DROPOFF_LOCATION_CODE) {
				if(data.getExtras() != null) {
					double latitude = data.getExtras().getDouble("latitude");
					double longitude = data.getExtras().getDouble("longitude");
					//Toast.makeText(this, "Main Activity: lat = "+ latitude + ";Long = " + longitude, Toast.LENGTH_LONG).show();
					String address = data.getExtras().getString("Address");
					//Toast.makeText(this, "Main Activity: Address = " + address, Toast.LENGTH_LONG).show();
					tvPlanDelivery.setText(address);
					cbDelivery.setChecked(true);
					if(isFlowFinished()) {
						showSendButton();
					}
				}
			}
		}
	}

	public class ProcessPhotoAsyncTask extends AsyncTask<String, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			startProgressBar();
		}
		
		@Override
		protected Void doInBackground(String... params) {
			try {
				if(PickPhotoSourceFragment.SOURCE_TYPE_CAMERA.equalsIgnoreCase(params[0])) {
					if(photoUri != null) {
						photoBitmap = PhotoScalerHelper.rotateBitmapOrientation(photoUri.getPath());
					}
				}
				if(photoBitmap != null) {
					processPhoto();
				}
			}catch(Exception e) {
				Toast.makeText(DonationFlowActivity.this, "error while processing photo", Toast.LENGTH_SHORT).show();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			if(parsePhotoFile != null) {
			parsePhotoFile.saveInBackground(new SaveCallback() {
				
				@SuppressLint("NewApi") @Override
				public void done(ParseException e) {
					if (e != null) {
						Toast.makeText(DonationFlowActivity.this,
								"Error saving: " + e.getMessage(),
								Toast.LENGTH_LONG).show();
					} else {
						//display in the imageview of the items detail
						ivDonatePhoto.setImageBitmap(photoBitmap);
						cbDonatePhoto.setChecked(true);
						if(isFlowFinished()) {
							showSendButton();
						}
					}
					stopProgressBar();
				}
			});
			}else{
				Toast.makeText(DonationFlowActivity.this, "Error retrieving photo bitmap",Toast.LENGTH_LONG).show();
			}
		}
		
	}

	private void processPhoto() {
		int screenWidth = PhotoScalerHelper.getDisplayWidth(this);
		int imageViewWidth = screenWidth - 20;
		photoBitmap = PhotoScalerHelper.scaleToFitWidth(photoBitmap, imageViewWidth);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		byte[] photoData = bos.toByteArray();
		String fileName = photoUri.getLastPathSegment();
		parsePhotoFile = new ParseFile(fileName, photoData);
	}
	
	private void showPickPhotoSourceDialog() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if(prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		DialogFragment frag = PickPhotoSourceFragment.getInstance();
		frag.show(ft, "dialog");
	}
	
	private void showWriteDescriptionDialog() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if(prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		DialogFragment frag = WriteItemDescriptionFragment.newInstance();
		frag.show(ft, "dialog");
	}

	private void showPickConditionDialog() {
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
		if(prev != null) {
			ft.remove(prev);
		}
		ft.addToBackStack(null);

		DialogFragment frag = PickItemConditionFragment.newInstance();
		frag.show(ft, "dialog");
	}	
	
	public void startProgressBar() {
		pbPhoto.setVisibility(View.VISIBLE);
	}
	
	public void stopProgressBar() {
		pbPhoto.setVisibility(View.GONE);
	}

	@SuppressLint("NewApi") @Override
	public void onWroteItemDescription(String descriptionText) {
		if(descriptionText != null && descriptionText.length() > 0) {
			//tvItemDescription.setTextSize(15);
			tvItemDescription.setText(descriptionText);
			cbDescription.setChecked(true);
			if(isFlowFinished()) {
				showSendButton();
			}
		}
		
	}

	@Override
	public void onPickedItemCondition(String condition) {
		if(condition != null) {
			//tvPickCondition.setTextSize(15);
			tvPickCondition.setText(condition);
			cbCondition.setChecked(true);
			if(isFlowFinished()) {
				showSendButton();
			}
		}
		
	}
	
	private void saveDonation() {
		startProgressBar();
		Item donateItem = new Item();

		donateItem.setDescription(tvItemDescription.getText().toString());
		donateItem.setPhotoFile(parsePhotoFile);
		ParseUser donor = ParseUser.getCurrentUser();
		String donorUsername = donor.getUsername();
		donateItem.setDonor(donor);
		donateItem.put("donorusername", donorUsername);
		donateItem.put("donationdate", new Date());
		donateItem.put("condition", tvPickCondition.getText().toString());
		donateItem.put("pickupaddress", tvPlanDelivery.getText().toString());
		donateItem.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				stopProgressBar();
				if(e != null) {
					Toast.makeText(DonationFlowActivity.this, "error saving: " + e.getMessage(), Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(DonationFlowActivity.this, "item saved!", Toast.LENGTH_LONG).show();
					Intent donorIntent = new Intent(DonationFlowActivity.this, DonorActivity.class);
					startActivity(donorIntent);
				}
			}
		});
	}
	
	private boolean isFlowFinished() {
		return (cbDonatePhoto.isChecked() && cbDescription.isChecked() && cbDelivery.isChecked() && cbCondition.isChecked());
	}
	
	private void showSendButton() {
		btSend.setVisibility(View.VISIBLE);
	}
	
}
