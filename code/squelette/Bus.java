package squelette;

import java.awt.Dimension;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JTextField;

import component.AbsComponent;
import component.NonConnectedException;
import component.bus.AbsBus;


/**
 * permet d'effectuer la communication entre differents composants
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class Bus extends AbsBus {
	
	private enum State{FREE, CALLED, TRANSMITING}
	private int data;
	private int adr;
	private int ctrl;
	private State state;
	//pour la fonction get UI
	private JPanel observed = null;
	
	/**
	 * creer et initialise un bus
	 */
	public Bus()
	{reset();state = State.FREE;}
	//set
	//get
	/**
	 * permet de r�cuperer l'addresse transmise par le bus
	 * @return l'adresse par le bus
	 */
	public int getTransmitedAdresse()
	{return adr;}
	/**
	 * permet de r�cuperer les donn�es �mise sur le bus
	 * @return les donn�es transmises
	 */
	public int getTransmitedData()
	{return data;}
	/**
	 * permet de r�cuperer les controles �mis sur le bus
	 * @return les controles transmises
	 */
	public int getTransmitedControl()
	{return ctrl;}
	//action
	/**
	 * effectue une demande d'�mmission sur le bus
	 * <br/>
	 * ce bus est "safe" si des donn�es sont en cours de transmition rien ne se passe
	 * @param adresse l'adresse du destiniataire
	 * @param datas les donn�s a envoyer
	 * @param controle les controles a envoyer
	 */
	public void call(int adresse, int datas, int controle)
	{
		if(state == State.FREE)
		{
			data = datas;
			adr = adresse; 
			ctrl = controle; 
			state = State.CALLED;
		}
		if(observed != null){
			this.setChanged();
			this.notifyObservers();
		}
	}
	/**
	 * le bus envois les donn�s re�ues lors de l'appel
	 */
	public void transmit()
	{state = State.TRANSMITING;}
	/**
	 * r�initialise le bus
	 */
	public void reset()
	{data = adr = ctrl = 0x00000000; state = State.FREE;}
	//question
	/**
	 * pour savoir si le bus transmet
	 * @return true si c'est vrai false sinon
	 */
	public boolean isTransmiting()
	{return state == State.TRANSMITING;}
	//redefinition
	
	@Override
	public void connectTo(AbsComponent c) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JPanel getUI() {//ceci est une essai
		//on cr�� une classe pour cet objet
		class Panel extends JPanel implements Observer{
			private static final long serialVersionUID = 1L;
			JTextField text;
			public Panel(){
				super();
				text = new JTextField();
				text.setEditable(false);
				text.setMaximumSize(new Dimension(100, 30));
				this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				this.add(text);
			}
			public void update(Observable arg0, Object arg1) {
				switch(state){
				case FREE:
					this.text.setText("FREE");
					break;
				case TRANSMITING:
					this.text.setText("TRANSMITING");
					break;
				case CALLED:
					this.text.setText("CALLED");
					break;
				}
			}
			
		}
		if(observed == null)//on donne le panel que si on est pas observ�
		{
			Panel pane = new Panel();//on cr�� le panel
			this.addObserver(pane);//on l'ajoute comme observer
			this.setChanged();;
			this.notifyObservers();//on met � jour le panel
			observed = pane;
		}
		return observed;//on le retourne
	}

	@Override
	public boolean isUsed()
	{return state != State.FREE;}
	
	@Override
	public void work() {
		switch(state)
		{
		case CALLED:
			transmit();
			break;
		case TRANSMITING:
			reset();
			break;
			default:
				break;
		}
		if(observed != null){
			this.setChanged();
			this.notifyObservers();
		}
	}
	@Override
	public String toString()
	{
		String res = "bus is : ";
			switch(state)
			{
			case FREE:
				res = res + "FREE ";
				break;
			case CALLED:
				res = res + "CALLED";
				break;
			case TRANSMITING:
				res = res + "TRANSMITING " + adr + " on adresse " + data + " as datas with " + ctrl + " control";
				break;
				default:
					res = res + "what?";
					break;
			}
		return res;
	}
	
}
