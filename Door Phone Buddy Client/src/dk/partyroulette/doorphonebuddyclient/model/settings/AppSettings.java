package dk.partyroulette.doorphonebuddyclient.model.settings;

import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;


/**
 * Contains all the settings for the application. The singleton-pattern is applied to make sure there is only one instance of the AppSettings.
 * Settings are automatically loaded from the memory on startup. 
 * Each time a setting is changed, it is saved to the memory.
 * @author Mads
 *
 */
public class AppSettings extends Settings
{
	private static AppSettings settings;
	
	private AppSettings(ObscuredSharedPreferences sharedPreferences) 
	{
		super(sharedPreferences);
	}
	
	public static synchronized AppSettings getSettings(ObscuredSharedPreferences sharedPreferences) {
		if (settings == null) 
		{
			settings = new AppSettings(sharedPreferences);
		}
		return settings;
	}
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

	@Override
	protected Settings[] getSubSettings() 
	{
		Settings[] subSettings = {new UserSettings(sharedPreferences),
				new ConnectionSettings(sharedPreferences) };
		return subSettings;
	}

	@Override
	protected String[] getOwnedKeys() 
	{
		return null;
	}
}
