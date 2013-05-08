package org.ttu_swri.goggles.ui;

import io.iron.ironmq.Client;
import io.iron.ironmq.Cloud;
import io.iron.ironmq.Message;
import io.iron.ironmq.Queue;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.reconinstruments.ReconSDK.ReconSDKManager;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.persistence.impl.UserPrefsDAO;

import edu.ttu.swri.messenger.AppContext;

public class TabViewActivity extends TabActivity {
    ReconSDKManager mDataManager   = ReconSDKManager.Initialize(this);

    private TabHost tabHost;
	private Gson gson = new Gson();
	private UserPrefsDAO userPrefsDao;
	private ListView mFriendlyList;
	private TextView mDistanceToFriendly;
	private TextView mHeadingToFriendly;
	private Type typeOfElementMates = new TypeToken<ArrayList<ElementMate>>(){}.getType();
	public static IntentFilter filter;
	InnerFriendUpdateReceiver receiver;
	private static String msgTag = "-im";
	
	SharedPreferences settings;    
    private ListView lv;
    private ListView lvf;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tabview);
 
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("Friendlies").setIndicator("Friendlies").setContent(R.id.layout_tab_friends));
        tabHost.addTab(tabHost.newTabSpec("Notifications").setIndicator("Notifications").setContent(R.id.layout_tab_messages));
        tabHost.addTab(tabHost.newTabSpec("Markers").setIndicator("Markers").setContent(R.id.layout_tab_pois));
 
        tabHost.setCurrentTab(0);
        mFriendlyList = (ListView) findViewById(R.id.friendly_list);
        mDistanceToFriendly = (TextView) findViewById(R.id.distance_to_friendly);
        mHeadingToFriendly = (TextView) findViewById(R.id.heading_to_friendly);
        
		userPrefsDao = new UserPrefsDAO(getBaseContext());
		settings = getSharedPreferences("prefs", 0);		
		filter = new IntentFilter();
		filter.addAction(AppContext.getFriendLocationAction());
		filter.addAction(AppContext.getFriendMessageAction());
		filter.addAction(AppContext.getUserLocationAction());        
		populateFriendlies();
    }
    
    @Override
    protected void onPause(){
    	stopAllServices();
		saveRelativeLocation();
		saveSpinnerLastSelected();
		saveButtonText();
    	super.onPause();
    }
    
    @Override
    protected void onResume(){
		setRelativeLocation();
		setSpinnerToLastSelected();
		setButtonText();			
    	super.onResume();
    }
    
	public void updateFriends(){
		//stopAllServices();
		//new GetFriendsAsync().execute("","","");	
	}
	
	public void startStopTracking(){
				startService(new Intent(this, edu.ttu.swri.messenger.FriendUpdateService.class)); 
				receiver = new InnerFriendUpdateReceiver();
				if(receiver != null){
				registerReceiver(receiver, filter);
		 }
	}

	private void setUpdatedFriendLocationText() {					
			try{
				ElementMate friendLocation = AppContext.getCurrentUserFriendLatestLocation();
				ElementMate myLocation = AppContext.getCurrentUserLatestLocation();

				if(friendLocation == null || myLocation == null || friendLocation.getLatitude() == 0 
						|| friendLocation.getLongitude() == 0 || myLocation.getLatitude() == 0
						|| myLocation.getLongitude() == 0){
					if(mDistanceToFriendly != null){
						mDistanceToFriendly.setText("N/A");		
						}					
					return;
				}

				float[] computedDistance= new float[3];
				Location.distanceBetween(AppContext.getCurrentUserLatestLocation().getLatitude(), 
						AppContext.getCurrentUserLatestLocation().getLongitude(), friendLocation.getLatitude()
						, friendLocation.getLongitude(), computedDistance);
				
				StringBuilder sb = new StringBuilder();
				if(computedDistance[0] > -1){
					String measure = computedDistance[0] < 800 ? " ft " : " mi ";
					sb.append(convertToFeet(computedDistance[0]) + measure);
				} else {
					sb.append("N/A");
				} if(mDistanceToFriendly != null){
				mDistanceToFriendly.setText(sb.toString());
				saveRelativeLocation();
				setRelativeLocation();		
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}   
	
	}
	
	
	public class SendMessagesAsync extends AsyncTask<String, String, String> {
	    @Override
	    protected String doInBackground(String... aurl) {
	        
	        try {
	    		// code to send messages to a certain queue
	    		// each user has their own queue, when we send message we send it with the user identifier (in this case just name like "Alex")
	        	// plus the msgTag to identify it's a text message, in this case it's "-im"
	    		Client client = new Client(AppContext.getProjectId(), AppContext.getToken(), Cloud.ironAWSUSEast);
	    		Queue queue = client.queue(aurl[1] + msgTag);
	    		queue.push(aurl[0]);
	           
	            } catch (Exception e) {}

	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(String unused) {           
	        Toast.makeText(getBaseContext(), "Message Sent to " + AppContext.getCurrrentUserFriend(), Toast.LENGTH_SHORT).show();
	    }	    

	}
	
	public class InnerFriendUpdateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals((AppContext.getFriendLocationAction())) || intent.getAction().equals((AppContext.getUserLocationAction())) ) {				
				setUpdatedFriendLocationText();
				saveRelativeLocation();
			} else if (intent.getAction().equals((AppContext.getFriendMessageAction()))) {	
				ElementMessage msg = gson.fromJson(intent.getExtras().getString(AppContext.getMessageBodyExtra()), ElementMessage.class);
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis(Long.valueOf(msg.getId()));
				Toast.makeText(getApplicationContext(), "New Message From " + msg.getFrom(), Toast.LENGTH_SHORT).show();
			}
		}

	}


	public void populateFriendlies(){
    	ArrayList<ElementMate> friends = (ArrayList<ElementMate>) userPrefsDao.getElementMates();
    	ArrayList<String> friendlies = new ArrayList<String>();
    	if(friends == null ||friends.isEmpty()){
    		friendlies.add("None Found");
    	} else {			
			if(receiver == null){
			receiver = new InnerFriendUpdateReceiver();
			registerReceiver(receiver, filter);   
			}
			startMessageUpdateService(); 
    	for(int i = 0; i < friends.size(); i++){
    		friendlies.add(friends.get(i).getName());
    	}
    		
    	}
    	//friendlies.add("Update List");
    	
        lv = (ListView) findViewById(R.id.friendly_list);
        lvf = (ListView) findViewById(R.id.friendly_list_messages);
        
        ArrayAdapter<String> arrayAdapter =      
        new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, friendlies);
        lv.setAdapter(arrayAdapter);   
        lvf.setAdapter(arrayAdapter);
        lv.setClickable(true);
        lvf.setClickable(true);
        
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

        	  @Override
        	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
        			AppContext.setCurrentUserFriend(lv.getItemAtPosition(position).toString());
                    startStopTracking();
        			//this.setConversation();
        			setRelativeLocation();
        	  }
        	});
        
        
        lvf.setOnItemClickListener(new AdapterView.OnItemClickListener() {

      	  @Override
      	  public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
      		  Toast.makeText(getApplicationContext(), "In Messages", Toast.LENGTH_SHORT).show();
      	  }
      	});
        
        lvf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

        	  @Override
        	  public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long arg3) {
        		  TextView tvMessages = (TextView) findViewById(R.id.tv_messages);
        		  tvMessages.setText("");
        		  if(!lvf.getSelectedItem().toString().contains("Found") && !lvf.getSelectedItem().toString().contains("Update")){
        		  AppContext.dao.getMessagesFrom(lvf.getSelectedItem().toString());
        		  }
        		  Toast.makeText(getApplicationContext(), "In Item Selected", Toast.LENGTH_SHORT).show();
        	  }
        	  
        	  @Override
        	  public void onNothingSelected(AdapterView<?> parent){
        		  Toast.makeText(getApplicationContext(), "Nothing Selected", Toast.LENGTH_SHORT).show();
        	  }
        	}); 
        
        
        
    }
	
	private void stopFriendLocationUpdateService(){
		stopService(new Intent(this, edu.ttu.swri.messenger.FriendUpdateService.class));
	}
	
	private void stopLocationUpdateService(){
		stopService(new Intent(this, edu.ttu.swri.messenger.location.LocationUpdateService.class));
	}
	
	private void stopMessageUpdateService(){
		stopService(new Intent(this, edu.ttu.swri.messenger.MessageUpdateService.class));
	}
	
	private void startMessageUpdateService(){
		startService(new Intent(this, edu.ttu.swri.messenger.MessageUpdateService.class));
	}
	
	private void stopAllServices(){
		try{
		stopLocationUpdateService();
		stopFriendLocationUpdateService();
		stopMessageUpdateService();
		if(receiver != null){
		unregisterReceiver(receiver);
		receiver = null;
		}	
		} catch (Exception e){
			e.printStackTrace();
		}
	}
	
	public class GetFriendsAsync extends AsyncTask<String, String, String> {	   
		@Override
	    protected String doInBackground(String... str) {
	    	 
			Client client = new Client(AppContext.getProjectId(), AppContext.getToken(), Cloud.ironAWSUSEast);
			Queue queue = client.queue(AppContext.swriFriendsQ);
			int queueSize = -1;
			Message msg = new Message();
			try {
				
				try{					
					queueSize = queue.getSize();		
				} catch (IOException ioe){
					if(ioe.getMessage().contains("Queue not found")){
						queue.push("build this queue");
						queue.clear();
						queueSize = 0;
					}
				}				
				
				ArrayList<ElementMate> friends = new ArrayList<ElementMate>();
				
				if(queueSize == 0){
					friends.add(new ElementMate(AppContext.getCurrentUserName(), AppContext.getCurrentUserName(), "TTU Student", null));
					queue.push(gson.toJson(friends));
					userPrefsDao.deleteAllMates();
					return null;
				}
				
				while(queueSize-- > 1){
				msg = queue.get();
				queue.deleteMessage(msg);
				} 
				
				
				msg = queue.get();
				String msgJson = msg.getBody();
				queue.deleteMessage(msg);
				friends = gson.fromJson(msgJson, typeOfElementMates);
				if(friends == null || friends.isEmpty()){
					friends = new ArrayList<ElementMate>();
				}
				boolean userIsAdded = false;
				int i = 0;
				for(ElementMate friend : friends){
					if(friend.getId().equals(AppContext.getCurrentUserName())){
						userIsAdded = true;
						friends.remove(i++);
						break;
					}
				}
				if(friends != null && !friends.isEmpty()){
				userPrefsDao.deleteAllMates();
				userPrefsDao.saveElementMates(friends);
				}
				if(!userIsAdded){
					friends.add(new ElementMate(AppContext.getCurrentUserName(), AppContext.getCurrentUserName(), "" , null));
					queue.push(gson.toJson(friends));	
				} else {
					queue.push(msgJson);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
	    }
	    
	    @Override
	    protected void onPostExecute(String string) { 
	        populateFriendlies();
			if(receiver == null){
			receiver = new InnerFriendUpdateReceiver();
			registerReceiver(receiver, filter);   
			}	        
	        startMessageUpdateService();
	        super.onPostExecute(string);
	    }	    

	}	
	
	public void clearMessages(View v){

	}

	private void saveRelativeLocation(){
		settings.edit().putString(AppContext.getCurrrentUserFriend() + "-relative-location", mDistanceToFriendly.getText().toString()).commit();			
	}

	
	public void setRelativeLocation(){
		String relativeLocationText = settings.getString(AppContext.getCurrrentUserFriend() + "-relative-location", "");
		mDistanceToFriendly.setText(relativeLocationText);		
	}
	
	public void saveSpinnerLastSelected(){
	}
	
	public void setSpinnerToLastSelected() {

	}
	
	public void saveButtonText(){
		
	}
	
	public void setButtonText(){

	}
	

	
	@SuppressLint("DefaultLocale")
	public String convertToFeet(float meters){
		//convert to feet
		float toFeet = (float) 3.28084;
		
		if(meters < 800){
			return String.format("%.2f", (meters * toFeet));
		}
		float toMiles = (float) .00062137;
		return String.format("%.2f", (meters * toMiles));
	}
	

}
