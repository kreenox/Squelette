package component.processor.unit;


/**
 * permet de décoder les instructions
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public abstract class AbsDecodeur {
	
	/**
	 * cette methode décode les instructions.
	 * les résultats de cette fonction doivent etre retourné par d'autres méthode de la classe
	 * @param instruction l'instruction à décoder
	 */
	public abstract void decode(int instruction);

}
