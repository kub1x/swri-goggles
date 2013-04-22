package edu.ttu.swri.data.model.impl;

import edu.ttu.swri.data.model.GogglesMessage;


public class IronIOMessageImpl implements GogglesMessage {
	
	@Override
	public String getFrom() {
		return from;
	}
	
	@Override
	public void setFrom(String from) {
		this.from = from;
	}
	
	@Override
	public String getTo() {
		return to;
	}
	
	@Override
	public void setTo(String to) {
		this.to = to;
	}
	
	@Override
	public long getSentDate() {
		return sentDate;
	}
	
	@Override
	public void setSentDate(long sentDate) {
		this.sentDate = sentDate;
	}
	public MessageType getType() {
		return type;
	}
	
	@Override
	public void setType(MessageType type) {
		this.type = type;
	}
	
	@Override
	public String getGpsLocation() {
		return gpsLocation;
	}
	
	@Override
	public void setGpsLocation(String gpsLocation) {
		this.gpsLocation = gpsLocation;
	}
	
	@Override
	public String getMsgBody() {
		return msgBody;
	}
	
	@Override
	public void setMsgBody(String msgBody) {
		this.msgBody = msgBody;
	}
	
	private String from;
	private String to;
	private long sentDate;
	private MessageType type;
	private String gpsLocation;
	private String msgBody;


}
