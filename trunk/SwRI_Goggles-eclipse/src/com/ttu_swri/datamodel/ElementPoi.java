package com.ttu_swri.datamodel;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;
import android.location.LocationManager;

/** @author kub1x */
public class ElementPoi extends Element {

	// String poi_id = null; // Is in Element.Id
	private String name = null;
	private String description = null;
	private Location location = null;
	private int checkpointNumber = -1;
	private Date expires = null;

	// Constructors ===========================================================

	public ElementPoi() {
		this("<no id>", "<no description>", new Location(
				LocationManager.PASSIVE_PROVIDER));
	}

	public ElementPoi(String name, String description, Location location) {
		this(name, description, location, -1, null);
	}

	public ElementPoi(String name, String description, Location location,
			int checkpointNumber, Date expires) {
		this("POI:" + name, name, description, location, -1, null);
	}

	public ElementPoi(String id, String name, String description,
			Location location, int checkpointNumber, Date expires) {
		this(id, new Date(System.currentTimeMillis()), name, description,
				location, -1, null);
	}

	/**
	 * Complete constructor
	 * 
	 * To be used here and by Parser only!
	 * 
	 * Never mark this constructor as public. Previous constructors let you do
	 * all you need. ElementType will be always POI here. LastEdit will be
	 * always current time on time of creation (can be changed by setter if
	 * otherwise).
	 * 
	 * @param id
	 * @param lastUpdate
	 * @param name
	 * @param description
	 * @param location
	 * @param checkpointNumber
	 * @param expires
	 */
	protected ElementPoi(String id, Date lastUpdate, String name,
			String description, Location location, int checkpointNumber,
			Date expires) {
		super(id, ElementType.T_POI, lastUpdate);

		this.name = name;
		this.description = description;
		this.location = location;
		this.checkpointNumber = checkpointNumber;
		this.expires = expires;
	}

	// Getters/Setters ========================================================

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.justEdited();
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
		this.justEdited();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location position) {
		if (location == null)
			this.location = new Location(LocationManager.PASSIVE_PROVIDER);
		else
			this.location = position;
		this.justEdited();
	}

	public int getCheckpointNumber() {
		return checkpointNumber;
	}

	public void setCheckpointNumber(int checkpointNumber) {
		this.checkpointNumber = checkpointNumber;
		this.justEdited();
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
		this.justEdited();
	}

	// ========================================================================

	@Override
	public JSONObject toJson() {
		// TODO handle NULL values
		JSONObject o = super.toJson();
		try {
			o.put("name", this.name);
			o.put("description", this.description);
			if (this.location == null)
				o.put("location", "0,0");
			else
				o.put("location", this.location.getLatitude() + ","
						+ this.location.getLongitude());
			o.put("checkpointNumber", this.checkpointNumber);
			if (this.expires == null)
				o.put("expires", "");
			else
				o.put("expires", this.expires.toGMTString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}

}
