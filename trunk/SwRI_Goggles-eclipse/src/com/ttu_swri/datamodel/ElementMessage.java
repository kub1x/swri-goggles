package com.ttu_swri.datamodel;

import java.util.Date;

/** @author kub1x */
public class ElementMessage extends Element {

	// String msg_id = null; // Is in Element.Id
	private String topic = null;
	private String text = null;
	private boolean isAlert = false;
	private Date expires = null;

	// Constructors ===========================================================

	public ElementMessage() {
		this("<no id>", "<no message>");
	}

	public ElementMessage(String topic, String text) {
		this(topic, text, false, null);
	}

	public ElementMessage(String topic, String text, boolean isAlert,
			Date expires) {
		this("Msg:" + topic.replace(' ', '_'), topic, text, isAlert, expires);
	}

	public ElementMessage(String id, String topic, String text,
			boolean isAlert, Date expires) {
		this(id, ElementType.T_MESSAGE, new Date(System.currentTimeMillis()),
				topic, text, isAlert, expires);
	}

	private ElementMessage(String id, ElementType type, Date lastEdit,
			String topic, String text, boolean isAlert, Date expires) {
		super(id, type, lastEdit);
		this.topic = topic;
		this.text = text;
		this.isAlert = isAlert;
		this.expires = expires;
	}

	// Getters/Setters ========================================================

	public String getTopic() {
		return topic;
	}

	public void setTopic(String topic) {
		this.topic = topic;
		this.justEdited();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
		this.justEdited();
	}

	public boolean isAlert() {
		return isAlert;
	}

	public void setAlert(boolean isAlert) {
		this.isAlert = isAlert;
		this.justEdited();
	}

	public Date getExpires() {
		return expires;
	}

	public void setExpires(Date expires) {
		this.expires = expires;
		this.justEdited();
	}

}
