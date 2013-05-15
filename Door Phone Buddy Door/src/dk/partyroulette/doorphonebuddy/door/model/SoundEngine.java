package dk.partyroulette.doorphonebuddy.door.model;

import dk.partyroulette.doorphonebuddy.door.R;
import dk.partyroulette.doorphonebuddy.door.R.raw;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundEngine 
{
	private SoundPool soundPool;
	private AudioManager audioManager;
	
	private boolean firstBuzz = true;
	private boolean firstDial = true;
	
	private int buzzerID;
	private int dialID;
	
	private int curBuzzerID;
	private int curDialID;
	
	public SoundEngine(Activity activity)
	{
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
		buzzerID = soundPool.load(activity, R.raw.buzzer, 1);
		dialID = soundPool.load(activity, R.raw.dial, 1);
		audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
	}
	
	public void startDialTone()
	{
		if(firstDial)
		{
			firstDial = false;
			float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			curDialID = soundPool.play(dialID, volume, volume, 1, -1, 1f);
		}
		else
		{
			soundPool.resume(curDialID);
		}
	}

	public void stopDialTone()
	{
		soundPool.pause(curDialID);
	}
	
	public void startBuzzer(){
		if(firstBuzz)
		{
			firstBuzz = false;
			float volume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
			curBuzzerID = soundPool.play(buzzerID, volume, volume, 1, -1, 1f);
		}
		else
		{
			soundPool.resume(curBuzzerID);
		}

	};

	public void stopBuzzer()
	{
		soundPool.pause(curBuzzerID);
	}

}
