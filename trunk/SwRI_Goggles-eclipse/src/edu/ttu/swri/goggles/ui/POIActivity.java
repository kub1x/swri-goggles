package edu.ttu.swri.goggles.ui;

import java.util.ArrayList;
import java.util.Arrays;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.ttu_swri.goggles.R;

public class POIActivity extends Activity {
	
	private ListView mainListView ;  
	  private ArrayAdapter<String> listAdapter ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_poi);
		
		// Find the ListView resource.   
	    mainListView = (ListView) findViewById( R.id.listView1 );  
	  
	    // Create and populate a List of planet names.  
	    String[] planets = new String[] { "Church", "Home", "Hospital", "Park",  
	                                      "Cafeteria", "Cinema", "Airport", "University"};    
	    ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(planets) );  
	      
	    // Create ArrayAdapter using the planet list.  
	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);  
	      
	    // Add more planets. If you passed a String[] instead of a List<String>   
	    // into the ArrayAdapter constructor, you must not add more items.   
	    // Otherwise an exception will occur.  
	    //listAdapter.add( "Ceres" );  
	    //listAdapter.add( "Pluto" );  
	    //listAdapter.add( "Haumea" );  
	    //listAdapter.add( "Makemake" );  
	    //listAdapter.add( "Eris" );  
	      
	    // Set the ArrayAdapter as the ListView's adapter.  
	    mainListView.setAdapter( listAdapter ); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.poi, menu);
		return true;
		
		
	}
	//Changes from POIs Activity to Main Menu
	public void RTMM(View MainMenu) {

		Intent intent = new Intent();
		intent.setClass(this, SebastianSandboxActivity.class);

		startActivity(intent);
	}

}
