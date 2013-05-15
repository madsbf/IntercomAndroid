package dk.partyroulette.doorphonebuddy.door.model.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

public class BitmapUtility 
{
	/**
	 * Downloads an image from a given URL address.
	 * @param url - A URL address pointing to an image, that should be downSloaded.
	 * @return A bitmap representation of the image at the given URL address.
	 * @throws IOException
	 */
	public static Bitmap downloadBitmap(String url) throws IOException 
	{
		try 
		{
			Bitmap bitmap = BitmapFactory.decodeStream((InputStream)new URL(url).getContent());
			return bitmap;
		} 
		catch (MalformedURLException e) 
		{
			throw e;
		} 
		catch (IOException e) 
		{
			throw e;
		}
	}
	
	/**
	 * Crops a bitmap to 1x1 format.
	 * @param original - The original bitmap that should be cropped.
	 * @return The cropped bitmap.
	 */
	public static Bitmap cropBitmap(Bitmap original)
	{
		if(original.getWidth() > original.getHeight())
		{
			return Bitmap.createBitmap(
					   original, 
					   original.getWidth()/2 - original.getHeight()/2,
					   0,
					   original.getHeight(), 
					   original.getHeight()
					   );
		}
		else if(original.getWidth() < original.getHeight())
		{
			return Bitmap.createBitmap(
					   original,
					   0, 
					   original.getHeight()/2 - original.getWidth()/2,
					   original.getWidth(),
					   original.getWidth() 
					   );
		}
		else
		{
			return original;
		}
	}

	/**
	 * Rounds the corners of the given bitmap with a corner radius of the given amount of pixels.
	 * @param bitmap - The bitmap that should get rounded corners.
	 * @param pixels - The corner radius in pixels.
	 * @return The given bitmap with rounded corners.
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) 
	{
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
				.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}
}
