package com.codepath.snyteam7.crossroads.activities;

import java.io.File;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;

public class DonateActivity extends Activity {
	
	private Uri photoUri;
	private Bitmap photoBitmap;
	private ImageView ivItemPhoto;
	
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
				// Call the method below to trigger the cropping
				cropPhoto(photoUri);
			} else if (requestCode == PICK_PHOTO_CODE) {
				if(data != null) {
					// Extract the photo that was just picked from the gallery
					photoUri = data.getData();
					//Call the method below to trigger the cropping
					cropPhoto(photoUri);
				}
			} else if (requestCode == CROP_PHOTO_CODE) {
				photoBitmap = data.getParcelableExtra("data");
				startPreviewPhotoActivity();
			} else if (requestCode == POST_PHOTO_CODE) {
				//display in the imageview of the items detail
				photoBitmap = data.getParcelableExtra("processedPhoto");
				ivItemPhoto.setImageBitmap(photoBitmap);
				ivItemPhoto.setVisibility(View.VISIBLE);
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
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
	
	private void startPreviewPhotoActivity() {
		Intent i = new Intent(this, PreviewPhotoActivity.class);
        i.putExtra("photo_bitmap", photoBitmap);
        startActivityForResult(i, POST_PHOTO_CODE);
	}
}
