package org.ttu_swri.goggles.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.sandbox.SebastianSandboxActivity;

public class CurrentLocationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_current_location);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.current_location, menu);
		return true;
	}
	//Changes from Current Location Activity to Main Menu
	public void RTMM(View MainMenu) {

		Intent intent = new Intent();
		intent.setClass(this, SebastianSandboxActivity.class);

		startActivity(intent);
	}

}
