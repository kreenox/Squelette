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
	
	private int offset16;
	private int offset;
	private int lastadr;
	private int lastinstr;
	
	public RAM(int size){
		this.size = size;
		data = new int[size];
		for(int n = 0; n < size; n++)
			data[n] = 0x0000;
		offset16 = 0;
		offset = 0;
		lastadr = 0;
		lastinstr = 0;
		b = new Bus[3];
	}
	@Override
	public void work() {
		try{
			if(b[0] == null || b[1] == null || b[2] == null)
				throw new NonConnectedException();
		}catch (NonConnectedException e){
			e.printStackTrace();
			return;
		}
		if(lastinstr != MemInstr.NOOP)
		{
			try{
				switch(lastinstr & 0xF000)
				{
				case MemInstr.SENDALL:
					if(!b[0].isUsed() && offset16 != 0)
					{
						if(offset != 0)
							if(offset < 16)
							{
								offset16 = offset;
							}
							else if(offset % 16 == 0)
							{
								offset16 = 16;
							}
						b[0].call(lastadr);
						b[1].call(MemInstr.WRITE | (lastinstr << 8));
						b[2].call(lastinstr & 0x00FF);
					}
				case MemInstr.READ:
					if(!b[0].isUsed() && offset16 != 0)
					{
						b[0].call(read(lastadr));
						b[1].call(0x0000);
						b[2].call(lastinstr & 0x00FF);
						lastadr++;
						offset16--;
						if(offset == 0 && (lastinstr & 0xFF00) == MemInstr.READ)
							lastinstr = MemInstr.NOOP;
					}
					
					break;
				case MemInstr.WRITE:
					if(offset16 != 0)
					{
						if(b[0].isTransmiting() && b[2].getTransmitedData() == SquelAdr.RAM)
						{
							write(lastadr, b[0].getTransmitedData());
							offset16--;
							lastadr++;
						}
					}else lastinstr = MemInstr.NOOP;
				case MemInstr.RESET:
					if(offset16 != 0)
					{
						reset(lastadr);
						offset16--;
						lastadr++;
					}else lastinstr = MemInstr.NOOP;
					default:
						break;
				}
			}catch(WRException e){
				e.printStackTrace();
				return;
			}
			
		}else 
			try{
				if(b[0].isTransmiting() && b[2].getTransmitedData() == SquelAdr.RAM)
				{
					lastinstr = b[1].getTransmitedData();
					switch(lastinstr & 0xF000)
					{
					case MemInstr.READ:
						offset16 = (lastinstr & 0x0F00) >> 8;
						lastadr = b[0].getTransmitedData();
						break;
					case MemInstr.SENDALL:
						offset = size;
						lastadr = 0x0000;
					case MemInstr.RESET:
						offset16 = (lastinstr & 0x0F00) >> 8;
						lastadr = b[0].getTransmitedData();
						reset(lastadr);
						offset16--;
						lastadr++;
					case MemInstr.WRITE:
						offset16 = (lastinstr & 0x0F00) >> 8;
						lastadr = b[0].getTransmitedData();
						default:
							lastinstr = MemInstr.NOOP;
					}
				}
			}catch(WRException e){}

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
