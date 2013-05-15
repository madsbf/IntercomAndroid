package exceptions;

import security.AuthenticationInstance;

public class WrongKeyException extends AuthException 
{
	private String key;
	private AuthenticationInstance authInstance;
	
	public WrongKeyException(String key, AuthenticationInstance authInstance)
	{
		super();
		this.key = key;
		this.authInstance = authInstance;
	}

}
