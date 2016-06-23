package component.processor.unit;

import component.Clockable;

/**
 * permet d'implementer les UAL
 * @author le_kr_000
 *
 */
public abstract class AbsUAL implements Clockable {

	
	
	/**
	 * une entr�e de l'UAL
	 */
	public Registre e1;
	/**
	 * une entr�e de l'UAL
	 */
	public Registre e2;
	/**
	 * la sortie de l'UAL
	 */
	public Registre s;
	/**
	 * l'instruction � effectuer lors du prochain tick d'horloge
	 */
	public int instruction;
	/**
	 * les flags lev�s par l'op�ration du tick
	 */
	public boolean[] flags;

}
