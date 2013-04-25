package com.ttu_swri.goggles.sandbox;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.reconinstruments.webapi.SDKWebService.WebResponseListener;
import com.ttu_swri.datamodel.Element;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.datamodel.ElementPoi;
import com.ttu_swri.goggles.DataManager;
import com.ttu_swri.goggles.R;
import com.ttu_swri.goggles.network.NetworkHandlerIronIo;
import com.ttu_swri.goggles.network.NetworkSyncService;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.app.Activity;

public class JakubSandboxActivity extends Activity {
	private static final String TAG = "JakubSandboxActivity";

	// TESTING data model, DataManager and custom Visualizer
	DataManager dm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jakub_sandbox);

		this.dm = DataManager.getInstance();

		Button bShow = (Button) findViewById(R.id.j_button_show);
		Button bSync = (Button) findViewById(R.id.j_button_sync);
		Button bCrea = (Button) findViewById(R.id.j_button_create);

		bShow.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showData();
			}
		});

		bSync.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				syncData();
			}
		});

		bCrea.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createData();
			}
		});

		// On following parts you can examine testing and usage examples of
		// various components of our program
		// createData();
		// syncData();
		// showData();
	}

	// ==============================================================================
	// ==============================================================================
	// ==============================================================================

	void createData() {
		{
			this.dm.update(new ElementMate("Anand",
					"The one with sorted closet. "));

			this.dm.update(new ElementPoi("Woody",
					"horse statue by Murray hall", null));

			this.dm.update(new ElementMessage("argh!", "they killed me!!"));
			this.dm.update(new ElementMate("Austin", "The Fruit King. "));
			this.dm.update(new ElementMate("Vlad", "\"I Love Rock'n'Roll!\""));
		}
	}

	void showData() {
		LinearLayout lv = (LinearLayout) findViewById(R.id.j_sand_datalist);
		lv.removeAllViews();
		for (Element el : this.dm.getElements()) {
			Button but = new Button(getBaseContext());
			but.setText(el.getId());
			// but.setOnClickListener(new OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// // TODO Auto-generated method stub
			// String id = get_element_id_associated_with_this_button();
			// Element el = dm.getElement(id);
			// Toast prName = Toast.makeText(getBaseContext(), el.detail_text,
			// Toast.LENGTH_SHORT);
			// prName.setGravity(Gravity.AXIS_SPECIFIED, 0, 0);
			// prName.show();
			// }
			// });
			lv.addView(but);
		}
	}

	public void syncData() {
		// Singletons can be final here and so reused in callback
		final NetworkHandlerIronIo nh = NetworkHandlerIronIo.getInstance();
		final DataManager dm = DataManager.getInstance();

		// Get new data from network
		nh.get(getApplicationContext(), new WebResponseListener() {
			@Override
			public void onComplete(String response, String statusCode,
					String statusId, String requestId) {
				try {
					Log.d(TAG, "Response to get: \n" + response);
					List<String> ids = new ArrayList<String>();
					List<JSONObject> elements = new ArrayList<JSONObject>();

					NetworkSyncService.parseMessages(response, ids, elements);

					dm.updateFromNetwork(elements);

					// Update successfull, send deletes
					for (String id : ids) {
						nh.delete(getApplicationContext(), id);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onComplete(byte[] response, String statusCode,
					String statusId, String requestId) {
				// Not used
			}

		});

		// Send newly created or updated data
		nh.post(getApplicationContext(), dm.getElementsToSync());

	}

}
