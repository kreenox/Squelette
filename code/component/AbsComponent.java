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
public abstract class AbsComponent extends Observable {
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
	 */
	abstract public void disconnectFrom(AbsComponent c) throws NonConnectedException;
	/**
	 * construit une interface graphique
	 */
	abstract public JPanel getUI();
	/**
	 * cette methodes contient le travaill qui doit etre effectué par le composant
	 */
	abstract public void work();
}
