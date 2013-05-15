package security;

import exceptions.InvalidAuthenticationStringException;
/**
 * Holds authentication data for a single authentication process.
 * @author Mads
 *
 */
public class AuthenticationHolder
{
	private String key;
	private String hmac;
	private long timestamp;
	private String nonce;
	
	public AuthenticationHolder(String key, long timestamp, String nonce, String hmac)
	{
		this.key = key;
		this.hmac = hmac;
		this.timestamp = timestamp;
		this.nonce = nonce;
	}
	
	/**
	 * Creates an AuthenticationHolder from the given authenticationString.
	 * @param authenticationString contains key, timestamp, nonce and hmac seperated by '&'.
	 * @throws InvalidAuthenticationStringException if the authenticationString is invalid.
	 */
	public AuthenticationHolder(String authenticationString) throws InvalidAuthenticationStringException
	{
		String[] values = authenticationString.split("&");
		if(values.length == 4)
		{
			this.key = values[0];
			try
			{
				this.timestamp = Long.valueOf(values[1]);
			}
			catch(NumberFormatException e)
			{
				throw new InvalidAuthenticationStringException(authenticationString);
			}
			this.nonce = values[2];
			this.hmac = values[3];
		}
		else
		{
			throw new InvalidAuthenticationStringException(authenticationString);
		}
	}

	public String getKey() {
		return key;
	}

	public String getHMAC() {
		return hmac;
	}

	public long getTimestamp() {
		return timestamp;
	}


	public String getNonce() {
		return nonce;
	}
}
