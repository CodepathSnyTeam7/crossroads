package com.codepath.snyteam7.crossroads.adapters;

import com.codepath.snyteam7.crossroads.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class DonorHomeListAdapter extends ArrayAdapter<DonorListItem> {

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

}
