package com.ttu_swri.goggles;

import android.os.Bundle;
import android.app.Activity;
import android.view.KeyEvent;
import android.view.Menu;

public class GogglesMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_goggles_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.goggles_main, menu);
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		// Let's handle D-pad events
		switch (keyCode) {
		case KeyEvent.KEYCODE_DPAD_LEFT:
			// To the sides, it will circulate between Views: dashboard, map, POIlist, Userlist
			break;

		case KeyEvent.KEYCODE_DPAD_RIGHT:
			// To the sides, it will circulate between Views: dashboard, map, POIlist, Userlist
			break;

		case KeyEvent.KEYCODE_DPAD_UP:
			// Up and down, it will control the view itself: on dashboard it will highlight messages, on map it will zoom in/out, on lists it will iterate through list
			break;

		case KeyEvent.KEYCODE_DPAD_DOWN:
			// Up and down, it will control the view itself: on dashboard it will highlight messages, on map it will zoom in/out, on lists it will iterate through list
			break;

		case KeyEvent.KEYCODE_DPAD_CENTER:
			// On lists this will select highlighted item, on map.. who knows
			break;

		default:
			// Any other event will be sent to our superclass (View)
			return super.onKeyDown(keyCode, event);
		}

		return false;
	}

}
