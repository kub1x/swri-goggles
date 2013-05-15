package edu.ttu.swri.goggles.position;

import edu.ttu.swri.data.DataManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;

public class MyPositionUpdateService extends Service {

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		
		LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final DataManager dm = DataManager.getInstance();
		
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
				new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						dm.setMyLocation(location);
					}
				});
    	
		super.onCreate();
	}
		
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
