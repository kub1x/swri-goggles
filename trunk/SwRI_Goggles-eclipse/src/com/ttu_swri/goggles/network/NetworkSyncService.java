package com.ttu_swri.goggles.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.ttu_swri.goggles.DataManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/** @author kub1x */
public class NetworkSyncService extends Service {
	private final String TAG = "NetworkSyncService";

	private Timer timer;

	public NetworkSyncService() {
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		timer = new Timer();
		int delay = 0; // NOW
		int period = 2000; // 2sec
		timer.schedule(new NetworkSyncTimerTask(), delay, period);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		timer.cancel();
		// TODO Auto-generated method stub
		super.onDestroy();
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
			final NetworkHandlerIronIo nh = NetworkHandlerIronIo.getInstance();
			final DataManager dm = DataManager.getInstance();

			// Get new data from network
			nh.get(getApplicationContext(), new WebResponseListener() {
				@Override
				public void onComplete(String response, String statusCode,
						String statusId, String requestId) {
					try {
						Log.d(TAG, "Syncing...");

						List<String> ids = new ArrayList<String>();
						List<String> elements = new ArrayList<String>();

						parseMessages(response, ids, elements);

						dm.updateFromNetwork(elements);

						// Update successfull, send deletes
						for (String id : ids) {
							nh.delete(getApplicationContext(), id);
						}
					} catch (JSONException e) {
						e.printStackTrace();
					}
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

	public static void parseMessages(String json, List<String> ids,
			List<String> elements) throws JSONException {
		// {"messages":[{"id":"5824513078343549739","body":"hello","timeout":60}]}

		// Parse the body of message
		JSONObject oMsgs;
		oMsgs = new JSONObject(json);
		// Get array of all messages
		JSONArray aMsgs = oMsgs.getJSONArray("messages");
		// For each message in array
		for (int i = 0; i < aMsgs.length(); i++) {
			// Get it's content
			JSONObject oMesg = aMsgs.getJSONObject(i);

			// Get id of message and delete it
			String msg_id = oMesg.getString("id");
			ids.add(msg_id);

			// We want only body of the message
			String oElem = oMesg.getString("body");

			// The body is our original element.. parse it!
			elements.add(oElem);
		}
	}
}
