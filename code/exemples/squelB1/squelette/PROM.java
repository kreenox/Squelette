package squelette;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JPanel;

import component.AbsComponent;
import component.ComponentTypes;
import component.NonConnectedException;
import component.WRException;
import component.bus.AbsBus;
import component.memory.AbsReadOnlyMemory;

public class PROM extends AbsReadOnlyMemory {
	
	private int size;
	private int[] data;
	private AbsBus[] b;
	private boolean send;
	private int TOsend;
	
	
	public PROM(String path) throws IOException
	{
		super();
		b = new AbsBus[3];
		send = false;
		TOsend = 0x0;
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
	public void work() 
	{
		try{// si pas de bus connecté on envoi une exception
			if(b[0] == null || b[1] == null || b[2] == null)
				throw new NonConnectedException();
		}catch (NonConnectedException e)
		{
			e.printStackTrace();
			return;
		}
		try{
			if(b[0].isTransmiting())//si le bus transmet
				if(((b[1].getTransmitedData() & MemInstr.PROC) != MemInstr.PROC) && 
						((b[1].getTransmitedData() & MemInstr.ROM) == MemInstr.ROM) &&
						((b[1].getTransmitedData() & MemInstr.PERI) != MemInstr.PERI))
				{//si le control indique que la transmition est destinée a la PROM
					if((b[1].getTransmitedData() & MemInstr.WRITE) != MemInstr.WRITE)
					{//si on est en lecture
						read(b[2].getTransmitedData());//on lit la mémoire a l'adresse reçue
						//trouver un moyen d'envoyer la valeure
						
					}else //si on est en écriture
					{
						//on fait rien
					}
				}
				else if(send && !b[0].isUsed())
				{
					b[0].call(TOsend);
					b[1].call(MemInstr.WRITE | MemInstr.PROC);
					b[2].call(0);
				}
			}catch(WRException e)
			{e.printStackTrace();}
		
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
