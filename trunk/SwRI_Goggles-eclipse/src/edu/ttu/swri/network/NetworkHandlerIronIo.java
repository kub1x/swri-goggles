package edu.ttu.swri.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.reconinstruments.webapi.SDKWebService;
import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.reconinstruments.webapi.WebRequestMessage.WebMethod;
import com.reconinstruments.webapi.WebRequestMessage.WebRequestBundle;

import edu.ttu.swri.data.model.Element;

/** @author kub1x */
public class NetworkHandlerIronIo {
	public static String TAG = "NetworkHandlerIronIo";

	// Singleton Design Pattern ===============================================

	private static NetworkHandlerIronIo nhiiInstance;

	public static NetworkHandlerIronIo getInstance() {
		if (NetworkHandlerIronIo.nhiiInstance == null)
			NetworkHandlerIronIo.nhiiInstance = new NetworkHandlerIronIo();
		return NetworkHandlerIronIo.nhiiInstance;
	}

	// ========================================================================

	private static String PROJECT_ID = "51661401ed3d7657b60014bc";
	private static String TOKEN = "zvS2csbud0tr6zgNbmjo9mSPByU";
	private static String QUEUE_IN_NAME = "goggles";
	private static String QUEUE_OUT_NAME = "test_user";

	private static String URL = "https://mq-aws-us-east-1.iron.io:443/1/projects/"
			+ PROJECT_ID + "/" + "queues";

	/**
	 * Method for posting updated or created elements Implementation of
	 * following command:
	 * 
	 * curl -i -H "Content-Type: application/json" -H
	 * "Authorization: OAuth {TOKEN}" -X POST -d '{"messages":[{"body":"hello
	 * world!"}]}'
	 * "https://mq-aws-us-east-1.iron.io:443/1/projects/{PROJECT_ID}/queues/test_queue/messages"
	 * 
	 * @param context
	 * @param element
	 * @param wrl
	 */
	public void post(Context context, Element element) {
		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Content-Type", "application/json"));
		header.add(new BasicNameValuePair("Authorization", "OAuth " + TOKEN));

		// Create message body in form:
		// {"messages":[{"body": ELEMENT_JSON_STRING }]}
		JSONObject messages = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			messages.put("messages", arr);
			JSONObject body = new JSONObject();
			body.put("body", element.toJson());
			arr.put(body);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		String url = URL + "/" + QUEUE_OUT_NAME + "/" + "messages";

		// Create request
		WebRequestBundle wrb = new WebRequestBundle("IntentFilterActionName",
				url, WebMethod.POST, "0", header, messages);

		// Create (empty) response listener
		WebResponseListener wrl = new WebResponseListener() {
			@Override
			public void onComplete(byte[] response, String arg1, String arg2,
					String arg3) {
				// DO NOTHING
			}

			@Override
			public void onComplete(String response, String arg1, String arg2,
					String arg3) {
				// DO NOTHING
				Log.d(TAG, "Response to POST: \n" + response);
			}
		};

		Log.d(TAG, "Sending: POST " + url + "\n" + messages.toString());

		// Execute Web request with listener
		this.send(context, wrb, wrl);
	}

	/**
	 * Method for getting elements from queue Implementation of following
	 * command:
	 * 
	 * curl -i -H "Content-Type: application/json" -H
	 * "Authorization: OAuth {TOKEN}"
	 * "http://mq-aws-us-east-1.iron.io/1/projects/{PROJECT_ID}/queues/test_queue/messages"
	 * 
	 * TODO WebResponseListener MUST handle JSON form of Element and work with
	 * it that way!!!
	 * 
	 * @param context
	 * @param element
	 * @param wrl
	 */
	public void get(Context context, WebResponseListener wrl) {
		Log.d(TAG, "Sending get request");

		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept", "application/json"));
		header.add(new BasicNameValuePair("Authorization", "OAuth " + TOKEN));

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		String url = URL + "/" + QUEUE_IN_NAME + "/" + "messages";
		
		WebRequestBundle wrb = new WebRequestBundle("IntentFilterActionName",
				url, WebMethod.GET, "1", header, params);

		Log.d(TAG, "Sending GET " + url);

		this.send(context, wrb, wrl);
	}

	public void delete(Context context, String msgId) {
		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept", "application/json"));
		header.add(new BasicNameValuePair("Authorization", "OAuth " + TOKEN));

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		String url = URL + "/" + QUEUE_IN_NAME + "/" + "messages" + "/" + msgId;

		WebRequestBundle wrb = new WebRequestBundle("IntentFilterActionName",
				url, WebMethod.DELETE, "2", header, params);

		// Create (empty) response listener
		WebResponseListener wrl = new WebResponseListener() {
			@Override
			public void onComplete(byte[] arg0, String arg1, String arg2,
					String arg3) {
				// DO NOTHING
			}

			@Override
			public void onComplete(String arg0, String arg1, String arg2,
					String arg3) {
				// DO NOTHING
				Log.d(TAG, "Response to Delete: \n" + arg0);
			}
		};

		Log.d(TAG, "Sending DELETE " + url);

		this.send(context, wrb, wrl);
	}

	public void send(Context context, WebRequestBundle wrb,
			WebResponseListener wrl) {
		synchronized (this) {
			SDKWebService.httpRequest(context, false, 0, wrb, wrl);
		}
	}
}
