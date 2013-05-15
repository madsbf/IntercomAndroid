package exceptions;

/**
 * Indicates an inputTimestamp is outside the legal range of a currentTimestamp
 * @author Mads
 *
 */
public class TimestampException extends SecurityException 
{
	private long inputTimestamp;
	private long currentTimestamp;
	private long legalRange;
	
	public TimestampException(long inputTimestamp, long currentTimestamp, long legalRange)
	{
		this.inputTimestamp = inputTimestamp;
		this.currentTimestamp = currentTimestamp;
		this.legalRange = legalRange;
	}

}
