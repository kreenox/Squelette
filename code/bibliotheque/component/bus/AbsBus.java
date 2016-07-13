 package component.bus;


import component.AbsComponent;
import component.ComponentTypes;
/**
 * les bus permettent de gerer les communications entre les composants.
 * le bus commence en etat d'attente tant qu'il n'est pas appel� par un composant il ne transmet aucune donn�.
 * une fois appel�, le bus transmet au prochain tick d'horloge.
 * @author Fabien ANXA
 * @version 0.1.0
 * @since 0.0.0
 *
 */
public abstract class AbsBus extends AbsComponent {

	private int data;
	
	/**
	 * cr�� et initialise le bus
	 */
	public AbsBus()
	{
		data = 0x00000000;
	}
	//set
	//get
	/**
	 * permet de r�cup�rer les donn�es transmise via le bus
	 * renvoi une donn�e eronn� si le bus ne transmet pas.
	 * @returnla donn�e transmise
	 */
	public int getTransmitedData()
	{
		return data;
	}
	//action
	/**
	 * appelle le bus pour transmettre des donn�s
	 * @param dat le donn�es a transmettre
	 */
	public void call(int dat)
	{
		data = dat;
	}
	/**
	 * fait transiter les donn�s par le bus
	 */
	public abstract void transmit();
	//questions
	/**
	 * pour savoir si le bus est en cours d'utilisation
	 * @return true si il est utilis� false sinon
	 */
	public abstract boolean isUsed();
	/**
	 * pour savoir si le bus est en train de transmettre.
	 * la methode getTransmitedData renvoi une donn�e eronn� si le bus ne transmet pas.
	 * @return true si le bus transmet des donn�es, false sinon
	 */
	public abstract boolean isTransmiting();
	//redefintion
	public ComponentTypes getComponentType()
	{
		return ComponentTypes.BUS;
	}

}
