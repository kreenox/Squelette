package component.memory;


/**
 * cette exception indique qu'une �criture ou une lecture illegale � �t� tent�e
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class WRException extends Exception {
	
	private static final long serialVersionUID = -193928745112601961L;
	private IndexOutOfBoundsException e;
	
	/**
	 * construit une exception de lecture/ecriture avec une exception indexOutOfBounds
	 * @param ex si l'exception est lev�e pour une lecture/ecriture en dehors des adresses m�moire valide sinon mettre null
	 */
	public WRException(IndexOutOfBoundsException ex)
	{e = ex;}
	//question
	/**
	 * permet de savoir si la lecture/ecriture � �chou� au cause d'une adresse ill�gale
	 * @return true si c'est le cas false sinon
	 */
	public boolean wasOutOfBound()
	{return e != null;}
}
