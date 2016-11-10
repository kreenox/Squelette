package component.processor.unit;

import divers.MaskOperation;

/**
 * permet de gerer les enregistrement rapides du processeur
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class Registre {
	private int val;
	private int mask;
	
	/**
	 * cr�� et initialise un registre de 16 bits
	 */
	public Registre()
	{
		val = 0x0;
		mask = MaskOperation.buildLeftFullMask(16);
	}
	
	/**
	 * cr�� un registre avec une taile
	 * @param taille la taille du registre en bits
	 */
	public Registre(int taille)
	{
		val = 0x0;
		mask = MaskOperation.buildLeftFullMask(taille);
	}
	
	//action
	/**
	 * permet de lire la valeur contenue dans le registre
	 * @return la valeur stock�e dans le registre
	 */
	public int read()
	{return val & mask;}
	/**
	 * r�initialise le registre
	 */
	public void reset()
	{val = 0x00000000;}
	/**
	 * �crit une valeur dans le registre
	 * @param v la valeur � stocker
	 */
	public void write(int v)
	{val = v & mask;}
	
	//redefinition
	@Override
	public boolean equals(Object o)
	{return o == this;}
}
