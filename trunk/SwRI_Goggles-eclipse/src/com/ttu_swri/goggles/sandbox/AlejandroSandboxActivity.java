package com.ttu_swri.goggles.sandbox;

import io.iron.ironmq.Client;
import io.iron.ironmq.Cloud;
import io.iron.ironmq.Message;
import io.iron.ironmq.Queue;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.ttu_swri.goggles.R;

import edu.ttu.swri.data.model.GogglesMessage;
import edu.ttu.swri.data.model.impl.IronIOMessageImpl;

public class AlejandroSandboxActivity extends Activity {

	private static String projectId = "51661401ed3d7657b60014bc";
	private static String token = "zvS2csbud0tr6zgNbmjo9mSPByU";
	TextView tv = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alejandro_sandbox);
		tv = (TextView) findViewById(R.id.messageText);
		tv.setMovementMethod(new ScrollingMovementMethod());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.alejandro_sandbox, menu);
		return true;
	}
	
	public void getMessage(View v){
		 // here we get message by invoking an inner class that allows
		 // running the request on a different thread, so the UI doesn't freeze
		Toast.makeText(getBaseContext(), "Getting messages from Q", Toast.LENGTH_SHORT).show();
		new GetMessagesAsync().execute(v);
	}	
	
	public class GetMessagesAsync extends AsyncTask<View, String, String> {

	    @Override
	    protected String doInBackground(View... views) {
	        
			
			StringBuilder sb = new StringBuilder();
			// these are the calls to the webservice queue to get messages for the current user, need to send projectid, and token
			// there are some dependancies to google json jars, they are included in the project.
			// we may need to extract the url and content that is generated and send it in a way that
			// is consistent with the Recon SDK
			// 
			Client client = new Client(projectId, token, Cloud.ironAWSUSEast);
			Queue queue = client.queue("my_queue");


			
			Message msg = new Message();
			try {
				if(queue.getSize() < 1){
					return "";
				}
				msg = queue.get();
				

				sb.append(msg.getBody());

				queue.deleteMessage(msg);

			} catch (IOException e) {
				e.printStackTrace();
			}
			return sb.toString();
	    }
	    
	    @Override
	    protected void onPostExecute(String string) {
	    	tv.setText("");
	    	if(string.isEmpty()){
	    		Toast.makeText(getBaseContext(), "Q is empty", Toast.LENGTH_SHORT).show();
	    		return;
	    	}
	    	StringBuilder sb = new StringBuilder();
	    	String newLine = "\n";
	    	sb.append("Raw json String: " + newLine + string);
	    	Gson gson = new Gson();
	    	// here we use the gson.jar library to inflate the json back into a live object.
	    	IronIOMessageImpl gm = gson.fromJson(string, IronIOMessageImpl.class);
	    	sb.append(newLine + newLine + "Message Object Data after json is inflated: ");
	    	sb.append(newLine + "From: " + gm.getFrom());
	    	sb.append(newLine + "To: " + gm.getTo());
	    	sb.append(newLine + "GPS Location: " + gm.getGpsLocation());
	    	sb.append(newLine + "Text: " + gm.getMsgBody());
	    	sb.append(newLine + "Sent Date: " + gm.getSentDate());
	    	
    		Calendar cal = Calendar.getInstance();
    		cal.setTimeInMillis(gm.getSentDate());
    		SimpleDateFormat sdf = new SimpleDateFormat();
    		sdf.setCalendar(cal);
    		sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
    		
    		sb.append(newLine + "Formatted Date: " + sdf.format(cal.getTime()));
    		
    		tv.append(sb.toString());
    		System.out.println(sb.toString());
    		
	        super.onPostExecute(string);
	    }	    

	}	
	
	
	
	public void sendMessage(View v) throws IOException{
		
		
		GogglesMessage gm = new IronIOMessageImpl();
		
		gm.setFrom("Alex Sandbox Activity");
		gm.setGpsLocation("Some GPS Location");
		gm.setMsgBody("This is the Message Text");
		gm.setSentDate(Calendar.getInstance().getTimeInMillis());
		gm.setTo("my_queue");
		gm.setType(GogglesMessage.MessageType.IM);
		// we can use the gson.jar library to serialize the object in json format
		Gson gson = new Gson();
		String json = gson.toJson(gm);		 
		
		 // here we send the message by invoking an inner class that allows
		 // running the request on a different thread, so the UI doesn't freeze
		 
		new SendMessagesAsync().execute(json, gm.getTo());

	}
	
	
	public class SendMessagesAsync extends AsyncTask<String, String, String> {
	    @Override
	    protected String doInBackground(String... aurl) {
	        
	        try {
	    		// code to send messages to a certain queue
	    		// each user has their own queue, when we send message we send it with the user identifier (in this case just name like "Alex")
	        	// plus the msgTag to identify it's a text message, in this case it's "-im"
	    		Client client = new Client(projectId, token, Cloud.ironAWSUSEast);
	    		Queue queue = client.queue(aurl[1]);
	    		Calendar cal = Calendar.getInstance();
	    		SimpleDateFormat sdf = new SimpleDateFormat();
	    		sdf.setCalendar(cal);
	    		sdf.setTimeZone(TimeZone.getTimeZone("America/Chicago"));
	    		queue.push(aurl[0]);
	           
	            } catch (Exception e) {
	            	e.printStackTrace();
	            	System.out.println(e.getMessage());
	            	
	            }

	        return null;
	    }
	    
	    @Override
	    protected void onPostExecute(String unused) {           
	    	// finally a message sent notification
	        Toast.makeText(getBaseContext(), "Message Sent", Toast.LENGTH_SHORT).show();
	    }	    

	}		

}
