 package component.bus;


import component.AbsComponent;
/**
 * les bus permettent de gerer les communications entre les composants
 * @author Fabien ANXA
 * @version 0.1.0
 * @since 0.0.0
 *
 */
public abstract class AbsBus extends AbsComponent {


	/**
	 * pour savoir si le bus est en cours d'utilisation
	 * @return true si il est utilisé false sinon
	 */
	public abstract boolean isUsed();
	/**
	 * pour savoir si le bus est en train de transmettre
	 * @return true si le bus transmet des données, false sinon
	 */
	public abstract boolean isTransmiting();

}
