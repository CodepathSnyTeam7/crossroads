package com.codepath.snyteam7.crossroads.adapters;

import com.codepath.snyteam7.crossroads.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

/*public class DonorHomeListAdapter extends ArrayAdapter<DonorListItem> {

	public DonorHomeListAdapter(Context context, int resource) {
		super(context, resource);
		// TODO Auto-generated constructor stub
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		DonorListItem dnrListItem;
		
		dnrListItem = getItem(position);
		
	    //Recycle view code:
		if (convertView == null) {
			convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_donor, parent, false);
		}
		
		
		return super.getView(position, convertView, parent);
	}

}*/


import java.util.Arrays;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.activities.ReceiverItemDetailsActivity;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

public class DonorHomeListAdapter extends ParseQueryAdapter<Item> {

	public DonorHomeListAdapter(Context context) {
		super(context, new ParseQueryAdapter.QueryFactory<Item>() {
			public ParseQuery<Item> create() {
				// Here we can configure a ParseQuery to display
				// items for review.
				ParseQuery query = new ParseQuery("Item");
				//TBD filter to get items not reviewed
				//query.whereContainedIn("rating", Arrays.asList("5", "4"));
				//query.orderByDescending("rating");
				return query;
			}
		});
	}

	@Override
	public View getItemView(Item item, View v, ViewGroup parent) {

		if (v == null) {
			v = View.inflate(getContext(), R.layout.donor_home_list_item, null);
		}

		super.getItemView(item, v, parent);

		ParseImageView itemImage = (ParseImageView) v.findViewById(R.id.ivDonorHomeUserPhoto);
		ParseFile photoFile = item.getPhotoFile();
		if (photoFile != null) {
			itemImage.setParseFile(photoFile);
			itemImage.loadInBackground(new GetDataCallback() {
				@Override
				public void done(byte[] data, ParseException e) {
					// nothing to do
					if (e == null) {

					} else {
						e.printStackTrace();
						Toast.makeText(
								getContext(),
								"error fetching photo for Donor home",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}

		TextView tvDonor = (TextView) v.findViewById(R.id.tvDonorHomeUser);
		// TBD: Get the username
		tvDonor.setText("donor");
		TextView tvItemDescription = (TextView) v.findViewById(R.id.tvDonorHomeUserDesc);
		tvItemDescription.setText(item.getDescription());
		return v;
	}

}
