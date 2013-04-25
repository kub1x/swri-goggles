package com.ttu_swri.goggles.sandbox;

import com.ttu_swri.datamodel.Element;
import com.ttu_swri.datamodel.ElementMate;
import com.ttu_swri.datamodel.ElementMessage;
import com.ttu_swri.datamodel.ElementPoi;
import com.ttu_swri.goggles.DataManager;
import com.ttu_swri.goggles.R;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.app.Activity;

public class JakubSandboxActivity extends Activity {
	// private static final String TAG = "JakubSandboxActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jakub_sandbox);

		// On following parts you can examine testing and usage examples of
		// various components of our program
		createData();
		showData();
	}

	// ==============================================================================
	// ==============================================================================
	// ==============================================================================

	// TESTING data model, DataManager and custom Visualizer
	DataManager dm = null;

	void createData() {
		this.dm = DataManager.getInstance();
		{
			this.dm.update(new ElementMate());
			this.dm.update(new ElementMate("Anand",
					"The one with sorted closet. "));

			this.dm.update(new ElementPoi());
			this.dm.update(new ElementPoi("Woody",
					"horse statue by Murray hall", null));

			this.dm.update(new ElementMessage());
			this.dm.update(new ElementMessage("argh!", "they killed me!!"));
			this.dm.update(new ElementMate("Austin", "The Fruit King. "));
			this.dm.update(new ElementMate("Vlad", "\"I Love Rock'n'Roll!\""));
		}
	}

	void showData() {
		LinearLayout lv = (LinearLayout) findViewById(R.id.j_sand_lin_lay);
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

}
