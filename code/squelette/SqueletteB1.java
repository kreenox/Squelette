package squelette;

import javax.swing.JPanel;

import component.AbsComponent;
import component.Clock;
import component.NonConnectedException;
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
			tab[n].write(0x0000);
		intmask = new Registre();
		ual = new UAL();
		dec = new Decodeur();
		ck = new Clock(500);
		ck.connectTo(ual);
	}
	@Override
	public void work() {
		if(boot != READY)
			bootSeq();
		else
		{
			
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
		if(boot == 1)
		{
			//recopier le contenu de la PROM dans la RAM
		}
		if(boot == 2)
		{
			for(int n = 0; n < tab.length; n++)
				tab[n].write(0x0000);
			boot++;
		}
		if(boot == 3)
		{
			tab[SquelInstr.CP].write(0x0100);
			boot++;
		}
		if(boot == 4)
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
