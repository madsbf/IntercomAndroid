package dk.partyroulette.doorphonebuddy.door.model.settings;

import java.util.UUID;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class AppSettings 
{
	private static AppSettings settings;
	private SharedPreferences sharedPreferences;
	
	private static final String PREFERENCES_NAME = "DOOR_PHONE_BUDY_DOOR_PREFERENCES";
	
	/** A private Constructor prevents any other class from instantiating. */
	private AppSettings(Context context) 
	{
		sharedPreferences = new ObscuredSharedPreferences(context, 
				context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE));
		
		sipUsername = sharedPreferences.getString(KEY_SIP_USERNAME, "");
		sipServerDomain = sharedPreferences.getString(KEY_SIP_SERVER_DOMAIN, "");
		sipPassword = sharedPreferences.getString(KEY_SIP_PASSWORD, "");
		sipAuthUsername = sharedPreferences.getString(KEY_SIP_AUTH_USERNAME, "");
		sipOutboundProxy = sharedPreferences.getString(KEY_SIP_OUTBOUND_PROXY, "");
		salt = sharedPreferences.getString(KEY_SECURITY_SALT, "");
		
		if(salt.equals(""))
		{
			UUID generatedUUID = UUID.randomUUID();
			setSalt(generatedUUID.toString());
		}
		
		// TODO: Remove
		sipUsername = "madsfrandsen2";
		sipServerDomain = "getonsip.com";
		sipPassword = "YwAAVymJhba9F8ud";
		sipAuthUsername = "getonsip_madsfrandsen";
		sipOutboundProxy = "sip.onsip.com";
	}
	
	public static synchronized AppSettings getSettings(Context context) {
		if (settings == null) 
		{
			settings = new AppSettings(context);
		}
		return settings;
	}
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	private boolean speakerOn;
	private boolean available;
	private String key;
	private String secret;
	
	private String sipUsername;
	private String sipServerDomain;
	private String sipPassword;
	private String sipAuthUsername;
	private String sipOutboundProxy;
	
	private String salt;
	
	private final String KEY_SPEAKER_ON = "KEY_SPEAKER_ON";
	private final String KEY_AVAILABLE = "KEY_AVAILABLE";
	private final String KEY_SECURITY_KEY = "KEY_SECURITY_KEY";
	private final String KEY_SECURITY_SECRET = "KEY_SECURITY_SECRET";
	
	private final String KEY_SIP_USERNAME = "KEY_SIP_USERNAME";
	private final String KEY_SIP_SERVER_DOMAIN = "KEY_SIP_SERVER_DOMAIN";
	private final String KEY_SIP_PASSWORD = "KEY_SIP_PASSWORD";
	private final String KEY_SIP_AUTH_USERNAME = "KEY_SIP_AUTH_USERNAME";
	private final String KEY_SIP_OUTBOUND_PROXY = "KEY_SIP_OUTBOUND_PROXY";
	private final String KEY_SIP_DOOR_USERNAME = "KEY_SIP_DOOR_USERNAME";
	
	private final String KEY_SECURITY_SALT = "KEY_SECURITY_SALT";
	
	public String getSalt()
	{
		return salt;
	}
	
	public void setSalt(String value)
	{
		salt = value;
		saveToMemory(KEY_SECURITY_SALT, value);
	}
	
	public String getSecret()
	{
		return secret;
	}
	
	public void setSecret(String value)
	{
		secret = value;
		saveToMemory(KEY_SECURITY_SECRET, value);
	}
	
	public String getKey()
	{
		return key;
	}
	
	public void setKey(String value)
	{
		key = value;
		saveToMemory(KEY_SECURITY_KEY, value);
	}
	
	public boolean isSpeakerOn()
	{
		return speakerOn;
	}
	
	public void setSpeakerOn(boolean value)
	{
		this.speakerOn = value;
		saveToMemory(KEY_SPEAKER_ON, value);
	}
	
	public boolean isAvailable()
	{
		return available;
	}
	
	public void setAvailable(boolean value)
	{
		this.available = value;
		saveToMemory(KEY_AVAILABLE, value);
	}
	
	private void saveToMemory(String key, boolean value)
	{
		Editor editor = sharedPreferences.edit();
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	private void saveToMemory(String key, String value)
	{
		Editor editor = sharedPreferences.edit();
		editor.putString(key, value);
		editor.commit();
	}

	public String getSipUsername() 
	{
		return sipUsername;
	}

	public void setSipUsername(String value) 
	{
		sipUsername = value;
		saveToMemory(KEY_SIP_USERNAME, value);
	}

	public String getSipServerDomain() 
	{
		return sipServerDomain;
	}

	public void setSipServerDomain(String value) 
	{
		sipServerDomain = value;
		saveToMemory(KEY_SIP_SERVER_DOMAIN, value);
	}

	public String getSipPassword() 
	{
		return sipPassword;
	}

	public void setSipPassword(String value) 
	{
		sipPassword = value;
		saveToMemory(KEY_SIP_PASSWORD, value);
	}

	public String getSipAuthUsername() 
	{
		return sipAuthUsername;
	}

	public void setSipAuthUsername(String value) 
	{
		sipAuthUsername = value;
		saveToMemory(KEY_SIP_AUTH_USERNAME, value);
	}

	public String getSipOutboundProxy() {
		return sipOutboundProxy;
	}

	public void setSipOutboundProxy(String value) 
	{
		sipOutboundProxy = value;
		saveToMemory(KEY_SIP_OUTBOUND_PROXY, value);
	}

}
