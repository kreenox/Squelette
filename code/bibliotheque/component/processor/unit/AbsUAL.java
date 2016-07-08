package component.processor.unit;


/**
 * permet d'implementer les UAL
 * @author le_kr_000
 *
 */
public abstract class AbsUAL {

	
	
	/**
	 * une entrée de l'UAL
	 */
	public Registre e1;
	/**
	 * une entrée de l'UAL
	 */
	public Registre e2;
	/**
	 * la sortie de l'UAL
	 */
	public Registre s;
	/**
	 * l'instruction à effectuer lors du prochain tick d'horloge
	 */
	public int instruction;

}
