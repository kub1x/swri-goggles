package com.ttu_swri.datamodel;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

/** @author kub1x */
public class Element implements IElementlike {

	public enum ElementType {
		// T_MY_POS("position"),
		T_MATE("userInfo"), T_POI("poi"), T_MESSAGE("message");

		private final String jsonName;

		private ElementType(String jsonName) {
			this.jsonName = jsonName;
		}

		public String toJson() {
			return this.jsonName;
		}

		public static ElementType parseJson(String jsonName) throws JSONException {
			// if(jsonName == "position")
			// return T_MY_POS;
			if (jsonName == "userInfo")
				return T_MATE;
			if (jsonName == "poi")
				return T_POI;
			if (jsonName == "message")
				return T_MESSAGE;

			throw new JSONException("Invalid ElementType name");
		}
	}

	protected final String id;
	protected final ElementType type;
	private Date lastUpdate;

	// XXX Type is essential. Do not allow implicit constructor
	// public Element() {
	// }

	// XXX Probably not to be used
	// public Element(ElementType type) {
	// this("<no id>", type, null);
	// }

	public Element(String id, ElementType type, Date lastUpdate) {
		this.id = id;
		this.type = type;
		this.lastUpdate = lastUpdate;
	}

	// Visitor Design Pattern =================================================

	@Override
	public void accept(IVisitable visitor) {
		visitor.visit(this);
	}

	// Getters/Setters ========================================================

	public String getId() {
		return id;
	}

	public ElementType getType() {
		return type;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}

	// ========================================================================

	protected void justEdited() {
		this.lastUpdate = new Date(System.currentTimeMillis());
	}

	public boolean isNewerThan(Element element) {
		return this.lastUpdate.after(element.lastUpdate);
	}

	public JSONObject toJson() {
		JSONObject o = new JSONObject();
		try {
			o.put("id", this.id);
			o.put("type", this.type.toJson());
			o.put("lastUpdate", this.lastUpdate.toGMTString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return o;
	}
}
