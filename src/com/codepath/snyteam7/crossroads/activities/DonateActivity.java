package com.codepath.snyteam7.crossroads.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.helper.PhotoScalerHelper;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class DonateActivity extends Activity {
	
	private Uri photoUri;
	private Bitmap photoBitmap;
	ParseFile parsePhotoFile;
	private ImageView ivItemPhoto;
	private EditText etItemDescription;
	private ProgressBar pbPhoto;
	private Spinner spCondition;
	private EditText etPickupDate;
	private EditText etPickupAddress;
	
	public static String APP_TAG = "crossroadssny";
	
	private static final int TAKE_PHOTO_CODE = 1;
	private static final int PICK_PHOTO_CODE = 2;
	private static final int CROP_PHOTO_CODE = 3;
	private static final int POST_PHOTO_CODE = 4;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donate);
		ivItemPhoto = (ImageView)findViewById(R.id.ivItemPicture);
		etItemDescription = (EditText)findViewById(R.id.etItemDescription);
		pbPhoto = (ProgressBar)findViewById(R.id.pbItemDetails);
		spCondition = (Spinner)findViewById(R.id.spCondition);
		etPickupDate = (EditText)findViewById(R.id.etPickupDate);
		//etPickupDate.setInputType( InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_NORMAL);
		etPickupAddress = (EditText)findViewById(R.id.etPickupAddress);
		etPickupAddress.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_POSTAL_ADDRESS | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
	}

	public void onCameraClicked(View v) {
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
	
	public void onMediaClicked(View v) {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, PICK_PHOTO_CODE);
	}
	
	private  File getOutputMediaFile() {
	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), DonateActivity.APP_TAG);
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
				startProgressBar();
				//photoBitmap = BitmapFactory.decodeFile(photoUri.getPath());
				photoBitmap = PhotoScalerHelper.rotateBitmapOrientation(photoUri.getPath());
				if(photoBitmap == null) {
					Toast.makeText(this, "Failed to rotate the image", Toast.LENGTH_LONG).show();
				}else{
					processPhoto();
				}
				
				//startPreviewPhotoActivity();
			} else if (requestCode == PICK_PHOTO_CODE) {
				if(data != null) {
					startProgressBar();
					// Extract the photo that was just picked from the gallery
					photoUri = data.getData();
					try {
						photoBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), photoUri);
						processPhoto();
					} catch (FileNotFoundException e) {
						Toast.makeText(this, "Error retrieving photo: "+e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					} catch (IOException e) {
						Toast.makeText(this, "Error retrieving photo: "+e.getMessage(), Toast.LENGTH_LONG).show();
						e.printStackTrace();
					}
					//startPreviewPhotoActivity();
				}
			} else if (requestCode == CROP_PHOTO_CODE) {
				// cropping can be called like this cropPhoto(photoUri);
				photoBitmap = data.getParcelableExtra("data");
				startPreviewPhotoActivity();
			} else if (requestCode == POST_PHOTO_CODE) {
				startProgressBar();
				photoBitmap = data.getParcelableExtra("processedPhoto");
				processPhoto();
			}
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.donate, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if(id == R.id.action_done) {
			startProgressBar();
			Item donateItem = new Item();
			Date pickupDate = validate();
			if(pickupDate != null) {
			donateItem.setDescription(etItemDescription.getText().toString());
			donateItem.setPhotoFile(parsePhotoFile);
			ParseUser donor = ParseUser.getCurrentUser();
			String donorUsername = donor.getUsername();
			donateItem.setDonor(donor);
			donateItem.put("donorusername", donorUsername);
			donateItem.put("donationdate", new Date());
			donateItem.put("condition", spCondition.getSelectedItem().toString());
			donateItem.put("pickupdate", pickupDate);
			donateItem.put("pickupaddress", etPickupAddress.getText().toString());
			donateItem.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException e) {
					stopProgressBar();
					if(e != null) {
						Toast.makeText(DonateActivity.this, "error saving: " + e.getMessage(), Toast.LENGTH_LONG).show();
					}else{
						Toast.makeText(DonateActivity.this, "item saved!", Toast.LENGTH_LONG).show();
						Intent donorIntent = new Intent(DonateActivity.this, DonorActivity.class);
						startActivity(donorIntent);
					}
				}
			});
			}else{
				stopProgressBar();
				Toast.makeText(this, "Please fill out all fields and one photo of the item.", Toast.LENGTH_LONG).show();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private Date validate() {
		Date result;
		if(etItemDescription.getText() == null || etItemDescription.getText().length() <1) {
			return null;
		}
		if(parsePhotoFile == null || ivItemPhoto.getVisibility() != View.VISIBLE) {
			return null;
		}
		if(etPickupDate.getText() == null || etPickupDate.getText().length() < 1) {
			return null;
		}
		if(etPickupAddress.getText() == null || etPickupAddress.getText().length() < 1) {
			return null;
		}
		
		SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		try {
			result = formatter.parse(etPickupDate.getText().toString());
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			Toast.makeText(this, "Please enter pickup date in format MM/dd/yyyy", Toast.LENGTH_LONG).show();
			return null;
		}

		return result;
	}

	private void startPreviewPhotoActivity() {
		Intent i = new Intent(this, PreviewPhotoActivity.class);
        i.putExtra("photo_bitmap", photoBitmap);
        i.putExtra("photo_uri", photoUri);
        startActivityForResult(i, POST_PHOTO_CODE);
	}
	
	private void cropPhoto(Uri photoUri) {
		//call the standard crop action intent (the user device may not support it)
		Intent cropIntent = new Intent("com.android.camera.action.CROP");
		//indicate image type and Uri
		cropIntent.setDataAndType(photoUri, "image/*");
		//set crop properties
		cropIntent.putExtra("crop", "true");
		//indicate aspect of desired crop
		cropIntent.putExtra("aspectX", 1);
		cropIntent.putExtra("aspectY", 1);
		//indicate output X and Y
		cropIntent.putExtra("outputX", 300);
		cropIntent.putExtra("outputY", 300);
		//retrieve data on return
		cropIntent.putExtra("return-data", true);
		//start the activity - we handle returning in onActivityResult
		startActivityForResult(cropIntent, CROP_PHOTO_CODE);
	}
	
	public void startProgressBar() {
		pbPhoto.setVisibility(View.VISIBLE);
	}
	
	public void stopProgressBar() {
		pbPhoto.setVisibility(View.GONE);
	}
	
	private void processPhoto() {
		int screenWidth = PhotoScalerHelper.getDisplayWidth(this);
		int imageViewWidth = screenWidth - 20;
		photoBitmap = PhotoScalerHelper.scaleToFitWidth(photoBitmap, imageViewWidth);
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		Log.d("DonateActivity", "Starting compression");
		photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
		Log.d("DonateActivity", "Finished compression");
		byte[] photoData = bos.toByteArray();
		String fileName = photoUri.getLastPathSegment();
		parsePhotoFile = new ParseFile(fileName, photoData);
		Log.d("DonateActivity", "Starting save In Background");
		parsePhotoFile.saveInBackground(new SaveCallback() {
			
			@Override
			public void done(ParseException e) {
				stopProgressBar();
				if (e != null) {
					Toast.makeText(DonateActivity.this,
							"Error saving: " + e.getMessage(),
							Toast.LENGTH_LONG).show();
				} else {
					//display in the imageview of the items detail
					ivItemPhoto.setImageBitmap(photoBitmap);
					ivItemPhoto.setVisibility(View.VISIBLE);
				}
			}
		});
	}
	
}
