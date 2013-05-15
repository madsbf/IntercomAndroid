package exceptions;

import security.AuthenticationInstance;

/**
 * Indicates a given key is not present in a given list of AuthenticationInstances
 * @author Mads
 *
 */
public class KeyMissingException extends AuthException 
{
	private String key;
	private AuthenticationInstance[] authInstances;
	
	public KeyMissingException(String key, AuthenticationInstance[] authInstances)
	{
		super();
		this.key = key;
		this.authInstances = authInstances;
	}

}
