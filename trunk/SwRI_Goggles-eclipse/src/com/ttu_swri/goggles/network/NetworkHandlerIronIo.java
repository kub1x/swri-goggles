package com.ttu_swri.goggles.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.reconinstruments.webapi.WebRequestMessage.WebMethod;
import com.reconinstruments.webapi.WebRequestMessage.WebRequestBundle;
import com.ttu_swri.datamodel.Element;

/** @author kub1x */
public class NetworkHandlerIronIo {
	public static String TAG = "NetworkHandlerIronIo";

	// Singleton Design Pattern ===============================================

	private static NetworkHandlerIronIo nhiiInstance;

	private NetworkHandler nh;

	protected NetworkHandlerIronIo() {
		this.nh = NetworkHandler.getInstance();
	}

	public static NetworkHandlerIronIo getInstance() {
		if (NetworkHandlerIronIo.nhiiInstance == null)
			NetworkHandlerIronIo.nhiiInstance = new NetworkHandlerIronIo();
		return NetworkHandlerIronIo.nhiiInstance;
	}

	// ========================================================================

	private static String PROJECT_ID = "51661401ed3d7657b60014bc";
	private static String TOKEN = "zvS2csbud0tr6zgNbmjo9mSPByU";
	private static String URL = "https://mq-aws-us-east-1.iron.io:443/1/projects/"
			+ PROJECT_ID + "/queues/test_queue/messages";

	/**
	 * Method for posting updated or created elements Implementation of
	 * following command:
	 * 
	 * curl -i -H "Content-Type: application/json" -H
	 * "Authorization: OAuth {TOKEN}" -X POST -d '{"messages":[{"body":"hello
	 * world!"}]}'
	 * "http://mq-aws-us-east-1.iron.io/1/projects/{PROJECT_ID}/queues/test_queue/messages"
	 * 
	 * @param context
	 * @param element
	 * @param wrl
	 */
	public void post(Context context, List<Element> elements) {
		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept", "application/json"));
		header.add(new BasicNameValuePair("Authorization", "OAuth " + TOKEN));

		// Create message body in form:
		// {"messages":[{"body": ELEMENT_JSON_OBJECT }]}
		JSONObject messages = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			messages.put("messages", arr);
			for (Element element : elements) {
				JSONObject body = new JSONObject();
				body.put("body", element.toJson());
				arr.put(body);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		// Create request
		WebRequestBundle wrb = new WebRequestBundle(
				"com.ttu_swri.goggles.sandbox", URL, WebMethod.POST, "1",
				header, messages);

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
				// TODO might do repsonse check here
			}
		};

		// Execute Web request with listener
		this.nh.send(context, wrb, wrl);
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
		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept", "application/json"));
		header.add(new BasicNameValuePair("Authorization", "OAuth " + TOKEN));

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		WebRequestBundle wrb = new WebRequestBundle(
				"com.ttu_swri.goggles.sandbox", URL, WebMethod.GET, "0",
				header, params);

		this.nh.send(context, wrb, wrl);
	}

}