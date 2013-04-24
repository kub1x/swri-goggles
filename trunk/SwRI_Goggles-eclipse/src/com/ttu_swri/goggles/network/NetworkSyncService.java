package com.ttu_swri.goggles.network;

import java.util.Timer;
import java.util.TimerTask;

import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.ttu_swri.goggles.DataManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/** @author kub1x */
public class NetworkSyncService extends Service {

	private Timer timer;

	public NetworkSyncService() {
		timer = new Timer();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		int delay = 0; // NOW
		int period = 1000; // 1sec
		timer.schedule(new NetworkSyncTimerTask(), delay, period);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	class NetworkSyncTimerTask extends TimerTask {

		@Override
		public void run() {
			// Singletons can be final here and so reused in callback
			final NetworkHandlerIronIo nh = NetworkHandlerIronIo
					.getInstance();
			final DataManager dm = DataManager.getInstance();

			// Get new data from network
			nh.get(getApplicationContext(), new WebResponseListener() {
				@Override
				public void onComplete(String response, String statusCode,
						String statusId, String requestId) {

					dm.updateFromNetwork(response);
				}

				@Override
				public void onComplete(byte[] response, String statusCode,
						String statusId, String requestId) {
					// Not used
				}

			});

			// Send newly created or updated data
			nh.post(getApplicationContext(), dm.getElementsToSync());

		}

	};

}
