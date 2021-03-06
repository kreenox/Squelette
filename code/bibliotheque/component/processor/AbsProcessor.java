package component.processor;

import component.AbsComponent;
import component.ComponentTypes;


/**
 * permet d'implementer les processeurs
 * @author Fabien ANXA
 * @version 0.1.0
 * @since 0.0.0
 *
 */

public abstract class AbsProcessor extends AbsComponent {

	private PowerState ps;
	/**
	 * permet de gerer l'alimentation du processeur
	 * @author Fabien ANXA
	 *
	 */
	public static enum PowerState{
		/**
		 * le processeur est eteint
		 */
		HALTED,
		/**
		 * le processeur ne fait que verifier si il n'y a pas d'interruption
		 */
		INT,
		/**
		 * le processeur n'est pas en economie d'energie
		 */
		FULL
	}
	public AbsProcessor()
	{
		super();
	}
	/**
	 * permet de changer l'alimentation du processeur
	 * @param s la nouvelle consomation du processeur
	 */
	protected void setPowerState(PowerState s)
	{ps = s;}
	/**
	 * permet de connaitre l'alimentation du processeur
	 * @return l'�tat de l'alimentation du processeur
	 */
	protected PowerState getPowerState()
	{return ps;}
	/**
	 * permet de lancer le processeur
	 */
	public abstract void bootSeq();
	/**
	 * signale au processeur qu'un PIC a une interruption
	 */
	public abstract void interrupt();
	//redefinition
	@Override
	public final ComponentTypes getComponentType()
	{
		return ComponentTypes.PROCESSOR;
	}

}
