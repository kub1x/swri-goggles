package com.ttu_swri.goggles;

import com.ttu_swri.goggles.sandbox.AlejandroSandboxActivity;
import com.ttu_swri.goggles.sandbox.AotuoSandboxActivity;
import com.ttu_swri.goggles.sandbox.JakubSandboxActivity;
import com.ttu_swri.goggles.sandbox.SebastianSandboxActivity;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	final String D_ALEJA = "Alejandro";
	final String D_AOTUO = "Aotuo";
	final String D_JAKUB = "Jakub";
	final String D_SEBAS = "Sebastian";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Button b_aleja = (Button) findViewById(R.id.but_aleja);
		Button b_aotuo = (Button) findViewById(R.id.but_aotuo);
		Button b_jakub = (Button) findViewById(R.id.but_jakub);
		Button b_sebas = (Button) findViewById(R.id.but_sebas);

		b_aleja.setOnClickListener(butChangeView);
		b_aotuo.setOnClickListener(butChangeView);
		b_jakub.setOnClickListener(butChangeView);
		b_sebas.setOnClickListener(butChangeView);

		Button b_app = (Button) findViewById(R.id.but_goggles_app);
		b_app.setOnClickListener(butChangeView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	private OnClickListener butChangeView = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				switch (v.getId()) {
				case R.id.but_aleja:
					startActivity(new Intent(getBaseContext(),
							AlejandroSandboxActivity.class));
					break;

				case R.id.but_aotuo:
					startActivity(new Intent(getBaseContext(),
							AotuoSandboxActivity.class));
					break;

				case R.id.but_jakub:
					startActivity(new Intent(getBaseContext(),
							JakubSandboxActivity.class));
					break;

				case R.id.but_sebas:
					startActivity(new Intent(getBaseContext(),
							SebastianSandboxActivity.class));
					break;

				case R.id.but_goggles_app:
					startActivity(new Intent(getBaseContext(),
							GogglesMainActivity.class));
					break;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};

}
