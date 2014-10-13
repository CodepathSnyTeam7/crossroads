package com.codepath.snyteam7.crossroads.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Point;
import android.text.Html;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.snyteam7.crossroads.R;


public class ReviewerHomeListAdapter extends ArrayAdapter<ReviewListItem> {
	
	public ReviewerHomeListAdapter(Context context, List<ReviewListItem> photos) {
		super(context, R.layout.reviewer_home_listitem, photos);
	}

	// Takes a data item in the position and converts to row in listview
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the item from the position
		ReviewListItem photo = getItem(position);
		CharSequence postdate;
		
		// check if view is recycled
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.reviewer_home_listitem, parent, false);
		}
		
		// lookup the subviews
		TextView imgDesciption = (TextView) convertView.findViewById(R.id.tvRHomeUserDesc);
		ImageView imgPhoto = (ImageView) convertView.findViewById(R.id.ivRHomeUserPhoto);
		TextView imgUser = (TextView) convertView.findViewById(R.id.tvRHomeUser);
		TextView imgTime = (TextView) convertView.findViewById(R.id.tvRHomeUserTime);
// TBD		CircularImageView imgUserPic = (CircularImageView) convertView.findViewById(R.id.ivRHomeUserImg);
	
		// Set the values
		// Get the username
		imgUser.setText(photo.username);
		
		// Get the create time
		//postdate = DateUtils.getRelativeTimeSpanString(photo.createtime * 1000, System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS, DateUtils.FORMAT_ABBREV_ALL);
		postdate = DateUtils.getRelativeTimeSpanString(photo.createtime * 1000, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
		//postdate = DateUtils.getRelativeTimeSpanString(getContext(), photo.createtime * 1000, false);
		imgTime.setText(postdate);
		//imgTime.setBackgroundColor(0xFF00FF00);
		
		// Get the caption
		if (photo.description == null) {
			imgDesciption.setVisibility(convertView.GONE);
		} else {
			imgDesciption.setText(Html.fromHtml("<font color=\"#206199\"><b>" + photo.username
                    + "  " + "</b></font>" + "<font color=\"#000000\">" + photo.description + "</font>"));
		}
		
		// Load profile picture
		if (photo.profimgurl != null) {
// TBD			Picasso.with(getContext()).load(photo.profimgurl).placeholder(R.drawable.default_avatar).into(imgUserPic);
		}
		
		// Instagram photo to fit Screen size
		WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int screenwidth = size.x;
		int screenheight = size.y;
		Log.d("DEBUG", "Instagram photo H:" + photo.imgheight + " W:" + photo.imgWidth + 
				"Screeen H:" + screenheight  + " W:" + screenwidth);

		/* Set the imageview layout params to the screen size width and aspect ratio */
		float fittedimgheight;
		float fittedimgwidth;
		fittedimgwidth = (float) screenwidth;
		fittedimgheight = fittedimgwidth * ((float) photo.imgheight / (float) photo.imgWidth);
		
		// Set the layout params to the newly calculated dimensions
		ViewGroup.LayoutParams iv_lparams = imgPhoto.getLayoutParams();
		iv_lparams.height = (int) fittedimgheight;
		iv_lparams.width = (int) fittedimgwidth;
		imgPhoto.setLayoutParams(iv_lparams);
		
		// cleanup subview if recycled to clear the previous image content
		imgPhoto.getLayoutParams().height = (int) fittedimgheight;
		imgPhoto.setImageResource(0);

		// fetch the photo from the url using Picassa asynchronously in background not in main thread
		// It downloads the imagebytes, converts to bitmap and loads the image
		//imgPhoto.setBackgroundColor(0xFF00FF00);
// TBD		Picasso.with(getContext()).load(photo.imgurl).fit().centerInside().into(imgPhoto);
		
		return convertView;
	}
}
