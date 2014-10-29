package com.codepath.snyteam7.crossroads.fragments;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.LoginFragment.OnLoginSuccessListener;
import com.codepath.snyteam7.crossroads.helper.OnSwipeTouchListener;
import com.codepath.snyteam7.crossroads.helper.TouchImageView;
import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class ItemDetailFragment extends Fragment {
	
	private TouchImageView ivPhoto;
	private TextView tvDonorName;
	private TextView tvDescription;
	private TextView tvDonateDate;
	private TextView tvItemCondition;
	private TextView tvPickupAddress;
	private ProgressBar pbItemDetails;
	private ImageView ivDonorpic;
	
	private String itemObjectId;

    private OnItemDetailsFragmentListener listener;
    
    public interface OnItemDetailsFragmentListener {
    	public void onPhotoSwipeLeft();
    	public void onPhotoSwipeRight();
    }	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		itemObjectId = getArguments().getString("item_objid", null);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.fragment_item_detail, container, false);

		ivPhoto = (TouchImageView) v.findViewById(R.id.ivItemPhoto);
		tvDonorName = (TextView) v.findViewById(R.id.tvDonorName);
		tvDescription = (TextView) v.findViewById(R.id.tvDescription);
		tvDonateDate = (TextView) v.findViewById(R.id.tvDonateDate);
		tvItemCondition = (TextView) v.findViewById(R.id.tvItemCondition);
		tvPickupAddress = (TextView) v.findViewById(R.id.tvPickupAddress);
		pbItemDetails = (ProgressBar)v.findViewById(R.id.pbItemDetails);
		ivPhoto.setOnTouchListener(new OnSwipeTouchListener(getActivity()){
			@Override
			public void onSwipeRight() {
				listener.onPhotoSwipeRight();
			}
			
			@Override
			public void onSwipeLeft() {
				listener.onPhotoSwipeLeft();
			}
		});
		ivDonorpic = (ImageView)v.findViewById(R.id.ivDonorPic);

		return v;
	}
	
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		queryPopulateItemViews(itemObjectId);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		listener = (OnItemDetailsFragmentListener)activity;
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
	    		if (donor.getUsername().equalsIgnoreCase("v1")) {
	    			ivDonorpic.setImageDrawable(getResources().getDrawable(R.drawable.cr_d3));
	    		}
	    		if (donor.getUsername().equalsIgnoreCase("jean")) {
	    			ivDonorpic.setImageDrawable(getResources().getDrawable(R.drawable.cr_d4));
	    		}
		    	Date donateDate = item0.getDate("donationdate");
		    	SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
		    	String donateDateStr = formatter.format(donateDate);
		    	tvDonateDate.setText(donateDateStr);
		    	String itemCondition = getResources().getString(R.string.condition_label) + " ";
		    	tvItemCondition.setText(itemCondition + item0.getString("condition"));
		    	String dropOff = getResources().getString(R.string.pickup_address_label) + " ";
		    	tvPickupAddress.setText(dropOff + item0.getString("pickupaddress"));
		    	
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
