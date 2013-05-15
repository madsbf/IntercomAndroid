package dk.partyroulette.doorphonebuddy.door.model.item;

import dk.partyroulette.doorphonebuddy.door.model.exceptions.NoImageException;
import dk.partyroulette.doorphonebuddy.door.model.utility.BitmapUtility;
import android.graphics.Bitmap;


public class Address
{
	private String title;
	private String detail;
	private String sipUsername;
	private Inhabitant[] inhabitants;
	private Bitmap image;
	
	public Address(String title, String detail, String sipUsername, Inhabitant[] inhabitants)
	{
		this(title, detail, sipUsername, inhabitants, null);
	}
	
	public Address(String title, String detail, String sipUsername, Inhabitant[] inhabitants, Bitmap image)
	{
		this.title = title;
		this.detail = detail;
		this.sipUsername = sipUsername;
		this.inhabitants = inhabitants;
		
		if(image != null)
		{
			this.image = BitmapUtility.cropBitmap(image);
		}
	}
	
	public Bitmap getImage() throws NoImageException
	{
		if(image != null)
		{
			return image;
		}
		throw new NoImageException();
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getDetail()
	{
		return detail;
	}

	public String getSipUsername() 
	{
		return sipUsername;
	}
	
	public Inhabitant[] getInhabitants()
	{
		return inhabitants;
	}

}
