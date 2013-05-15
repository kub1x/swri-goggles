package edu.ttu.swri.data.model;

import java.util.Date;


import android.location.Location;
import android.location.LocationManager;

/** @author kub1x */
public class ElementMate extends Element {

	// String user_id = null; // Is in Element.Id
	private String name = "";
	private String description = "";
	private double latitude = 0;
	private double longitude = 0;
	private transient Location location = null;

	// Constructors ===========================================================

	public ElementMate() {
		this("<no id>", "<no description>");
	}

	public ElementMate(String name, String description) {
		this(name, description, new Location(LocationManager.PASSIVE_PROVIDER));
	}

	public ElementMate(String name, String description, Location location) {
		// TODO change name in following line to some ID
		this("Mate:" + name.replace(' ', '_'), name, description, location);
	}

	public ElementMate(String id, String name, String description,
			Location location) {
		this(id, new Date(System.currentTimeMillis()), name, description,
				location);
	}

	/**
	 * Complete constructor
	 * 
	 * To be used here and by parser only!
	 * 
	 * @param id
	 * @param lastUpdate
	 * @param name
	 * @param description
	 * @param location
	 */
	protected ElementMate(String id, Date lastUpdate, String name,
			String description, Location location) {
		super(id, ElementType.T_MATE, new Date(System.currentTimeMillis()));
		this.name = name;
		this.description = description;
		this.setLocation(location);
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
	
	public double getLatitude(){
		return this.latitude;
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;		
	}
	
	public double getLongitude(){
		return this.longitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
	}
	
	

	public void setLocation(Location location) {
		if (location == null){
			this.location = new Location(LocationManager.PASSIVE_PROVIDER);
		}
		else {
			this.location = location;
			this.latitude = location.getLatitude();
			this.longitude = location.getLongitude();
		}
		this.justEdited();
	}
	
    /**
     * Computes the approximate distance in meters between two
     * locations, and optionally the initial and final bearings of the
     * shortest path between them.  Distance and bearing are defined using the
     * WGS84 ellipsoid.
     *
     * <p> The computed distance is stored in results[0].  If results has length
     * 2 or greater, the initial bearing is stored in results[1]. If results has
     * length 3 or greater, the final bearing is stored in results[2].
     *
     * @param startLatitude the starting latitude
     * @param startLongitude the starting longitude
     * @param endLatitude the ending latitude
     * @param endLongitude the ending longitude
     * @param results an array of floats to hold the results
     *
     * @throws IllegalArgumentException if results is null or has length < 1
     */
    public static void distanceBetween(double startLatitude, double startLongitude,
        double endLatitude, double endLongitude, float[] results) {
        if (results == null || results.length < 1) {
            throw new IllegalArgumentException("results is null or has length < 1");
        }
        computeDistanceAndBearing(startLatitude, startLongitude,
            endLatitude, endLongitude, results);
    }

	// ========================================================================

	// @Override
	// public JSONObject toJson() {
	// // TODO handle NULL values
	// JSONObject o = super.toJson();
	// try {
	// o.put("name", this.name);
	// o.put("description", this.description);
	// if (this.location == null)
	// o.put("location", "0,0");
	// else
	// o.put("location", this.location.getLatitude() + ","
	// + this.location.getLongitude());
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return o;
	// }

}
