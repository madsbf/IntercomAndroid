package dk.partyroulette.doorphonebuddyclient.model.settings;

import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;

public class ConnectionSettings extends Settings
{

	public ConnectionSettings(ObscuredSharedPreferences sharedPreferences) 
	{
		super(sharedPreferences);
	}

	public static final String KEY_SECURITY_KEY = "KEY_CONNECTION_SECURITY_KEY";
	public static final String KEY_SECURITY_SECRET = "KEY_CONNECTION_SECURITY_SECRET";
	public static final String KEY_SIP_USERNAME = "KEY_CONNECTION_SIP_USERNAME";
	public static final String KEY_SIP_SERVER_DOMAIN = "KEY_CONNECTION_SIP_SERVER_DOMAIN";
	public static final String KEY_SIP_PASSWORD = "KEY_CONNECTION_SIP_PASSWORD";
	public static final String KEY_SIP_AUTH_USERNAME = "KEY_CONNECTION_SIP_AUTH_USERNAME";
	public static final String KEY_SIP_OUTBOUND_PROXY = "KEY_CONNECTION_SIP_OUTBOUND_PROXY";
	public static final String KEY_SIP_DOOR_USERNAME = "KEY_CONNECTION_SIP_DOOR_USERNAME";
	
	private static final String[] KEYS = 
	{KEY_SECURITY_KEY, KEY_SECURITY_SECRET, KEY_SIP_USERNAME, KEY_SECURITY_SECRET, 
		KEY_SIP_USERNAME, KEY_SIP_SERVER_DOMAIN, KEY_SIP_PASSWORD, KEY_SIP_AUTH_USERNAME, 
		KEY_SIP_OUTBOUND_PROXY, KEY_SIP_DOOR_USERNAME};

	@Override
	protected String[] getOwnedKeys() 
	{
		return KEYS;
	}
	

}
