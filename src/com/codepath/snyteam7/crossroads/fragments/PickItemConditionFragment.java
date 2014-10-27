package com.codepath.snyteam7.crossroads.fragments;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.fragments.WriteItemDescriptionFragment.OnWroteItemDescriptionListener;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class PickItemConditionFragment extends DialogFragment implements OnClickListener {
	private static final String ARG_PARAM1 = "param1";
	private Button btNew;
	private Button btLightlyUsed;
	private Button btHeavilyUsed;
	private Button btBroken;

	private OnPickedItemConditionListener mListener;

	public static PickItemConditionFragment newInstance() {
		PickItemConditionFragment fragment = new PickItemConditionFragment();
		return fragment;
	}

	public PickItemConditionFragment() {
		// Required empty public constructor
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = super.onCreateDialog(savedInstanceState);
		dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		WindowManager.LayoutParams wLayoutParams = dialog.getWindow().getAttributes();
		wLayoutParams.gravity = Gravity.CENTER;
		wLayoutParams.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
		wLayoutParams.width = LayoutParams.MATCH_PARENT;
		dialog.getWindow().setAttributes(wLayoutParams);
		return dialog;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_pick_item_condition, container,
				false);
			
		btNew = (Button)view.findViewById(R.id.btNew);		
		btNew.setOnClickListener(this);	
		btLightlyUsed = (Button)view.findViewById(R.id.btLightlyUsed);		
		btLightlyUsed.setOnClickListener(this);
		btHeavilyUsed = (Button)view.findViewById(R.id.btHeavilyUsed);		
		btHeavilyUsed.setOnClickListener(this);
		btBroken = (Button)view.findViewById(R.id.btBroken);		
		btBroken.setOnClickListener(this);		
		return view;
	}
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnPickedItemConditionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnPickedItemConditionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnPickedItemConditionListener {
		
		public void onPickedItemCondition(String condition);
	}

	@Override
	public void onClick(View v) {
		mListener.onPickedItemCondition(((TextView)v).getText().toString());
		dismiss();
		
	}

}
