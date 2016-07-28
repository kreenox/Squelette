package squelette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;

import component.AbsComponent;
import component.ComponentTypes;
import component.WRException;
import component.bus.AbsBus;
import component.memory.AbsReadOnlyMemory;

public class PROM extends AbsReadOnlyMemory {
	
	private int size;
	private int[] data;
	private AbsBus[] b;
	private int offset;
	private int lastadr;
	private int lastinstr;
	
	public PROM(String path) throws IOException
	{
		lastadr = 0;
		lastinstr = 0;
		offset = 0;
		b = new AbsBus[3];
		loadFrom(path);
	}
	//redefinition
	@Override
	public void connectTo(AbsComponent c)
	{
		if(c.getComponentType() == ComponentTypes.BUS)
		{
			Bus b = (Bus)c;
			switch(b.getBusType())
			{
			case DATA:
				this.b[0] = b;
				break;
			case CONT:
				this.b[1] = b;
				break;
			case ADR:
				this.b[2] = b;
				break;
			}
		}
	}
	@Override
	public void work() {
		if(lastinstr != MemInstr.NOOP)
		{
			switch(lastinstr & 0xF000)
			{
			case MemInstr.SENDALL://effectue le meme travaille de READ
			case MemInstr.READ:
				if(!b[0].isUsed() && offset != 0)
				{
					try {
						b[0].call(read(lastadr));
						b[1].call(MemInstr.WRITE | 0x0100 | (lastinstr & 0x00FF));
						b[2].call(lastinstr & 0x00FF);
					} catch (WRException e) {e.printStackTrace();}
					offset--; lastadr++;
					if(offset == 0)
						lastinstr = MemInstr.NOOP;
				}
				break;
				default:
					break;
			}
		} else
			try {
				if(b[0].isTransmiting() && b[2].getTransmitedData() == SquelAdr.PROM)
				{
					lastinstr = b[1].getTransmitedData();
					switch(lastinstr & 0xF000)
					{
					case MemInstr.READ:
						offset = (lastinstr & 0x0F00) >> 8;
						lastadr = b[0].getTransmitedData();
						break;
					case MemInstr.SENDALL:
						offset = size;
						lastadr = 0x0000;
						break;
					//write/reset ne font rien
					case MemInstr.RESET:
					case MemInstr.WRITE:
						lastinstr = MemInstr.NOOP;
					case MemInstr.NOOP:
						default:
							break;
					}
				}
			} catch (WRException e) {e.printStackTrace();}
	}

	@Override
	public int read(int adr) throws WRException {
		if(adr < 0 || adr >= size)
			throw new WRException(true);
		return data[adr];
	}

	@Override
	public JPanel getUI() {
		// TODO Auto-generated method stub
		return null;
	}
	
	private void loadFrom(String path) throws IOException
	{
		File f = new File(path);
		if(!f.exists())
			throw new FileNotFoundException();
		FileInputStream in = new FileInputStream(f);
		size = in.read();
		size = size << 8;
		size |= in.read();
		
		data = new int[size];
		int n = 0;
		while(n < size)
		{
			if(in.available() != 0)
			{
				data[n] = in.read();
				data[n] = data[n] << 8;
				data[n] = in.read();
			}else data[n] = 0;
			n++;
		}
		in.close();
	}

}
