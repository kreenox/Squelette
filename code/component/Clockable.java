package component;

/**
 * permet de rendre une classe connectable � une horloge
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public interface Clockable {
	/**
	 * la fonction appel�e par l'horloge a chaques ticks
	 */
	public abstract void work(); 
}
