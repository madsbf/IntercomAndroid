package dk.partyroulette.doorphonebuddyclient.model.settings.exceptions;

/**
 * Indicates that there is no value associated with the given key.
 * @author Mads
 *
 */
public class NoValueAtKeyException extends SettingsException 
{
	private String key;
	
	public NoValueAtKeyException(String key)
	{
		this.key = key;
	}
	
	public String getMessage()
	{
		return "No value assigned to the key: " + key;
	}

}
