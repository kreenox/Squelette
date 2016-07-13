package squelette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;

import component.AbsComponent;
import component.NonConnectedException;
import component.memory.AbsMemory;
import component.memory.WRException;

public class PROM extends AbsMemory {

	public int size;
	public int bitsize;
	public int[] content;
	public int[] nextAct;
	public Bus bus;
	
	private boolean sendall;
	private int adrloc;
	private int adrcomp;
	
	public PROM(String path) throws FileNotFoundException , IOException
	{
		load(path);
		nextAct = null;
		sendall = false;
		adrcomp = 0;
		adrloc = 0;
	}
	
	@Override
	public int read(int adr) throws WRException {
		if(adr < 0 || adr >= size)
			throw new WRException(new IndexOutOfBoundsException());
		return content[adr];
	}

	@Override
	public void write(int val, int adr) throws WRException {
		throw new WRException(null);
	}

	@Override
	public void disconnect() {
		bus = null;

	}

	@Override
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		if(c == bus)
			disconnect();
		else throw new NonConnectedException();

	}

	@Override
	public void connectTo(AbsComponent c) {
		if(c.getClass().equals(Bus.class))
			bus = (Bus)c;

	}

	@Override
	public void work() {
		if(bus == null)
			try {throw new NonConnectedException();} catch (NonConnectedException e1) {e1.printStackTrace();}
		if(bus.isTransmiting() && !sendall){
			if(bus.getTransmitedAdresse() == SquelAdr.ROM)
			{
				switch(bus.getTransmitedControl() & 0xF000)
				{
				case READ:
					nextAct = new int[3];
					nextAct[0] = SquelAdr.PROC;
					try {
						nextAct[1] = read(bus.getTransmitedData());
						nextAct[2] = 0x0000;
						} catch (WRException e) {
							nextAct[1] = 0x0000;
							if(e.wasOutOfBound()) 
								nextAct[2] = ADROUT;
								
						}
					break;
				case SEND:
					sendall = true;
					adrloc = 0;
					adrcomp = bus.getTransmitedData();
					break;
					default:
						break;
				}
				
			}
		}
		else if(sendall)
		{
			if(adrloc >= size)
			{
				if(!bus.isUsed())
				{
					sendall = false;
					bus.call(SquelAdr.PROC, CPEND, NOOP);
				}
				
			}
			else{
				if(!bus.isUsed() && nextAct == null)
				{
					bus.call(adrcomp, adrloc, WRITE);
					nextAct = new int[3];
					nextAct[0] = adrcomp;
					try{nextAct[1] = read(adrloc);}catch(WRException e){e.printStackTrace();}
					nextAct[2] = WRITE;
					adrloc++;
				}
			}
		}
		if(nextAct != null && !bus.isUsed())
		{
			if(!bus.isUsed())
			{
				bus.call(nextAct[0], nextAct[1], nextAct[2]);
				nextAct = null;
			}
		}

	}

	@Override
	public JPanel getUI() {
		// TODO Auto-generated method stub
		return null;
	}
	//private
	private void load(String path) throws FileNotFoundException, IOException
	{
		File f = new File(path);
		FileInputStream in;
		if(f.exists())
		{
			in = new FileInputStream(f);
			size = 0x0 | in.read();
			size = size << 8;
			size = size | in.read();
			content = new int[size];
			for(int n = 0; 0 < in.available() && n<size; n++)
			{
				content[n] = 0x0 | in.read();
				content[n] = content[n] << 8;
				content[n] = content[n] | in.read();
			}
			in.close();
		}
		else throw new FileNotFoundException(); 
		
	}

}
