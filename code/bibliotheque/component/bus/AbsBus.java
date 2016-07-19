 package component.bus;


import component.AbsComponent;
import component.ComponentTypes;
import component.WRException;
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
	private BusState etat;//temporaire
	
	/**
	 * créé et initialise le bus
	 */
	public AbsBus()
	{
		super();
		reset();
	}
	//set
	//get
	/**
	 * permet de récupérer les données transmise via le bus
	 * @return la donnée transmise
	 * @throws WRException si le bus ne transmet pas de données
	 */
	public int getTransmitedData()throws WRException
	{
		if(etat == BusState.TRANSMITING)
			return data;
		else throw new WRException(null);
	}
	//action
	/**
	 * appelle le bus pour transmettre des donnés
	 * @param dat le données a transmettre
	 */
	public void call(int dat)
	{
		if(etat == BusState.FREE);
			data = dat;
	}
	/**
	 * fait transiter les donnés par le bus
	 */
	public void transmit()
	{
		if(etat == BusState.CALLED)
			etat = BusState.TRANSMITING;
	}
	/**
	 * réinitialise le bus
	 */
	public void reset()
	{
		etat = BusState.FREE;
		data = 0x00000000;
	}
	//questions
	/**
	 * pour savoir si le bus est en cours d'utilisation
	 * @return true si il est utilisé false sinon
	 */
	public boolean isUsed()
	{return etat != BusState.FREE;}
	/**
	 * pour savoir si le bus est en train de transmettre.
	 * la methode getTransmitedData renvoi une donnée eronné si le bus ne transmet pas.
	 * @return true si le bus transmet des données, false sinon
	 */
	public boolean isTransmiting()
	{return etat == BusState.TRANSMITING;}
	//redefintion
	@Override
	public final ComponentTypes getComponentType()
	{
		return ComponentTypes.BUS;
	}

}
