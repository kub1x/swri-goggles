package com.ttu_swri.goggles.sandbox;

import org.json.JSONException;
import org.json.JSONObject;

import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.ttu_swri.datamodel.Element;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.datamodel.ElementPoi;
import com.ttu_swri.goggles.DataManager;
import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.network.NetworkHandler;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.app.Activity;
import android.content.Context;

public class JakubSandboxActivity extends Activity {
	// private static final String TAG = "JakubSandboxActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jakub_sandbox);

		// On following parts you can examine testing and usage examples of
		// various components of our program

		// 1) Network
		// TextView textView = (TextView)
		// findViewById(R.id.j_sand_text_view_weather);
		// textView.setText("Obtaining forecast...");
		// getForecast();

		// 2) Location
		setLocationListener();

		// 3) Data
		createData();
		showData();
	}

	// ==============================================================================
	// ==============================================================================
	// ==============================================================================

	void getForecast() {
		// Following API KEY belongs to kub1x. Please don't use it =P
		final String DARK_SKY_API_KEY = "7235d90c9485da14594674e2813be175";

		double lat = 33.566389;
		double lon = -101.886667;

		String url = "https://api.forecast.io/forecast/" + DARK_SKY_API_KEY
				+ "/" + lat + "," + lon;

		NetworkHandler.getInstance().getJson(getBaseContext(), url,
				new WebResponseListener() {

					@Override
					public void onComplete(byte[] response, String statusCode,
							String statusId, String requestId) {
						// Not used
					}

					@Override
					public void onComplete(String response, String statusCode,
							String statusId, String requestId) {

						try {
							JSONObject json = new JSONObject(response);
							TextView textView = (TextView) findViewById(R.id.j_sand_text_view_weather);
							textView.setText("Next Hour: "
									+ json.getJSONObject("hourly").getString(
											"summary"));
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
	}

	// ==============================================================================
	// ==============================================================================
	// ==============================================================================

	void setLocationListener() {
		TextView textView = (TextView) findViewById(R.id.j_sand_text_view_pos);
		textView.setText("Loading location...");

		LocationManager locationManager;
		String context = Context.LOCATION_SERVICE;
		locationManager = (LocationManager) getSystemService(context);

		Criteria mycriteria = new Criteria();
		mycriteria.setAccuracy(Criteria.ACCURACY_FINE);
		mycriteria.setAltitudeRequired(false);
		mycriteria.setBearingRequired(false);
		mycriteria.setCostAllowed(true);
		mycriteria.setPowerRequirement(Criteria.POWER_LOW);
		String provider = locationManager.getBestProvider(mycriteria, true);

		locationManager.requestLocationUpdates(provider, 1000, 0,
				new LocationListener() {

					@Override
					public void onStatusChanged(String provider, int status,
							Bundle extras) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProviderEnabled(String provider) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onProviderDisabled(String provider) {
						// TODO Auto-generated method stub
					}

					@Override
					public void onLocationChanged(Location location) {
						// TODO Auto-generated method stub
						TextView textView = (TextView) findViewById(R.id.j_sand_text_view_pos);
						if (location == null)
							textView.setText("NO LOCATION");
						else
							textView.setText("Loc: " + location.getLatitude()
									+ ", " + location.getLongitude());
					}
				});
	}

	// ==============================================================================
	// ==============================================================================
	// ==============================================================================

	// TESTING data model, DataManager and custom Visualizer
	DataManager dm = null;

	void createData() {
		this.dm = DataManager.getInstance();
		{
			this.dm.update(new ElementMate());
			this.dm.update(new ElementMate("Anand",
					"The one with sorted closet. "));

			this.dm.update(new ElementPoi());
			this.dm.update(new ElementPoi("Woody",
					"horse statue by Murray hall", null));

			this.dm.update(new ElementMessage());
			this.dm.update(new ElementMessage("argh!", "they killed me!!"));
			this.dm.update(new ElementMate("Austin", "The Fruit King. "));
			this.dm.update(new ElementMate("Vlad", "\"I Love Rock'n'Roll!\""));
		}
	}

	void showData() {
		LinearLayout lv = (LinearLayout) findViewById(R.id.j_sand_lin_lay);
		lv.removeAllViews();
		for (Element el : this.dm.getElements()) {
			Button but = new Button(getBaseContext());
			but.setText(el.getId());
			// but.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// String id = get_element_id_associated_with_this_button();
			// Element el = dm.getElement(id);
			// Toast prName = Toast.makeText(getBaseContext(), el.detail_text,
			// Toast.LENGTH_SHORT);
			// prName.setGravity(Gravity.AXIS_SPECIFIED, 0, 0);
			// prName.show();
			// }
			// });
			lv.addView(but);
		}
	}

}
