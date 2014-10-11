package com.codepath.snyteam7.crossroads.model;

import com.parse.ParseClassName;
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

	  // Get the user for this item
	  public ParseUser getDonateUserId()  {
	    return getParseUser("donateuserid");
	  }

	  // Associate each item with a user
	  public void setDonateUserId(ParseUser user) {
	    put("donateuserid", user);
	  }
}
