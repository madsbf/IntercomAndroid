package dk.partyroulette.doorphonebuddyclient.model;

import security.SecurityFunctions;

public class Constants 
{
	/**
	 * The application-specific salt used for generating nonces
	 */
	public static final String SALT = "door-phones-are-excellent-buddies";
	
	/**
	 * The MAC-algorithm used for encrypting data when generating a MAC value.
	 * The list of algorithms is defined in security.SecurityFunctions.
	 */
	public static final String MAC_ALGORITHM = SecurityFunctions.MAC_ALGORITHM_SHA_512;
	
	public static final String APP_PREFERENCES = "DOOR_PHONE_BUDY_CLIENT_PREFERENCES";

}
