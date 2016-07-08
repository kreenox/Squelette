package component.processor.unit;


/**
 * permet de gerer les enregistrement rapides du processeur
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class Registre {
	private int val;
	
	/**
	 * cr�� et initialise un registre
	 */
	public Registre()
	{val = 0x0;}
	
	//action
	/**
	 * permet de lire la valeur contenue dans le registre
	 * @return la valeur stock�e dans le registre
	 */
	public int read()
	{return val;}
	/**
	 * r�initialise le registre
	 */
	public void reset()
	{val = 0x0;}
	/**
	 * �crit une valeur dans le registre
	 * @param v la valeur � stocker
	 */
	public void write(int v)
	{val = v;}
	
	//redefinition
	@Override
	public boolean equals(Object o)
	{return o == this;}
}
