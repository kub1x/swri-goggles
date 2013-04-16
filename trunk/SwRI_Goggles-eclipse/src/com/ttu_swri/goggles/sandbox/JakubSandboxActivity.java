package com.ttu_swri.goggles.sandbox;

import com.ttu_swri.goggles.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class JakubSandboxActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jakub_sandbox);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.jakub_sandbox, menu);
		return true;
	}

}
