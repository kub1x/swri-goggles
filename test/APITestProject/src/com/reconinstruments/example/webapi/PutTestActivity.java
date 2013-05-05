package com.reconinstruments.example.webapi;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
 *  Simple Example that shows the guideline for making PUT HTTP REQUEST
 *  using ReconWebAPIs.
 *  
 * 
 *  @author Patrick Cho
 *
 */

public class PutTestActivity extends Activity {
	private static final String TAG = "PutTestActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Content-Type","application/json"));

		String data = "{'json':'data', 'can':'go' , 'here':'string'";

		WebRequestBundle wrb = new WebRequestBundle(
				"IntentFilterActionName",
				"http://yoursite.com" ,
				WebMethod.PUT,
				"1",
				header,
				data
				);

		SDKWebService.httpRequest(getBaseContext(), false, 0, wrb, new WebResponseListener(){

			@Override
			public void onComplete(byte[] response, String statusCode, String statusId, String requestId) {
				Log.d(TAG, "onComplete byte[] statusCode : " + statusCode);
				Log.d(TAG, "onComplete byte[] statusId   : " + statusCode);
				Log.d(TAG, "onComplete byte[] requestId  : " + statusCode);
				Log.d(TAG, "onComplete byte[] content    : " + new String(response));
			}

			@Override
			public void onComplete(String response, String statusCode, String statusId, String requestId) {
				Log.d(TAG, "onComplete String content    : " + response);
			}
			
		});


	}
}