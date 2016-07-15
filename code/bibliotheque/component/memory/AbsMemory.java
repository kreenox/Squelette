package component.memory;

import component.AbsComponent;
import component.ComponentTypes;
import component.WRException;


/**
 * cette classe g�re les emplacements m�moir � l'interieur de la m�moire
 * @author Fabien ANXA
 * @version 0.1.0
 * @since 0.0.0
 *
 */
public abstract class AbsMemory extends AbsComponent {

	/**
	 * la memoire ne doit effectuer aucune action
	 */
	public static final int NOOP = 0x0000;
	/**
	 * la memoire doit effecture une lecture
	 */
	public static final int READ = 0x1000;
	/**
	 * la memoire doit effectuer une ecriture
	 */
	public static final int WRITE = 0x2000;
	/**
	 * la memoire doit effectuer un reset
	 */
	public static final int RST = 0x3000;
	/**
	 * envoi le contenu de la m�moire � un autre composant
	 */
	public static final int SEND = 0x4000;
	/**
	 * la memoire � re�u une adresse invalide
	 */
	public static final int ADROUT = 0x0001;
	/**
	 * indique la fin de la copie de la m�moire
	 */
	public static final int CPEND = 0x0002;
	
	public AbsMemory()
	{
		super();
	}
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
	@Override
	public final ComponentTypes getComponentType()
	{
		return ComponentTypes.MEMORY;
	}
	

}
