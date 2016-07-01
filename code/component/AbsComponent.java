package component;

import java.util.Observable;

import javax.swing.JPanel;

import java.util.ArrayList;

/**
 * cet classe est pour pouvoir gerer les composant
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 */
public abstract class AbsComponent extends Observable implements Clockable{
	/**
	 * une liste des éléments connéctés au composant
	 */
	protected ArrayList<AbsComponent> connected;
	
	/**
	 * connecte un composant à celui-ci
	 * @param c le composant à connecter
	 */
	abstract public void connectTo(AbsComponent c);
	/**
	 * déconnecte tout les composants qui sont connecté a celui-ci
	 * 
	 */
	abstract public void disconnect();
	/**
	 * déconnecte un composant à celui-ci
	 * @param c le composant à déconnecter
	 * @throws NonConnectedException si le composant n'est pas connecté
	 */
	abstract public void disconnectFrom(AbsComponent c) throws NonConnectedException;
	/**
	 * construit une interface graphique
	 * @return un JPanel qui contient l'interfacage graphique du composant
	 */
	abstract public JPanel getUI();
	
	@Override
	public boolean equals(Object o)
	{return o == this;}
}
