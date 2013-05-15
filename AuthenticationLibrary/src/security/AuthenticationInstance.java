package security;

/**
 * Defines a single instance that can be authenticated.
 * @author Mads
 *
 */
public class AuthenticationInstance 
{
	protected String key;
	protected String secret;
	
	public AuthenticationInstance(String key, String secret)
	{
		this.key = key;
		this.secret = secret;
	}
	
	public String getKey()
	{
		return key;
	}
	
	public String getSecret()
	{
		return secret;
	}

}
