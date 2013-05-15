package dk.partyroulette.doorphonebuddyclient.model.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import android.content.SharedPreferences;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.NoValueAtKeyException;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.UnsaveableFormatException;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.WrongTypeException;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.ObscuredSharedPreferences.Editor;

public abstract class Settings 
{
	protected ObscuredSharedPreferences sharedPreferences;
	
	private HashMap<String, Object> map = new HashMap<String, Object>();
	protected abstract String[] getOwnedKeys();
	
	public Settings(ObscuredSharedPreferences sharedPreferences) 
	{
		this.sharedPreferences = sharedPreferences;
		
		load();
	}
	
	public Settings(HashMap<String, Object> map)
	{
		this.map = map;
	}
	
	public void load()
	{
		map.clear();
		String[] keys = getKeys();
		
		if(keys != null)
		{
			for(String key : keys)
			{
				String value = sharedPreferences.getString(key, null);
				if(value != null)
				{
					map.put(key, value);
				}
			}
		}
	}
	
	protected Settings[] getSubSettings()
	{
		return null;
	}

	protected String[] getKeys()
	{
		ArrayList<String> keyList = new ArrayList<String>();
		
		String[] ownedKeys = getOwnedKeys();
		if(ownedKeys != null)
		{
			Collections.addAll(keyList, ownedKeys);
		}
		
		Settings[] subSettings = getSubSettings();
		if(subSettings != null)
		{
			for(Settings settings : subSettings)
			{
				String[] subKeys = settings.getKeys();
				if(subKeys != null)
				{
					Collections.addAll(keyList, subKeys);
				}
			}
		}
		
		ownedKeys = null;
		
		return keyList.toArray(new String[keyList.size()]);
	}
	
	/**
	 * Saves all values of the Settings object to the memory.
	 * @throws UnsaveableFormatException Thrown when some objects were not in a format that could be saved to the memory. All other objects were saved.
	 */
	public void saveToMemory() throws UnsaveableFormatException
	{
		Editor editor = sharedPreferences.edit();
		
		ArrayList<Object> unsaveableObjects = new ArrayList<Object>();
		
		String[] keys = getKeys();
		if(keys != null)
		{
			for(String key : getKeys())
			{
				Object value = map.get(key);
				
			}
			
			editor.commit();
			
			if(unsaveableObjects.size() != 0)
			{
				throw new UnsaveableFormatException(unsaveableObjects.toArray());
			}
		}
	}
	
	public void addValueToEditor(String key, Object value, Editor editor) throws UnsaveableFormatException
	{
		if(value != null)
		{
			if(value instanceof String)
			{
				editor.putString(key, (String) value);
			}
			else if(value instanceof Boolean)
			{
				editor.putBoolean(key, (Boolean) value);
			}
			else if(value instanceof Float)
			{
				editor.putFloat(key, (Float) value);
			}
			else if(value instanceof Integer)
			{
				editor.putInt(key, (Integer) value);
			}
			else if(value instanceof Long)
			{
				editor.putLong(key, (Long) value);
			}
			else
			{
				throw new UnsaveableFormatException(value);
			}
		}
	}
	
	public void setValue(String key, Object value) throws UnsaveableFormatException
	{
		Editor editor = sharedPreferences.edit();
		addValueToEditor(key, value, editor);
		editor.commit();
	}
	
	public Object getValue(String key) throws NoValueAtKeyException
	{
		try
		{
			Object value = map.get(key);
			if(value != null)
			{
				return value;
			}
			throw new NoValueAtKeyException(key);
		}
		catch(NullPointerException e)
		{
			throw new NoValueAtKeyException(key);
		}
		
	}
	
	public Boolean getBoolean(String key) throws NoValueAtKeyException, WrongTypeException
	{
		try
		{
			Object value = getValue(key);
			try
			{
				return (Boolean) value;
			}
			catch (ClassCastException e)
			{
				throw new WrongTypeException(key, value, WrongTypeException.TYPE_BOOLEAN);
			}
		}
		catch (NoValueAtKeyException e)
		{
			throw e;
		}		
	}
	
	public String getString(String key) throws NoValueAtKeyException, WrongTypeException
	{
		try
		{
			Object value = getValue(key);
			try
			{
				return (String) value;
			}
			catch (ClassCastException e)
			{
				throw new WrongTypeException(key, value, WrongTypeException.TYPE_STRING);
			}
		}
		catch (NoValueAtKeyException e)
		{
			throw e;
		}		
	}
}
