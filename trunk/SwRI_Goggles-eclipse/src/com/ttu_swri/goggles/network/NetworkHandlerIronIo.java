package com.ttu_swri.goggles.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

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
	 * Implementation of following command: 
	 *
	 * curl -i 
	 * -H "Content-Type: application/json"
	 * -H "Authorization: OAuth {TOKEN}" 
	 * -X POST -d
	 * '{"messages":[{"body":"hello world!"}]}'
	 * "http://mq-aws-us-east-1.iron.io/1/projects/{PROJECT_ID}/queues/test_queue/messages"
	 * 
	 * @param context
	 * @param element
	 * @param wrl
	 */
	public void post(Context context, Element element, WebResponseListener wrl) {
		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept", "application/json"));
		header.add(new BasicNameValuePair("Authorization", "OAuth " + TOKEN));

		Log.d(TAG, element.toJson().toString());

		JSONObject body = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			body.put("messages", arr);
			arr.put(element.toJson());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		WebRequestBundle wrb = new WebRequestBundle(
				"com.ttu_swri.goggles.sandbox", URL, WebMethod.POST, "1",
				header, body);

		this.nh.send(context, wrb, wrl);
	}
	
}
