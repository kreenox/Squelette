package component;

import java.util.Observable;

import javax.swing.JPanel;

import java.util.ArrayList;

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
	
	public AbsComponent()
	{
		connected = new ArrayList<AbsComponent>();
	}
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
	 * construit une interface graphique
	 * @return un JPanel qui contient l'interfacage graphique du composant
	 */
	abstract public JPanel getUI();
	/**
	 * permet de connaitre le type d'un composant
	 * @return le type du composant
	 */
	abstract public ComponentTypes getComponentType();
	
	@Override
	public boolean equals(Object o)
	{return o == this;}
}
