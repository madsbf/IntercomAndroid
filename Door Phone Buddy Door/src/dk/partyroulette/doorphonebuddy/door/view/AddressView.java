package dk.partyroulette.doorphonebuddy.door.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;
import dk.partyroulette.doorphonebuddy.door.R;
import dk.partyroulette.doorphonebuddy.door.activity.OnCallClickListener;
import dk.partyroulette.doorphonebuddy.door.model.Entrance;
import dk.partyroulette.doorphonebuddy.door.model.exceptions.NoImageException;
import dk.partyroulette.doorphonebuddy.door.model.item.Address;

public class AddressView extends LinearLayout implements OnClickListener
{
	private TextView title;
	private TextView detail;
	private LinearLayout root;
	private ImageView image;
	
	private Address address;
	
	private OnCallClickListener listener;
	
	public AddressView(Context context)
	{
		super(context);
		init(context);
	}
	
	public AddressView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}
	
	public AddressView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context)
	{		
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		root = (LinearLayout) inflater.inflate(R.layout.address, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(root, params);
		
		title = (TextView) findViewById(R.id.textTitle);
		detail = (TextView) findViewById(R.id.textDetail);
		image = (ImageView) findViewById(R.id.imageView);
	}
	
	public void loadAddress(Address address, OnCallClickListener listener)
	{
		this.address = address;
		
		this.listener = listener;
		
		title.setText(address.getTitle());
		detail.setText(address.getDetail());
		try 
		{
			image.setImageBitmap(address.getImage());
		} 
		catch (NoImageException e) 
		{
			image.setVisibility(View.GONE);
		}
		
		root.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		listener.onAddressClick(address);
		
	}
}
