package edu.ttu.swri.network;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.reconinstruments.webapi.SDKWebService.WebResponseListener;

import edu.ttu.swri.data.DataManager;
import edu.ttu.swri.data.model.Element;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

/** @author kub1x */
public class NetworkSyncService extends IntentService {
	private static final String TAG = "NetworkSyncService";
	public static final String NETWORK_SYNC_SERVICE = "com.ttu_swri.goggles.network.NETWORK_SYNC_SERVICE";

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
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		sync();
	}

	@Override
	public boolean stopService(Intent name) {
		return super.stopService(name);
	}

	// ------------------------------------------------------------------------

	void sync() {
		// Singletons can be final here and so reused in callback
		final NetworkHandlerIronIo nh = NetworkHandlerIronIo.getInstance();
		final DataManager dm = DataManager.getInstance();
		final List<String> ids = new ArrayList<String>();
		final List<String> elements = new ArrayList<String>();

		// Get new data from network
		nh.get(getApplicationContext(), new WebResponseListener() {
			@Override
			public void onComplete(String response, String statusCode,
					String statusId, String requestId) {
				try {
					// Parsing and storing synchronized data
					parseMessages(response, ids, elements);
					
					Log.d(TAG, "Response to GET: \n" + response);

				} catch (JSONException e) {
					Log.e(TAG, "Error parsing message from Iron.io");
					e.printStackTrace();
				}
			}

			@Override
			public void onComplete(byte[] response, String statusCode,
					String statusId, String requestId) {
				// Not used
			}

		});

		try {
			dm.updateFromNetwork(elements);
		} catch (JSONException e) {
			Log.e(TAG, "Error parsing elements obtainded through sync");
			e.printStackTrace();
		}
		
		// Update successful, send deletes
		Log.d(TAG, "Gonna Delete " + ids.size() + " messages");
		for (String id : ids)
			nh.delete(getApplicationContext(), id);

		// Send newly created or updated data
		for (Element el : dm.getElementsToSync())
			nh.post(getBaseContext(), el);
	}

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
		// Parsing message in format:
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
