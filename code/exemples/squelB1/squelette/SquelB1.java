package squelette;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.*;

import component.AbsComponent;
import component.ComponentTypes;
import component.WRException;
import component.processor.AbsProcessor;
import component.processor.unit.Registre;

public class SquelB1 extends AbsProcessor {
	
	private Bus[] b;//0 data, 1 control, 2 adresse
	private Registre[] reg;
	private Registre intMask;
	private Sequenceur seq;
	private Decodeur deq;
	private UAL ual;
	
	private JPanel panel = null;
	
	public SquelB1()
	{
		super();
		setPowerState(PowerState.HALTED);
		intMask = new Registre();
		intMask.write(0x0000);
		reg = new Registre[16];
		for(int n = 0; n < reg.length; n++)
			reg[n] = new Registre();
		seq = new Sequenceur();
		deq = new Decodeur();
		ual = new UAL(reg[SquelInstr.ME]);
		b = new Bus[3];
	}

	//set
	//get
	//actions
	public void powerON()
	{
		if(getPowerState() == PowerState.HALTED)
		{
			setPowerState(PowerState.FULL);
			seq.wakeUp();
		}
	}
	public void powerOFF()
	{
		setPowerState(PowerState.HALTED);
		seq.shutdown();
	}
	//question
	//redefinition
	@Override
	public void connectTo(AbsComponent c)
	{
		if(c.getComponentType() == ComponentTypes.BUS)
		{
			Bus temp = (Bus)c;
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
				default:
			}
		}else super.connectTo(c);
	}
	
	@Override
	public void work() {
		if(seq.initiating())
			bootSeq();
		//on va chercher la prochaine instruction
		if(seq.isFree() && !seq.isFetching())
		{//on appelle le bus
			try{
				writeBus(0 , MemInstr.NOOP, reg[SquelInstr.CO].read());
				seq.isFetching();
			}catch(WRException e){return;}
		}
		if(seq.isFree() && seq.isFetching())
		{//on attend la réponse du bus
			try {
				reg[SquelInstr.RI].write(readBus());
				notifyObservers(SquelInstr.RI);
				seq.startWork();
				seq.setFetching(false);
			} catch (WRException e) {return;}
		}
		//on decode l'instruction
		if(seq.isWorking() && seq.getSousPhase() == 0)
		{
				deq.decode(reg[SquelInstr.RI].read());
				seq.SousPhaseSuiv();
		}
		//on effectue le travaille lié à l'instruction
		if(seq.isWorking() && seq.getSousPhase() == 1)
		{
			switch(deq.getInstr())
			{
			case SquelInstr.NOOP: //no operation
				workend();
				break;
			case SquelInstr.CP: //copie arg1 dans arg2
				reg[deq.getArg2()].write(reg[deq.getArg1()].read());//penser a mettre a jour le mot d'état
				notifyObservers(deq.getArg2());
				workend();
				break;
			case SquelInstr.HALT://arret du processeur
				seq.shutdown();
				break;
			case SquelInstr.RST://mise a zero d'un registre
				reg[deq.getArg1()].reset();
				notifyObservers(deq.getArg1());
				reg[SquelInstr.ME].write(reg[SquelInstr.ME].read() | 0x0001);
				notifyObservers(SquelInstr.ME);
				workend();
				break;
			case SquelInstr.BUS://appel du bus
				try{
					writeBus(reg[SquelInstr.RBD].read(), reg[SquelInstr.RBC].read(), reg[SquelInstr.RBA].read());
					seq.SousPhaseSuiv();
					//on attend la reponse
				}catch(WRException e){return;}
				break;
			case SquelInstr.JZ://saut si zero flag
				if((reg[SquelInstr.ME].read() & 0x0001) != 0)
				{
					reg[SquelInstr.CO].write(reg[deq.getArg1()].read());
					notifyObservers(SquelInstr.CO);
				}
				workend();
				break;
			case SquelInstr.JNZ://saut si pas zero flag
				if((reg[SquelInstr.ME].read() & 0x0001) == 0)
				{
					reg[SquelInstr.CO].write(reg[deq.getArg1()].read());
					notifyObservers(SquelInstr.CO);
				}
				workend();
				break;
			case SquelInstr.JMP://saut non conditionel
				reg[SquelInstr.CO].write(reg[deq.getArg1()].read());
				notifyObservers(SquelInstr.CO);
				workend();
				break;
			case SquelInstr.CALL://appel de fonction
				try{
					writeBus(reg[SquelInstr.CO].read(), MemInstr.WRITE, reg[SquelInstr.PP].read());
					reg[SquelInstr.PP].write(reg[SquelInstr.PP].read() + 1);
					notifyObservers(SquelInstr.PP);
					reg[SquelInstr.CO].write(reg[deq.getArg1()].read());
					notifyObservers(SquelInstr.CO);
					workend();
				}catch(WRException e){return;}
				break;
			case SquelInstr.RET://retour de fonction
				try{
					writeBus(0, MemInstr.NOOP, reg[SquelInstr.PP].read() - 1);
					seq.SousPhaseSuiv();
				}catch(WRException e){return;}
				break;
			case SquelInstr.INT://routine d'interuption
			case SquelInstr.IRET://retour d'interuption
			case SquelInstr.LOAD://chargement depuis la memoire
				try{
					writeBus(0, MemInstr.NOOP, reg[deq.getArg2()].read());
					seq.SousPhaseSuiv();
				}catch(WRException e){return;}
				break;
			case SquelInstr.SAVE://sauvegarde dans la memoire
				try{
					writeBus(reg[deq.getArg1()].read(), MemInstr.WRITE, reg[deq.getArg2()].read());
					workend();
				}catch(WRException e){return;}
				break;
			case SquelInstr.PUSH://pousse sur la pile
				try{
					writeBus(reg[deq.getArg1()].read(), MemInstr.WRITE, reg[SquelInstr.PP].read());
					reg[SquelInstr.PP].write(reg[SquelInstr.PP].read() + 1);
					notifyObservers(SquelInstr.PP);
					workend();
				}catch(WRException e){return;}
				break;
			case SquelInstr.POP://retire de la pile
				try{
					writeBus(0 ,MemInstr.NOOP ,reg[SquelInstr.PP].read() - 1);
					seq.SousPhaseSuiv();
				}catch(WRException e){return;}
				break;
			case SquelInstr.SET://charge une valeure dans un registre
				reg[SquelInstr.CO].write(reg[SquelInstr.CO].read() + 1);//on incremente le compteur ordinal pour lire la valeur
				notifyObservers(SquelInstr.CO);
				try{
					writeBus(0, 0, reg[SquelInstr.CO].read());
					seq.SousPhaseSuiv();//on attend la reponse du bus
				}catch(WRException e){return;}
				break;
				//UAL
				//binaire
			case SquelInstr.ADD:
			case SquelInstr.SUB:
			case SquelInstr.MUL:
			case SquelInstr.DIV:
			case SquelInstr.MOD:
			case SquelInstr.OR:
			case SquelInstr.AND:
			case SquelInstr.XOR:
			case SquelInstr.CMP:
				ual.e1 = reg[deq.getArg1()];
				ual.e2 = reg[deq.getArg2()];
				ual.s = reg[deq.getArg2()];
				ual.instruction = deq.getInstr();
				ual.work();
				notifyObservers(deq.getArg2());
				workend();
				break;
				//unaire
			case SquelInstr.NOT:
			case SquelInstr.INC:
			case SquelInstr.DEC:
			case SquelInstr.ROL:
			case SquelInstr.ROR:
			case SquelInstr.SHL:
			case SquelInstr.SHR:
				ual.e1 = reg[deq.getArg1()];
				ual.s = reg[deq.getArg1()];
				ual.instruction = deq.getInstr();
				ual.work();
				notifyObservers(deq.getArg1());
				workend();
				break;
				
			}
		}
		if(seq.isWorking() && seq.getSousPhase() == 2)
		{
			switch(deq.getInstr())
			{
			case SquelInstr.BUS:
				if(b[0].isTransmiting())
				{
					try {
						reg[SquelInstr.RBD].write(b[0].getTransmitedData());
						notifyObservers(SquelInstr.RBD);
						reg[SquelInstr.RBC].write(b[1].getTransmitedData());
						notifyObservers(SquelInstr.RBC);
						reg[SquelInstr.RBA].write(b[2].getTransmitedData());
						notifyObservers(SquelInstr.RBA);
					} catch (WRException e) {e.printStackTrace();}
					workend();
				}
				break;
			case SquelInstr.RET:
				try{
					reg[SquelInstr.CO].write(readBus());
					notifyObservers(SquelInstr.CO);
					reg[SquelInstr.PP].write(reg[SquelInstr.PP].read() - 1);
					notifyObservers(SquelInstr.PP);
					workend();
				}catch(WRException e){return;}
				break;
			case SquelInstr.LOAD:
				try{
					reg[deq.getArg1()].write(readBus());
					notifyObservers(deq.getArg1());
					workend();
				}catch(WRException e){return;}
				break;
			case SquelInstr.POP:
				try{
					reg[deq.getArg1()].write(readBus());
					notifyObservers(deq.getArg1());
					workend();
				}catch(WRException e){return;}
				break;
			case SquelInstr.SET:
				try{
					reg[deq.getArg1()].write(readBus());
					notifyObservers(deq.getArg1());
					workend();
				}catch(WRException e){return;}
				break;
			}
		}
		
		//mise a jour du registre d'horloge
		reg[SquelInstr.RH].write(reg[SquelInstr.RH].read() + 1);
		notifyObservers(SquelInstr.RH);

	}

	@Override
	public void bootSeq() {
		switch(seq.getSousPhase())
		{
		case 0://on vide tous les registres sauf RH
			intMask.write(0x0000);
			for(int n = 0; n < reg.length; n++)
			{
				if(n == SquelInstr.RH)
					continue;
				else
				{
					reg[n].write(0x0000);
					notifyObservers(n);
				}
			}
			seq.SousPhaseSuiv();
			break;
		case 1://on copie la rom dans la ram
			
			//on effectue la lecture de l'adresse 0 de la ROM
			if(!seq.isFetching() && reg[SquelInstr.R0].read() == 0)
			try{
				//appel a la ROM : lecture de l'adresse 0
				writeBus(0 , MemInstr.ROM, reg[SquelInstr.CO].read());
				seq.setFetching(true);
				reg[SquelInstr.R0].write(1);
				notifyObservers(SquelInstr.R0);
			}catch(WRException e){return;}
			
			if(seq.isFetching())//si on est en fetch
				try{
					//lecture de la reponse de la ROM (on la stock temporairement dans R1)
					reg[SquelInstr.R1].write(readBus());
					notifyObservers(SquelInstr.R1);
					seq.setFetching(false);
				}catch(WRException e){return;}
			
			if(!seq.isFetching() && reg[SquelInstr.R0].read() == 1)
				try{
					//appel de la RAM : ecriture à l'adresse 0 de la donnée stockée dans R1 
					writeBus(reg[SquelInstr.R1].read(), MemInstr.WRITE, reg[SquelInstr.CO].read());
					reg[SquelInstr.R0].write(0);
					notifyObservers(SquelInstr.R0);
					seq.SousPhaseSuiv();
					reg[SquelInstr.CO].write(reg[SquelInstr.CO].read() + 1);
					notifyObservers(SquelInstr.CO);
				}catch(WRException e){return;}
			break;
		case 2:
			if(!seq.isFetching() && reg[SquelInstr.R0].read() == 0)
			{
				try{
					writeBus(0 , MemInstr.ROM, reg[SquelInstr.CO].read());
					reg[SquelInstr.R0].write(1);
					notifyObservers(SquelInstr.R0);
					seq.setFetching(true);
				}catch(WRException e){return;}
			}
			
			if(seq.isFetching())
			{
				try{
					reg[SquelInstr.R1].write(readBus());
					notifyObservers(SquelInstr.R1);
					seq.setFetching(false);
				}catch(WRException e){return;}
			}
			
			if(!seq.isFetching() && reg[SquelInstr.R0].read() == 1)
			{
				try {
					writeBus(reg[SquelInstr.R1].read(), MemInstr.WRITE, reg[SquelInstr.CO].read());
					reg[SquelInstr.R0].write(0);
					notifyObservers(SquelInstr.R0);
					reg[SquelInstr.CO].write(reg[SquelInstr.CO].read() + 1);
					notifyObservers(SquelInstr.CO);
				} catch (WRException e) {return;}
			}
			if(reg[SquelInstr.CO].read() == 0)
				seq.SousPhaseSuiv();
			
			break;
		case 3://on initialise les registre CO ME PP et les registres généraux
			for(int n = 0; n < reg.length; n++)
			{
				if(n == SquelInstr.RH || n == SquelInstr.RBA || n == SquelInstr.RBC || n == SquelInstr.RBD)
					continue;
				if(n == SquelInstr.CO)
				{
					reg[n].write(0x0100);
					notifyObservers(n);
				}
				else if(n == SquelInstr.PP)
				{
					reg[n].write(0x0030);
					notifyObservers(n);
				}
				else if(n == SquelInstr.ME)
				{
					reg[n].write(0x0);
					notifyObservers(n);
				}
				else 
				{
					reg[n].write(0x0000);
					notifyObservers(n);
				}
			}
			break;
		case 4://on remet le masque d'interruption à 0xFFFF
			intMask.write(0xFFFF);
			notifyObservers(16);
			break;
		case 5://on indique au sequenceur que la phase d'initialisation est finie
			seq.initEnd();
			break;
			default:
				
		}

	}

	@Override
	public void interrupt() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void notifyObservers(Object o)
	{
		if(panel != null)
		{
			setChanged();
			super.notifyObservers(o);
		}
	}

	@Override
	public JPanel getUI() {
		
		@SuppressWarnings("serial")
		class Panel extends JPanel implements Observer{

			JTextField[] registres;
			JTextField intmask;
			JButton b;
			
			public Panel()
			{
				//initialisation des valeurs de textfields
				registres = new JTextField[reg.length];
				for(int n = 0; n < registres.length; n++)
				{
					registres[n] = new JTextField(divers.Affichages.hexStringFromInt(reg[n].read(), 4));
					registres[n].setEditable(false);
					registres[n].setMaximumSize(new Dimension(125, 50));
					//registres[n].setMinimumSize(new Dimension(75, 50));
				}
				intmask = new JTextField(divers.Affichages.binStringFromInt(intMask.read(), 16));
				intmask.setEditable(false);
				intmask.setMaximumSize(new Dimension(150, 30));
				//mise en place de l'affichage
				b = new JButton("ON");
				b.setSize(new Dimension(100, 50));
				b.addActionListener(new ActionListener(){
					@Override
					public void actionPerformed(ActionEvent arg0) {
						if(b.getText() == "ON")
						{
							powerON();
							b.setText("OFF");
						}
						else if(b.getText() == "OFF")
						{
							powerOFF();
							b.setText("ON");
						}
						
					}
				});
				this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				JPanel hori = new JPanel();
				add(hori);
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				hori.add(new JLabel("Processeur : "));
				hori.add(b);
				hori.add(new JLabel("Masque d'interuption : "));
				hori.add(intmask);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R0 :  "));
				hori.add(registres[SquelInstr.R0]);
				hori.add(new JLabel("RBA : "));
				hori.add(registres[SquelInstr.RBA]);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R1 :  "));
				hori.add(registres[SquelInstr.R1]);
				hori.add(new JLabel("RBC : "));
				hori.add(registres[SquelInstr.RBC]);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R2 :  "));
				hori.add(registres[SquelInstr.R2]);
				hori.add(new JLabel("RBD : "));
				hori.add(registres[SquelInstr.RBD]);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R3 :  "));
				hori.add(registres[SquelInstr.R3]);
				hori.add(new JLabel("RH :  "));
				hori.add(registres[SquelInstr.RH]);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R4 :  "));
				hori.add(registres[SquelInstr.R4]);
				hori.add(new JLabel("PP :  "));
				hori.add(registres[SquelInstr.PP]);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R5 :  "));
				hori.add(registres[SquelInstr.R5]);
				hori.add(new JLabel("RI :  "));
				hori.add(registres[SquelInstr.RI]);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R6 :  "));
				hori.add(registres[SquelInstr.R6]);
				hori.add(new JLabel("ME :  "));
				hori.add(registres[SquelInstr.ME]);
				hori = new JPanel();
				hori.setLayout(new BoxLayout(hori, BoxLayout.LINE_AXIS));
				add(hori);
				hori.add(new JLabel("R7 :  "));
				hori.add(registres[SquelInstr.R7]);
				hori.add(new JLabel("CO :  "));
				hori.add(registres[SquelInstr.CO]);
			}
			@Override
			public void update(Observable arg0, Object arg1) {
				if(arg1 == null) return;
				
				if(arg1.getClass() == Integer.class)
				{
					switch(((Integer)arg1).intValue())
					{
					case SquelInstr.R0:
						registres[SquelInstr.R0].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R0].read(), 4));
						break;
					case SquelInstr.R1:
						registres[SquelInstr.R1].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R1].read(), 4));
						break;
					case SquelInstr.R2:
						registres[SquelInstr.R2].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R2].read(), 4));
						break;
					case SquelInstr.R3:
						registres[SquelInstr.R3].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R3].read(), 4));
						break;
					case SquelInstr.R4:
						registres[SquelInstr.R4].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R4].read(), 4));
						break;
					case SquelInstr.R5:
						registres[SquelInstr.R5].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R5].read(), 4));
						break;
					case SquelInstr.R6:
						registres[SquelInstr.R6].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R6].read(), 4));
						break;
					case SquelInstr.R7:
						registres[SquelInstr.R7].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.R7].read(), 4));
						break;
					case SquelInstr.RBA:
						registres[SquelInstr.RBA].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.RBA].read(), 4));
						break;
					case SquelInstr.RBC:
						registres[SquelInstr.RBC].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.RBC].read(), 4));
						break;
					case SquelInstr.RBD:
						registres[SquelInstr.RBD].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.RBD].read(), 4));
						break;
					case SquelInstr.RH:
						registres[SquelInstr.RH].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.RH].read(), 4));
						break;
					case SquelInstr.PP:
						registres[SquelInstr.PP].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.PP].read(), 4));
						break;
					case SquelInstr.RI:
						registres[SquelInstr.RI].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.RI].read(), 4));
						break;
					case SquelInstr.ME:
						registres[SquelInstr.ME].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.ME].read(), 4));
						break;
					case SquelInstr.CO:
						registres[SquelInstr.CO].setText(divers.Affichages.hexStringFromInt(reg[SquelInstr.CO].read(), 4));
						break;
					case 16://le masque d'interuption
						intmask.setText(divers.Affichages.hexStringFromInt(intMask.read(), 4));
						break;
						default:
					}
				}
				
			}
			
		}
		
		if(panel == null)
		{
			Panel temp = new Panel();
			addObserver(temp);
			panel = temp;
		}
		return panel;
	}
	//private
	private int readBus() throws WRException
	{
		if(b[0].isTransmiting() && (b[1].getTransmitedData() & MemInstr.PROC) == MemInstr.PROC)
			return b[0].getTransmitedData();
		else throw new WRException(false);
	}
	/**
	 * ecrit dans le bus
	 * @param dat les données
	 * @param cont le controle
	 * @param adr l'adresse
	 * @throws WRException
	 */
	private void writeBus(int dat, int cont, int adr) throws WRException
	{
		if(b[0].isUsed())
			throw new WRException(false);
		b[0].call(dat);
		b[1].call(cont);
		b[2].call(adr);
	}
	private void workend()
	{
		seq.workend();
		reg[SquelInstr.CO].write(reg[SquelInstr.CO].read() + 1);
		if(panel != null)
		{
			setChanged();
			notifyObservers(SquelInstr.CO);
		}
	}

}
