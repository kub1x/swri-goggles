package edu.ttu.swri.goggles.ui;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ttu_swri.goggles.R;

public class JakubSandboxActivity extends Activity {
	private static final String TAG = JakubSandboxActivity.class
			.getSimpleName();

	// TODO COPY THIS
	private SensorManager mSensorManager;
	private ArrowView mArrowView; // see ArrowView class below

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jakub_sandbox);

		// TODO COPY THIS
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mArrowView = new ArrowView(getBaseContext()); // see ArrowView class
														// below
	}

	@Override
	protected void onResume() {
		super.onResume();

		// TODO COPY THIS - INCLUDE IN onResume()
		mSensorManager.registerListener(mArrowView,
				mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
				SensorManager.SENSOR_DELAY_NORMAL);

		mSensorManager.registerListener(mArrowView,
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onPause() {
		super.onPause();

		// TODO COPY THIS - INCLUDE IN onPause
		mSensorManager.unregisterListener(mArrowView);
	}

	// TODO IMPLEMENT THIS!!
	// This is method that shows the navigation info.
	// this is what should change the direction of arrow.
	void showText(String text) {
		((TextView) findViewById(R.id.j_sand_navigation)).setText(text);
	}

	/**
	 * TODO Include following class in class of that Arrow picture.
	 * 
	 * Following class contains all the code necessary to navigate user to some
	 * position.
	 * 
	 * Target is hardcoded on campus on Texas Tech.
	 * 
	 * 
	 * @author kub1x
	 * 
	 */
	class ArrowView extends View implements SensorEventListener {

		private Location origin; // My Position
		private Location target; // Final Position

		double azimuth = 0; // here I store current azimuth

		public ArrowView(Context context) {
			super(context);

			// HardCoded Target - Horse statue next to Murray Hall
			target = new Location(LocationManager.PASSIVE_PROVIDER);
			target.setLatitude(33.586661);
			target.setLongitude(-101.879246);

			// Current position
			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,
					new LocationListener() {

						@Override
						public void onStatusChanged(String provider,
								int status, Bundle extras) {
						}

						@Override
						public void onProviderEnabled(String provider) {
						}

						@Override
						public void onProviderDisabled(String provider) {
						}

						@Override
						public void onLocationChanged(Location location) {
							updateLocation(location);
						}
					});
		}

		void updateLocation(Location location) {
			this.origin = location;
			navigate();
		}

		void navigate() {
			if (origin == null || target == null)
				return;
			double bearing = origin.bearingTo(target);
			double direction = normalize_angle(azimuth - bearing);

			double distance = origin.distanceTo(target);

			String text = "Azimuth: " + azimuth + "\n" + "Bearing: " + bearing
					+ "\n" + "Dir: " + direction + "\n" + "Dist: " + distance;

			if (distance < 10) {
				// stop
				text += "\n" + "stop";
			} else {

				// Forward direction is +/- "trashhold" degrees
				double trashhold = 20;

				if (direction < trashhold && direction > -trashhold) {
					text += "\n" + "forward";
					// forward
				}

				if (direction >= trashhold && direction < 180 - trashhold) {
					text += "\n" + "left";
					// left
				}

				if (direction <= -trashhold && direction > -180 + trashhold) {
					text += "\n" + "right";
					// right
				}

				if (direction <= -180 + trashhold
						|| direction >= 180 - trashhold) {
					text += "\n" + "behind";
					// behind
				}
			}
			showText(text);
		}

		@Override
		public void onAccuracyChanged(Sensor arg0, int arg1) {
			// DO NOTHING
		}

		float[] mGravity;
		float[] mGeomagnetic;

		@Override
		public void onSensorChanged(SensorEvent event) {
			if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
				mGravity = event.values.clone();

			if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
				mGeomagnetic = event.values.clone();

			if (mGravity != null && mGeomagnetic != null) {
				float R[] = new float[9];
				float I[] = new float[9];
				float rR[] = new float[9];

				boolean success = SensorManager.getRotationMatrix(R, I,
						mGravity, mGeomagnetic);

				if (success) {
					SensorManager.remapCoordinateSystem(R,
							SensorManager.AXIS_X, SensorManager.AXIS_Z, rR);
					float orientation[] = new float[3];
					SensorManager.getOrientation(rR, orientation);
					azimuth = orientation[0]; // azimuth, pitch and roll
				}
			}

			// Corelation for goggles
			// and fix btwn 180 and -180 after corelation
			final double corelation = -70;
			azimuth = normalize_angle(Math.toDegrees(azimuth) + corelation);

			navigate(); // i.e. invalidate(); updates according to new
						// conditions
		}

		/**
		 * get angle back between -180 and 180 after addition/substraction
		 * 
		 * @param angle
		 * @return
		 */
		double normalize_angle(double angle) {
			return ((angle + 360 + 180) % 360) - 180;
		}
	}

}
