package component.memory;

import component.AbsComponent;


/**
 * cette classe g�re les emplacements m�moir � l'interieur de la m�moire
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
	 * permet de lire une information contenue � une adresse
	 * @param adr l'addresse qu'il faut lire
	 * @return le mot lu � l'adresse
	 * @throws WRException si on tente de lire � une adresse illegale ou si la m�moire est prot�g�e en lecture
	 */
	public abstract int read(int adr) throws WRException;
	/**
	 * permet d'�crire une information � une adresse
	 * @param val la valeur � ecrire
	 * @param adr l'adrese ou ecrire la valeure
	 * @throws WRException si on tente d'�crire � une adresse illegale ou si la m�moire est prot�g�e en �criture
	 */
	public abstract void write(int val, int adr) throws WRException;
	
	//question
	//redefinition
	

}
