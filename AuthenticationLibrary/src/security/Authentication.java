package security;

import java.util.Calendar;

import exceptions.HMACMismatchException;
import exceptions.InvalidAuthenticationStringException;
import exceptions.KeyMissingException;
import exceptions.NonceReuseException;
import exceptions.TimestampException;
import exceptions.WrongKeyException;

/**
 * Handles authentication.
 * @author Mads
 *
 */
public class Authentication 
{
	
	public static String createAuthenticationString(AuthenticationInstance authInstance, String salt, String macAlgorithm)
	{
		long timestamp = SecurityFunctions.generateTimestamp();
		String nonce = SecurityFunctions.createNonce(salt, timestamp);
		
		return createAuthenticationString(authInstance, timestamp, nonce, macAlgorithm);
	}
	
	public static String createDataString(AuthenticationInstance authInstance, long timestamp, String nonce)
	{
		String key = authInstance.getKey();
		
		return key + "&" + String.valueOf(timestamp) + "&" + nonce;
	}
	
	public static String createAuthenticationString(AuthenticationInstance authInstance, long timestamp, String nonce, String macAlgorithm)
	{

		String secret = authInstance.getSecret();
		
		String data = createDataString(authInstance, timestamp, nonce);
		
		return data + "&" + SecurityFunctions.generateMAC(macAlgorithm, data, secret);
	}
	
	public static boolean authenticate(AuthenticationInstance authInstance, AuthenticationHolder securityHolder, String macAlgorithm) throws HMACMismatchException, TimestampException, NonceReuseException, WrongKeyException
	{
		return authenticate(authInstance, securityHolder.getKey(), securityHolder.getTimestamp(), securityHolder.getNonce(), securityHolder.getHMAC(), macAlgorithm);
	}
	
	public static boolean authenticate(AuthenticationInstance authInstance, String key, long timestamp, String nonce, String hmac, String macAlgorithm) throws HMACMismatchException, TimestampException, NonceReuseException, WrongKeyException
	{
		if(authInstance.getKey().equals(key))
		{
			//if(SecurityFunctions.checkTimestamp(timestamp))
			//{
				if(SecurityFunctions.checkNonce(nonce))
				{
						return checkHMAC(key + "&" + timestamp + "&" + nonce, hmac, authInstance, macAlgorithm);
				}
			//}
		}
		else
		{
			throw new WrongKeyException(key, authInstance);
		}
		throw new SecurityException();
	}
	
	public static AuthenticationInstance findAuthenticationInstance(AuthenticationInstance[] authInstances, String key) throws KeyMissingException
	{		
		for(AuthenticationInstance authInstance : authInstances)
		{
			if(authInstance.getKey().equals(key))
			{
				return authInstance;
			}
		}
		throw new KeyMissingException(key, authInstances);
	}
	
	private static boolean checkHMAC(String data, String hmac, AuthenticationInstance authInstance, String macAlgorithm) throws HMACMismatchException
	{
		String hmacGenerated = SecurityFunctions.generateMAC(macAlgorithm, data, authInstance.getSecret());
		
		if(hmacGenerated.equals(hmac))
		{
			return true;
		}
		
		throw new HMACMismatchException(data, hmac, hmacGenerated, authInstance);
	}

}
