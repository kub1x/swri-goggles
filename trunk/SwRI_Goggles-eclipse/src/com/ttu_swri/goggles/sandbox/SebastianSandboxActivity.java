package com.ttu_swri.goggles.sandbox;

import org.ttu_swri.goggles.ui.CurrentLocationActivity;
import org.ttu_swri.goggles.ui.FriendsLocationActivity;
import org.ttu_swri.goggles.ui.MessagesActivity;
import org.ttu_swri.goggles.ui.POIActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.ttu_swri.goggles.R;

public class SebastianSandboxActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sebastian_sandbox);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.sebastian_sandbox, menu);
		return true;
	}

	// Changes from Main Menu Activity to POIs Activity
	public void POIsButton(View POIs) {

		Intent intent = new Intent();
		intent.setClass(this, POIActivity.class);
		startActivity(intent);
	}

	// Changes from Main Menu Activity to Current Location Activity
	public void CurrentLocationButton(View CurrentLocation) {

		Intent intent = new Intent();
		intent.setClass(this, CurrentLocationActivity.class);
		startActivity(intent);
	}

	// Changes from Main Menu Activity to Friend's Location Activity
	public void FriendsLocationButton(View FriendsLocation) {

		Intent intent = new Intent();
		intent.setClass(this, FriendsLocationActivity.class);
		startActivity(intent);
	}

	// Changes from Main Menu Activity to Messages Activity
	public void MessagesButton(View Messages) {

		Intent intent = new Intent();
		intent.setClass(this, MessagesActivity.class);
		startActivity(intent);
	}

}
