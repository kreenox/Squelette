package component.memory;

import component.AbsComponent;


/**
 * cette classe gère les emplacements mémoir à l'interieur de la mémoire
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public abstract class AbsMemory extends AbsComponent {

	
	//set
	//get
	//action
	/**
	 * permet de lire une information contenue à une adresse
	 * @param adr l'addresse qu'il faut lire
	 * @return le mot lu à l'adresse
	 * @throws WRException si on tente de lire à une adresse illegale ou si la mémoire est protégée en lecture
	 */
	public abstract int read(int adr) throws WRException;
	/**
	 * permet d'écrire une information à une adresse
	 * @param val la valeur à ecrire
	 * @param adr l'adrese ou ecrire la valeure
	 * @throws WRException si on tente d'écrire à une adresse illegale ou si la mémoire est protégée en écriture
	 */
	public abstract void write(int val, int adr) throws WRException;
	
	//question
	//redefinition
	

}
