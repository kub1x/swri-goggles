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

public class FriendsLocationActivity extends Activity {
	
	private ListView mainListView ;  
	private ArrayAdapter<String> listAdapter ; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_friends_location);
		
		// Find the ListView resource.   
	    mainListView = (ListView) findViewById( R.id.listView1 );  
	  
	    // Create and populate a List of planet names.  
	    String[] planets = new String[] { ""};    
	    ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(planets) );  
	      
	    // Create ArrayAdapter using the planet list.  
	    listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);  
	      
	    // Add more planets. If you passed a String[] instead of a List<String>   
	    // into the ArrayAdapter constructor, you must not add more items.   
	    // Otherwise an exception will occur.  
	    listAdapter.add( "Friend 1 (1.5 miles)" );  
	    listAdapter.add( "Friend 2 (0.45 miles)" );  
	    listAdapter.add( "Friend 3 (2.3 miles)" );  
	    listAdapter.add( "Friend 4 (10.5 miles)" );  
	    listAdapter.add( "Friend 5 (1.7 miles)" );  
	      
	    // Set the ArrayAdapter as the ListView's adapter.  
	    mainListView.setAdapter( listAdapter ); 
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.friends_location, menu);
		return true;
	}
	//Changes from Friend's Location Activity to Main Menu
	public void RTMM(View MainMenu) {

		Intent intent = new Intent();
		intent.setClass(this, SebastianSandboxActivity.class);

		startActivity(intent);
	}

}
