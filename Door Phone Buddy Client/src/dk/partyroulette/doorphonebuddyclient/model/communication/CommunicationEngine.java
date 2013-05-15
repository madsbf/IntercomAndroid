package dk.partyroulette.doorphonebuddyclient.model.communication;

import java.text.ParseException;

import dk.partyroulette.doorphonebuddyclient.model.Constants;
import dk.partyroulette.doorphonebuddyclient.model.settings.AppSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.ConnectionSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.UserSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.SettingsException;
import dk.partyroulette.doorphonebuddyclient.model.webservice.ConnectionException;

import security.Authentication;
import security.AuthenticationInstance;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.sip.SipAudioCall;
import android.net.sip.SipException;
import android.net.sip.SipManager;
import android.net.sip.SipProfile;
import android.net.sip.SipRegistrationListener;
import android.net.sip.SipSession;
import android.util.Log;

/**
 * Handles all communication.
 * @author Mads
 *
 */
public class CommunicationEngine
{
	public SipManager sipManager = null;
	public SipProfile sipProfile = null;
	public SipAudioCall.Listener sipListener = null;
	public SipAudioCall sipCall = null;

	private CommunicationListener listener;

	private AppSettings appSettings;

	private AuthenticationInstance authInstance;

	private Service service;

	public CommunicationEngine(Service service, AppSettings appSettings) throws ConnectionException
	{
		this.appSettings = appSettings;
		this.service = service;
	}

	public void reload() throws ConnectionException
	{
		try 
		{
			this.authInstance = new AuthenticationInstance(appSettings.getString(ConnectionSettings.KEY_SECURITY_KEY), 
					appSettings.getString(ConnectionSettings.KEY_SECURITY_SECRET));

			initSip();

			registerCallReceiver(service);
		} 
		catch (SettingsException e) 
		{
			throw new ConnectionException(e.getMessage());
		} 
		catch (SipException e) 
		{
			throw new ConnectionException(e.getMessage());
		}
	}

	public void sendUnlockRequest() throws ConnectionException
	{
		if(sipCall != null)
		{
			if(sipCall.isInCall())
			{
				// TODO
			}
		}

		try 
		{
			SipSession sipSession = sipManager.createSipSession(sipProfile, null);

			if(sipSession.getState() == SipSession.State.READY_TO_CALL)
			{
				String securityString = Authentication.createAuthenticationString(authInstance, Constants.SALT, Constants.MAC_ALGORITHM);
				sipSession.makeCall(getDoorSipProfile(), "unlock&endRequest&" + securityString, 30);
			}
		} 
		catch (SipException e) 
		{
			throw new ConnectionException(e.getMessage());
		}
	}

	private SipProfile getDoorSipProfile() throws ConnectionException
	{
		SipProfile.Builder builder = null;
		try 
		{
			builder = new SipProfile.Builder(appSettings.getString(ConnectionSettings.KEY_SIP_DOOR_USERNAME), appSettings.getString(ConnectionSettings.KEY_SIP_SERVER_DOMAIN));
		} 
		catch (ParseException e) 
		{
			throw new ConnectionException(e.getMessage());
		}
		catch (SettingsException e)
		{
			throw new ConnectionException(e.getMessage());
		}

		return builder.build();
	}

	public void sendLockRequest() throws ConnectionException
	{
		try 
		{
			SipSession sipSession = sipManager.createSipSession(sipProfile, null);

			if(sipSession.getState() == SipSession.State.READY_TO_CALL)
			{
				sipSession.makeCall(getDoorSipProfile(), "lock&endRequest&" + Authentication.createAuthenticationString(authInstance, Constants.SALT, Constants.MAC_ALGORITHM), 30);
			}
		} 
		catch (SipException e) 
		{
			throw new ConnectionException(e.getMessage());
		}
	}

	private void registerCallReceiver(Service service)
	{
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.Sip.INCOMING_CALL");
		service.registerReceiver(new IncomingCallReceiver(), filter);
	}

	public void initSip() throws SipException, ConnectionException
	{
		if(sipManager == null) {
			sipManager = SipManager.newInstance(service);
		}

		SipProfile.Builder builder = null;
		try 
		{
			builder = new SipProfile.Builder(appSettings.getString(ConnectionSettings.KEY_SIP_USERNAME), appSettings.getString(ConnectionSettings.KEY_SIP_SERVER_DOMAIN));
			builder.setPassword(appSettings.getString(ConnectionSettings.KEY_SIP_PASSWORD));
			builder.setProfileName(appSettings.getString(ConnectionSettings.KEY_SIP_AUTH_USERNAME));
			// builder.setAuthUserName(appSettings.getString(ConnectionSettings.KEY_SIP_AUTH_USERNAME));
			builder.setDisplayName("Door Phone Buddy Client");
			builder.setOutboundProxy(appSettings.getString(ConnectionSettings.KEY_SIP_OUTBOUND_PROXY));
			sipProfile = builder.build();

			Intent intent = new Intent();
			intent.setAction("android.Sip.INCOMING_CALL");
			PendingIntent pendingIntent = PendingIntent.getBroadcast(service, 0, intent, Intent.FILL_IN_DATA);
			sipManager.open(sipProfile, pendingIntent, null);

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
		} 
		catch (SipException e) 
		{
			throw new ConnectionException(e.getMessage());
		} 
		catch (SettingsException e) 
		{
			throw new ConnectionException(e.getMessage());
		}
		catch (ParseException e) 
		{
			throw new ConnectionException(e.getMessage());
		}
	}

	public void acceptIncomingCall() throws ConnectionException
	{
		try 
		{
			sipCall.answerCall(30);
			sipCall.startAudio();

			try 
			{
				sipCall.setSpeakerMode(appSettings.getBoolean(UserSettings.KEY_SPEAKER_ON));
			} 
			catch (SettingsException e) 
			{
				sipCall.setSpeakerMode(false);
			}

			if(sipCall.isMuted())
			{
				sipCall.toggleMute();
			}
		} 
		catch (SipException e) 
		{
			throw new ConnectionException(e.getMessage());
		}

	}

	public void rejectIncomingCall()
	{
		try 
		{
			if (sipCall !=null) 
			{

				sipCall.endCall();
				sipCall.close();
			}

		}
		catch(Exception e)
		{

			System.out.println(e.toString());
		}
	}

	public class IncomingCallReceiver extends BroadcastReceiver 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			boolean receive = false;
			try 
			{
				receive = appSettings.getBoolean(UserSettings.KEY_AVAILABLE);
			} catch (SettingsException e) 
			{
				receive = true;
			}

			if(receive)
			{
				if(listener != null)
				{
					listener.onReceiveCall();
				}
				else
				{
				}


				Log.d("SIP", "receiving call");

				sipCall = null;
				try 
				{
					SipAudioCall.Listener callListener = new SipAudioCall.Listener() 
					{		            	
						@Override
						public void onCallEnded (SipAudioCall call)
						{
							listener.onEndCall();
						}
					};

					sipCall = sipManager.takeAudioCall(intent, null);
					sipCall.setListener(callListener);


				} catch (Exception e) {
					if (sipCall != null) {
						sipCall.close();
					}
				}
			}
		}
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
			// TODO: Log.d("WalkieTalkieActivity/onDestroy", "Failed to close local profile.", ee);
		}
	}

	public void setCommunicationListener(CommunicationListener listener) 
	{
		this.listener = listener;	
	}

	public void endCall() 
	{
		if(sipCall != null)
		{
			try {
				sipCall.endCall();
			} 
			catch (SipException e) 
			{
				// TODO
			}
			sipCall.close();
		}

	}

}
