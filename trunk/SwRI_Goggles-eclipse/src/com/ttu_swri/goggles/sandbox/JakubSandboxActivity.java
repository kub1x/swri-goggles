package com.ttu_swri.goggles.sandbox;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Location;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import com.ttu_swri.datamodel.Element;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.datamodel.ElementPoi;
import com.ttu_swri.datamodel.IVisualizer;
import com.ttu_swri.goggles.DataManager;
import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.network.NetworkSyncService;

public class JakubSandboxActivity extends TabActivity {
	@SuppressWarnings("unused")
	private static final String TAG = JakubSandboxActivity.class
			.getSimpleName();

	private ButtonVisualizer bv;
    private TabHost tabHost;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabview);
		
        tabHost = getTabHost();
        tabHost.addTab(tabHost.newTabSpec("Friendlies").setIndicator("Friendlies").setContent(R.id.layout_tab_friends));
        tabHost.addTab(tabHost.newTabSpec("Notifications").setIndicator("Notifications").setContent(R.id.layout_tab_messages));
        tabHost.addTab(tabHost.newTabSpec("Markers").setIndicator("Markers").setContent(R.id.layout_tab_pois));		

//		// Visualizer
//		bv = new ButtonVisualizer(
//				(LinearLayout) findViewById(R.id.j_sand_datalist));
//		DataManager dm = DataManager.getInstance();
//		dm.register(bv);

		// Example data for testing POST
		// Element element = new ElementMessage("test",
		// "testing message for engage");
		// dm.update(element);
	}

	@Override
	protected void onResume() {
		super.onResume();
		start();
	}

	@Override
	protected void onPause() {
		super.onPause();
		stop();
	}

	// Handle network sync ----------------------------------------------------
	
	Timer timer;
	TimerTask task;

	void start() {
		int delay = 0; // now
		int period = 5000; // 5 sec
		timer = new Timer();
		task = new TimerTask() {

			@Override
			public void run() {
				startService(new Intent(NetworkSyncService.NETWORK_SYNC_SERVICE));
			}
		};
		timer.scheduleAtFixedRate(task, delay, period);
	}

	void stop() {
		timer.cancel();
	}

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
