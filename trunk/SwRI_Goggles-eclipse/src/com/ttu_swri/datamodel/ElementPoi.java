package com.ttu_swri.datamodel;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

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
		this("<no id>", "<no description>", null);
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
		this(id, ElementType.T_POI, new Date(System.currentTimeMillis()), name,
				description, location, -1, null);
	}

	/**
	 * Never mar this as public. Previous constructors let you do all you need.
	 * ElementType will be always POI. LastEdit will be always current time on
	 * time of creation (can be changed by setter if otherwise).
	 */
	private ElementPoi(String id, ElementType type, Date lastEdit, String name,
			String description, Location location, int checkpointNumber,
			Date expires) {
		super(id, type, lastEdit);

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
			o.put("location",
					this.location.getLatitude() + ","
							+ this.location.getLongitude());
			o.put("checkpointNumber", this.checkpointNumber);
			o.put("expires", this.expires.toGMTString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}

}
