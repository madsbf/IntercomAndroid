package dk.partyroulette.doorphonebuddyclient.activity;

import dk.partyroulette.doorphonebuddyclient.model.Constants;
import dk.partyroulette.doorphonebuddyclient.model.settings.AppSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

public class SettingsActivity extends Activity 
{
	protected AppSettings settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		settings = AppSettings.getSettings(new ObscuredSharedPreferences(this, 
				getSharedPreferences(Constants.APP_PREFERENCES, Context.MODE_PRIVATE)));
	}

}
