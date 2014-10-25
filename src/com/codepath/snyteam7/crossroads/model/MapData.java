package com.codepath.snyteam7.crossroads.model;

import java.io.Serializable;

import com.google.android.gms.maps.model.LatLng;

public class MapData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8498109454778932405L;
	private LatLng latlng;
	public MapData(LatLng latlng) {
		this.latlng = latlng;
	}
}
