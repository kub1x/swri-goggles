package com.ttu_swri.goggles.sandbox;

import com.ttu_swri.goggles.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class AotuoSandboxActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aotuo_sandbox);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.aotuo_sandbox, menu);
		return true;
	}

}