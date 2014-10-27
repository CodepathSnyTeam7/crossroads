package com.codepath.snyteam7.crossroads.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;

public class PickPhotoSourceFragment extends DialogFragment {

		private Button btPickCamera;
		private Button btPickMedia;
		
	    private OnPhotoSourcePickedListener listener;
	    
	    public static String SOURCE_TYPE_CAMERA = "camera";
	    public static String SOURCE_TYPE_MEDIA = "media";
	    
	    public interface OnPhotoSourcePickedListener {
	    	public void onPhotoSourcePicked(String type);
	    }
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
		}
		
		@Override
		public void onActivityCreated(Bundle arg0) {
			super.onActivityCreated(arg0);
			int width = getResources().getDimensionPixelSize(R.dimen.popup_photosource_width);
			int height = getResources().getDimensionPixelSize(R.dimen.popup_photosource_height);
			getDialog().getWindow().setLayout(width, height);
		}
		
		@Override
		public View onCreateView(LayoutInflater inflater,
				@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.fragment_pick_photo_source, container, false);
			btPickCamera = (Button)view.findViewById(R.id.btPickCamera);
			btPickMedia = (Button)view.findViewById(R.id.btPickMedia);
			
			btPickCamera.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					onCameraClicked(v);					
				}
			});
			btPickMedia.setOnClickListener(new OnClickListener() {				
				@Override
				public void onClick(View v) {
					onMediaClicked(v);					
				}
			});
  	
			return view;
		}
		
		public void onCameraClicked(View v) {
			listener.onPhotoSourcePicked(SOURCE_TYPE_CAMERA);
			dismiss();
		}
		
		public void onMediaClicked(View v) {
			listener.onPhotoSourcePicked(SOURCE_TYPE_MEDIA);
			dismiss();
		}
		
		@Override
		public void onAttach(Activity activity) {
		    super.onAttach(activity);
		    if(activity instanceof OnPhotoSourcePickedListener) {
		    	listener = (OnPhotoSourcePickedListener)activity;
		    }else{
		    	throw new ClassCastException("Activity must implement OnPhotoSourcePickedListener");
		    }
		}		
		
		public static PickPhotoSourceFragment getInstance() {
			PickPhotoSourceFragment frag = new PickPhotoSourceFragment();
			return frag;
		}

}
