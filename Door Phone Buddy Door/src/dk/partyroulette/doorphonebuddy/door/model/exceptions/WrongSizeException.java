package dk.partyroulette.doorphonebuddy.door.model.exceptions;

public class WrongSizeException extends Exception 
{	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6407833051065194011L;

	public WrongSizeException(int size)
	{
		super("Wrong layout size integer: " + size);
	}

}
