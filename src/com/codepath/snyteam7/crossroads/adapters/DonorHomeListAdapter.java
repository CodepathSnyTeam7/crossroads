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
	public View getItemView(Item item, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(getContext(), R.layout.donor_home_list_item, null);
			holder = new ViewHolder();
			holder.vh_itemImage = (ParseImageView) convertView.findViewById(R.id.ivDonorHomeUserPhoto);
			holder.vh_tvDonorHomeUserTime = (TextView) convertView.findViewById(R.id.tvDonorHomeUserTime);
			holder.vh_tvDonorHomeItemStatus = (TextView) convertView.findViewById(R.id.tvDonorHomeItemStatus);
			holder.vh_tvItemDescription = (TextView) convertView.findViewById(R.id.tvDonorHomeUserDesc);
			convertView.setTag(holder);
		} else {
	    	holder = (ViewHolder) convertView.getTag();
	    }
			

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
								"error fetching photo for Donor home",
								Toast.LENGTH_LONG).show();
					}
				}
			});
		}

		//TextView tvDonor = (TextView) v.findViewById(R.id.tvDonorHomeUser);
		// TBD: Get the username
		//tvDonor.setText("donor");
		
		DateFormat df = new DateFormat();
		holder.vh_tvDonorHomeUserTime.setText(df.format("MM/dd/yyyy", item.getCreationDate()));
		holder.vh_tvItemDescription.setText(item.getDescription());
		
		Date acceptedDate = item.getDate("accepteddate");
		Date rejectedDate = item.getDate("rejecteddate");
		
		if(acceptedDate != null) {
			holder.vh_tvDonorHomeItemStatus.setText(getContext().getResources().getString(R.string.accepted));
			holder.vh_tvDonorHomeItemStatus.setTextColor(getContext().getResources().getColor(android.R.color.holo_blue_dark));
		}else if(rejectedDate != null) {
			holder.vh_tvDonorHomeItemStatus.setText(getContext().getResources().getString(R.string.rejected));
			holder.vh_tvDonorHomeItemStatus.setTextColor(getContext().getResources().getColor(android.R.color.holo_orange_dark));
		}else{
			holder.vh_tvDonorHomeItemStatus.setText(getContext().getResources().getString(R.string.pending));
		}
		return convertView;
	}

	static class ViewHolder {
		ParseImageView vh_itemImage;
		TextView vh_tvItemDescription;
		TextView vh_tvDonorHomeUserTime;
		TextView vh_tvDonorHomeItemStatus;
	}
}
