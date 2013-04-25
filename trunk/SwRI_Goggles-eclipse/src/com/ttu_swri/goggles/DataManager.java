package com.ttu_swri.goggles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.ttu_swri.datamodel.Element;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.Parser;

import android.location.Location;

/** @author kub1x */
public class DataManager {

	private HashMap<String, Element> elements = null;
	private List<Element> toSync = null;

	private ElementMate myUserInfo = null;

	// Constructor ============================================================

	private DataManager() {
		this.elements = new HashMap<String, Element>();
		this.toSync = new ArrayList<Element>();

		this.myUserInfo = new ElementMate();
		myUserInfo.setName("Popeye");
		myUserInfo.setDescription("The Sailor Man"); // =D
	}

	// Singleton design pattern ===============================================

	private static DataManager dminst = null;

	public static DataManager getInstance() {
		if (DataManager.dminst == null) {
			DataManager.dminst = new DataManager();
		}
		return DataManager.dminst;
	}

	// Getters/Setters ========================================================

	public Location getMyLocation() {
		return this.myUserInfo.getLocation();
	}

	public void setMyLocation(Location location) {
		this.myUserInfo.setLocation(location);
	}

	// ========================================================================

	public Collection<Element> getElements() {
		// TODO create something we can iterate on for Visualization to iterate
		// on. It should contain copy of our elements from HashMap. Following
		// code probably returns references to original elements. Need to look
		// for best practice here.
		return this.elements.values();
	}

	/**
	 * This method serves OUR app, not network, i.e. this is what our user
	 * created or updated.
	 * 
	 * @param element
	 */
	public void update(Element element) {
		toSync.add(element);
		update_element(element);
	}

	private void update_element(Element element) {
		synchronized (this) {
			if (!this.elements.containsKey(element.getId())) {
				this.elements.put(element.getId(), element);
				// TODO return true..?
			} else {

				Element orig = (Element) this.elements.get(element.getId());

				if (orig.isNewerThan(element)) {
					// DO NOTHING, We already have newest version
					// TODO at least return false
				} else {
					// This is just stupid one to one replacement
					this.elements.remove(element.getId());
					this.elements.put(element.getId(), element);
					// TODO XXX !!! How to deal with DELETE? If other user
					// deletes POI should we delete it too?
				}
			}
		}
	}

	public void updateFromNetwork(List<JSONObject> elements) throws JSONException {
		for (JSONObject element : elements) {
			update_element(Parser.parseElement(element));
		}
	}

	public List<Element> getElementsToSync() {
		// Copy stuff to be sent and clear original
		List<Element> elements = new ArrayList<Element>(this.toSync);
		this.toSync.clear(); // TODO clear only when successfully sent..?
		// Add info about current user
		//elements.add(this.myUserInfo); //TODO XXX UNCOMMENT !!! when the rest is tested
		// Send
		return elements;
	}

}
