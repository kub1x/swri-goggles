/**
 * 
 */
package com.ttu_swri.goggles.network;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.content.Context;

import com.reconinstruments.webapi.SDKWebService;
import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.reconinstruments.webapi.WebRequestMessage.WebMethod;
import com.reconinstruments.webapi.WebRequestMessage.WebRequestBundle;

/**
 * @author kub1x
 * 
 */
public class NetworkHandler {
	// private static final String TAG = "NetworkHandler";

	// Singleton ==============================================================
	
	private static NetworkHandler nhinst;

	protected NetworkHandler() {
	}

	public static NetworkHandler getInstance() {
		if (NetworkHandler.nhinst == null)
			NetworkHandler.nhinst = new NetworkHandler();
		return NetworkHandler.nhinst;
	}
	
	// ========================================================================

	public void getJson(Context context, String url, WebResponseListener wrl) {
		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept", "application/json"));

		// Setting Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("callback","process"));

		get(context, url, header, params, wrl);
	}

	public void postJson(Context context, String url, WebResponseListener wrl) {
		// Setting HTTP Headers
		List<NameValuePair> header = new ArrayList<NameValuePair>();
		header.add(new BasicNameValuePair("Accept", "application/json"));

		// Setting Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		// params.add(new BasicNameValuePair("callback","process"));

		post(context, url, header, params, wrl);
	}

	public void get(Context context, String url, List<NameValuePair> header,
			List<NameValuePair> params, WebResponseListener wrl) {

		// Creating get request
		WebRequestBundle wrb = new WebRequestBundle(
				"com.ttu_swri.goggles.sandbox", url, WebMethod.GET, "1",
				header, params);

		send(context, wrb, wrl);
	}

	public void post(Context context, String url, List<NameValuePair> header,
			List<NameValuePair> params, WebResponseListener wrl) {

		// Creating get request
		WebRequestBundle wrb = new WebRequestBundle(
				"com.ttu_swri.goggles.sandbox", url, WebMethod.POST, "1",
				header, params);
		
		send(context, wrb, wrl);
	}

	public void send(Context context, WebRequestBundle wrb,
			WebResponseListener wrl) {
		SDKWebService.httpRequest(context, false, 0, wrb, wrl);
	}

}
