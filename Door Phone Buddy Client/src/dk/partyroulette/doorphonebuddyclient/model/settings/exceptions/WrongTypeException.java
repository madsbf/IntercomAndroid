package dk.partyroulette.doorphonebuddyclient.model.settings.exceptions;

public class WrongTypeException extends SettingsException
{
	private String key;
	private Object object;
	private String expectedType;
	
	public static final String TYPE_BOOLEAN = "Boolean";
	public static final String TYPE_LONG = "Long";
	public static final String TYPE_FLOAT = "Float";
	public static final String TYPE_STRING = "String";
	public static final String TYPE_INTEGER = "Integer";
	
	public WrongTypeException(String key, Object object, String expectedType)
	{
		this.key = key;
		this.object = object;
		this.expectedType = expectedType;
	}
	
	@Override
	public String getMessage()
	{
		return "Wrong type at key: " + key + ". Expected type: " + expectedType + ". Actual type: " + object.getClass().getSimpleName();
	}
}
