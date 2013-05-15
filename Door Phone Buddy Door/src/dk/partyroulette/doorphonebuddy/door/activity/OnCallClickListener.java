package dk.partyroulette.doorphonebuddy.door.activity;

import dk.partyroulette.doorphonebuddy.door.model.item.Address;

public interface OnCallClickListener
{
	public void onAddressClick(Address address);
	
	public void onHangUpClick();

}
