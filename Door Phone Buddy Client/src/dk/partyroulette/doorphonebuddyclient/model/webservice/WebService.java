package dk.partyroulette.doorphonebuddyclient.model.webservice;

import dk.partyroulette.doorphonebuddyclient.model.settings.AppSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.ConnectionSettings;
import dk.partyroulette.doorphonebuddyclient.model.settings.exceptions.SettingsException;


public class WebService 
{
	public static void connect(String key, String secret, AppSettings settings) throws ConnectionException
	{	
		try
		{
			if(key.equals("Mads"))
			{
				settings.setValue(ConnectionSettings.KEY_SIP_USERNAME, "madsfrandsen");
				settings.setValue(ConnectionSettings.KEY_SIP_SERVER_DOMAIN, "getonsip.com");
				settings.setValue(ConnectionSettings.KEY_SIP_PASSWORD, "VADabEqwXNf6Lzsr");
				settings.setValue(ConnectionSettings.KEY_SIP_AUTH_USERNAME, "getonsip_madsfrandsen");
				settings.setValue(ConnectionSettings.KEY_SIP_OUTBOUND_PROXY, "sip.onsip.com");
				settings.setValue(ConnectionSettings.KEY_SIP_DOOR_USERNAME, "madsfrandsen2");
				settings.load();
			}
			else if(key.equals("Sara"))
			{
				settings.setValue(ConnectionSettings.KEY_SIP_USERNAME, "madsfrandsen3");
				settings.setValue(ConnectionSettings.KEY_SIP_SERVER_DOMAIN, "getonsip.com");
				settings.setValue(ConnectionSettings.KEY_SIP_PASSWORD, "Y9YLgvGFbUbeYB6X");
				settings.setValue(ConnectionSettings.KEY_SIP_AUTH_USERNAME, "getonsip_madsfrandsen3");
				settings.setValue(ConnectionSettings.KEY_SIP_OUTBOUND_PROXY, "sip.onsip.com");
				settings.setValue(ConnectionSettings.KEY_SIP_DOOR_USERNAME, "madsfrandsen2");
				settings.load();
			}
			else if(key.equals("Kasper"))
			{
				settings.setValue(ConnectionSettings.KEY_SIP_USERNAME, "madsfrandsen4");
				settings.setValue(ConnectionSettings.KEY_SIP_SERVER_DOMAIN, "getonsip.com");
				settings.setValue(ConnectionSettings.KEY_SIP_PASSWORD, "8R2hc3hBDhZLqsHu");
				settings.setValue(ConnectionSettings.KEY_SIP_AUTH_USERNAME, "getonsip_madsfrandsen4");
				settings.setValue(ConnectionSettings.KEY_SIP_OUTBOUND_PROXY, "sip.onsip.com");
				settings.setValue(ConnectionSettings.KEY_SIP_DOOR_USERNAME, "madsfrandsen2");
				settings.load();
			}
		}
		catch(SettingsException e)
		{
			throw new ConnectionException(e.getMessage());
		}
	}

}
