package com.codepath.snyteam7.crossroads;

import android.app.Application;
import android.util.Log;

import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.SaveCallback;

public class CrossroadsApplication extends Application {
	@Override
	public void onCreate() {
		ParseObject.registerSubclass(Item.class);
		//ParseObject.registerSubclass(CrossroadsUser.class);
		Parse.initialize(this, "wcoPa6w0bpaQuqUVkSlRe90K0TH8b8bXsBgBl7jl", "DpK6mli61BLXWvN02X6pqXGMrhV09U2FOP3UtmJ3");
		ParseInstallation.getCurrentInstallation().saveInBackground();

		// Add Push notification receiver (Broadcast when channel is "")
		ParsePush.subscribeInBackground("", new SaveCallback() {
			  @Override
			  public void done(ParseException e) {
			    if (e != null) {
			      Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
			    } else {
			      Log.e("com.parse.push", "failed to subscribe for push", e);
			    }
			  }
			});

	}

}
