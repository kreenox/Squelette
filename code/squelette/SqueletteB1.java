package squelette;

import javax.swing.JPanel;

import component.AbsComponent;
import component.Clock;
import component.NonConnectedException;
import component.memory.AbsMemory;
import component.processor.AbsProcessor;
import component.processor.unit.Registre;

/**
 * le premier build du processeur Squelette
 * pour l'utilisation du processeur voir la documentation fournie a part
 * <u>caractéristiques de ce processeur :</u>
 * <ul>
 * 	<li>8 registre généreaux</li>
 * </ul>
 * 
 * @author Fabien ANXA
 * @version B1.0.0.0
 *
 */
public class SqueletteB1 extends AbsProcessor {

	private UAL ual;
	private Decodeur dec;
	private Sequenceur seq;
	private Clock ck;
	private Registre[] tab;
	private Registre intmask;
	private Bus bus;
	//relatif a la seqence de boot
	private int boot = 0;
	private final int READY = 15;//temporaire
	
	public  SqueletteB1()
	{
		tab = new Registre[16];
		for(int n = 0; n < tab.length; n++)
			tab[n] = new Registre();
		intmask = new Registre();
		ual = new UAL();
		dec = new Decodeur();
		ck = new Clock(500);
		seq = new Sequenceur();
		ck.connectTo(this);
	}
	@Override
	public void work() {
		
		if(boot != READY)
			bootSeq();
		else
		{
			seq.work(bus, tab, intmask, ual, dec);
		}
		
		
	}
	
	@Override
	public void bootSeq() 
	{
		
		if(boot == 0)
		{
			intmask.write(0x0);
			boot++;
		}
		else if(boot == 1)
		{
			bus.call(SquelAdr.ROM, SquelAdr.RAM, AbsMemory.SEND);
			boot++;
		}
		else if(boot == 2){
			if(bus.isTransmiting() && bus.getTransmitedAdresse() == SquelAdr.PROC && bus.getTransmitedData() == AbsMemory.CPEND)
				boot++;
		}
		else if(boot == 3)
		{
			for(int n = 0; n < tab.length; n++)
				tab[n].write(0x0000);
			boot++;
		}
		else if(boot == 4)
		{
			tab[SquelInstr.CO].write(0x0100);
			boot++;
		}
		else if(boot == 5)
		{
			intmask.write(0xFFFF);
			boot = READY;
		}
		
	}

	@Override
	public void connectTo(AbsComponent c) {
		if(c.getClass().equals(Bus.class))
			bus = (Bus)c;
		
	}

	@Override
	public void disconnect() {
		bus = null;
		
	}

	@Override
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		if(c.getClass().equals(Bus.class) && bus != null)
			bus = null;
		else if(c.getClass().equals(Bus.class) && bus == null)
			throw new NonConnectedException();
	}

	@Override
	public JPanel getUI() {
		// TODO Auto-generated method stub
		return null;
	}

}
