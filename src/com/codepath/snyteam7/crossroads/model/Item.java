package com.codepath.snyteam7.crossroads.model;

import java.util.Date;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Item")
public class Item extends ParseObject {
	public Item() {
		super();
	}

	// Use getString and others to access fields
	public String getDescription() {
		return getString("description");
	}

	// Use put to modify field values
	public void setDescription(String value) {
		put("description", value);
	}

	// Get the donor
	public ParseUser getDonor() {
		return getParseUser("donor");
	}

	public void setDonor(ParseUser user) {
		put("donor", user);
	}

	public ParseUser getReviewer() {
		return getParseUser("reviewer");
	}

	public void setReviewer(ParseUser user) {
		put("reviewer", user);
	}

	public ParseFile getPhotoFile() {
		return getParseFile("photo");
	}

	public void setPhotoFile(ParseFile file) {
		put("photo", file);
	}
	  public Date getCreationDate() {
		  return getCreatedAt();
	}
}
