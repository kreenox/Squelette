package squelette;

import javax.swing.JPanel;

import component.bus.AbsBus;

public class Bus extends AbsBus {

	private int mask;
	private int masksize;
	public BusType type;
	
	public enum BusType{ADR, DATA, CONT}
	
	public Bus()
	{
		super();
		masksize = 32;
		mask = 0xFFFFFFFF;
	}
	public Bus(int nblignes, BusType t){
		super();
		masksize = nblignes;
		buildMask();
		type = t;
	}
	//set
	//get
	public BusType getBusType()
	{return type;}
	//action
	//question
	//redefinition
	
	@Override
	public void work() {
		if(this.isTransmiting())
			reset();
		if(this.isUsed() && !this.isTransmiting())
			transmit();

	}

	@Override
	public void call(int data)
	{
		super.call(data & mask);
	}
	@Override
	public JPanel getUI() {
		// TODO Auto-generated method stub
		return null;
	}
	//private
	private void buildMask()
	{
		int n = masksize;
		mask = 0x00000000;
		while(n != 0)
		{
			mask |= 0b1;
			mask = mask << 1;
			n--;
		}
	}

}
