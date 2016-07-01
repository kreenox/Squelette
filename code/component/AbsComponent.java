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
	 * une liste des �l�ments conn�ct�s au composant
	 */
	protected ArrayList<AbsComponent> connected;
	
	/**
	 * connecte un composant � celui-ci
	 * @param c le composant � connecter
	 */
	abstract public void connectTo(AbsComponent c);
	/**
	 * d�connecte tout les composants qui sont connect� a celui-ci
	 * 
	 */
	abstract public void disconnect();
	/**
	 * d�connecte un composant � celui-ci
	 * @param c le composant � d�connecter
	 * @throws NonConnectedException si le composant n'est pas connect�
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
