package squelette;

import javax.swing.JPanel;

import component.AbsComponent;
import component.ComponentTypes;
import component.NonConnectedException;
import component.WRException;
import component.memory.AbsMemory;

public class RAM extends AbsMemory {

	private int[] data;
	private int size;
	private Bus[] b;
	private boolean send;
	private int TOsend;
	
	
	public RAM(int size)
	{
		super();
		this.size = size;
		data = new int[size];
		for(int n = 0; n < size; n++)
			data[n] = 0x0000;
		b = new Bus[3];
		send = false;
		TOsend = 0;
	}
	@Override
	public void work() 
	{
		try{
			if(b[0] == null || b[1] == null || b[2] == null)
				throw new NonConnectedException();
		}catch (NonConnectedException e){
			e.printStackTrace();
			return;
		}
		try{
			if(b[0].isTransmiting())//si le bus transmet
			{
				if(((b[1].getTransmitedData() & MemInstr.PERI) != MemInstr.PERI) &&
						((b[1].getTransmitedData() & MemInstr.PROC) != MemInstr.PROC) &&
						((b[2].getTransmitedData() & MemInstr.ROM) != MemInstr.ROM))
				{//si la transmition est pour la RAM
					if((b[1].getTransmitedData() & MemInstr.WRITE) == MemInstr.WRITE)
					{//si on est en ecriture
						write(b[0].getTransmitedData(), b[2].getTransmitedData());//on ecrit dans la memoire
					}else //si on est en lecture
					{
						TOsend = read(b[2].getTransmitedData());//on lit la valeur à l'adresse
						send = true;
					}
				}
			}
			else if(send)
			{
				if(!b[0].isUsed())
				{
					b[0].call(TOsend);
					b[1].call(MemInstr.PROC | MemInstr.WRITE);
					b[2].call(0);
					send = false;
				}
			}
		}catch(WRException e)
		{
			e.printStackTrace();
		}
		
	}

	@Override
	public void connectTo(AbsComponent c)
	{
		if(c.getComponentType() == ComponentTypes.BUS)
		{
			Bus temp = (Bus) c;
			switch(temp.getBusType())
			{
			case DATA:
				b[0] = temp;
				break;
			case CONT:
				b[1] = temp;
				break;
			case ADR:
				b[2] = temp;
				break;
			}
		}
	}
	
	@Override
	public int read(int adr) throws WRException {
		if(adr < 0 || adr > size)
			throw new WRException(true);
		return data[adr];
	}

	@Override
	public void write(int val, int adr) throws WRException {
		if(adr < 0 || adr > size)
			throw new WRException(true);
		data[adr] = val;
	}

	@Override
	public void reset(int adr) throws WRException {
		if(adr < 0 || adr > size)
			throw new WRException(true);
		data[adr] = 0x0000;
	}

	@Override
	public JPanel getUI() {
		// TODO Auto-generated method stub
		return null;
	}

}
