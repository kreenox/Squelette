package component.processor.unit;


/**
 * permet de d�coder les instructions
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public abstract class AbsDecodeur {
	
	/**
	 * cette methode d�code les instructions.
	 * les r�sultats de cette fonction doivent etre retourn� par d'autres m�thode de la classe
	 * @param instruction l'instruction � d�coder
	 */
	public abstract void decode(int instruction);

}
