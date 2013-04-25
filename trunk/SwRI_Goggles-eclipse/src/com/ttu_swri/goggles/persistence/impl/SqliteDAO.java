package com.ttu_swri.goggles.persistence.impl;

import java.util.Collection;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

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
	public void saveElementPoi(ElementPoi elementPoi) {
		open();
		ContentValues cv = new ContentValues();
		cv.put(SqliteDAO.POI_CHECKPOINT, elementPoi.getCheckpointNumber());
		cv.put(SqliteDAO.POI_DESCRIPTION, elementPoi.getDescription());
		//cv.put(SqliteDAO.POI_EXPIRES, "");
		//cv.put(SqliteDAO.POI_LOCATION, elementPoi.getLocation().getLatitude()+ ";" + elementPoi.getLocation().getLongitude());
		cv.put(SqliteDAO.POI_NAME, elementPoi.getName());
		database.insertWithOnConflict(POI_TABLE, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
		close();
	}

	@Override
	public void saveElementPois(Collection<ElementPoi> elementPoiCollection) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveElementMessage(ElementMessage elementMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveElementMessages(Collection<ElementMessage> elementMessage) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveElementMate(ElementMate elementMate) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveElementMates(Collection<ElementMate> elementMate) {
		// TODO Auto-generated method stub

	}

	@Override
	public ElementPoi getElementPoi(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ElementPoi> getElementPois() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementMessage getElementMessage(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ElementMessage> getElementMessages() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ElementMate getElementMate(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<ElementMate> getElementMates() {
		// TODO Auto-generated method stub
		return null;
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
                SqliteDAO.POI_EXPIRES + " integer);";
		
		private static final String MESSAGE_TABLE_CREATE =
                "CREATE TABLE " + MESSAGE_TABLE + " (" +
                SqliteDAO.MESSAGE_ID + " integer primary key autoincrement, " +
                SqliteDAO.MESSAGE_TOPIC + " text, " +
                SqliteDAO.MESSAGE_TEXT + " text, " +
                SqliteDAO.MESSAGE_SENT_DATE + " integer, " +
                SqliteDAO.MESSAGE_EXPIRES + " integer, " +
                SqliteDAO.MESSAGE_LAST_UPDATE + " integer, " +
                SqliteDAO.MESSAGE_IS_ALERT + " integer);";		
		
		private static final String MATE_TABLE_CREATE =
                "CREATE TABLE " + MATE_TABLE + " (" +
                SqliteDAO.MATE_ID + " integer primary key autoincrement, " +
                SqliteDAO.MATE_LAST_UPDATE + " integer, " +
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
