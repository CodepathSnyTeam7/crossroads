package com.codepath.snyteam7.crossroads.activities;

import java.io.IOException;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.Toast;

import com.codepath.snyteam7.crossroads.R;
import com.codepath.snyteam7.crossroads.model.MapData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, OnMapLongClickListener {

	private SupportMapFragment mapFragment;
	private GoogleMap map;
	Geocoder selected_place_geocoder;
	//List<Address> address_list;
	
	private LocationClient mLocationClient;
	//private List<LatLng>;
//	private LatLng listingPosition;
	/*
	 * Define a request code to send to Google Play services This code is
	 * returned in Activity.onActivityResult
	 */
	private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
	
		mLocationClient = new LocationClient(this, this, this);
		mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map));
		//Marker mapMarker = mapFragment.addMarker(new MarkerOptions().position(listingPosition).title(listing.name).snippet("Open at: " + listing.hoursOpen).icon(defaultMarker));
		if (mapFragment != null) {
			map = mapFragment.getMap();
			if (map != null) {
				//Toast.makeText(this, "Map Fragment was loaded properly!", Toast.LENGTH_SHORT).show();
				map.setMyLocationEnabled(true);
				map.setOnMapLongClickListener(this);

				map.setInfoWindowAdapter(new CustomWindowAdapter(getLayoutInflater()));
				
				setDonationCentres();
				
				/*LatLng Donation_centre_1 = new LatLng(37.4145, -121.905);
				LatLng Donation_centre_2 = new LatLng(37.418813, -122.024566);
				LatLng Donation_centre_3 = new LatLng(37.418000, -122.024668);
				LatLng Donation_centre_4 = new LatLng(37.410965, -122.027163);
				LatLng Donation_centre_5 = new LatLng(37.411954, -122.017893);
	              Marker marker = map.addMarker(new MarkerOptions()
	                .position(Donation_centre_1)
	                .title("Home")
	                .snippet("155 evening star ct, Miliptas, CA")       
	                .icon(defaultMarker));
	              Marker marker1 = map.addMarker(new MarkerOptions()
	                .position(Donation_centre_2)
	                .title("Yahoo Building F")
	                .snippet("155 evening star ct, Miliptas, CA")       
	                .icon(defaultMarker));
	              Marker marker2 = map.addMarker(new MarkerOptions()
	                .position(Donation_centre_5)
	                .title("Home")
	                .snippet("155 evening star ct, Miliptas, CA")       
	                .icon(defaultMarker));
	              dropPinEffect(marker);
	              dropPinEffect(marker1);
	              dropPinEffect(marker2);*/
	              map.setOnInfoWindowClickListener(getInfoWindowClickListener());
	              selected_place_geocoder = new Geocoder(this);
			} else {
				Toast.makeText(this, "Error - Map was null!!", Toast.LENGTH_SHORT).show();
			}
		} else {
			Toast.makeText(this, "Error - Map Fragment was null!!", Toast.LENGTH_SHORT).show();
		}

	}
	


	


	public OnInfoWindowClickListener getInfoWindowClickListener() {
	    return new OnInfoWindowClickListener() 
	    {       
	        @Override
	        public void onInfoWindowClick(Marker marker) 
	        {
	        	dropPinEffect(marker);
	            //Toast.makeText(getApplicationContext(), "Clicked a window with title..." + marker.getSnippet(), Toast.LENGTH_SHORT).show();
	            Intent data = new Intent();
	            MapData mLocation = new MapData(marker.getPosition());
	            data.putExtra("latitude", marker.getPosition().latitude);
	            data.putExtra("longitude", marker.getPosition().longitude);
	            data.putExtra("Address", marker.getSnippet());
	            //data.putExtra(name, value)
	            // Activity finished ok, return the data
	            setResult(RESULT_OK, data); // set result code and bundle data for response
	            finish(); // closes the activity, pass data to parent
	        }
	    };     
	}


	/*
	 * Called when the Activity becomes visible.
	 */
	@Override
	protected void onStart() {
		super.onStart();
		// Connect the client.
		if (isGooglePlayServicesAvailable()) {
			mLocationClient.connect();
		}

	}

	/*
	 * Called when the Activity is no longer visible.
	 */
	@Override
	protected void onStop() {
		// Disconnecting the client invalidates it.
		mLocationClient.disconnect();
		super.onStop();
	}

	/*
	 * Handle results returned to the FragmentActivity by Google Play services
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {

		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				mLocationClient.connect();
				break;
			}

		}
	}

	private boolean isGooglePlayServicesAvailable() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			return true;
		} else {
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(resultCode, this,
					CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(getSupportFragmentManager(), "Location Updates");
			}

			return false;
		}
	}

	/*
	 * Called by Location Services when the request to connect the client
	 * finishes successfully. At this point, you can request the current
	 * location or start periodic updates
	 */
	@Override
	public void onConnected(Bundle dataBundle) {
		// Display the connection status
		Location location = mLocationClient.getLastLocation();
		if (location != null) {
			//Toast.makeText(this, "GPS location was found!", Toast.LENGTH_SHORT).show();
			LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10.0f);
			map.animateCamera(cameraUpdate);
		} else {
			Toast.makeText(this, "Current location was null, enable GPS on emulator!", Toast.LENGTH_SHORT).show();
		}
	}

	/*
	 * Called by Location Services if the connection to the location client
	 * drops because of an error.
	 */
	@Override
	public void onDisconnected() {
		// Display the connection status
		Toast.makeText(this, "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
	}

	/*
	 * Called by Location Services if the attempt to Location Services fails.
	 */
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		/*
		 * Google Play services can resolve some errors it detects. If the error
		 * has a resolution, try sending an Intent to start a Google Play
		 * services activity that can resolve error.
		 */
		if (connectionResult.hasResolution()) {
			try {
				// Start an Activity that tries to resolve the error
				connectionResult.startResolutionForResult(this,
						CONNECTION_FAILURE_RESOLUTION_REQUEST);
				/*
				 * Thrown if Google Play services canceled the original
				 * PendingIntent
				 */
			} catch (IntentSender.SendIntentException e) {
				// Log the error
				e.printStackTrace();
			}
		} else {
			Toast.makeText(getApplicationContext(),
					"Sorry. Location services not available to you", Toast.LENGTH_LONG).show();
		}
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {

		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}

	@Override
	public void onMapLongClick(final LatLng point) {
		// TODO Auto-generated method stub
		//Toast.makeText(this, "Long Press", Toast.LENGTH_LONG).show();
		//showAlertDialogForPoint(point);
		showPoint(point);
		
	}
	
	
	   private void showPoint(final LatLng point) {
		// TODO Auto-generated method stub
           BitmapDescriptor defaultMarker =
	                 BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
	              // Extract content from alert dialog
	              List<Address> address = null;
				try {
					address = selected_place_geocoder.getFromLocation(point.latitude, point.longitude, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("MapActivity",
		                    "IO Exception in getFromLocation()");
					e.printStackTrace();
				}
	              /* String snippet = ((EditText) alertDialog.findViewById(R.id.etSnippet)).
	                  getText().toString();*/
	              // Creates and adds marker to the map
				String locality = address.get(0).getLocality();
				String addr = address.get(0).getAddressLine(0);
				
	              Marker marker = map.addMarker(new MarkerOptions()
	                .position(point)
	                .title((locality.length()>0) ?locality:"Default Location")
	                .snippet(addr.length()>0?addr:"Yahoo Building D")
	                //.snippet(title)       
	                .icon(defaultMarker));
	              // Animate marker using drop effect
	              // --> Call the dropPinEffect method here
	              dropPinEffect(marker);
	}






	/* Display the alert that adds the marker
	   private void showAlertDialogForPoint(final LatLng point) {
	      // inflate message_item.xml view
	      View  messageView = LayoutInflater.from(MapActivity.this).
	        inflate(R.layout.message_item, null);
	      // Create alert dialog builder
	      AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
	      // set message_item.xml to AlertDialog builder
	      alertDialogBuilder.setView(messageView);

	      // Create alert dialog
	      //final AlertDialog alertDialog = alertDialogBuilder.create();

	      // Configure dialog button (OK)
	        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", 
	        new DialogInterface.OnClickListener() {
	          @Override
	          public void onClick(DialogInterface dialog, int which) {
	        	  
	              // Define color of marker icon
	              BitmapDescriptor defaultMarker =
	                 BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN);
	              // Extract content from alert dialog
	              String title = ((EditText) alertDialog.findViewById(R.id.etTitle)).
	                  getText().toString();
	              List<Address> address = null;
				try {
					address = selected_place_geocoder.getFromLocation(point.latitude, point.longitude, 1);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("MapActivity",
		                    "IO Exception in getFromLocation()");
					e.printStackTrace();
				}
	               String snippet = ((EditText) alertDialog.findViewById(R.id.etSnippet)).
	                  getText().toString();
	              // Creates and adds marker to the map
				String locality = address.get(0).getLocality();
				String addr = address.get(0).getAddressLine(0);
				
	              Marker marker = map.addMarker(new MarkerOptions()
	                .position(point)
	                .title((locality.length()>0) ?locality:"Default Location")
	                .snippet(addr.length()>0?addr:"Yahoo Building D")
	                //.snippet(title)       
	                .icon(defaultMarker));
	              // Animate marker using drop effect
	              // --> Call the dropPinEffect method here
	              dropPinEffect(marker);
	          }
	      });

	      // Configure dialog button (Cancel)
	      /*alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", 
	      new DialogInterface.OnClickListener() {
	        public void onClick(DialogInterface dialog, int id) { dialog.cancel(); }
	      });

	      // Display the dialog
	      alertDialog.show();
	  }*/
	   
	   private void dropPinEffect(final Marker marker) {
		      // Handler allows us to repeat a code block after a specified delay
		      final android.os.Handler handler = new android.os.Handler();
		      final long start = SystemClock.uptimeMillis();
		      final long duration = 1500;

		      // Use the bounce interpolator
		      final android.view.animation.Interpolator interpolator = 
		          new BounceInterpolator();

		      // Animate marker with a bounce updating its position every 15ms
		      handler.post(new Runnable() {
		          @Override
		          public void run() {
		              long elapsed = SystemClock.uptimeMillis() - start;
		              // Calculate t for bounce based on elapsed time 
		              float t = Math.max(
		                      1 - interpolator.getInterpolation((float) elapsed
		                              / duration), 0);
		              // Set the anchor
		              marker.setAnchor(0.5f, 1.0f + 14 * t);
		         
		              if (t > 0.0) {
		                  // Post this event again 15ms from now.
		                  handler.postDelayed(this, 15);
		              } else { // done elapsing, show window
		                  marker.showInfoWindow();
		              }
		          }
		      });
		  }
	   
		private void setDonationCentres() {
			// TODO Auto-generated method stub		LatLng Donation_centre_1 = new LatLng(37.4145, -121.905);
	        BitmapDescriptor defaultMarker =
	                BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED);
			LatLng loc_1 = new LatLng(37.368499,-122.032041);
			String snippet_1 = "105 E El Camino Real, Sunnyvale, CA 94087";
			String title_1 = "Sunnyvale Dropoff";
			
			LatLng loc_2 = new LatLng(37.404285,-122.0978186);
			String snippet_2 = "112 N Rengstorff Ave, Mountain View, CA 94043";
			String title_2 = "Mountain View Dropoff";
			
			LatLng loc_3 = new LatLng(37.3791323,-122.0727804);
			String snippet_3 = "121 E El Camino Real, Mountain View, CA 94040";
			String title_3 = "Mountain View Dropoff";
			
			 
			LatLng loc_4 = new LatLng(37.3518426,-121.9921608);
			String snippet_4 = "3460 El Camino Real, Santa Clara, CA 950510";
			String title_4 = "Santa Clara Dropoff";
			
			LatLng loc_5 = new LatLng(37.415138,-122.024394);
			String snippet_5 = "701 First Ave, Sunnyvale, CA";
			String title_5 = "Yahoo Building D Dropoff ";
			
			LatLng loc_6 = new LatLng(37.4111284,-122.1250076);
			String snippet_6= "4170 El Camino Real, Palo Alto, CA 94306";
			String title_6 = "Palo Alto Dropoff";
			
			LatLng loc_7 = new LatLng(37.5650926,-122.3236004);
			String snippet_7= "191 E 3rd Ave, San Mateo, CA 94401";
			String title_7 = "San Mateo Dropoff";
			
			LatLng loc_8 = new LatLng(37.598555,-122.399733);
			String snippet_8 = "615 Broadway, Millbrae, CA 94030";
			String title_8 = "Millbrae Dropoff";
			
			LatLng loc_9 = new LatLng(37.6032849,-122.3951162);
			String snippet_9 = "1414 El Camino Real, San Carlos, CA 94070";
			String title_9 = "San Carlos Dropoff";
			
			LatLng loc_10 = new LatLng(37.6032849,-122.3951162);
			String snippet_10 = "399 El Camino Real South San Francisco, CA 94080";
			String title_10 = "South San Francisco Dropoff";
			
			
			LatLng loc_11 = new LatLng(37.4459026,-122.1611659);
			String snippet_11 = "300 University Ave Palo Alto, CA 94301";
			String title_11 = "Palo Alto";
			
			
			LatLng loc_12 = new LatLng(37.474899,-122.207731);
			String snippet_12 = "2938 Crocker Ave Redwood City, CA 94063";
			String title_12 = "Redwood City";
			
			LatLng loc_13 = new LatLng(37.7913396,-122.4005373);
			String snippet_13 = "100 Sansome St San Francisco, CA 94104";
			String title_13 = "Sansome St, SF Dropoff";

			
			LatLng loc_14 = new LatLng(37.7757012,-122.4190747);
			String snippet_14 = "1496 Market St San Francisco, CA 94102";
			String title_14 = "Market St,SF Dropoff";

			LatLng loc_15 = new LatLng(37.7860596,-122.4082021);
			String snippet_15 = "135 Powell St San Francisco, CA 94102";
			String title_15 = "San Francisco Dropoff";


			LatLng loc_16 = new LatLng(37.7437788,-122.4395816);
			String snippet_16 = "5260 Diamond Heights Blvd San Francisco, CA 94131";
			String title_16 = "Diamond Heights Blvd,SF Dropoff";
			
			//String timings = "\nDrop off Hours: 9:00am - 5:00pm ";
			String timings = "";
			//String timings_weekends = " and 9:00am - 5:00pm Saturdays/Sundays";

	        Marker marker2 = map.addMarker(new MarkerOptions()
	        .position(loc_2)
	        .title(title_2)
	        .snippet(snippet_2 + timings)     
	        .icon(defaultMarker));
			
	          /*Marker marker1 = map.addMarker(new MarkerOptions()
	            .position(loc_1)
	            .title(title_1)
	            .snippet(snippet_1)       
	            .icon(defaultMarker));

	         Marker marker3 = map.addMarker(new MarkerOptions()
	          .position(loc_3)
	          .title(title_3)
	          .snippet(snippet_3)       
	          .icon(defaultMarker));
	          Marker marker4 = map.addMarker(new MarkerOptions()
	          .position(loc_4)
	          .title(title_4)
	          .snippet(snippet_4)       
	          .icon(defaultMarker));*/
	          Marker marker5 = map.addMarker(new MarkerOptions()
	          .position(loc_5)
	          .title(title_5)
	          .snippet(snippet_5 + timings)       
	          .icon(defaultMarker));
	          Marker marker6 = map.addMarker(new MarkerOptions()
	          .position(loc_6)
	          .title(title_6)
	          .snippet(snippet_6 + timings )      
	          .icon(defaultMarker));
	          Marker marker7 = map.addMarker(new MarkerOptions()
	          .position(loc_7)
	          .title(title_7)
	          .snippet(snippet_7 + timings)       
	          .icon(defaultMarker));
	          /*Marker marker8 = map.addMarker(new MarkerOptions()
	          .position(loc_8)
	          .title(title_8)
	          .snippet(snippet_8)       
	          .icon(defaultMarker));*/
	          
	          Marker marker9 = map.addMarker(new MarkerOptions()
	          .position(loc_9)
	          .title(title_9)
	          .snippet(snippet_9  + timings)       
	          .icon(defaultMarker));
	          Marker marker10 = map.addMarker(new MarkerOptions()
	          .position(loc_10)
	          .title(title_10)
	          .snippet(snippet_10  +timings)       
	          .icon(defaultMarker));
	         
	          Marker marker11 = map.addMarker(new MarkerOptions()
	          .position(loc_11)
	          .title(title_11)
	          .snippet(snippet_11)       
	          .icon(defaultMarker));
	          
	          Marker marker12 = map.addMarker(new MarkerOptions()
	          .position(loc_12)
	          .title(title_12)
	          .snippet(snippet_12)       
	          .icon(defaultMarker));
	          
	          /*Marker marker13 = map.addMarker(new MarkerOptions()
	          .position(loc_13)
	          .title(title_13)
	          .snippet(snippet_13)       
	          .icon(defaultMarker));*/
	          
	          Marker marker14 = map.addMarker(new MarkerOptions()
	          .position(loc_14)
	          .title(title_14)
	          .snippet(snippet_14  + timings)       
	          .icon(defaultMarker));
	          Marker marker15 = map.addMarker(new MarkerOptions()
	          .position(loc_15)
	          .title(title_15)
	          .snippet(snippet_15  + timings)       
	          .icon(defaultMarker));
	          Marker marker16 = map.addMarker(new MarkerOptions()
	          .position(loc_16)
	          .title(title_16)
	          .snippet(snippet_16  + timings)       
	          .icon(defaultMarker));
	          dropPinEffect(marker2);
	          dropPinEffect(marker5);
	          dropPinEffect(marker6);
	          dropPinEffect(marker7);
	          //dropPinEffect(marker8);
	          dropPinEffect(marker9);
	          dropPinEffect(marker10);
	          dropPinEffect(marker14);
	          dropPinEffect(marker15);
	          dropPinEffect(marker16);
	          //dropPinEffect(marker1);     
	          
		}
	
}


