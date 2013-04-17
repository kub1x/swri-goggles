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

	// TESTING data model, DataManager and custom Visualizer
	DataManager dm = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jakub_sandbox);

		createData();
		showData();
	}

	void createData() {
		this.dm = new DataManager();
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
			but.setText(el.Id);
			lv.addView(but);
		}
	}

}
