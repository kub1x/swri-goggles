/**
 * 
 */
package com.ttu_swri.goggles;

import java.util.Collection;
import java.util.HashMap;

import com.ttu_swri.datamodel.Element;

import android.location.Location;

/**
 * @author kub1x
 * 
 */
public class DataManager {

	// TODO MAKE THIS CLASS SINGLETON!!!!

	private HashMap<String, Element> elements = null;
	private Location my_location = null;

	public DataManager() {
		this.elements = new HashMap<String, Element>();
	}

	public Location getMyLocation() {
		// TODO probably will be more sophisticated
		return this.my_location;
	}

	public void updateMyLocation(Location new_location) {
		// TODO probably some check needed..
		this.my_location = new_location;
	}

	public Collection<Element> getElements() {
		// TODO create something we can iterate on for Visualization to iterate
		// on. It should contain copy of our elements from HashMap. Following
		// code probably returns references to original elements. Need to look
		// for best practice here.
		return this.elements.values();
	}

	public void update(Element element) {
		if (!this.elements.containsKey(element.Id)) {
			add_element(element);
		} else {
			update_element(element);
		}
	}

	private void add_element(Element element) {
		// TODO check if element didn't overceed it's expiration time
		this.elements.put(element.Id, element);
	}

	private void update_element(Element element) {
		// TODO this part might need to be locked. Updates might come on
		// irregular basis thus concurrently

		// TODO recognize type of element, it's last edited time compare it to
		// already stored element and update data if necessary

		Element orig = (Element) this.elements.get(element.Id);

		if (orig.LastUpdate.after(element.LastUpdate)) {
			// We already have newest version
			// TODO do something! at least return false
			return;
		} else {
			this.elements.remove(element.Id);
			this.elements.put(element.Id, element);
		}
	}

}
