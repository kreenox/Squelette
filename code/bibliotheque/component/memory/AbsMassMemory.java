package component.memory;

import component.AbsComponent;
import component.ComponentTypes;
import component.WRException;

/**
 * permet de gere les mémoires de stockage de masses. la memoire est divisée en secteurs.
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.1.0
 *
 */

public abstract class AbsMassMemory extends AbsComponent {
	private int sectorsize;
	
	
	public AbsMassMemory(int sectsize)
	{
		super();
		sectorsize = sectsize;
	}
	//set
	//get
	/**
	 * permet de connaitre la taille des secteurs de la memoire
	 * @return la taille d'un secteur
	 */
	public int getSectorSize()
	{return sectorsize;}
	//action
	/**
	 * initialise le composant pour une action de lecture ou d'écriture.
	 * cette methode est à utilisier pour les composants qui ont des parties a mettre en mouvement par exemple
	 */
	public abstract void init();
	/**
	 * permet de lire un secteur de la mémoire
	 * @param sect le secteur à lire
	 * @return un tableau conetant les données du secteur lu. (le tableau fait la taille du secteur)
	 * @throws WRException si on tente une lecture illegale
	 */
	public abstract int[] read(int sect) throws WRException;
	/**
	 * vide un secteur
	 * @param sect le secteur a vider
	 * @throws WRException si ont tente de mettre a zero un secteur qui ne peut pas l'etre
	 */
	public abstract void reset(int sect) throws WRException;
	/**
	 * permet d'ecrire des donnés sur un secteur
	 * @param sect le secteur ou on doit ecrire
	 * @param data les donnés a écrire, si le secteur est trop petit les données en exces sont perdue si le secteur est trop grand il sera complété
	 * @throws WRException si on tente une ecriture illegale
	 */
	public abstract void write(int sect, int[] data) throws WRException;
	//question

	@Override
	public final ComponentTypes getComponentType() {
		return ComponentTypes.MASSMEMORY;
	}

}
