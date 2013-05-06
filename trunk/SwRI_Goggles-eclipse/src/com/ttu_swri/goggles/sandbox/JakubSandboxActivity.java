package com.ttu_swri.goggles.sandbox;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.reconinstruments.webapi.SDKWebService;
import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.reconinstruments.webapi.WebRequestMessage.WebMethod;
import com.reconinstruments.webapi.WebRequestMessage.WebRequestBundle;
import com.ttu_swri.datamodel.Element;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.datamodel.ElementPoi;
import com.ttu_swri.datamodel.IVisualizer;
import com.ttu_swri.goggles.DataManager;
import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.network.NetworkSyncService;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class JakubSandboxActivity extends Activity {
	private static final String TAG = "JakubSandboxActivity";

	private ButtonVisualizer bv;

	private static String PROJECT_ID = "51661401ed3d7657b60014bc";
	private static String TOKEN = "zvS2csbud0tr6zgNbmjo9mSPByU";
	private static String QUEUE_NAME = "goggles";
	// private static String QUEUE_OUT_NAME = "test_user";

	private static String URL = "https://mq-aws-us-east-1.iron.io:443/1/projects/"
			+ PROJECT_ID + "/queues/" + QUEUE_NAME + "/messages";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jakub_sandbox);

		// ((Button) findViewById(R.id.j_sand_but_start))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Log.d(TAG, "start click");
		// startService(new Intent(
		// NetworkSyncService.NETWORK_SYNC_SERVICE));
		// // startService(new Intent(getApplicationContext(),
		// // NetworkSyncService.class));
		// }
		// });
		//
		// ((Button) findViewById(R.id.j_sand_but_stop))
		// .setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// Log.d(TAG, "stop click");
		// stopService(new Intent(
		// NetworkSyncService.NETWORK_SYNC_SERVICE));
		// // stopService(new Intent(getApplicationContext(),
		// // NetworkSyncService.class));
		// }
		// });

		// Visualizer
		bv = new ButtonVisualizer(
				(LinearLayout) findViewById(R.id.j_sand_datalist));
		DataManager dm = DataManager.getInstance();
		dm.register(bv);

		Element el = new ElementMessage("test", "testing message for engage");
		// Example data for testing POST
		dm.update(el);

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
			body.put("body", el.toJson());
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

	// @Override
	// protected void onResume() {
	// super.onResume();
	// startService(new Intent(NetworkSyncService.NETWORK_SYNC_SERVICE));
	// }
	//
	// @Override
	// protected void onPause() {
	// super.onPause();
	// stopService(new Intent(NetworkSyncService.NETWORK_SYNC_SERVICE));
	// }

	// ==============================================================================
	// ==============================================================================
	// ==============================================================================

	// private void startGps() {
	// // Start GPS service
	// // startService(new Intent(this, MyPositionUpdateService.class));
	//
	// LocationManager lm = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
	// new LocationListener() {
	// DecimalFormat df = new DecimalFormat("#.000000");
	//
	// @Override
	// public void onStatusChanged(String provider, int status,
	// Bundle extras) {
	// // TODO Auto-generated method stub
	// }
	//
	// @Override
	// public void onProviderEnabled(String provider) {
	// // TODO Auto-generated method stub
	// }
	//
	// @Override
	// public void onProviderDisabled(String provider) {
	// // TODO Auto-generated method stub
	// }
	//
	// @Override
	// public void onLocationChanged(Location location) {
	// TextView tv = (TextView) findViewById(R.id.j_sand_location);
	// String text = "Loc: "
	// + df.format(location.getLatitude()) + ", "
	// + df.format(location.getLongitude());
	//
	// tv.setText(text);
	// }
	// });
	//
	// }

	private class ButtonVisualizer implements IVisualizer {

		DataManager dm;
		LinearLayout ll;

		public ButtonVisualizer(LinearLayout ll) {
			this.dm = DataManager.getInstance();
			this.ll = ll;
		}

		@Override
		public void update() {
			ll.removeAllViews();
			for (Element el : this.dm.getElements()) {
				MyButton but = new MyButton(getBaseContext(), el);
				ll.addView(but);
			}
		}

		private class MyButton extends Button {

			DecimalFormat df = new DecimalFormat("#.0000");
			// private Element element;
			String location = "";

			private Paint m_paint = new Paint();

			// private int m_color = 0XFF92C84D; // LIKE AN OLIVE GREEN..

			public MyButton(Context context, Element element) {
				super(context);

				// this.element = element;

				Location loc;
				switch (element.getType()) {
				case T_MATE:
					setText(((ElementMate) element).getName());
					loc = ((ElementMate) element).getLocation();
					double lat = loc.getLatitude();
					double lon = loc.getLongitude();
					location = "Loc: " + df.format(lat) + ", " + df.format(lon);
					break;
				case T_POI:
					setText(((ElementPoi) element).getName());
					loc = ((ElementPoi) element).getLocation();
					location = df.format(loc.getLatitude()) + "\n"
							+ df.format(loc.getLongitude());
					break;
				case T_MESSAGE:
					setText(((ElementMessage) element).getTopic());
					break;
				default:
				}

				this.setOnClickListener((OnClickListener) new MyOnClickListener(
						element));
			}

			@Override
			protected void onDraw(Canvas canvas) {
				super.onDraw(canvas);
				m_paint.setColor(Color.DKGRAY);
				canvas.drawText(location, 5, 15, m_paint);
			}

			class MyOnClickListener implements OnClickListener {
				Element element;

				public MyOnClickListener(Element element) {
					this.element = element;
				}

				@Override
				public void onClick(View arg0) {
					switch (element.getType()) {
					case T_MATE:
						toast(((ElementMate) element).getDescription());
						break;
					case T_POI:
						toast(((ElementPoi) element).getDescription());
						break;
					case T_MESSAGE:
						toast(((ElementMessage) element).getText());
						break;
					default:
						toast("unknown element: " + element.getId());
					}
				}

				void toast(String text) {
					Toast prName = Toast.makeText(getBaseContext(), text,
							Toast.LENGTH_LONG);
					prName.setGravity(Gravity.AXIS_SPECIFIED, 0, 0);
					prName.show();
				}

			} // class MyOnClickListener

		} // class MyButton

	} // class ButtonVisualizer

}
