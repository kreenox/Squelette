package squelette;

import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;

import component.AbsComponent;
import component.NonConnectedException;
import component.memory.AbsMemory;
import component.memory.WRException;

/**
 * permet de gerer la ram d'une mémoire
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class RAM extends AbsMemory {
	

	private int content[];
	private int size;
	private int bitsize;
	private Bus bus;//a changer plus tard
	private int[] nextBusCall;//temporaire en attendant une meilleure solution
	private boolean waitNext;
	//pour le getUI
	private JPanel observed = null;
	
	//sert a stocker l'addresse de la prochaine ecriture
	private int adr = -1;
	
	public RAM(int size)
	{
		this.size = size;
		bitsize = 8;
		content = new int[size];
		reset();
	}
	public RAM(int size, int bitsize)
	{
		this.size = size;
		this.bitsize = bitsize;
		content = new int[size];
		reset();
	}
	//set
	//get
	public int getSize()
	{return size;}
	public int getBitSize()
	{return bitsize;}
	//action
	//question
	//redefinition
	@Override
	public int read(int adr) throws WRException {
		if(adr < 0 || adr >= size)
			throw new WRException(new IndexOutOfBoundsException());
		return content[adr];
	}

	@Override
	public void write(int val, int adr) throws WRException {
		if(adr < 0 || adr >= size)
			throw new WRException(new IndexOutOfBoundsException());
		content[adr] = val;

	}

	@Override
	public void disconnect() {
		bus = null;
	}

	@Override
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		if(c != bus)
			throw new NonConnectedException();
		disconnect();
	}

	@Override
	public void connectTo(AbsComponent c) {
		if(c.getClass().equals(Bus.class))
			bus = (Bus)c;
	}

	@Override
	public boolean equals(Object o)
	{return o == this;}
	
	@Override
	public void work() {
		if(bus == null)
			try {throw new NonConnectedException();} catch (NonConnectedException e) {e.printStackTrace();}
		if(bus.isTransmiting())
		{
			if(bus.getTransmitedAdresse() == SquelAdr.RAM)
			{
				switch(bus.getTransmitedControl())
				{
				case READ://action de lecture
					nextBusCall = new int[3];
					nextBusCall[0] = SquelAdr.PROC;
					adr = bus.getTransmitedData();
					try {
						nextBusCall[1] = read(adr);
						nextBusCall[2] = 0x0;
					} catch (WRException e) {
						if(e.wasOutOfBound())
							nextBusCall[2] = ADROUT;
						nextBusCall[1] = adr;
					}
					adr = -1;
					break;
				case RST:
					try{write(bus.getTransmitedControl(), 0x0000);}catch (WRException e){}
					break;
				case WRITE:
					
					if(!waitNext)
					{
						adr = bus.getTransmitedData();
						waitNext = true;
					}
					else
					{
						try{write(bus.getTransmitedData(), adr);}catch (WRException e){e.printStackTrace();}
						waitNext = false;
						this.setChanged();
						this.notifyObservers();
					}
					break;
					default:
						
						break;
				}
			}
		}
		if(nextBusCall != null && !bus.isUsed())
		{
			bus.call(nextBusCall[0], nextBusCall[1], nextBusCall[2]);
			nextBusCall = null;
		}
		if(observed != null){
			setChanged();
			notifyObservers();
		}
		
	}

	@Override
	public JPanel getUI() {
		
		class Panel extends JPanel implements Observer{
			private static final long serialVersionUID = 1L;
			private JTable data;

			public Panel(){
				this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				this.add(new JLabel("RAM :"));
				data = new JTable(size, 2);
				data.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
				for(int n = 0; n < size; n++){
					data.setValueAt(divers.Affichages.hexStringFromInt(n, 4), n, 0);
				}
				this.add(new JScrollPane(data, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER));
			}
			public void update(Observable arg0, Object arg1) {
				for(int n = 0; n < size; n++)
					data.setValueAt(divers.Affichages.hexStringFromInt(content[n], 4), n, 1);
			}
			
		}
		
		if(observed == null)
		{
			Panel pane = new Panel();
			this.addObserver(pane);
			this.setChanged();
			this.notifyObservers();
			observed = pane;
		}
		return observed;
	}
	//private
	private void reset()
	{
		for(int n = 0; n < size; n++)
			content[n] = 0x00000000;
		waitNext = false;
	}

}
