package dk.partyroulette.doorphonebuddy.door.model;

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
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.util.Log;
import dk.partyroulette.doorphonebuddy.door.activity.MainActivity;
import dk.partyroulette.doorphonebuddy.door.model.item.Address;
import dk.partyroulette.doorphonebuddy.door.model.item.Inhabitant;

public class CommunicationEngine
{
	private SipAudioCall sipCall;
	public SipManager sipManager = null;
	public SipProfile sipProfile = null;
	public SipAudioCall.Listener sipListener = null;

	private IncomingCallReceiver incomingCallReceiver;
	
	private MainActivity activity;
	
	private Timer callTimer;
	private boolean hangupAllowed = false;
	
	
	
	public CommunicationEngine(MainActivity activity)
	{
		this.activity = activity;
		
		try {
			initSip();
		} catch (SipException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		registerCallReceiver();
	}
	
	private void registerCallReceiver()
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.Sip.INCOMING_CALL");
		incomingCallReceiver = new IncomingCallReceiver();
		activity.registerReceiver(incomingCallReceiver, filter);
	}
	
	public void initSip() throws SipException
	{
		if(sipManager == null) {
			sipManager = SipManager.newInstance(activity);
		}

		SipProfile.Builder builder = null;
		try 
		{
			builder = new SipProfile.Builder("madsfrandsen2", "getonsip.com");
		} catch (ParseException e) {}
		builder.setPassword("YwAAVymJhba9F8ud");
		builder.setAuthUserName("getonsip_madsfrandsen2");
		builder.setDisplayName("Door Phone Buddy Door");
		builder.setOutboundProxy("sip.onsip.com");
		sipProfile = builder.build();

		Intent intent = new Intent();
		intent.setAction("android.Sip.INCOMING_CALL");
		PendingIntent pendingIntent = PendingIntent.getBroadcast(activity, 0, intent, Intent.FILL_IN_DATA);
		sipManager.open(sipProfile, pendingIntent, null);

		try {
			sipManager.setRegistrationListener(sipProfile.getUriString(), new SipRegistrationListener() {

				public void onRegistering(String localProfileUri) {
					Log.d("SIP", "Registering with SIP Server...");
				}

				public void onRegistrationDone(String localProfileUri, long expiryTime) {
					Log.d("SIP", "Ready");
				}

				public void onRegistrationFailed(String localProfileUri, int errorCode,
						String errorMessage) {
					Log.d("SIP", "Registration failed.  Please check settings.");
				}
			});
		} catch (SipException e) {}

		sipListener = new SipAudioCall.Listener() 
		{
			@Override
			public void onCallEnded(SipAudioCall call)
			{
				if(hangupAllowed)
				{
					activity.runOnUiThread(new Runnable()
					{
						@Override
						public void run() 
						{
							activity.hangup();
						}
					});
				}
			}
			
			@Override
			public void onRingingBack(SipAudioCall call)
			{
				hangupAllowed = false;
				callTimer = new Timer();
				TimerTask timerTask = new TimerTask()
				{

					@Override
					public void run() 
					{
						if(!sipCall.isInCall())
						{
							activity.runOnUiThread(new Runnable()
							{
								@Override
								public void run() 
								{
									activity.hangup();
								}
							});
						}
						else
						{
							hangupAllowed = true;
						}
					}
				};
				callTimer.schedule(timerTask, 15000l);
			}
			
			@Override
			public void onCallEstablished(SipAudioCall call) 
			{
				hangupAllowed = true;
				sipCall = call;
				sipCall.startAudio();
				sipCall.setSpeakerMode(true);
				if(sipCall.isMuted())
				{
					sipCall.toggleMute();
				}
				
				activity.runOnUiThread(new Runnable()
				{
					@Override
					public void run() 
					{
						activity.callReceived();
					}
				});
			}
		};
	}

	public void closeLocalProfile() {
		if (sipManager == null) {
			return;
		}
		try {
			if (sipProfile != null) {
				sipManager.close(sipProfile.getUriString());
			}
		} catch (Exception ee) {
			Log.d("WalkieTalkieActivity/onDestroy", "Failed to close local profile.", ee);
		}
	}
	
	public void unregisterCallReceiver()
	{
		if(incomingCallReceiver != null)
		{
			activity.unregisterReceiver(incomingCallReceiver);
		}
	}

	public class IncomingCallReceiver extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{

			String description = SipManager.getOfferSessionDescription(intent);
			String[] values = description.split("&endRequest&");
			String request = values[0];

			try
			{
				AuthenticationHolder authHolder = new AuthenticationHolder(values[1]);

				Inhabitant inhabitant = (Inhabitant) Authentication.findAuthenticationInstance(activity.getEntrance().getInhabitants(), authHolder.getKey());
				Authentication.authenticate(inhabitant, authHolder, Constants.MAC_ALGORITHM);
				
				if(request.equals("unlock"))
				{
					activity.unlock();
					
					try {
						sipManager.takeAudioCall(intent, null).close();
					} catch (SipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else if(request.equals("lock"))
				{
					activity.lock();
					
					try {
						sipManager.takeAudioCall(intent, null).close();
					} catch (SipException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			catch (exceptions.SecurityException e) 
			{
				Log.d("security", e.toString());
				// TODO: LOG security exception
			} 
		}
	}
	
	public void hangup()
	{
		if(sipCall != null)
		{
			try {
				sipCall.endCall();
			} catch (SipException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			sipCall.close();
		}
	}
	
	public void startCall(Address address)
	{
		try 
		{
			sipCall = sipManager.makeAudioCall(sipProfile.getUriString(), address.getSipUsername() + "@getonsip.com", sipListener, 20);
		} catch (SipException e) {}
	}
}
