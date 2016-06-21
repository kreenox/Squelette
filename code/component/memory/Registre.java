package component.memory;

import javax.swing.JPanel;

import component.AbsComponent;
import component.NonConnectedException;

/**
 * cette classe gère des registre, deux registres ne doivent pas avoir le meme nom
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class Registre extends AbsMemory {

	private String name;
	private int val;
	
	/**
	 * créé un registre
	 * @param nom le nom du registre
	 */
	public Registre(String nom)
	{name = nom; val =0;}
	@Override
	public void connectTo(AbsComponent c) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void disconnect() {
		// TODO Auto-generated method stub

	}

	@Override
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(this.getClass().equals(o.getClass()))
		{
			Registre r = (Registre)o;
			if(r.name == this.name)
				return true;
		}
		return false;
	}
	@Override
	public JPanel getUI() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	int read(int adr) throws WRException {
		return val;
	}

	@Override
	public String toString()
	{return name;}
	@Override
	public void work() {
		// TODO Auto-generated method stub

	}

	@Override
	void write(int val, int adr) throws WRException {
		this.val = val;
	}


}
