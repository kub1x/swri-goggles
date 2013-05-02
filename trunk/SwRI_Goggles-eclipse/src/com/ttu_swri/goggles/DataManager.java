package com.ttu_swri.goggles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;

import com.ttu_swri.datamodel.Element;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.IVisitable;
import com.ttu_swri.datamodel.IVisualizer;
import com.ttu_swri.datamodel.Parser;

import android.location.Location;

/** @author kub1x */
public class DataManager {

	private HashMap<String, Element> elements = null;
	private List<Element> toSync = null;
	private List<IVisualizer> visualizers = null;

	private ElementMate myUserInfo = null;

	// Constructor ============================================================

	private DataManager() {
		this.elements = new HashMap<String, Element>();
		this.toSync = new ArrayList<Element>();
		this.visualizers = new ArrayList<IVisualizer>();

		// TODO DELME
		// This is hardcoded UserInfo for user of goggles
		this.myUserInfo = new ElementMate();
		this.myUserInfo.setName("Popeye");
		this.myUserInfo.setDescription("The Sailor Man"); // =D
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

	public void register(IVisualizer visualizer) {
		if (!this.visualizers.contains(visualizer)) {
			this.visualizers.add(visualizer);
			visualizer.update();
		}
	}

	private void updateAllVisualizers() {
		for (IVisualizer visu : this.visualizers)
			visu.update();
	}

	// ========================================================================

	public Collection<Element> getElements() {
		// TODO create something we can iterate on for Visualization. It should
		// contain copy of our elements from HashMap. Following code probably
		// returns references to original elements. Need to look for best
		// practice here.
		return this.elements.values();
	}

	/**
	 * Update adds or updates elements in our elements collection. It also adds
	 * elements to queue for synchronizing via network. This method serves OUR
	 * app, not network!, i.e. this is what our user created or updated.
	 * 
	 * @param element
	 */
	public void update(Element element) {
		if (update_element(element)) {
			this.toSync.add(element);
		}
	}

	private boolean update_element(Element element) {
		synchronized (this) {
			if (!this.elements.containsKey(element.getId())) {
				this.elements.put(element.getId(), element);
				return true;
			} else {
				Element orig = (Element) this.elements.get(element.getId());

				if (orig.isNewerThan(element)) {
					// We already have the newest version
					return false;
				} else {
					// This is just stupid one to one replacement
					this.elements.remove(element.getId());
					this.elements.put(element.getId(), element);
					// TODO XXX !!! How to deal with DELETE? If other user
					// deletes POI should we delete it too?
					return true;
				}
			}
		}
	}

	/**
	 * This Update method serves for elements we obtained from network sync.
	 * 
	 * @param elements
	 * @throws JSONException
	 */
	public void updateFromNetwork(List<String> elements) throws JSONException {
		boolean changed = false;
		for (String element : elements) {
			changed |= update_element(Parser.parseElement(element));
		}

		if (changed)
			updateAllVisualizers();
	}

	/**
	 * This getter serves for Network Component. It returns Elements that need
	 * to be synchronized with other users.
	 * 
	 * @return
	 */
	public List<Element> getElementsToSync() {
		// Copy stuff to be sent and clear original
		List<Element> elements = new ArrayList<Element>(this.toSync);
		this.toSync.clear(); // TODO clear only when successfully sent..?

		// Add info about current user
		// elements.add(this.myUserInfo); //TODO XXX UNCOMMENT !!!

		// Send
		return elements;
	}

}
