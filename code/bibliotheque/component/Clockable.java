package component;

/**
 * permet de rendre une classe connectable à une horloge
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public interface Clockable {
	/**
	 * la fonction appelée par l'horloge a chaques ticks
	 */
	public abstract void work(); 
}
