package com.codepath.snyteam7.crossroads;

import android.app.Application;

import com.codepath.snyteam7.crossroads.model.Item;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;

public class CrossroadsApplication extends Application {
	@Override
	public void onCreate() {
		ParseObject.registerSubclass(Item.class);
		Parse.initialize(this, "wcoPa6w0bpaQuqUVkSlRe90K0TH8b8bXsBgBl7jl", "DpK6mli61BLXWvN02X6pqXGMrhV09U2FOP3UtmJ3");
		ParseInstallation.getCurrentInstallation().saveInBackground();

	}

}
