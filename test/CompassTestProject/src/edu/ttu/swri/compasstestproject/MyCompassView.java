package edu.ttu.swri.compasstestproject;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class MyCompassView extends View implements SensorEventListener {
	@SuppressWarnings("unused")
	private final static String TAG = MyCompassView.class.getSimpleName();

	private double direction = 0;
	private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
	private boolean firstDraw;

	public MyCompassView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyCompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}

	public MyCompassView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}

	private void init() {

		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);

		firstDraw = true;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
				MeasureSpec.getSize(heightMeasureSpec));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int cxCompass = getMeasuredWidth() / 2;
		int cyCompass = getMeasuredHeight() / 2;
		float radiusCompass;

		if (cxCompass > cyCompass) {
			radiusCompass = (float) (cyCompass * 0.9);
		} else {
			radiusCompass = (float) (cxCompass * 0.9);
		}
		canvas.drawCircle(cxCompass, cyCompass, radiusCompass, paint);
		canvas.drawRect(0, 0, getMeasuredWidth(), getMeasuredHeight(), paint);

		if (!firstDraw) {

			canvas.drawLine(
					cxCompass,
					cyCompass,
					(float) (cxCompass + radiusCompass
							* Math.sin((double) (-direction) * 3.14 / 180)),
					(float) (cyCompass - radiusCompass
							* Math.cos((double) (-direction) * 3.14 / 180)),
					paint);

			canvas.drawText(String.valueOf(direction), cxCompass, cyCompass,
					paint);
		}

	}

	public void updateDirection(double d) {
		Log.d(TAG, "New azimuth: " + d);
		firstDraw = false;
		direction = d;
		invalidate();
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
	}

	float[] mGravity;
	float[] mGeomagnetic;

	@Override
	public void onSensorChanged(SensorEvent event) {
		Log.d(TAG, "Type: " + event.sensor.getType());

		float azimuth = 0;
		final float corelation = -70;

		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER)
			mGravity = event.values.clone();

		if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD)
			mGeomagnetic = event.values.clone();

		if (mGravity != null && mGeomagnetic != null) {
			float R[] = new float[9];
			float I[] = new float[9];
			float rR[] = new float[9];

			boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
					mGeomagnetic);

			if (success) {
				SensorManager.remapCoordinateSystem(R, SensorManager.AXIS_X,
						SensorManager.AXIS_Z, rR);
				float orientation[] = new float[3];
				SensorManager.getOrientation(rR, orientation);
				azimuth = orientation[0]; // azimuth, pitch and roll
			}
		}
		updateDirection(Math.toDegrees(azimuth) + corelation );
	}
}
