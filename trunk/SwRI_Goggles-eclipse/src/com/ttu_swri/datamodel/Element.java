package com.ttu_swri.datamodel;

import java.util.Date;

import com.google.gson.Gson;

/** @author kub1x */
public class Element implements IElementlike {

	public enum ElementType {
		T_MATE, T_POI, T_MESSAGE;
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

	// ========================================================================

	public String toJson() {
		return new Gson().toJson(this);
	}

	// ========================================================================

}
