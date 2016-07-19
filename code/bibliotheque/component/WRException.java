package component;


/**
 * cette exception indique qu'une écriture ou une lecture illegale à été tentée
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class WRException extends Exception {
	
	private static final long serialVersionUID = -193928745112601961L;
	private IndexOutOfBoundsException e;
	private boolean adr;
	
	/**
	 * construit une exception de lecture/ecriture avec une exception indexOutOfBounds
	 * @param ex si l'exception est levée pour une lecture/ecriture en dehors des adresses de lecture/ecriture valide sinon mettre null
	 */
	@Deprecated
	public WRException(IndexOutOfBoundsException ex)
	{e = ex;}
	/**
	 * permet de construire une exception de lecture/ecriture
	 * @param adrinv si l'exception est levé par une adresse invalide
	 */
	public WRException(boolean adrinv)
	{adr = adrinv;}
	//question
	/**
	 * permet de savoir si la lecture/ecriture à échoué au cause d'une adresse illégale
	 * @return true si c'est le cas false sinon
	 */
	public boolean wasOutOfBound()
	{return e != null || adr;}
}
