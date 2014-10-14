package com.codepath.snyteam7.crossroads.adapters;

import java.text.SimpleDateFormat;
import java.util.List;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.model.Item;


public class ReviewerHomeListAdapter extends ArrayAdapter<Item> {
	
	public ReviewerHomeListAdapter(Context context, List<Item> photos) {
		super(context, R.layout.reviewer_home_listitem, photos);
	}

	// Takes a data item in the position and converts to row in listview
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// get the item from the position
		Item item = getItem(position);
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
		// TBD: Get the username
		imgUser.setText("dummy");
		
		// Get the create time
		
		//postdate = DateUtils.getRelativeTimeSpanString(item.getCreatedAt(), System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		postdate= df.format(item.getCreatedAt());
		imgTime.setText(postdate);
		//imgTime.setBackgroundColor(0xFF00FF00);
		
		// Get the caption
		if (item.getDescription() == null) {
			imgDesciption.setVisibility(convertView.GONE);
		} else {
			imgDesciption.setText(Html.fromHtml("<font color=\"#206199\"><b>" + "dummy user"
                    + "  " + "</b></font>" + "<font color=\"#000000\">" + item.getDescription() + "</font>"));
		}
		
		// Load profile picture
		/* TBD
		if (item.profimgurl != null) {
			Picasso.with(getContext()).load(item.profimgurl).placeholder(R.drawable.default_avatar).into(imgUserPic);
		}
		*/
		
		imgPhoto.setImageResource(0);

		// fetch the photo from the url using Picassa asynchronously in background not in main thread
		// It downloads the imagebytes, converts to bitmap and loads the image
		//imgPhoto.setBackgroundColor(0xFF00FF00);
		//Picasso.with(getContext()).load(item.imgurl).fit().centerInside().into(imgPhoto);
		
		return convertView;
	}
}
