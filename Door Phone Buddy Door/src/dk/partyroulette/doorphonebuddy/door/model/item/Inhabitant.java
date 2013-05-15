package dk.partyroulette.doorphonebuddy.door.model.item;

import security.AuthenticationInstance;
import android.content.SharedPreferences;
import dk.partyroulette.doorphonebuddy.door.model.exceptions.MemoryException;

public class Inhabitant extends AuthenticationInstance
{
	private final static String KEY_KEY = "KEY";
	private final static String KEY_SECRET = "SECRET";
	
	public Inhabitant(String key, String secret)
	{
		super(key, secret);
	}
	
	public Inhabitant(SharedPreferences sharedPreferences, String memoryKey) throws Exception
	{
		super(sharedPreferences.getString(KEY_KEY, ""), 
				sharedPreferences.getString(KEY_SECRET, ""));

		if(key.equals(""))
		{
			throw new MemoryException("No inhabitant key found.");
		}
		
		if(secret.equals(""))
		{
			throw new MemoryException("No inhabitant secret found.");
		}
		
		
	}
}
