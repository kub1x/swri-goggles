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

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/** @author kub1x */
public class NetworkSyncService extends IntentService {
	private static final String TAG = "NetworkSyncService";
	public static final String NETWORK_SYNC_SERVICE = "com.ttu_swri.goggles.network.NETWORK_SYNC_SERVICE";
	private Timer timer;
	private NetworkSyncTimerTask task;

	/**
	 * This constructor avoids java.lang.InstantiationException see:
	 * http://stackoverflow
	 * .com/questions/6176255/why-do-i-get-an-instantiationexception
	 * -if-i-try-to-start-a-service
	 */
	public NetworkSyncService() {
		this(".network.NetworkSyncService");
	}

	public NetworkSyncService(String name) {
		super(name);
		timer = new Timer();
		task = new NetworkSyncTimerTask();
	}

	// @Override
	// public void onCreate() {
	// }

	@Override
	protected void onHandleIntent(Intent intent) {
		int delay = 0; // NOW
		int period = 2000; // 5 sec
		timer.schedule(task, delay, period);
	}

	@Override
	public boolean stopService(Intent name) {
		timer.cancel();
		task.cancel();
		return super.stopService(name);
	}

	// ------------------------------------------------------------------------

	class NetworkSyncTimerTask extends TimerTask {

		@Override
		public void run() {
			// Singletons can be final here and so reused in callback
			final NetworkHandlerIronIo nh = NetworkHandlerIronIo.getInstance();
			final DataManager dm = DataManager.getInstance();

			// Get new data from network
			// nh.get(getApplicationContext(), new WebResponseListener() {
			// @Override
			// public void onComplete(String response, String statusCode,
			// String statusId, String requestId) {
			// try {
			// Log.d(TAG, "Syncing...");
			//
			// List<String> ids = new ArrayList<String>();
			// List<String> elements = new ArrayList<String>();
			//
			// parseMessages(response, ids, elements);
			//
			// dm.updateFromNetwork(elements);
			//
			// // Update successfull, send deletes
			// for (String id : ids) {
			// nh.delete(getApplicationContext(), id);
			// }
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }
			// }
			//
			// @Override
			// public void onComplete(byte[] response, String statusCode,
			// String statusId, String requestId) {
			// // Not used
			// }
			//
			// });

			// Send newly created or updated data
			nh.post(getBaseContext(), dm.getElementsToSync());

		}

	};

	/**
	 * 
	 * @param json
	 * @param ids
	 * @param elements
	 * @throws JSONException
	 *             Every possible error in JSON here means we have wrong message
	 *             format and is therefore critical.
	 */
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
