package com.codepath.snyteam7.crossroads.adapters;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

public class ReviewerHomeListAdapter extends ParseQueryAdapter<Item>  {
	

	public ReviewerHomeListAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Item>() {
			public ParseQuery<Item> create() {
				// Here we can configure a ParseQuery to display
				// items for review.
				ParseQuery query = new ParseQuery("Item");
				//query.whereContainedIn("rating", Arrays.asList("5", "4"));
				//query.orderByDescending("rating");
				// filter to get items not reviewed
				query.whereDoesNotExist("accepteddate");
				query.whereDoesNotExist("rejecteddate");
				query.orderByDescending("updatedAt");
				return query;
			}
		});

	}

	@Override
	public View getItemView(Item item, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.reviewer_home_listitem, null);
			holder = new ViewHolder();
			holder.vh_itemImage = (ParseImageView) convertView.findViewById(R.id.ivRHomeUserPhoto);
			holder.vh_tvDonor = (TextView) convertView.findViewById(R.id.tvRHomeUser);
			holder.vh_tvDonateDate = (TextView) convertView.findViewById(R.id.tvRHomeUserDonateDate);
			holder.vh_tvItemDescription = (TextView) convertView.findViewById(R.id.tvRHomeUserDesc);
			holder.vh_profImage = (ImageView) convertView.findViewById(R.id.ivRHomeUserProfileImg);
			convertView.setTag(holder);
		} else {
	    	holder = (ViewHolder) convertView.getTag();
	    }

		super.getItemView(item, convertView, parent);

		ParseFile photoFile = item.getPhotoFile();
		if (photoFile != null) {
			holder.vh_itemImage.setParseFile(photoFile);
			holder.vh_itemImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
					if (e == null) {

					} else {
						e.printStackTrace();
						Toast.makeText(
								getContext(),
								"error fetching photo for Reviewer home",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}

		// Get the username
    	ParseUser donor = item.getDonor();
    	if (donor != null) {
    		holder.vh_tvDonor.setText(item.getString("donorusername"));
    		if (item.getString("donorusername").equalsIgnoreCase("v1")) {
    			holder.vh_profImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cr_d3));
    		}
    		if (item.getString("donorusername").equalsIgnoreCase("jean")) {
    			holder.vh_profImage.setImageDrawable(getContext().getResources().getDrawable(R.drawable.cr_d4));
    		}
    	} else {
    		holder.vh_tvDonor.setText("Donor");
    	}
		
		// Get the donation date
		Date donateDate = item.getDate("donationdate");
		if (donateDate != null) {
	    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
	    	String donateDateStr = formatter.format(donateDate);
	    	holder.vh_tvDonateDate.setText(donateDateStr);
		} else {
			holder.vh_tvDonateDate.setText("Date");
		}
    	
		// Item description
		holder.vh_tvItemDescription.setText(item.getDescription());
		return convertView;
	}
	
	static class ViewHolder {
		ParseImageView vh_itemImage;
		TextView vh_tvDonor;
		TextView vh_tvDonateDate;
		TextView vh_tvItemDescription;
		ImageView vh_profImage;
	}
}

