package dk.partyroulette.doorphonebuddyclient.activity;

import dk.partyroulette.doorphonebuddyclient.R;
import dk.partyroulette.doorphonebuddyclient.R.id;
import dk.partyroulette.doorphonebuddyclient.R.layout;
import dk.partyroulette.doorphonebuddyclient.model.settings.AppSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.ConnectionSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.UnsaveableFormatException;
import dk.partyroulette.doorphonebuddyclient.model.webservice.ConnectionException;
import dk.partyroulette.doorphonebuddyclient.model.webservice.WebService;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Presents the user with the possibility of inputting a key and a secret, and connecting his device.
 * @author Mads
 *
 */
public class ConnectionActivity extends SettingsActivity implements OnClickListener
{
	private EditText inputKey;
	private EditText inputSecret;
	private Button buttonConnect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{		
		setContentView(R.layout.activity_settings);
	
		registerViews();
		registerOnClickListeners();
	}
	
	/**
	 * Register the views of the activity.
	 */
	private void registerViews()
	{
		buttonConnect = (Button) findViewById(R.id.buttonConnect);
		inputKey = (EditText) findViewById(R.id.inputKey);
		inputSecret = (EditText) findViewById(R.id.inputSecret);
	}
	
	/**
	 * Register the OnClickListener for the views on the activity.
	 */
	private void registerOnClickListeners()
	{
		buttonConnect.setOnClickListener(this);
	}

	public void onClick(View view) 
	{
		if(view.getId() == R.id.buttonConnect)
		{
			String[] inputs = new String[2];
			inputs[0] = inputKey.getText().toString();
			inputs[1] = inputSecret.getText().toString();
			
			new ConnectionTask().execute(inputs);
		}	
	}
	
	private class ConnectionTask extends AsyncTask<String, Void, String>
	{
		@Override
		protected String doInBackground(String... inputs) 
		{
			if(!inputs[0].equals(""))
			{
				if(!inputs[1].equals(""))
				{
					if(connect(inputs))
					{
						// Connection successfull!
						return "success";
					}
					else
					{
						return "Connection problem. Double check your key and password, and try again.";
					}
				}
				else
				{
					return "You have not typed in a password.";
				}
			}
			else
			{
				return "You have not typed in a key.";
			}
		}
		
		@Override
		protected void onPostExecute (String result)
		{
			if(result.equals("success"))
			{
				relaunchMainActivity();
			}
			else
			{
				Toast toast = Toast.makeText(ConnectionActivity.this, result, Toast.LENGTH_SHORT);
				toast.show();
			}
		}	
	}
	
	private void relaunchMainActivity()
	{
		this.finish();
	}
	
	private boolean connect(String[] inputs)
	{
		try 
		{
			settings.setValue(ConnectionSettings.KEY_SECURITY_KEY, inputs[0]);
			settings.setValue(ConnectionSettings.KEY_SECURITY_SECRET, inputs[1]);
		} 
		catch (UnsaveableFormatException e) 
		{
			Log.d("UnsaveableFormatException", e.getMessage());
			return false;
		}
		
		try 
		{
			WebService.connect(inputs[0], inputs[1], settings);
		} 
		catch (ConnectionException e) 
		{
			Log.d("ConnectionException", e.getLocalizedMessage());
			return false;
		}
		
		return true;
	}
}
