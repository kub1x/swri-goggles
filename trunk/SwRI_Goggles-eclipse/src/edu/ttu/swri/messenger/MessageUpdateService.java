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
import com.ttu_swri.datamodel.ElementMessage;

public class MessageUpdateService extends Service {

	private Timer t;
	private Gson gson;

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		gson = new Gson();
		t = new Timer();
		t.schedule(new MessageTimerTask(), 0, 3000);
		return super.onStartCommand(intent, flags, startId);
	}	
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private boolean isValidMessage(ElementMessage message){
		return (message != null && message.getId() != null && !message.getId().toString().trim().equals("")
				&& message.getFrom() != null && !message.getFrom().toString().trim().equals(""));
	}	
	
	private class MessageTimerTask extends TimerTask {

		@Override
		public void run() {
			try{
			Client client =    new Client(AppContext.getProjectId(), AppContext.getToken(), Cloud.ironAWSUSEast);
			Queue queue = client.queue(AppContext.getCurrentUserName() + "-im");
			int queueSize = 0;
			Message msg = new Message();	
				try{
					queueSize = queue.getSize();
				if (queueSize == 0) {
					return;
				}
				} catch(IOException ioe){
					if(ioe.getMessage().contains("Queue not found")){
						queue.push("build this queue");
						queue.clear();
						return;
					}
				}
				while(queueSize-- > 0){
					msg = queue.get();
					String msgBody = msg.getBody();
					queue.deleteMessage(msg);					
					Intent intent = new Intent();
					ElementMessage elementMessage = gson.fromJson(msgBody, ElementMessage.class);
					if(isValidMessage(elementMessage)){
						AppContext.dao.saveElementMessage(elementMessage);
					}
					intent.setAction((AppContext.getFriendMessageAction()));
					intent.putExtra(AppContext.getMessageBodyExtra(), msgBody);
					sendBroadcast(intent);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}	

}
