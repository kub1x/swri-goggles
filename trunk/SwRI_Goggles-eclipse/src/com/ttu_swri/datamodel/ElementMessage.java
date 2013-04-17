/**
 * 
 */
package com.ttu_swri.datamodel;

import java.util.Date;

/**
 * @author kub1x
 * 
 */
public class ElementMessage extends Element {

	// TODO set as private, generate getters and setters
	// String msg_id = null; // Is in Element.Id
	public String Topic = null;
	public String Text = null;
	public boolean IsAlert = false;
	public Date Expires = null;

	/**
	 * 
	 */
	public ElementMessage() {
		this("<no id>", "<no message>");
	}

	public ElementMessage(String topic, String text) {
		this(topic, text, false, null);
	}

	public ElementMessage(String topic, String text, boolean isAlert,
			Date expires) {
		super(topic, ElementType.T_MESSAGE,
				new Date(System.currentTimeMillis()));
		this.Topic = topic;
		this.Text = text;
		this.IsAlert = isAlert;
		this.Expires = expires;
	}
}
