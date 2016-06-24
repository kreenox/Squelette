package squelette;

import javax.swing.JPanel;

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
	public JPanel getUI() {
		// TODO Auto-generated method stub
		return null;
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
