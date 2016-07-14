package component.processor;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * permet de prevenir un composant quand un autre composant veut effectuer une interruption
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.1.0
 *
 */

public abstract class AbsPIC extends AbsProcessor {

	private ArrayList<AbsProcessor> not;
	
	public AbsPIC()
	{
		super();
		not = new ArrayList<AbsProcessor>();
	}
	/**
	 * permet de signaler au <emph>PIC</emph> qu'un composant demande une interuption
	 * le pic doit, à chaque tick d'horloge verifier tout le pin et si necessaire notifier tout les composants qui ont besoin de l'etre
	 * @param num le numero de la pin a changer
	 * @param val la valeur a mettre sur la pin : true pour lever une exception  false pour ne rien faire
	 */
	public abstract void setPinVal(int num, boolean val);
	
	/**
	 * cette fonction prévient tout les notifiés qu'une exception est levée
	 */
	public void notifyEveryone()
	{
		Iterator<AbsProcessor> it = not.iterator();
		while(it.hasNext())
		{
			it.next().interrupt();
		}
	}
	
	/**
	 * ajoute un composant qu'il faut prévenir d'une interruption
	 * @param p
	 */
	public void addNotified(AbsProcessor p)
	{
		if(!not.contains(p));
			not.add(p);
	}

}
