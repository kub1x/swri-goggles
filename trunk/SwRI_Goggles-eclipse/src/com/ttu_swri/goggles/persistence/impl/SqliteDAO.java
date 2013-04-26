package com.ttu_swri.goggles.persistence.impl;

import java.util.ArrayList;
import java.util.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.datamodel.ElementPoi;
import com.ttu_swri.goggles.persistence.ElementDAO;

public class SqliteDAO implements ElementDAO {
	
	private static final String POI_TABLE = "ELEMENT_POI";
	private static final String MESSAGE_TABLE = "ELEMENT_MESSAGE";
	private static final String MATE_TABLE = "ELEMENT_MATE";
	
	private static final String POI_ID = "_id";
	private static final String POI_NAME = "name";
	private static final String POI_DESCRIPTION = "description";
	private static final String POI_LOCATION = "location";
	private static final String POI_LAST_UPDATE = "last_update";
	private static final String POI_CHECKPOINT = "checkpoint";
	private static final String POI_EXPIRES = "expires";
	
	private static final String MESSAGE_ID = "_id";
	private static final String MESSAGE_TOPIC = "topic";
	private static final String MESSAGE_TEXT = "text";
	private static final String MESSAGE_IS_ALERT = "is_alert";
	private static final String MESSAGE_EXPIRES = "expires";
	private static final String MESSAGE_LAST_UPDATE = "last_update";
	private static final String MESSAGE_SENT_DATE = "sent_date";

	private static final String MATE_ID = "_id";
	private static final String MATE_LAST_UPDATE = "last_update";
	private static final String MATE_NAME = "name";
	private static final String MATE_LOCATION = "location";
	private static final String MATE_DESCRIPTION = "description";	
	
	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;
	
	public SqliteDAO(Context context){
		dbHelper = new DatabaseHelper(context);
	}
	
	  private void open() throws SQLException {
		    database = dbHelper.getWritableDatabase();
		  }
	  
	  private void close() {
		    dbHelper.close();
		  }	  

	@Override
	public String saveElementPoi(ElementPoi elementPoi) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(SqliteDAO.POI_CHECKPOINT, elementPoi.getCheckpointNumber());
		cv.put(SqliteDAO.POI_DESCRIPTION, elementPoi.getDescription());
		cv.put(SqliteDAO.POI_EXPIRES, elementPoi.getExpires() == null ? "" : elementPoi.getExpires().toGMTString());
		cv.put(SqliteDAO.POI_LAST_UPDATE, elementPoi.getLastUpdate() == null ? "" : elementPoi.getLastUpdate().toGMTString());
		cv.put(SqliteDAO.POI_LOCATION, locationToString(elementPoi.getLocation()));
		cv.put(SqliteDAO.POI_NAME, elementPoi.getName());
		long id = database.insertWithOnConflict(POI_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE); 
		close();
		return String.valueOf(id);
	}

	@Override
	public void saveElementPois(Collection<ElementPoi> elementPoiCollection) {
		if(elementPoiCollection == null || elementPoiCollection.isEmpty()){
			return;
		}
		for(ElementPoi poi : elementPoiCollection){
			saveElementPoi(poi);
		}

	}

	@Override
	public String saveElementMessage(ElementMessage elementMessage) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(SqliteDAO.MESSAGE_EXPIRES, elementMessage.getExpires().toGMTString());
		cv.put(SqliteDAO.MESSAGE_IS_ALERT, elementMessage.isAlert());
		cv.put(SqliteDAO.MESSAGE_LAST_UPDATE, elementMessage.getLastUpdate().toGMTString());
		//cv.put(SqliteDAO.MESSAGE_SENT_DATE, elementMessage.getSentDate().toGMTString());
		cv.put(SqliteDAO.MESSAGE_TEXT, elementMessage.getText());
		cv.put(SqliteDAO.MESSAGE_TOPIC, elementMessage.getTopic());
		long id = database.insertWithOnConflict(MESSAGE_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		close();
		return String.valueOf(id);
	}

	@Override
	public void saveElementMessages(Collection<ElementMessage> elementMessages) {
		if(elementMessages == null || elementMessages.isEmpty()){
			return;
		}
		for(ElementMessage msg : elementMessages){
			saveElementMessage(msg);
		}

	}

	@Override
	public String saveElementMate(ElementMate elementMate) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(SqliteDAO.MATE_DESCRIPTION, elementMate.getDescription());
		cv.put(SqliteDAO.MATE_LAST_UPDATE, elementMate.getLastUpdate().toGMTString());
		cv.put(SqliteDAO.MATE_LOCATION, locationToString(elementMate.getLocation()));
		cv.put(SqliteDAO.MATE_NAME, elementMate.getName());
		long id = database.insertWithOnConflict(MATE_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		close();
		return String.valueOf(id);

	}

	@Override
	public void saveElementMates(Collection<ElementMate> elementMates) {
		if(elementMates == null || elementMates.isEmpty()){
			return;
		}
		for(ElementMate mate : elementMates){
			saveElementMate(mate);
		}

	}

	@Override
	public ElementPoi getElementPoi(String id) {
		open();
		Cursor cursor = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = %s", POI_TABLE, POI_ID, id), null);
		if(cursor == null || cursor.getCount() < 1){
			close();
			return null;
		}
		cursor.moveToFirst();
		ElementPoi poi = new ElementPoi();
		poi.setCheckpointNumber(cursor.getInt(cursor.getColumnIndex(POI_CHECKPOINT)));
		poi.setDescription(cursor.getString(cursor.getColumnIndex(POI_DESCRIPTION)));
		poi.setExpires("".equals(cursor.getString(cursor.getColumnIndex(POI_EXPIRES))) ? null : new java.util.Date(cursor.getString(cursor.getColumnIndex(POI_EXPIRES))));
		poi.setLastUpdate("".equals(cursor.getString(cursor.getColumnIndex(POI_LAST_UPDATE))) ? null : new java.util.Date(cursor.getString(cursor.getColumnIndex(POI_LAST_UPDATE))));
		poi.setLocation(stringToLocation(cursor.getString(cursor.getColumnIndex(POI_LOCATION))));
		poi.setName(cursor.getString(cursor.getColumnIndex(POI_NAME)));
		close();		
		return poi;
	}

	@Override
	public Collection<ElementPoi> getElementPois() {
		open();
		Cursor cursor = this.database.rawQuery(String.format("SELECT * FROM %s", POI_TABLE), null);		
		if(cursor == null || cursor.getCount() < 1){
			close();
			return null;
		}		
		Collection<ElementPoi> poiColl = new ArrayList<ElementPoi>();
		cursor.moveToFirst();
		while(cursor.getCount() <= cursor.getPosition()){
			ElementPoi poi = new ElementPoi();
			poi.setCheckpointNumber(cursor.getInt(cursor.getColumnIndex(POI_CHECKPOINT)));
			poi.setDescription(cursor.getString(cursor.getColumnIndex(POI_DESCRIPTION)));
			poi.setExpires(new java.util.Date(cursor.getString(cursor.getColumnIndex(POI_EXPIRES))));
			poi.setLastUpdate(new java.util.Date(cursor.getString(cursor.getColumnIndex(POI_LAST_UPDATE))));
			poi.setLocation(stringToLocation(cursor.getString(cursor.getColumnIndex(POI_LOCATION))));
			poi.setName(cursor.getString(cursor.getColumnIndex(POI_NAME)));	
			poiColl.add(poi);
			cursor.moveToNext();
		}
		close();
		return poiColl;
	}

	@Override
	public ElementMessage getElementMessage(String id) {
		open();
		Cursor cursor = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = %s", MESSAGE_TABLE, MESSAGE_ID, id), null);
		if(cursor == null || cursor.getCount() < 1){
			close();
			return null;
		}
		cursor.moveToFirst();
		ElementMessage em = new ElementMessage();
		em.setAlert(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(MESSAGE_IS_ALERT))));
		em.setExpires(new java.util.Date(cursor.getString(cursor.getColumnIndex(MESSAGE_EXPIRES))));
		em.setLastUpdate(new java.util.Date(cursor.getString(cursor.getColumnIndex(MESSAGE_LAST_UPDATE))));
		em.setText(cursor.getString(cursor.getColumnIndex(MESSAGE_TEXT)));
		em.setTopic(cursor.getString(cursor.getColumnIndex(MESSAGE_TOPIC)));
		close();		
		return em;
	}

	@Override
	public Collection<ElementMessage> getElementMessages() {
		open();
		Cursor cursor = this.database.rawQuery(String.format("SELECT * FROM %s", POI_TABLE), null);		
		if(cursor == null || cursor.getCount() < 1){
			close();
			return null;
		}		
		Collection<ElementMessage> emColl = new ArrayList<ElementMessage>();
		cursor.moveToFirst();
		while(cursor.getCount() <= cursor.getPosition()){
			ElementMessage em = new ElementMessage();		
			em.setAlert(Boolean.valueOf(cursor.getString(cursor.getColumnIndex(MESSAGE_IS_ALERT))));
			em.setExpires(new java.util.Date(cursor.getString(cursor.getColumnIndex(MESSAGE_EXPIRES))));
			em.setLastUpdate(new java.util.Date(cursor.getString(cursor.getColumnIndex(MESSAGE_LAST_UPDATE))));
			em.setText(cursor.getString(cursor.getColumnIndex(MESSAGE_TEXT)));
			em.setTopic(cursor.getString(cursor.getColumnIndex(MESSAGE_TOPIC)));
			emColl.add(em);			
		}
		close();
		return emColl;
	}

	@Override
	public ElementMate getElementMate(String id) {
		open();
		Cursor cursor = this.database.rawQuery(String.format("SELECT * FROM %s WHERE %s = %s", MATE_TABLE, MATE_ID, id), null);
		if(cursor == null || cursor.getCount() < 1){
			close();
			return null;
		}
		cursor.moveToFirst();
		ElementMate em = new ElementMate();
		em.setLocation(stringToLocation(cursor.getString(cursor.getColumnIndex(MATE_LOCATION))));
		em.setLastUpdate(new java.util.Date(cursor.getString(cursor.getColumnIndex(MESSAGE_LAST_UPDATE))));
		em.setName(cursor.getString(cursor.getColumnIndex(MATE_NAME)));
		em.setDescription(cursor.getString(cursor.getColumnIndex(MATE_DESCRIPTION)));
		close();		
		return em;
	}

	@Override
	public Collection<ElementMate> getElementMates() {
		open();
		Cursor cursor = this.database.rawQuery(String.format("SELECT * FROM %s", MATE_TABLE), null);		
		if(cursor == null || cursor.getCount() < 1){
			close();
			return null;
		}		
		Collection<ElementMate> emColl = new ArrayList<ElementMate>();
		cursor.moveToFirst();
		while(cursor.getCount() <= cursor.getPosition()){	
			ElementMate em = new ElementMate();
			em.setLocation(stringToLocation(cursor.getString(cursor.getColumnIndex(MATE_LOCATION))));
			em.setLastUpdate(new java.util.Date(cursor.getString(cursor.getColumnIndex(MESSAGE_LAST_UPDATE))));
			em.setName(cursor.getString(cursor.getColumnIndex(MATE_NAME)));
			em.setDescription(cursor.getString(cursor.getColumnIndex(MATE_DESCRIPTION)));
			close();	
			emColl.add(em);			
		}
		close();
		return emColl;
	}
	
	private String locationToString(Location location){
		if (location == null){
			return "0,0";
		} else {
			return location.getLatitude() + "," + location.getLongitude();
		}		
	}
	
	private Location stringToLocation(String stringLocation){
		if(stringLocation == null || stringLocation.isEmpty() || stringLocation.indexOf(",") < 0){
			return null;
		}
		String[] latLong = stringLocation.split(",");
		Location location = new Location("");
		location.setLatitude(Double.parseDouble(latLong[0]));
		location.setLongitude(Double.parseDouble(latLong[1]));
		return location;	
	}	
	
	public class DatabaseHelper extends SQLiteOpenHelper {
		
		//private String DB_NAME = "GOGGLES_DB";
		
		//private int VERSION = 1;
		
		private static final String POI_TABLE_CREATE =
                "CREATE TABLE " + POI_TABLE + " (" +
                SqliteDAO.POI_ID + " integer primary key autoincrement, " +
                SqliteDAO.POI_NAME + " text, " +
                SqliteDAO.POI_DESCRIPTION + " text, " +
                SqliteDAO.POI_LOCATION + " text, " +
                SqliteDAO.POI_CHECKPOINT + " text, " +
                SqliteDAO.POI_LAST_UPDATE + " text, " +
                SqliteDAO.POI_EXPIRES + " text);";
		
		private static final String MESSAGE_TABLE_CREATE =
                "CREATE TABLE " + MESSAGE_TABLE + " (" +
                SqliteDAO.MESSAGE_ID + " integer primary key autoincrement, " +
                SqliteDAO.MESSAGE_TOPIC + " text, " +
                SqliteDAO.MESSAGE_TEXT + " text, " +
                SqliteDAO.MESSAGE_SENT_DATE + " text, " +
                SqliteDAO.MESSAGE_EXPIRES + " text, " +
                SqliteDAO.MESSAGE_LAST_UPDATE + " text, " +
                SqliteDAO.MESSAGE_IS_ALERT + " text);";		
		
		private static final String MATE_TABLE_CREATE =
                "CREATE TABLE " + MATE_TABLE + " (" +
                SqliteDAO.MATE_ID + " integer primary key autoincrement, " +
                SqliteDAO.MATE_LAST_UPDATE + " text, " +
                SqliteDAO.MATE_NAME + " text, " +
                SqliteDAO.MATE_LOCATION + " text, " +
                SqliteDAO.MATE_DESCRIPTION + " text);";	
		
		private DatabaseHelper(Context context){
			super(context, "GOGGLES_DB", null, 1);			
		}

		private DatabaseHelper(Context context, String name,
				CursorFactory factory, int version) {			
			super(context, name, factory, version);				
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(POI_TABLE_CREATE);
			db.execSQL(MESSAGE_TABLE_CREATE);
			db.execSQL(MATE_TABLE_CREATE);			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			
		} 
		
		
	}	

}
