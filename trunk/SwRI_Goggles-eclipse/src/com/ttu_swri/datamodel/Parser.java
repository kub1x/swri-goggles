package com.ttu_swri.datamodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.location.Location;

import com.ttu_swri.datamodel.Element.ElementType;

/** @author kub1x */
public class Parser {

	public static List<Element> parse(String json) {
		List<Element> elements = new ArrayList<Element>();

		// {"messages":[{"id":"5824513078343549739","body":"hello","timeout":60}]}

		try {
			// Parse the body of message
			JSONObject oMsgs;
			oMsgs = new JSONObject(json);
			// Get array of all messages
			JSONArray aMsgs = oMsgs.getJSONArray("messages");
			// For each message in array
			for (int i = 0; i < aMsgs.length(); i++) {
				// Get it's content
				JSONObject oMesg = aMsgs.getJSONObject(i);
				// We want only body of the message
				JSONObject oElem = oMesg.getJSONObject("body");
				// The body is our original element.. parse it!
				elements.add(parseElement(oElem));
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return elements;
	}

	public static Element parseElement(JSONObject jsonElement)
			throws JSONException {
		Element ret;
		// Get type of element...
		ElementType type = ElementType.parseJson(jsonElement.getString("type"));
		// Create specific type of element
		switch (type) {
		case T_MATE:
			ret = parseElementMate(jsonElement);
			break;
		case T_POI:
			ret = parseElementPoi(jsonElement);
			break;
		case T_MESSAGE:
			ret = parseElementMessage(jsonElement);
			break;
		default:
			ret = null;
		}
		return ret; // TODO XXX !!!
	}

	private static Element parseElementMessage(JSONObject jsonElement)
			throws JSONException {
		// TODO replace strings with resources
		String id = jsonElement.getString("id");
		Date lastUpdate = new Date(jsonElement.getString("lastUpdate"));
		String topic = jsonElement.getString("topic");
		String text = jsonElement.getString("text");
		boolean isAlert = jsonElement.getBoolean("isAlert");
		String sExpires = jsonElement.getString("expires");
		Date expires = null;
		if (sExpires != "")
			expires = new Date(sExpires);
		return new ElementMessage(id, lastUpdate, topic, text, isAlert, expires);
	}

	private static Element parseElementPoi(JSONObject jsonElement)
			throws JSONException {
		String id = jsonElement.getString("id");
		Date lastUpdate = new Date(jsonElement.getString("lastUpdate"));
		String name = jsonElement.getString("name");
		String description = jsonElement.getString("description");

		String sLoc = jsonElement.getString("location");
		String[] aLoc = sLoc.split(",");
		double latitude = Double.parseDouble(aLoc[0]);
		double longitude = Double.parseDouble(aLoc[1]);
		Location location = new Location(""); // TODO SET PROVIDER !!!
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		int checkpointNumber = jsonElement.getInt("checkpointNumber");
		
		String sExpires = jsonElement.getString("expires");
		Date expires = null;
		if (sExpires != "")
			expires = new Date(sExpires);
		
		return new ElementPoi(id, lastUpdate, name, description, location,
				checkpointNumber, expires);
	}

	private static Element parseElementMate(JSONObject jsonElement)
			throws JSONException {
		String id = jsonElement.getString("id");
		Date lastUpdate = new Date(jsonElement.getString("lastUpdate"));
		String name = jsonElement.getString("name");
		String description = jsonElement.getString("description");

		String sLoc = jsonElement.getString("location");
		String[] aLoc = sLoc.split(",");
		double latitude = Double.parseDouble(aLoc[0]);
		double longitude = Double.parseDouble(aLoc[1]);
		Location location = new Location(""); // TODO SET PROVIDER !!!
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		return new ElementMate(id, lastUpdate, name, description, location);
	}

}
