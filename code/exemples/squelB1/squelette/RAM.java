package squelette;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

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
	
	
	private JPanel pane = null;
	
	
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
		if(pane != null)
		{
			setChanged();
			notifyObservers(adr);
		}
	}

	@Override
	public void reset(int adr) throws WRException {
		if(adr < 0 || adr > size)
			throw new WRException(true);
		data[adr] = 0x0000;
	}

	@Override
	public JPanel getUI() {

		class Panel extends JPanel implements Observer{

			private static final long serialVersionUID = 1L;
			
			private JTable tab;
			
			@SuppressWarnings("serial")
			public Panel()
			{
				
				//initialisation de la table
				String[] names = {"Adresse", "Valeur"};
				Object[][] o = new Object[0xFFFF][2];
				tab = new JTable(o, names){//une JTable non editable
					@Override
					public boolean isCellEditable(int row, int col)
					{return false;}
				};
				for(int n = 0; n < size; n++)
				{
					tab.setValueAt(divers.Affichages.hexStringFromInt(n, 4), n, 0);
					tab.setValueAt(divers.Affichages.hexStringFromInt(data[n], 4), n, 1);
				}
				//mise en place des composants
				this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				this.setAutoscrolls(true);
				add(new JLabel("RAM"));
				add(new JScrollPane(tab, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER));
			}

			@Override
			public void update(Observable arg0, Object arg1) {
				if(arg1.getClass() == Integer.class)
				{
					int temp = ((Integer)arg1).intValue();
					tab.setValueAt(divers.Affichages.hexStringFromInt(data[temp], 4), temp, 1);
					tab.clearSelection();
					tab.addRowSelectionInterval(temp, temp);
				}
				
			}
			
		}
		if(pane == null)
		{
			Panel p = new Panel();
			p.setMaximumSize(new Dimension(200, 10000));
			p.setMinimumSize(new Dimension(200, 500));
			addObserver(p);
			pane = p;
		}
		return pane;
	}

}
