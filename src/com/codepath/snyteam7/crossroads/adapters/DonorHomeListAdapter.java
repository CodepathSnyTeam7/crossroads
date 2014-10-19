package com.codepath.snyteam7.crossroads.adapters;

import java.util.Date;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQueryAdapter;

public class DonorHomeListAdapter extends ParseQueryAdapter<Item> {

	public DonorHomeListAdapter(Context context, ParseQueryAdapter.QueryFactory<Item> queryFactory) {
		super(context, queryFactory);
	}

	@Override
	public View getItemView(Item item, View v, ViewGroup parent) {
		v = View.inflate(getContext(), R.layout.donor_home_list_item, null);
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

		//TextView tvDonor = (TextView) v.findViewById(R.id.tvDonorHomeUser);
		// TBD: Get the username
		//tvDonor.setText("donor");
		TextView tvDonorHomeUserTime = (TextView) v.findViewById(R.id.tvDonorHomeUserTime);
		DateFormat df = new DateFormat();
		tvDonorHomeUserTime.setText(df.format("MM/dd/yyyy", item.getCreationDate()));
		TextView tvItemDescription = (TextView) v.findViewById(R.id.tvDonorHomeUserDesc);
		tvItemDescription.setText(item.getDescription());
		
		Date acceptedDate = item.getDate("accepteddate");
		Date rejectedDate = item.getDate("rejecteddate");
		TextView tvDonorHomeItemStatus = (TextView) v.findViewById(R.id.tvDonorHomeItemStatus);
		if(acceptedDate != null) {
			tvDonorHomeItemStatus.setText(getContext().getResources().getString(R.string.accepted));
			tvDonorHomeItemStatus.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
		}else if(rejectedDate != null) {
			tvDonorHomeItemStatus.setText(getContext().getResources().getString(R.string.rejected));
			tvDonorHomeItemStatus.setTextColor(getContext().getResources().getColor(android.R.color.holo_orange_dark));
		}else{
			tvDonorHomeItemStatus.setText("");
		}
		return v;
	}

}
