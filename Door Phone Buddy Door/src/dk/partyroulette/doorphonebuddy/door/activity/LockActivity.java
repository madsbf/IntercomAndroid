package dk.partyroulette.doorphonebuddy.door.activity;

import ioio.lib.api.DigitalOutput;
import ioio.lib.api.IOIO;
import ioio.lib.api.exception.ConnectionLostException;
import ioio.lib.util.BaseIOIOLooper;
import ioio.lib.util.IOIOLooper;
import ioio.lib.util.android.IOIOActivity;

public class LockActivity extends IOIOActivity 
{
	private final int LOCK_PIN = 11;
	private boolean locked = true;
	
	public void setLocked(boolean value)
	{
		this.locked = value;
	}
	
	class Looper extends BaseIOIOLooper 
	{
		private DigitalOutput lock;
		private DigitalOutput led;
		
		@Override
		public void setup() throws ConnectionLostException 
		{
			try 
			{
				lock = ioio_.openDigitalOutput(LOCK_PIN);
				led = ioio_.openDigitalOutput(IOIO.LED_PIN, false);
			} 
			catch (ConnectionLostException e) 
			{
				throw e;
			}
		}
		
		@Override
		public void loop() throws ConnectionLostException 
		{
			try 
			{
				lock.write(!locked);
				led.write(false);
				Thread.sleep(10);
			} 
			catch (InterruptedException e) 
			{
				ioio_.disconnect();
			} 
			catch (ConnectionLostException e) 
			{
				throw e;
			}
		}
	}

	@Override
	protected IOIOLooper createIOIOLooper() 
	{
		return new Looper();
	}

}
