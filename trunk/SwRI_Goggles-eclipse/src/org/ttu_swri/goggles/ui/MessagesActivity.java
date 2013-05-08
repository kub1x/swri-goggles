package org.ttu_swri.goggles.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.sandbox.SebastianSandboxActivity;

public class MessagesActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messages);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messages, menu);
		return true;
	}
	//Changes from Message Activity to Main Menu
	public void SendMessage(View SendMessage) {

		Intent intent = new Intent();
		intent.setClass(this, SendMessage.class);
		startActivity(intent);
	}

}
