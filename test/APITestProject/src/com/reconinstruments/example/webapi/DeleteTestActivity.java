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
 *  Simple Example that shows the guideline for making DELETE HTTP REQUEST
 *  using ReconWebAPIs.
 * 
 *  @author Patrick Cho
 *
 */

public class DeleteTestActivity extends Activity {
	private static final String TAG = "DeleteTestActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Content-Type","application/json"));
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("exampleKey", "exampleValue"));
		
		
		WebRequestBundle wrb = new WebRequestBundle(
				"IntentFilterActionName",
				"http://PutYourURLHere.com/delete" ,
				WebMethod.DELETE,
				"1",
				header,
				params
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