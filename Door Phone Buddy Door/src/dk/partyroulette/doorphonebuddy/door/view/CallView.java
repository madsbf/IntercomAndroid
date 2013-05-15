package dk.partyroulette.doorphonebuddy.door.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import dk.partyroulette.doorphonebuddy.door.R;
import dk.partyroulette.doorphonebuddy.door.activity.OnCallClickListener;
import dk.partyroulette.doorphonebuddy.door.model.item.Address;

public class CallView extends LinearLayout implements OnClickListener
{
	private TextView title;
	private TextView detail;
	private TextView connectStatus;
	private ImageButton buttonCallEnd;
	
	private OnCallClickListener listener;
	
	public CallView(Context context)
	{
		super(context);
		init(context);
	}
	
	public CallView(Context context, AttributeSet attrs) 
	{
		super(context, attrs);
		init(context);
	}
	
	public CallView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		init(context);
	}
	
	private void init(Context context)
	{
		LayoutInflater inflater = (LayoutInflater)context.getSystemService
			      (Context.LAYOUT_INFLATER_SERVICE);
		View root = inflater.inflate(R.layout.call, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		this.addView(root, params);
		
		title = (TextView) findViewById(R.id.textTitle);
		detail = (TextView) findViewById(R.id.textDetail);
		buttonCallEnd = (ImageButton) findViewById(R.id.buttonCallEnd);
		connectStatus = (TextView) findViewById(R.id.textConnectStatus);
	}
	
	public void callReceived()
	{
		connectStatus.setText(R.string.connected_to);
	}
	
	public void loadAddress(Address address, OnCallClickListener listener)
	{
		this.listener = listener;
		
		title.setText(address.getTitle());
		detail.setText(address.getDetail());
		buttonCallEnd.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) 
	{
		listener.onHangUpClick();
	}
}
