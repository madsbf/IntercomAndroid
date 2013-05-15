package dk.partyroulette.doorphonebuddyclient.model.settings;

import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;


public class UserSettings extends Settings
{
	public UserSettings(ObscuredSharedPreferences sharedPreferences) 
	{
		super(sharedPreferences);
	}

	public static final String KEY_SPEAKER_ON = "KEY_USER_SPEAKER_ON";
	public static final String KEY_AVAILABLE = "KEY_USER_AVAILABLE";

	private static final String[] KEYS = {KEY_SPEAKER_ON, KEY_AVAILABLE};
	
	@Override
	protected String[] getOwnedKeys()
	{
		return KEYS;
	}

}
