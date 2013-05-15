package dk.partyroulette.doorphonebuddyclient.model.communication;

/**
 * Listener for communication events.
 * @author Mads
 *
 */
public interface CommunicationListener 
{
	/**
	 * Called when an incoming SIP call is received.
	 */
	public void onReceiveCall();
	
	/**
	 * Called when the current call is ended.
	 */
	public void onEndCall();
}
