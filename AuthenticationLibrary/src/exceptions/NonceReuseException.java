package exceptions;

/**
 * Indicates a nonce is being reused.
 * @author Mads
 *
 */
public class NonceReuseException extends SecurityException 
{
	private String nonce;
	
	public NonceReuseException(String nonce)
	{
		this.nonce = nonce;
	}
}
