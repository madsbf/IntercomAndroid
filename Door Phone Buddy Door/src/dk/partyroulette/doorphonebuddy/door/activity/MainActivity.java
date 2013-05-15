package dk.partyroulette.doorphonebuddy.door.activity;

import java.text.ParseException;
import java.util.Timer;
import java.util.TimerTask;

import security.Authentication;
import security.AuthenticationHolder;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import dk.partyroulette.doorphonebuddy.door.R;
import dk.partyroulette.doorphonebuddy.door.model.CommunicationEngine;
import dk.partyroulette.doorphonebuddy.door.model.Constants;
import dk.partyroulette.doorphonebuddy.door.model.Entrance;
import dk.partyroulette.doorphonebuddy.door.model.SoundEngine;
import dk.partyroulette.doorphonebuddy.door.model.item.Address;
import dk.partyroulette.doorphonebuddy.door.model.item.Inhabitant;
import dk.partyroulette.doorphonebuddy.door.view.EntranceView;
import exceptions.HMACMismatchException;
import exceptions.InvalidAuthenticationStringException;
import exceptions.KeyMissingException;
import exceptions.NonceReuseException;
import exceptions.TimestampException;

public class MainActivity extends LockActivity implements OnCallClickListener
{

	private Entrance entrance;

	private EntranceView entranceView;
	private LinearLayout loaderBox;
	private FrameLayout rootView;

	private boolean inCall = false;
	
	private SoundEngine sound;
	
	private CommunicationEngine comEngine;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		rootView = (FrameLayout) findViewById(R.id.root);
		rootView.getRootView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
		loaderBox = (LinearLayout) findViewById(R.id.boxLoading);
		
		sound = new SoundEngine(this);
		
		comEngine = new CommunicationEngine(this);
		
		new InitTask().execute();
	}

	private class InitTask extends AsyncTask<Void, Void, Void>
	{
		@Override
		protected Void doInBackground(Void... arg0) 
		{
			entranceView = new EntranceView(MainActivity.this);
			entranceView.setVisibility(View.INVISIBLE);
			entrance = new Entrance("Hammelstrupvej 4A");
			entranceView.loadEntrance(entrance, MainActivity.this);
			return null;
		}

		protected void onPostExecute(Void result) 
		{
			rootView.addView(entranceView);

			Animation fadeOut = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_out);
			final Animation fadeIn = AnimationUtils.loadAnimation(MainActivity.this, R.anim.fade_in);

			fadeOut.setAnimationListener(new AnimationListener()
			{

				@Override
				public void onAnimationEnd(Animation arg0) 
				{
					loaderBox.setVisibility(View.INVISIBLE);
					entranceView.startAnimation(fadeIn);
					entranceView.setVisibility(View.VISIBLE);
				}

				@Override
				public void onAnimationRepeat(Animation arg0) {}

				@Override
				public void onAnimationStart(Animation arg0) {}

			});
			loaderBox.startAnimation(fadeOut);
		}
	}
	
	public Entrance getEntrance()
	{
		return entrance;
	}
	
	public void unlock()
	{
		sound.startBuzzer();
		entranceView.unlock();
		setLocked(false);
	}
	
	public void lock()
	{
		sound.stopBuzzer();
		entranceView.lock();
		setLocked(true);
	}
	
	@Override
	public void onAddressClick(Address address) 
	{
		if(!inCall)
		{
			inCall = true;
			entranceView.callAddress(address, this);
			comEngine.startCall(address);
			sound.startDialTone();
		}	
	}

	@Override
	public void onHangUpClick() 
	{
		hangup();
	}
	
	public void callReceived()
	{
		entranceView.callReceived();
		sound.stopDialTone();
	}
	
	public void hangup()
	{
		inCall = false;
		entranceView.hangup();
		comEngine.hangup();
		sound.stopDialTone();

	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		comEngine.unregisterCallReceiver();
	}	

	
}
