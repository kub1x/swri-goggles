package com.ttu_swri.datamodel;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

/** @author kub1x */
public class ElementMate extends Element {

	// String user_id = null; // Is in Element.Id
	private String name = null;
	private String description = null;
	private Location location = null;

	// Constructors ===========================================================

	public ElementMate() {
		this("<no id>", "<no description>");
	}

	public ElementMate(String name, String description) {
		this(name, description, null);
	}

	public ElementMate(String name, String description, Location location) {
		// TODO change name in following line to some ID
		this("Mate:" + name.replace(' ', '_'), name, description, location);
	}

	public ElementMate(String id, String name, String description,
			Location location) {
		this(id, ElementType.T_MATE, new Date(System.currentTimeMillis()),
				name, description, location);
	}

	private ElementMate(String id, ElementType type, Date lastEdit,
			String name, String description, Location location) {
		super(name, ElementType.T_MATE, new Date(System.currentTimeMillis()));
		this.name = name;
		this.description = description;
		this.location = location;
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

	public void setLocation(Location location) {
		this.location = location;
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
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}

}
