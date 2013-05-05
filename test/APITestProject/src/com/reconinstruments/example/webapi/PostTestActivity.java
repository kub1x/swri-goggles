package com.reconinstruments.example.webapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.reconinstruments.example.api.R;
import com.reconinstruments.webapi.SDKWebService;
import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.reconinstruments.webapi.WebRequestMessage.WebMethod;
import com.reconinstruments.webapi.WebRequestMessage.WebRequestBundle;

/**
 * 
 * Simple Example that shows the guideline for making POST HTTP REQUEST using
 * ReconWebAPIs.
 * 
 * used following website to test POST REQUEST
 * 
 * http://postcatcher.in/
 * 
 * @author Patrick Cho
 * 
 */

public class PostTestActivity extends Activity {
	private static final String TAG = "PostTestActivity";

	private static String PROJECT_ID = "51661401ed3d7657b60014bc";
	private static String TOKEN = "zvS2csbud0tr6zgNbmjo9mSPByU";
	private static String QUEUE_NAME = "goggles";
	// private static String QUEUE_OUT_NAME = "test_user";

	private static String URL = "https://mq-aws-us-east-1.iron.io:443/1/projects/"
			+ PROJECT_ID + "/queues/" + QUEUE_NAME + "/messages";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Content-Type", "application/json"));
		header.add(new BasicNameValuePair("Authorization", "OAuth " + TOKEN));

		// Create a JSONObject for data entity
		JSONObject messages = new JSONObject();
		JSONArray arr = new JSONArray();
		try {
			messages.put("messages", arr);
			JSONObject body = new JSONObject();
			body.put("body", "TEST");
			arr.put(body);
		} catch (JSONException e) {
			e.printStackTrace();
		}

		WebRequestBundle wrb = new WebRequestBundle("IntentFilterActionName",
				URL, WebMethod.POST, "1", header, messages);

		Log.d(TAG, "SENDING DATA!");

		SDKWebService.httpRequest(getBaseContext(), false, 0, wrb,
				new WebResponseListener() {

					@Override
					public void onComplete(byte[] response, String statusCode,
							String statusId, String requestId) {
						// DO NOTHING
					}

					@Override
					public void onComplete(String response, String statusCode,
							String statusId, String requestId) {
						Log.d(TAG, "onComplete String content    : " + response);
					}

				});

	}
}
