package dk.partyroulette.doorphonebuddy.door.model;

import java.io.IOException;
import java.util.ArrayList;

import dk.partyroulette.doorphonebuddy.door.model.item.Address;
import dk.partyroulette.doorphonebuddy.door.model.item.Inhabitant;
import dk.partyroulette.doorphonebuddy.door.model.utility.BitmapUtility;

public class Entrance 
{
	private String title;
	private Address[][] addresses;
	private Inhabitant[] inhabitants;

	public Entrance(String title) 
	{
		this.title = title;
		
		addresses = new Address[2][4];
		
		Inhabitant[] inhabitants1 = { new Inhabitant("key", "secret") };
		addresses[0][0] = new Address("st.tv.", "Henrik R. Sørensen og Ditte Kristensen", "madsfrandsen3", inhabitants1);
		
		Inhabitant[] inhabitants2 = { new Inhabitant("key", "secret") };
		addresses[1][0] = new Address("st.th.", "Thomas J. Petersen og Karina Petersen", "madsfrandsen3", inhabitants2);
		
		Inhabitant[] inhabitants3 = { new Inhabitant("Sara", "SaraKodeord") };
		addresses[0][1] = new Address("1.tv.", "Sara E. Kaae", "madsfrandsen3", inhabitants3);
		
		Inhabitant[] inhabitants4 = { new Inhabitant("key", "secret") };
		addresses[1][1] = new Address("1.th.", "Kasper Frederiksen", "madsfrandsen3", inhabitants4);
		
		Inhabitant[] inhabitants5 = { new Inhabitant("Mads", "MadsKodeord") };
		try {
			addresses[0][2] = new Address("2.tv.", "Mads B. Frandsen og Joakim J. Møller", "madsfrandsen", inhabitants5, BitmapUtility.downloadBitmap("http://m.c.lnkd.licdn.com/media/p/2/000/0a9/1e2/123ae0e.jpg"));
		} 
		catch (IOException e) 
		{
			addresses[0][2] = new Address("2.tv.", "Mads B. Frandsen og Joakim J. Møller", "madsfrandsen", inhabitants5);
		}
		
		Inhabitant[] inhabitants6 = { new Inhabitant("key", "secret") };
		addresses[1][2] = new Address("2.th.", "Matthias Goldschmidt", "madsfrandsen3", inhabitants6);
		
		Inhabitant[] inhabitants7 = { new Inhabitant("Kasper", "KasperKodeord") };
		addresses[0][3] = new Address("3.tv.", "Kasper T. Jacobsen", "madsfrandsen4", inhabitants7);
		
		Inhabitant[] inhabitants8 = { new Inhabitant("key", "secret") };
		addresses[1][3] = new Address("3.th.", "Jonas E. Nilsson og Peter Larsen", "madsfrandsen3", inhabitants8);
		
		inhabitants = initInhabitants();
	}
	
	public Inhabitant[] getInhabitants()
	{
		return inhabitants;
	}
	
	private Inhabitant[] initInhabitants()
	{
		ArrayList<Inhabitant> inhabitantsList = new ArrayList<Inhabitant>();
		for(Address[] innerAddresses : addresses)
		{
			for(Address address : innerAddresses)
			{
				for(Inhabitant inhabitant : address.getInhabitants())
				{
					inhabitantsList.add(inhabitant);
				}
			}
		}
		
		Inhabitant[] inhabitantsArray = new Inhabitant[inhabitantsList.size()];
		inhabitantsList.toArray(inhabitantsArray);
		return inhabitantsArray;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public Address[][] getAddresses()
	{
		return addresses;
	}
}
