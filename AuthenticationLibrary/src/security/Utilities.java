package security;

import java.util.Random;

public class Utilities 
{
	public static String toHex(byte[] buf) 
	{
		if (buf == null)
			return "";
		StringBuffer result = new StringBuffer(2*buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}
	
	private final static String HEX = "0123456789ABCDEF";
	private static void appendHex(StringBuffer sb, byte b) {
		sb.append(HEX.charAt((b>>4)&0x0f)).append(HEX.charAt(b&0x0f));
	}
	
	public static String getRandomString(int length)
	{
		Random generator = new Random();
		StringBuilder randomStringBuilder = new StringBuilder();
		char tempChar;
		for (int i = 0; i < length; i++)
		{
			tempChar = (char) (generator.nextInt(96) + 32);
			randomStringBuilder.append(tempChar);
		}
		return randomStringBuilder.toString();
	}

}
