package dk.partyroulette.doorphonebuddyclient.model.communication;

import dk.partyroulette.doorphonebuddyclient.model.webservice.ConnectionException;

/**
 * Provides the communication methods, that are available for a CommunicationService.
 * @author Mads
 *
 */
public interface CommunicationInterface 
{
	/**
	 * Sends a request to unlock the door.
	 */
	public void sendUnlockRequest() throws ConnectionException; 
	
	/**
	 * Sends a request to lock the door.
	 */
	public void sendLockRequest() throws ConnectionException;
	
	/**
	 * Accepts and incoming call.
	 */
	public void acceptIncomingCall() throws ConnectionException;
	
	/**
	 * Rejects and incoming call.
	 */
	public void rejectIncomingCall();
	
	/**
	 * Ends the current call, if there is one.
	 */
	public void endCall();
	
	/**
	 * Determines whether the MainActivity is currently in the foreground on the device.
	 * @param value true, if the MainActivity is currently in the foreground.
	 */
	public void setForeground(Boolean value);
	
	/**
	 * Sets the listener for communication events.
	 * @param listener the listener for communication events.
	 */
	public void setCommunicationListener(CommunicationListener listener);
	
	/**
	 * Reloads the CommunicationEngine with the credentials given in AppSettings.
	 */
	public void reload() throws ConnectionException;
	
}
