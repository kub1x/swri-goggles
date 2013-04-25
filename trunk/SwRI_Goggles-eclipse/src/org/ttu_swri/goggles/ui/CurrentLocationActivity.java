package org.ttu_swri.goggles.ui;

import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.R.layout;
import com.ttu_swri.goggles.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

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

}
