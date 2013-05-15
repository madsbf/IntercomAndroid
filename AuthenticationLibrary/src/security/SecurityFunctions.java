package security;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Timer;

import javax.crypto.Mac;

import exceptions.NonceReuseException;
import exceptions.TimestampException;

/**
 * Contains static security functions.
 * @author Mads
 *
 */
public class SecurityFunctions 
{
	public static final String MD_ALGORITHM_MD5 = "MD5";
	
	public static final String MAC_ALGORITHM_SHA1 = "HmacSHA1";
	public static final String MAC_ALGORITHM_SHA_256 = "HmacSHA256";
	public static final String MAC_ALGORITHM_SHA_512 = "HmacSHA512";
	
	private static final int randomWordLength = 10;
	
	public static String generateMAC(String macAlgorithm, String data, String secret)
	{
		try 
		{
			javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(secret.getBytes(), macAlgorithm);
			Mac mac = Mac.getInstance(macAlgorithm);
			mac.init(secretKeySpec);
			final byte[] macData = mac.doFinal(data.getBytes());
			
			return Utilities.toHex(macData);
		}
		catch ( final NoSuchAlgorithmException e )
		{
			// TODO
		} 
		catch (InvalidKeyException e) 
		{
			// TODO
		}
		
		return "";
	}
	
	public static boolean checkTimestamp(long inputTimestamp) throws TimestampException
	{
		long currentTimestamp = (long)(new Date().getTime()/1000);
		long legalRange = 300l;
		
		return SecurityFunctions.checkTimestamp(inputTimestamp, currentTimestamp, legalRange);
	}
	
	public static long generateTimestamp()
	{
		return (long)(new Date().getTime()/1000);
	}
	
	public static boolean checkTimestamp(long inputTimestamp, long currentTimestamp, long legalRange) throws TimestampException
	{
		long timestampDiff = currentTimestamp - inputTimestamp;
		if(timestampDiff < 0)
		{
			timestampDiff = -timestampDiff;
		}
		
		if(timestampDiff < legalRange)
		{
			return true;
		}
		throw new TimestampException(inputTimestamp, currentTimestamp, legalRange);
	}
	
	public static String generateMessageDigest(String algorithm, String data) 
	{
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest.getInstance(algorithm);
			digest.update(data.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i=0; i<messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static boolean checkNonce(String nonce) throws NonceReuseException
	{
		// TODO
		if(true)
		{
			return true;
		}
		throw new NonceReuseException(nonce);
	}

	public static String createNonce(String salt, long timeStamp)
	{
		return SecurityFunctions.generateMessageDigest(SecurityFunctions.MD_ALGORITHM_MD5, salt + "," + String.valueOf(timeStamp) + "," + Utilities.getRandomString(randomWordLength));
	}

}
