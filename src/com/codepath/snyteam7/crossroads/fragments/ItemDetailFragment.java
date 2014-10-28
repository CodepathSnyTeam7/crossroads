package com.codepath.snyteam7.crossroads.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ItemDetailFragment extends Fragment {
	
	private ParseImageView ivPhoto;
	private TextView tvDonorName;
	private TextView tvDescription;
	private TextView tvDonateDate;
	private TextView tvItemCondition;
	private TextView tvPickupDate;
	private TextView tvPickupAddress;
	private ProgressBar pbItemDetails;
	
	private String itemObjectId;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		itemObjectId = getArguments().getString("item_objid", null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

		ivPhoto = (ParseImageView) v.findViewById(R.id.ivItemPhoto);
		tvDonorName = (TextView) v.findViewById(R.id.tvDonorName);
		tvDescription = (TextView) v.findViewById(R.id.tvDescription);
		tvDonateDate = (TextView) v.findViewById(R.id.tvDonateDate);
		tvItemCondition = (TextView) v.findViewById(R.id.tvItemCondition);
		tvPickupDate = (TextView) v.findViewById(R.id.tvPickupDate);
		tvPickupAddress = (TextView) v.findViewById(R.id.tvPickupAddress);
		pbItemDetails = (ProgressBar)v.findViewById(R.id.pbItemDetails);
		return v;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		queryPopulateItemViews(itemObjectId);
	}
	
	public static ItemDetailFragment newInstance(String itemObjId) {
		ItemDetailFragment frag = new ItemDetailFragment();
		Bundle args = new Bundle();
		args.putString("item_objid", itemObjId);
		frag.setArguments(args);
		return frag;
	}
	
	
	private void queryPopulateItemViews(String itemObjIdStr) {
		ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
		query.include("donor");
		// First try to find from the cache and only then go to network
		query.setCachePolicy(ParseQuery.CachePolicy.CACHE_ELSE_NETWORK); // or CACHE_ONLY
		// Execute the query to find the object with ID
		startProgressBar();
		query.getInBackground(itemObjIdStr, new GetCallback<Item>() {
		  public void done(Item item0, ParseException e) {
			stopProgressBar();
		    if (e == null) {
		       // item was found 
		    	tvDescription.setText(item0.getDescription());
		    	ParseUser donor = item0.getDonor();
		    	tvDonorName.setText(donor.getUsername());
		    	Date donateDate = item0.getDate("donationdate");
		    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		    	String donateDateStr = formatter.format(donateDate);
		    	tvDonateDate.setText(donateDateStr);
		    	tvItemCondition.setText(item0.getString("condition"));
		    	//Date pickupDate = item0.getDate("pickupdate");
		    	//String pickupDateStr = formatter.format(pickupDate);
		    	//tvPickupDate.setText(pickupDateStr);
		    	tvPickupAddress.setText(item0.getString("pickupaddress"));
		    	
				ParseFile photoFile = item0.getPhotoFile();
				if (photoFile != null) {
					ivPhoto.setParseFile(photoFile);
					ivPhoto.loadInBackground(new GetDataCallback() {

						@Override
						public void done(byte[] arg0, ParseException e) {
							if (e == null) {

							} else {
								e.printStackTrace();
								Toast.makeText(
										getActivity(),
										"error fetching photo",
										Toast.LENGTH_LONG).show();
							}

						}
					});
				}
		    } else {
				e.printStackTrace();
				Toast.makeText(getActivity(),
						"error finding items", Toast.LENGTH_LONG).show();
			}
		  }
		});		
	}
	
	private void startProgressBar() {
		pbItemDetails.setVisibility(View.VISIBLE);
	}
	
	private void stopProgressBar() {
		pbItemDetails.setVisibility(View.GONE);
	}
	
	public String getDonorName() {
		return ((String)tvDonorName.getText());
	}
}
