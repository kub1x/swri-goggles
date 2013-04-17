/**
 * 
 */
package com.ttu_swri.datamodel;

import java.util.Date;

/**
 * @author kub1x
 * 
 */
public class Element implements IElementlike {

	public enum ElementType {
		T_MY_POS, T_MATE, T_POI, T_MESSAGE,
	}

	public String Id = null;
	public ElementType Type = null;
	public Date LastUpdate = null;

	//XXX Type is essential. Do not allow implicit constructor
	//public Element() {
	//}

	public Element(ElementType type) {
		this.Type = type;
	}

	public Element(String id, ElementType type, Date lastUpdate) {
		this.Id = id;
		this.Type = type;
		this.LastUpdate = lastUpdate;
	}

	@Override
	public void accept(IVisitable visitor) {
		visitor.visit(this);
	}

}
