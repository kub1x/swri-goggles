/**
 * 
 */
package com.ttu_swri.datamodel;

import java.util.Date;
import android.location.Location;

/**
 * @author kub1x
 * 
 */
public class ElementPoi extends Element {

	// String poi_id = null; // Is in Element.Id
	public String Name = null;
	public String Description = null;
	public Location Position = null;
	public int CheckpointNumber = -1;
	public Date Expires = null;

	/**
	 * 
	 */
	public ElementPoi() {
		this("<no id>", "<no description>", null);
	}

	public ElementPoi(String name, String description, Location location) {
		this(name, description, location, -1, null);
	}

	public ElementPoi(String name, String description, Location location,
			int checkpointNumber, Date expires) {
		super(name, ElementType.T_POI, new Date(System.currentTimeMillis()));
		this.Name = name;
		this.Description = description;
		this.Position = location;
		this.CheckpointNumber = checkpointNumber;
		this.Expires = expires;
	}

}
