package edu.ttu.swri.data.model;


public interface GogglesMessage {

	public String getFrom();
	public void setFrom(String from);
	public String getTo();
	public void setTo(String to);
	public long getSentDate();
	public void setSentDate(long sentDateinGMTMillis);
	public MessageType getType();
	public void setType(MessageType msgType);
	public String getGpsLocation();
	public void setGpsLocation(String gpsLocation);
	public String getMsgBody();
	public void setMsgBody(String msgBody);

	public static enum MessageType {IM, GPS};
}
