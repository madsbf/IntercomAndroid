package exceptions;

import security.AuthenticationInstance;

/**
 * Indicates a mismatch between two given HMAC values.
 * @author Mads
 *
 */
public class HMACMismatchException extends AuthException 
{
	private String originalHMAC;
	private String generatedHMAC;
	private AuthenticationInstance authInstance;
	private String data;
	
	public HMACMismatchException(String data, String originalHMAC, String generatedHMAC, AuthenticationInstance authInstance)
	{
		this.data = data;
		this.originalHMAC = originalHMAC;
		this.generatedHMAC = generatedHMAC;
		this.authInstance = authInstance;
	}

}
