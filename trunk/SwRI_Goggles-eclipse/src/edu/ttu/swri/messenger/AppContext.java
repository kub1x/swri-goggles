package edu.ttu.swri.messenger;

import java.util.ArrayList;

import android.app.Application;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;

import com.google.gson.Gson;
import com.ttu_swri.goggles.R;

import edu.ttu.swri.data.model.ElementMate;
import edu.ttu.swri.data.model.ElementPoi;
import edu.ttu.swri.goggles.persistence.ElementDAO;
import edu.ttu.swri.goggles.persistence.impl.UserPrefsDAO;

public class AppContext extends Application {

	public static UserPrefsDAO dao;
	private static final String friendLocationAction = "FRIEND_UPDATE";
	private static final String friendMessageAction = "FRIEND_MESSAGE";
	private static final String userLocationAction = "USER_LOCATION_CHANGED";
	private static final String messageBodyExtra = "MESSAGE_BODY";
	
	private static String current_user_name;
	private static String currrent_user_friend;
	private static String projectId;
	private static String token;
	
	private static String poiName = "custom_poi";
	public static final String swriFriendsQ = "SWRI_FRIENDS";
	private static String uniqueDeviceId;
	
	private static ElementMate currentUserLatestLocation = null;
	private static ElementMate currentUserFriendLatestLocation = null;
	private static ElementPoi poi = null;
	
	
	private static Gson gson = new Gson();
	
	private static SharedPreferences settings = null;
	
	@Override
	public void onCreate(){
		dao = new UserPrefsDAO(getApplicationContext());
		dao.deleteAllMates();		
		projectId =  getResources().getString(R.string.project_id);
		token =  getResources().getString(R.string.token);
		settings = getSharedPreferences("prefs", 0);
		AppContext.current_user_name = "GogglesDemo";//settings.getString("selectedUser", "Goggles_UI");
		//AppContext.currrent_user_friend = getResources().getString(R.string.current_user_friend);
		
		String locationString = settings.getString(AppContext.current_user_name, "{\"description\":\"\",\"name\":\"Alex\",\"longitude\":-98.311491,\"latitude\":26.23413147,\"id\":\"cb08ba625f0009c6\",\"lastUpdate\":\"May 7, 2013 4:27:40 AM\",\"type\":\"T_MATE\"}");
		if(locationString != null){
			AppContext.currentUserLatestLocation = gson.fromJson(locationString, ElementMate.class);
		}
		
		locationString = settings.getString(AppContext.currrent_user_friend, null);
		if(locationString != null){
			AppContext.currentUserFriendLatestLocation = gson.fromJson(locationString, ElementMate.class);
		}
		
		locationString = settings.getString(AppContext.poiName, null);		
		if(locationString != null){
			AppContext.poi = gson.fromJson(locationString, ElementPoi.class);
		}
		uniqueDeviceId =  Secure.getString(getApplicationContext().getContentResolver(),
	            Secure.ANDROID_ID) + "x"; 
		dao.saveElementMates(getFriendlies());
		super.onCreate();
	}
	
	public static String getUniqueDeviceId(){
		return uniqueDeviceId;
	}
	
	public static String getCurrentUserName() {
		return current_user_name;
	}
	
	public static void setCurrentUserName(String username) {
		current_user_name = username;
	}	

	public static String getCurrrentUserFriend() {
		return currrent_user_friend;
	}
	
	public static void setCurrentUserFriend(String currentFriend) {
		currrent_user_friend = currentFriend;
	}

	public static ElementMate getCurrentUserLatestLocation() {
		return currentUserLatestLocation;
	}

	public static ElementMate getCurrentUserFriendLatestLocation() {
		return currentUserFriendLatestLocation;
	}
	
	public static ElementPoi getPoi() {		
		return AppContext.poi;
	}	

	public static void setCurrentUserLatestLocation(
			ElementMate currentUserLatestLocation) {
		AppContext.currentUserLatestLocation = currentUserLatestLocation;
		settings.edit().putString(AppContext.current_user_name, gson.toJson(currentUserLatestLocation));
		
	}

	public static void setCurrentUserFriendLatestLocation(
			ElementMate currentUserFriendLatestLocation) {
		AppContext.currentUserFriendLatestLocation = currentUserFriendLatestLocation;
		settings.edit().putString(AppContext.currrent_user_friend, gson.toJson(currentUserFriendLatestLocation));		
	}

	public static void setPoi(ElementPoi poi) {
		AppContext.poi = poi;
		settings.edit().putString(AppContext.poiName, gson.toJson(poi));
	}

	public static String getProjectId() {
		return projectId;
	}

	public static String getToken() {
		return token;
	}

	public static String getFriendLocationAction() {
		return friendLocationAction;
	}

	public static String getFriendMessageAction() {
		return friendMessageAction;
	}

	public static String getUserLocationAction() {
		return userLocationAction;
	}

	public static String getMessageBodyExtra() {
		return messageBodyExtra;
	}
	
	private ArrayList<ElementMate> getFriendlies(){
		ArrayList<ElementMate> matesArray = new ArrayList<ElementMate>();
		ElementMate mate = new ElementMate("Alex", "");
		matesArray.add(mate);
		mate = new ElementMate("Jakub", "");
		matesArray.add(mate);
		mate = new ElementMate("Sebastian", "");
		matesArray.add(mate);		
		mate = new ElementMate("Aotuo", "");
		matesArray.add(mate);		
		return matesArray;
	}	


}
