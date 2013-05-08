package edu.ttu.swri.messenger.location;

import io.iron.ironmq.Client;
import io.iron.ironmq.Cloud;
import io.iron.ironmq.Queue;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;

import com.google.gson.Gson;
import com.ttu_swri.datamodel.ElementMate;

import edu.ttu.swri.messenger.AppContext;

public class LocationUpdateService extends Service {
	
	SharedPreferences settings;
	LocationManager lm;
	LocationListener locationListener;
	Gson gson;
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		gson = new Gson();
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		locationListener = new LocationListener() {
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
			}

			@Override
			public void onProviderEnabled(String provider) {
			}

			@Override
			public void onProviderDisabled(String provider) {
			}

			/**
			 * Here we update the current location to the latest known value
			 */
			@Override
			public void onLocationChanged(Location location) {
				ElementMate elementMate = new ElementMate(AppContext.getCurrentUserName(), AppContext.getCurrentUserName(), "", location);
				AppContext.dao.saveElementMate(elementMate);
				AppContext.setCurrentUserLatestLocation(elementMate);
				Intent intent = new Intent();
				intent.setAction((AppContext.getUserLocationAction()));
				sendBroadcast(intent);
				String serializedLocation = gson.toJson(elementMate); 
					new SendLocationUpdateAsync().execute(serializedLocation, AppContext.getCurrentUserName());
			}
			
			
					
			
		};		
		
		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 3, locationListener);
		return super.onStartCommand(intent, flags, startId);
		
	}
	
	public void sendLocationUpdate(){
		gson = new Gson();
		new SendLocationUpdateAsync().execute(gson.toJson(AppContext.getCurrentUserLatestLocation()), AppContext.getCurrentUserName());
	}
	
	/**
	 * 
	 * @author rochaa1
	 *
	 */
	final class SendLocationUpdateAsync extends AsyncTask<String, String, String> {
	    @Override
	    protected String doInBackground(String... vars) {
	        int queueSize = 0;
	        try {
	    		Client client = new Client(AppContext.getProjectId(), AppContext.getToken(), Cloud.ironAWSUSEast);
	    		Queue queue = client.queue(vars[1] + "-location");
				try{
					
					queueSize = queue.getSize();		
				} catch (IOException ioe){
					if(ioe.getMessage().contains("Queue not found")){
						queue.push("build this queue");
						queue.clear();
					}
				}	    		
	    		if(queueSize > 0){
	    			queue.clear();
	    		}
	    		queue.push(vars[0]);			           
	            } catch (Exception e) {
	            	// error sending or receiving; likely network related, best to let it go...
	            }

	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(String unused) {           
	    }	    

	}	
	
	@Override
	public void onDestroy(){
		if(locationListener != null){		
		lm.removeUpdates(locationListener);
		}
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	


}
