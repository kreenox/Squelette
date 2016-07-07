package squelette;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
	//getui
	private JPanel observed = null;
	
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

		class Panel extends JPanel implements Observer{
			private static final long serialVersionUID = 1L;
			private JTextField[] reg;
			private JTextField mask;
			
			public Panel(){
				this.setLayout(new GridLayout(8, 4, 5, 5));
				reg = new JTextField[16];
				for(int n = 0; n < reg.length; n++){
					reg[n] = new JTextField();
					reg[n].setMaximumSize(new Dimension(75, 30));
				}
				mask = new JTextField();
				mask.setMaximumSize(new Dimension(75, 30));
				this.add(new JLabel("R0 : "));
				this.add(reg[0]);
				this.add(new JLabel("R1 : "));
				this.add(reg[1]);
				this.add(new JLabel("R2 : "));
				this.add(reg[2]);
				this.add(new JLabel("R3 : "));
				this.add(reg[3]);
				this.add(new JLabel("R4 : "));
				this.add(reg[4]);
				this.add(new JLabel("R5 : "));
				this.add(reg[5]);
				this.add(new JLabel("R6 : "));
				this.add(reg[6]);
				this.add(new JLabel("R7 : "));
				this.add(reg[7]);
				this.add(new JLabel("RBA : "));
				this.add(reg[8]);
				this.add(new JLabel("RBD : "));
				this.add(reg[9]);
				this.add(new JLabel("RBC : "));
				this.add(reg[10]);
				this.add(new JLabel("RH : "));
				this.add(reg[11]);
				this.add(new JLabel("PP : "));
				this.add(reg[12]);
				this.add(new JLabel("RI : "));
				this.add(reg[13]);
				this.add(new JLabel("ME : "));
				this.add(reg[14]);
				this.add(new JLabel("CO : "));
				this.add(reg[15]);
				
			}
			@Override
			public void update(Observable o, Object arg) {
				for(int n = 0; n < reg.length; n++)
					reg[n].setText(divers.Affichages.hexStringFromInt(tab[n].read(), 4));
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

}
