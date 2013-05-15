package dk.partyroulette.doorphonebuddyclient.model.communication;

import dk.partyroulette.doorphonebuddyclient.R;
import dk.partyroulette.doorphonebuddyclient.R.drawable;
import dk.partyroulette.doorphonebuddyclient.activity.MainActivity;
import dk.partyroulette.doorphonebuddyclient.model.Constants;
import dk.partyroulette.doorphonebuddyclient.model.settings.AppSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;
import dk.partyroulette.doorphonebuddyclient.model.webservice.ConnectionException;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.sip.SipException;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

/**
 * Service that provides two-way communication between the CommunicationEngine and the MainActivity.
 * The Service makes sure, that communication is available even when the MainActivity is not running, and starts the MainActivity, when it is needed.
 * @author Mads
 *
 */
public class CommunicationService extends Service implements CommunicationInterface, CommunicationListener
{
	private final IBinder mBinder = new SipServiceBinder();
	private CommunicationEngine communicator;
	private CommunicationListener listener;
	
	private boolean foreground = false;
	
	public Boolean communicationStatus = false;

	@Override
	public void onCreate ()
	{
		super.onCreate();
		
		try 
		{
			communicator = new CommunicationEngine(this, AppSettings.getSettings(new ObscuredSharedPreferences(this, 
					getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE))));
			communicator.setCommunicationListener(this);
			showNotification();
			communicationStatus = true;
		} 
		catch (ConnectionException e) 
		{
			// Do nothing - MainActivity is responsible for checking status when it connects.
		}

		
	}
	
	public class SipServiceBinder extends Binder 
	{
        public CommunicationInterface getInterface() 
        {
            return CommunicationService.this;
        }
    }
	
	@Override
    public IBinder onBind(Intent intent) 
	{
        return mBinder;
    }
	
	@Override
	public boolean onUnbind(Intent intent) 
	{
	    return false;
	}
	
	public void setForeground(Boolean value)
	{
		foreground = value;
	}

	@Override
	public void onDestroy() 
	{
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).cancel(1);
		super.onDestroy();
	}

	/**
	 * Notifies the user, that the background service is running.
	 */
	private void showNotification()
	{
		NotificationCompat.Builder mBuilder =
				new NotificationCompat.Builder(this)
		.setSmallIcon(R.drawable.ic_launcher)
		.setContentTitle("Door Phone Buddy")
		.setContentText("Door Phone Buddy is waiting for calls.");
		Notification not = mBuilder.build();
		
		Intent notificationIntent = new Intent(this, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		not.contentIntent = contentIntent;

		not.flags = Notification.FLAG_ONGOING_EVENT;
		((NotificationManager) getSystemService(NOTIFICATION_SERVICE)).notify(1, not);
	}

	public void sendUnlockRequest() throws ConnectionException 
	{
		communicator.sendUnlockRequest();
	}

	public void sendLockRequest() throws ConnectionException 
	{
		communicator.sendLockRequest();
	}

	public void setCommunicationListener(CommunicationListener listener) 
	{
		this.listener = listener;		
	}
	
	public void acceptIncomingCall() throws ConnectionException
	{
		communicator.acceptIncomingCall();
	}
	
	public void rejectIncomingCall()
	{
		communicator.rejectIncomingCall();
	}

	public void onReceiveCall() 
	{
		if(listener != null && foreground)
		{
			listener.onReceiveCall();
		}
		else
		{
			startMainActivity();
		}
	}
	
	public void startMainActivity()
	{
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("dk.partyroulette.doorphonebuddyclient.IS_CALL", true);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addFlags(Intent.FLAG_FROM_BACKGROUND);
		this.startActivity(intent);
	}

	public void reload() throws ConnectionException {
		communicator.reload();
	}

	public void onEndCall() 
	{
		listener.onEndCall();
	}

	public void endCall() 
	{
		communicator.endCall();
		
	}
}
