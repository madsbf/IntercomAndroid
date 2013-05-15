package dk.partyroulette.doorphonebuddyclient.model.settings.exceptions;

public class UnsaveableFormatException extends SettingsException 
{
	private Object[] objects;
	
	public UnsaveableFormatException(Object... objects)
	{
		this.objects = objects;
	}
	
	@Override
	public String getMessage()
	{
		String objectTypes = "";		
		for(Object object : objects)
		{
			objectTypes = objectTypes + object.getClass().getSimpleName() + " ";
		}
		return objectTypes + " are unsaveable formats.";
	}

}
