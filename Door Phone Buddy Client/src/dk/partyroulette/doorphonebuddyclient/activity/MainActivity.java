package dk.partyroulette.doorphonebuddyclient.activity;

import java.lang.reflect.Field;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem.OnMenuItemClickListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.VideoView;
import dk.partyroulette.doorphonebuddyclient.R;
import dk.partyroulette.doorphonebuddyclient.R.id;
import dk.partyroulette.doorphonebuddyclient.R.layout;
import dk.partyroulette.doorphonebuddyclient.R.menu;
import dk.partyroulette.doorphonebuddyclient.R.string;
import dk.partyroulette.doorphonebuddyclient.model.communication.CommunicationInterface;
import dk.partyroulette.doorphonebuddyclient.model.communication.CommunicationListener;
import dk.partyroulette.doorphonebuddyclient.model.communication.CommunicationService;
import dk.partyroulette.doorphonebuddyclient.model.communication.CommunicationService.SipServiceBinder;
import dk.partyroulette.doorphonebuddyclient.model.settings.AppSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.ConnectionSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.UserSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.SettingsException;
import dk.partyroulette.doorphonebuddyclient.model.webservice.ConnectionException;

public class MainActivity extends SettingsActivity implements OnTouchListener, OnMenuItemClickListener, OnClickListener 
{
	private ImageButton unlockButton;
	private ImageButton acceptCallButton;
	private ImageButton ignoreCallButton;
	private CommunicationInterface sipInterface;
	private boolean mBound;

	private boolean call = false;
	private boolean callSession = false;

	private Vibrator vibe;

	private VideoView vv;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Check to see, if the user is connected. Otherwise run connection settings.
		try 
		{
			settings.getString(ConnectionSettings.KEY_SECURITY_KEY);
		}
		catch (SettingsException e) 
		{
			launchConnectionSettings();
		}

		setFlags();
		setContentView(R.layout.activity_call);
		forceMenuIcon();
		vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE) ;

		// Video streaming disabled
		// startVideoStreaming();

		registerViews();
		registerOnClickListeners();

		Bundle extras = this.getIntent().getExtras();
		if(extras != null && extras.getBoolean("dk.partyroulette.doorphonebuddyclient.IS_CALL", false))
		{
			// Incoming call
			connectIncomingCall();
		}
		else
		{
			// No incoming call
			connectCallListener();
		}
	}
	
	private void connectIncomingCall()
	{
		callSession = true;
		call = true;
		Intent intent = new Intent(this, CommunicationService.class);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	private void connectCallListener()
	{
		deactivateCallButtons();
		Intent intent = new Intent(this, CommunicationService.class);
		startService(intent);
		bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * Register the views of the activity.
	 */
	private void registerViews()
	{
		acceptCallButton = (ImageButton) findViewById(R.id.buttonAcceptCall);
		ignoreCallButton = (ImageButton) findViewById(R.id.buttonIgnoreCall);
		unlockButton = (ImageButton) findViewById(R.id.buttonUnlock);
	}
	
	/**
	 * Register the OnClickListener for the views on the activity.
	 */
	private void registerOnClickListeners()
	{
		acceptCallButton.setOnClickListener(this);
		ignoreCallButton.setOnClickListener(this);
		unlockButton.setOnTouchListener(this);
	}
	
	/**
	 * Set the flags of the activity to force it to the front, 
	 * even when the screen is locked.
	 */
	private void setFlags()
	{
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | 
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
				WindowManager.LayoutParams.FLAG_FULLSCREEN |
				WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | 
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
				WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
	}
	
	// Hack to force the menu icon to be shown
	private void forceMenuIcon()
	{
		try {
	        ViewConfiguration config = ViewConfiguration.get(this);
	        Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
	        if(menuKeyField != null) {
	            menuKeyField.setAccessible(true);
	            menuKeyField.setBoolean(config, false);
	        }
	    } catch (Exception ex) {
	        // Ignore
	    }
	}

	public void startVideoStreaming()
	{
		vv = (VideoView) findViewById(R.id.videoView);
		String url = "rtsp://192.168.1.17:8086/playlist/door.3gp";				

		try 
		{
			vv.setVideoURI(Uri.parse(url));
			vv.requestFocus();
			vv.start();
		} 
		catch (IllegalArgumentException e) 
		{
			e.printStackTrace();
		} 
		catch (IllegalStateException e) 
		{
			e.printStackTrace();
		}	
	}

	@Override
	public void onResume() 
	{
		super.onResume();

		if(sipInterface != null)
		{
			sipInterface.setForeground(true);
			//sipInterface.reload();
		}
	}

	@Override
	public void onStop()
	{
		super.onStop();
		if(sipInterface != null)
		{
			sipInterface.setForeground(false);
		}
	}

	private void activateCallButtons()
	{
		acceptCallButton.setEnabled(true);
		ignoreCallButton.setEnabled(true);

		acceptCallButton.setVisibility(View.VISIBLE);
		ignoreCallButton.setVisibility(View.VISIBLE);
	}

	private void deactivateCallButtons()
	{
		runOnUiThread(new Runnable()
		{
			public void run() 
			{
				acceptCallButton.setEnabled(false);
				ignoreCallButton.setEnabled(false);

				acceptCallButton.setVisibility(View.INVISIBLE);
				ignoreCallButton.setVisibility(View.INVISIBLE);

			}
		});
	}

	/** Defines callbacks for service binding, passed to bindService() */
	private ServiceConnection mConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className,
				IBinder service) 
		{
			SipServiceBinder binder = (SipServiceBinder) service;
			sipInterface = binder.getInterface();
			sipInterface.setForeground(true);

			if(call)
			{
				activateCallButtons();
				startVibrate();
			}
			else
			{
				try 
				{
					sipInterface.reload();
				} 
				catch (ConnectionException e) 
				{
					Log.d("ConnectionException", e.getMessage());
					Toast toast = Toast.makeText(MainActivity.this, 
							"There was a problem connecting to the door device. Please try again.", Toast.LENGTH_SHORT);
					toast.show();
				}
			}

			sipInterface.setCommunicationListener(new CommunicationListener()
			{
				public void onReceiveCall() 
				{
					callSession = false;
					call = true;
					activateCallButtons();
					startVibrate();
				}

				public void onEndCall() 
				{
					stopCall();
				}
			});
			mBound = true;
		}

		public void onServiceDisconnected(ComponentName arg0) {
			mBound = false;
		}
	};

	private void startVibrate()
	{
		long[] vibratePattern = {1000l, 1000l, 1000l, 1000l, 1000l, 
				1000l, 1000l, 1000l, 1000l, 1000l,
				1000l, 1000l, 1000l, 1000l, 1000l, 
				1000l, 1000l, 1000l, 1000l};

		vibe.vibrate(vibratePattern, -1);
	}

	private void stopVibrate()
	{
		vibe.cancel();
	}

	@Override
	public boolean onOptionsItemSelected (MenuItem item)
	{
		try
		{
			if(item.getItemId() == R.id.menu_speaker)
			{
				if(!settings.getBoolean(UserSettings.KEY_SPEAKER_ON))
				{
					item.setChecked(true);
					item.setTitle(getString(R.string.menu_speaker_on));
					settings.setValue(UserSettings.KEY_SPEAKER_ON, true);
				}
				else
				{
					item.setChecked(false);
					item.setTitle(getString(R.string.menu_speaker_off));
					settings.setValue(UserSettings.KEY_SPEAKER_ON, false);
				}
			}
			else
			{
				if(!settings.getBoolean(UserSettings.KEY_AVAILABLE))
				{
					item.setChecked(true);
					item.setTitle(getString(R.string.menu_status_available));
					item.setTitleCondensed(getString(R.string.menu_status_available_condensed));
					settings.setValue(UserSettings.KEY_AVAILABLE, true);
				}
				else
				{
					item.setChecked(false);
					item.setTitle(getString(R.string.menu_status_unavailabe));
					item.setTitleCondensed(getString(R.string.menu_status_unavailabe_condensed));
					settings.setValue(UserSettings.KEY_AVAILABLE, false);
				}
			}
		}
		catch(SettingsException e)
		{
			Log.d("SettingsException", e.getMessage());
		} 	

		return true;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);

		MenuItem menuSpeaker = menu.findItem(R.id.menu_speaker);
		try
		{
			if(settings.getBoolean(UserSettings.KEY_SPEAKER_ON))
			{
				menuSpeaker.setChecked(true);
				menuSpeaker.setTitle(getString(R.string.menu_speaker_on));
			}
			else
			{
				menuSpeaker.setChecked(false);
				menuSpeaker.setTitle(getString(R.string.menu_speaker_off));
			}

			MenuItem menuStatus = menu.findItem(R.id.menu_status);
			if(settings.getBoolean(UserSettings.KEY_AVAILABLE))
			{
				menuStatus.setChecked(true);
				menuStatus.setTitle(getString(R.string.menu_status_available));
				menuStatus.setTitleCondensed(getString(R.string.menu_status_available_condensed));

			}
			else
			{
				menuStatus.setChecked(false);
				menuStatus.setTitle(getString(R.string.menu_status_unavailabe));
				menuStatus.setTitleCondensed(getString(R.string.menu_status_unavailabe_condensed));
			}
		}
		catch(SettingsException e)
		{
			Log.d("SettingsException", e.getMessage());
		} 

		MenuItem menuConnection = menu.findItem(R.id.menu_connection);
		menuConnection.setOnMenuItemClickListener(this);
		return true;
	}


	public boolean onTouch(View v, MotionEvent event) 
	{
		switch(v.getId())
		{
		case R.id.buttonUnlock:
			if(event.getAction() == MotionEvent.ACTION_DOWN)
			{
				if(mBound)
				{
					try 
					{
						sipInterface.sendUnlockRequest();
					} 
					catch (ConnectionException e) 
					{
						Log.d("ConnectionException", e.getMessage());
						Toast toast = Toast.makeText(MainActivity.this, 
								"There was a problem connecting to the door device. Please try again.", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				else
				{
					deactivateCallButtons();
					Intent intent = new Intent(this, CommunicationService.class);
					startService(intent);
					bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
				}
			}
			else if(event.getAction() == MotionEvent.ACTION_UP)
			{
				if(mBound)
				{
					try 
					{
						sipInterface.sendLockRequest();
					} 
					catch (ConnectionException e) 
					{
						Toast toast = Toast.makeText(MainActivity.this, 
								"There was a problem connecting to the door device. Please try again.", Toast.LENGTH_SHORT);
						toast.show();
					}
				}
				else
				{
					deactivateCallButtons();
					Intent intent = new Intent(this, CommunicationService.class);
					startService(intent);
					bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
				}
			}

			break;
		}
		return false;
	}

	private void launchConnectionSettings()
	{
		this.startActivity(new Intent(this, ConnectionActivity.class));
	}

	public boolean onMenuItemClick(MenuItem item) 
	{
		if(item.getItemId() == R.id.menu_connection)
		{
			launchConnectionSettings();
		}
		return false;
	}

	public void onClick(View v) 
	{
		switch(v.getId())
		{
		case R.id.buttonAcceptCall:
			try 
			{
				sipInterface.acceptIncomingCall();
			} 
			catch (ConnectionException e) 
			{
				Toast toast = Toast.makeText(MainActivity.this, 
						"There was a problem connecting to the door device. Please try again.", Toast.LENGTH_SHORT);
				toast.show();
				stopCall();
			}
			stopVibrate();
			break;
		case R.id.buttonIgnoreCall:
			sipInterface.rejectIncomingCall();
			stopCall();
			break;
		}
	}

	private void stopCall()
	{
		call = false;
		stopVibrate();
		if(callSession)
		{
			sipInterface.endCall();
			this.finish();
		}
		else
		{
			sipInterface.endCall();
			deactivateCallButtons();
		}
	}
}
