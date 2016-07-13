 package component.bus;


import component.AbsComponent;
import component.ComponentTypes;
/**
 * les bus permettent de gerer les communications entre les composants.
 * le bus commence en etat d'attente tant qu'il n'est pas appelé par un composant il ne transmet aucune donné.
 * une fois appelé, le bus transmet au prochain tick d'horloge.
 * @author Fabien ANXA
 * @version 0.1.0
 * @since 0.0.0
 *
 */
public abstract class AbsBus extends AbsComponent {

	private int data;
	
	/**
	 * créé et initialise le bus
	 */
	public AbsBus()
	{
		data = 0x00000000;
	}
	//set
	//get
	/**
	 * permet de récupérer les données transmise via le bus
	 * renvoi une donnée eronné si le bus ne transmet pas.
	 * @returnla donnée transmise
	 */
	public int getTransmitedData()
	{
		return data;
	}
	//action
	/**
	 * appelle le bus pour transmettre des donnés
	 * @param dat le données a transmettre
	 */
	public void call(int dat)
	{
		data = dat;
	}
	/**
	 * fait transiter les donnés par le bus
	 */
	public abstract void transmit();
	//questions
	/**
	 * pour savoir si le bus est en cours d'utilisation
	 * @return true si il est utilisé false sinon
	 */
	public abstract boolean isUsed();
	/**
	 * pour savoir si le bus est en train de transmettre.
	 * la methode getTransmitedData renvoi une donnée eronné si le bus ne transmet pas.
	 * @return true si le bus transmet des données, false sinon
	 */
	public abstract boolean isTransmiting();
	//redefintion
	public ComponentTypes getComponentType()
	{
		return ComponentTypes.BUS;
	}

}
