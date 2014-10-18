package com.codepath.snyteam7.crossroads.helper;

import java.io.IOException;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class PhotoScalerHelper {

	// scale and keep aspect ratio
	public static Bitmap scaleToFitWidth(Bitmap b, int width) {
		float factor = width / (float) b.getWidth();
		return Bitmap.createScaledBitmap(b, width,
				(int) (b.getHeight() * factor), true);
	}

	// scale and keep aspect ratio
	public static Bitmap scaleToFitHeight(Bitmap b, int height) {
		float factor = height / (float) b.getHeight();
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor),
				height, true);
	}

	// scale and keep aspect ratio
	public static Bitmap scaleToFill(Bitmap b, int width, int height) {
		float factorH = height / (float) b.getWidth();
		float factorW = width / (float) b.getWidth();
		float factorToUse = (factorH > factorW) ? factorW : factorH;
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
				(int) (b.getHeight() * factorToUse), true);
	}

	// scale and don't keep aspect ratio
	public static Bitmap strechToFill(Bitmap b, int width, int height) {
		float factorH = height / (float) b.getHeight();
		float factorW = width / (float) b.getWidth();
		return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorW),
				(int) (b.getHeight() * factorH), true);
	}

	public static int getDisplayWidth(Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return displayMetrics.widthPixels;
	}

	// display height in pixels
	public static int getDisplayHeight(Context context) {
		DisplayMetrics displayMetrics = context.getResources()
				.getDisplayMetrics();
		return displayMetrics.heightPixels;
	}

	// convertDpToPixel(25f, context) => (25dp converted to pixels)
	public static float convertDpToPixel(float dp, Context context) {
		Resources r = context.getResources();
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
				r.getDisplayMetrics());
	}

	// convertPixelsToDp(25f, context) => (25px converted to dp)
	public static float convertPixelsToDp(float px, Context context) {
		Resources r = context.getResources();
		DisplayMetrics metrics = r.getDisplayMetrics();
		float dp = px / (metrics.densityDpi / 160f);
		return dp;
	}
	
	public static Bitmap rotateBitmapOrientation(String file) {
	    // Create and configure BitmapFactory
	    BitmapFactory.Options bounds = new BitmapFactory.Options();
	    bounds.inJustDecodeBounds = true;
	    BitmapFactory.decodeFile(file, bounds);
	    BitmapFactory.Options opts = new BitmapFactory.Options();
	    Bitmap bm = BitmapFactory.decodeFile(file, opts);
	    // Read EXIF Data
	    ExifInterface exif;
		try {
			exif = new ExifInterface(file);
		    String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
		    int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
		    int rotationAngle = 0;
		    if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
		    if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
		    if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
		    // Rotate Bitmap
		    Matrix matrix = new Matrix();
		    matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
		    Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0, bounds.outWidth, bounds.outHeight, matrix, true);
		    // Return result
		    return rotatedBitmap;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}
}
