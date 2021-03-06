package edu.ttu.swri.messenger;

import io.iron.ironmq.Client;
import io.iron.ironmq.Cloud;
import io.iron.ironmq.Message;
import io.iron.ironmq.Queue;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.google.gson.Gson;

import edu.ttu.swri.data.model.ElementMate;

public class FriendUpdateService extends Service {

	private Timer t;
	Gson gson;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		gson = new Gson();
		t = new Timer();
		t.schedule(new FriendLocationTask(), 0, 5000);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		t.cancel();
		t.purge();
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isValidMate(ElementMate mate){
		return (mate != null && mate.getId() != null && !mate.getId().toString().trim().equals(""));
	}	

	private class FriendLocationTask extends TimerTask {

		ElementMate location;

		@Override
		public void run() {
			Client client = new Client(AppContext.getProjectId(), AppContext.getToken(), Cloud.ironAWSUSEast);
			Queue queue = client.queue(AppContext.getCurrrentUserFriend()
					+ "-location");
			int queueSize = 0;
			Message msg = new Message();
			try {
				
				try{
					queueSize = queue.getSize(); 
				if (queueSize == 0) {
					AppContext.setCurrentUserFriendLatestLocation(new ElementMate());
					return;
				}
				} catch(IOException ioe){
					if(ioe.getMessage().contains("Queue not found")){
						queue.push("build this queue");
						queue.clear();
						AppContext.setCurrentUserFriendLatestLocation(new ElementMate());
						return;
					}
				}
				while (queueSize > 1) {
					--queueSize;
					queue.deleteMessage(queue.get());
				}
					msg = queue.get();
					String msgBody = msg.getBody();
					queue.deleteMessage(msg);
					queue.push(msgBody);
					location = gson.fromJson(msgBody, ElementMate.class);
					if(isValidMate(location)){
						AppContext.dao.saveElementMate(location);
					}					
					AppContext.setCurrentUserFriendLatestLocation(location);
					Intent intent = new Intent();
					intent.setAction((AppContext.getFriendLocationAction()));
					sendBroadcast(intent);

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

}
