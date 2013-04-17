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
public class ElementMate extends Element {

	// String user_id = null; // Is in Element.Id
	String name = null;
	String description = null;
	Location location = null;

	/**
	 * 
	 */
	public ElementMate() {
		this("<no id>", "<no description>");
	}
	
	public ElementMate(String name, String description){
		this(name, description, null);
	}

	public ElementMate(String name, String description, Location location) {
		// TODO change name in following line to some ID
		super(name, ElementType.T_MATE, new Date(System.currentTimeMillis()));
		this.name = name;
		this.description = description;
		this.location = location;
	}

}
