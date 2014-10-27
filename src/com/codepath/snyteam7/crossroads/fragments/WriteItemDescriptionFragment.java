package com.codepath.snyteam7.crossroads.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.snyteam7.crossroads.R;

public class WriteItemDescriptionFragment extends DialogFragment implements OnClickListener {
	private static final String ARG_PARAM1 = "param1";
	
	private EditText etText;
	private Button btnDescriptionDone;
	private TextView tvCount;

	private OnWroteItemDescriptionListener mListener;

	public static WriteItemDescriptionFragment newInstance() {
		WriteItemDescriptionFragment fragment = new WriteItemDescriptionFragment();
		return fragment;
	}

	public WriteItemDescriptionFragment() {
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
		View view = inflater.inflate(R.layout.fragment_write_item_description, container,
				false);
		
		etText = (EditText)view.findViewById(R.id.etText);	
		btnDescriptionDone = (Button)view.findViewById(R.id.btnDescriptionDone);
		tvCount = (TextView)view.findViewById(R.id.tvCharCount);
		btnDescriptionDone.setOnClickListener(this);
		
		etText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {	
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String text = (s != null) ? s.toString() : null;
				if(text != null && text.length() >0) {
					tvCount.setText(Integer.toString(140-text.length()));
					if(text.length() <= 140) {					  
						btnDescriptionDone.setEnabled(true);
					  tvCount.setTextColor(getResources().getColor(android.R.color.primary_text_light));
					}else{
						btnDescriptionDone.setEnabled(false);
					  tvCount.setTextColor(getResources().getColor(android.R.color.holo_red_light));
					}
				}else{
					tvCount.setText(Integer.toString(140));
					tvCount.setTextColor(getResources().getColor(android.R.color.primary_text_light));
					btnDescriptionDone.setEnabled(false);
				}				
			}
		});
		
		
		return view;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		etText.requestFocus();
		InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		inputMethodManager.showSoftInput(etText, InputMethodManager.SHOW_IMPLICIT);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnWroteItemDescriptionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	public interface OnWroteItemDescriptionListener {
		
		public void onWroteItemDescription(String descriptionText);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.btnDescriptionDone:
			if(validate()) {
				
				if (mListener != null) {
					mListener.onWroteItemDescription(etText.getText().toString());
				}
				etText.setText("");
				dismiss();
			}else{
				etText.setError(getResources().getString(R.string.error_notext));
			}
			
			break;
		}
		
	}
	
	private boolean validate() {
		if(etText.getText() != null && (etText.getText().length() > 0 && etText.getText().length() < 141)) {
			return true;
		}
		return false;
	}


}
