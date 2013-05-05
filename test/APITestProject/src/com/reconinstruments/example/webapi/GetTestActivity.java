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
 *  Simple Example that shows the guideline for making GET HTTP REQUEST
 *  using ReconWebAPIs.
 *  
 *  <b>With GET, only following WebRequestBundle will work</b><br><br>
 *  
 *  WebRequestBundle(String callbackIntent, String URL, WebMethod method, String requestId, List headerNameValuePairs, List nameValuePairs) <br>
 * 
 *  @author Patrick Cho 
 *
 */

public class GetTestActivity extends Activity {
	private static final String TAG = "GetTestActivity";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept","application/json"));
		
		// Setting Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("callback","process"));
		
		WebRequestBundle wrb = new WebRequestBundle(
				"IntentFilterActionName",
				"http://openlibrary.org/authors/OL1A.json" ,
				WebMethod.GET,
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