package com.codepath.snyteam7.crossroads.adapters;

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

public class ReviewerHomeListAdapter extends ParseQueryAdapter<Item> {

	public ReviewerHomeListAdapter(Context context) {
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
			v = View.inflate(getContext(), R.layout.reviewer_home_listitem, null);
		}

		super.getItemView(item, v, parent);

		ParseImageView itemImage = (ParseImageView) v.findViewById(R.id.ivRHomeUserPhoto);
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
								"error fetching photo for Reviewer home",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}

		TextView tvDonor = (TextView) v.findViewById(R.id.tvRHomeUser);
		// TBD: Get the username
		tvDonor.setText("donor");
		TextView tvItemDescription = (TextView) v.findViewById(R.id.tvRHomeUserDesc);
		tvItemDescription.setText(item.getDescription());
		return v;
	}

}

