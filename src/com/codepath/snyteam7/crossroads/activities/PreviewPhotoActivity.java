package com.codepath.snyteam7.crossroads.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.codepath.libraries.androidviewhelpers.SimpleProgressDialog;
import com.codepath.snyteam7.crossroads.ImageFilterProcessor;
import com.codepath.snyteam7.crossroads.R;
import com.parse.ParseFile;

public class PreviewPhotoActivity extends Activity {
	private Bitmap photoBitmap;
	private Bitmap processedBitmap;
	private Uri photoUri;
	private ParseFile photoFile;
	private SimpleProgressDialog dialog;
	private ImageView ivPreview;
	private ImageFilterProcessor filterProcessor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_photo);
		ivPreview = (ImageView) findViewById(R.id.ivPreview);
		photoBitmap = getIntent().getParcelableExtra("photo_bitmap");
		photoUri = getIntent().getParcelableExtra("photo_uri");
		filterProcessor = new ImageFilterProcessor(photoBitmap);
		redisplayPreview(ImageFilterProcessor.NONE);
	}
	
	private void redisplayPreview(int effectId) {
        processedBitmap = filterProcessor.applyFilter(effectId);
        ivPreview.setImageBitmap(processedBitmap);
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.preview_photo, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int itemId = item.getItemId();
		if (itemId == R.id.more || itemId == R.id.action_save)
			return true;
		
		int effectId = 0;
		
		switch (itemId) {
		case R.id.filter_none:
			effectId = ImageFilterProcessor.NONE;
			break;
		case R.id.filter_blur:
			effectId = ImageFilterProcessor.BLUR;
			break;
		case R.id.filter_grayscale:
			effectId = ImageFilterProcessor.GRAYSCALE;
			break;
		case R.id.filter_crystallize:
			effectId = ImageFilterProcessor.CRYSTALLIZE;
			break;
		case R.id.filter_solarize:
			effectId = ImageFilterProcessor.SOLARIZE;
			break;
		case R.id.filter_glow:
			effectId = ImageFilterProcessor.GLOW;
			break;
		default:
			effectId = ImageFilterProcessor.NONE;
			break;
		}
		redisplayPreview(effectId);
		return true;
	}
	
	public void onSaveButton(MenuItem menuItem) {
		Intent i = new Intent();
		i.putExtra("processedPhoto", processedBitmap);
		setResult(RESULT_OK, i);
		
		PreviewPhotoActivity.this.finish();
	}
}
