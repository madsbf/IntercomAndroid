package exceptions;

/**
 * Indicates an invalid format of a given SecurityString
 * @author Mads
 *
 */
public class InvalidAuthenticationStringException extends AuthException 
{
	private String securityString;

	public InvalidAuthenticationStringException(String securityString)
	{
		super();
		this.securityString = securityString;
	}
}
