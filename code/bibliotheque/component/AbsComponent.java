package component;

import java.util.Observable;

import javax.swing.JPanel;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * cet classe est pour pouvoir gerer les composant
 * @author Fabien ANXA
 * @version 0.1.0
 * @since 0.0.0
 */
public abstract class AbsComponent extends Observable implements Clockable{
	/**
	 * une liste des éléments connéctés au composant
	 */
	private ArrayList<AbsComponent> connected;
	private Clock inck;//horloge interne
	
	public AbsComponent()
	{
		inck = new Clock();
		inck.connectTo(this);
		connected = new ArrayList<AbsComponent>();
	}
	//set
	/**
	 * permet de synchroniser ce composant sur une autre horloge.
	 * pour liberer ce composant de l'horloge externe il faut voir avec l'horloge maitresse
	 * @param ck l'horloge externe pour ce composant
	 */
	public void SetExternalClock(Clock ck)
	{
		ck.enslave(inck);
	}
	//get
	/**
	 * permet de connaitre le type d'un composant
	 * @return le type du composant
	 */
	abstract public ComponentTypes getComponentType();
	/**
	 * permet de recuperer la liste des composants connéctés à celui-ci
	 * @return un iterator contenant les composants connectés a celui-ci
	 */
	public Iterator<AbsComponent> getConnectedList()
	{return connected.iterator();}
	/**
	 * construit une interface graphique
	 * @return un JPanel qui contient l'interfacage graphique du composant
	 */
	abstract public JPanel getUI();
	/**
	 * retourne l'horloge interne du composant
	 * @return l'horloge du composant
	 */
	public Clock getInternalClock()
	{return inck;}
	//actions
	/**
	 * connecte un composant à celui-ci
	 * @param c le composant à connecter
	 */
	public void connectTo(AbsComponent c)
	{
		if(!connected.contains(c))
			connected.add(c);
	}
	/**
	 * déconnecte tout les composants qui sont connecté a celui-ci
	 * 
	 */
	public void disconnect()
	{
		connected.clear();
	}
	/**
	 * déconnecte un composant à celui-ci
	 * @param c le composant à déconnecter
	 * @throws NonConnectedException si le composant n'est pas connecté
	 */
	public void disconnectFrom(AbsComponent c) throws NonConnectedException
	{
		if(!connected.contains(c))
			throw new NonConnectedException();
		connected.remove(c);
	}
	/**
	 * permet de deconecter l'horloge externe
	 */
	public void unsetExternalClock()
	{
		try {inck.unslaveSelf();} catch (NonConnectedException e) {}
	}
	
	
	@Override
	public boolean equals(Object o)
	{return o == this;}
}
