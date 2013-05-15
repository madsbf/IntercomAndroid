package dk.partyroulette.doorphonebuddy.door.view;

import dk.partyroulette.doorphonebuddy.door.R;
import dk.partyroulette.doorphonebuddy.door.activity.OnCallClickListener;
import dk.partyroulette.doorphonebuddy.door.model.Entrance;
import dk.partyroulette.doorphonebuddy.door.model.item.Address;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EntranceView extends LinearLayout
{	
	private TextView title;
	private ImageView imageLockStatus;
	private TextView textLockStatus;
	private FrameLayout actionView;
	private LinearLayout addressHolder;
	
	private CallView callView;
	
	public EntranceView(Context context)
	{
		super(context);
		init(context);
	}
	
	public EntranceView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}
	
	public EntranceView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context)
	{
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.entrance, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(root, params);
		
		title = (TextView) findViewById(R.id.textTitle);
		imageLockStatus = (ImageView) findViewById(R.id.imageLockStatus);
		textLockStatus = (TextView) findViewById(R.id.textLockStatus);
		addressHolder = (LinearLayout) findViewById(R.id.addressHolder);
		actionView = (FrameLayout) findViewById(R.id.actionView);
	}
	
	public void loadEntrance(Entrance entrance, OnCallClickListener listener)
	{		
		title.setText(entrance.getTitle());
		
		Address[][] addresses = entrance.getAddresses();
		for(int i = 0; i < addresses.length; i++)
		{
			LinearLayout innerAddressHolder = new LinearLayout(this.getContext());
			addressHolder.addView(innerAddressHolder, 
					new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
			innerAddressHolder.setOrientation(LinearLayout.VERTICAL);
			
			for(int j = addresses[0].length - 1; j >= 0; j--)
			{
				AddressView addressView = new AddressView(getContext());
				addressView.loadAddress(addresses[i][j], listener);
				
				innerAddressHolder.addView(addressView, 
						new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, 1.0f));
			}
		}
	}
	
	public void callAddress(Address address, OnCallClickListener listener)
	{
		callView = new CallView(getContext());
		callView.loadAddress(address, listener);
		actionView.addView(callView, 
						new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		
	}
	
	public void callReceived()
	{
		callView.callReceived();
	}
	
	public void hangup()
	{
		actionView.removeView(callView);
	}
	
	public void unlock()
	{
		imageLockStatus.setImageResource(R.drawable.button_lock_unlocked);
		textLockStatus.setText(R.string.lock_status_unlocked);
	}
	
	public void lock()
	{
		imageLockStatus.setImageResource(R.drawable.button_lock_locked);
		textLockStatus.setText(R.string.lock_status_locked);
	}
}
