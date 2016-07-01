package component;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Iterator;

//import javax.swing.JPanel; a remettre avec getUI


/**
 * cette classe sert � g�nerer des ticks d'horloge � interval r�guliers.
 * a chaque tick elle appelle toutes les m�thodes <i>work()</i> des composant auquels elle est connect�
 * @author Fabien ANXA
 * @version 0.0.0
 * @since 0.0.0
 *
 */
public class Clock implements Clockable 
{
	
	private int ticksize;
	private long lasttick;
	private boolean enslaved;
	private boolean paused;
	private ArrayList<Clockable> connected;
	private Thread t;
	
	/**
	 * cr�� une hologe
	 */
	public Clock()
	{
		ticksize = 0;
		connected = new ArrayList<Clockable>();
		enslaved = false;
	}
	/**
	 * c�� une horloge cadenc�e
	 * @param ticks la taille d'un tick en ms
	 */
	public Clock(int ticks)
	{
		if(ticks < 0)
			throw new InvalidParameterException();
		ticksize = ticks;
		connected = new ArrayList<Clockable>();
		enslaved = false;
	}
	//set
	/**
	 * change le temps entre deux ticks
	 * @param val la taille d'un tick  en ms
	 */
	public void setTickSize(int val)
	{
		if(val >=0)
			ticksize = val;
		else throw new InvalidParameterException();
	}
	//get
	/**
	 * permet de r�cup�rer la taille d'un tick
	 * @return la taille d'un tick en ms
	 */
	public int getTickSize()
	{return ticksize;}
	//a d�commenter plus tard
	/*public JPanel getUI() {
		return null;
		
	}*/
	//action
	/**
	 * met imediatement l'horloge en pause, elle n'emmet pas de tick
	 */
	public void pause()
	{paused = true;}
	/**
	 * relance les ticks d'horloges, il peut y avoir du d�lais entre l'appel et le prochain tick
	 */
	public void unpause()
	{paused = false;}
	/**
	 * permet de connecter un objet � cette horloge.
	 * l'horloge appelle la m�thode <i>work()</i> de tout le object auxquel elle est connect�e
	 * @param c l'objet � connecter
	 */
	public void connectTo(Clockable c) {
		if(connected.contains(c))
			;//throw
		connected.add(c);
	}
	/**
	 * permet de d�connecter tous les objet de cette horloge
	 */
	public void disconnect() {
		connected.clear();
		
	}
	/**
	 * permet de d&connecter un objet de l'horloge
	 * @param c l'objet � d�connecter
	 * @throws NonConnectedException est envoy� si l'objet n'est pas encor connect� � l'horloge
	 */
	public void disconnectFrom(AbsComponent c) throws NonConnectedException {
		if(!connected.contains(c))
			throw new NonConnectedException();
		connected.remove(c);
		
	}
	/**
	 * synchronize les ticks d'une horloge sur ceux de cette horloge 
	 * @param c l'horloge a synchroniser
	 */
	public void enslave(Clock c)
	{
		c.setEnslaved(true);
		this.connected.add(c);
	}
	/**
	 * met en marche l'horloge
	 */
	public void start()
	{
		t = new Thread(){
			public void run(){
				work();
			}
		};
		t.start();
	}
	/**
	 * appelle les methodes work de tout les composants connect�s � l'horloge
	 */
	public void tick()
	{
		System.out.println("tick emmited");
		Iterator<Clockable> it = connected.iterator();
		while(it.hasNext())
			it.next().work();
	}
	/**
	 * l'horloge ne synchronise plus les ticks d'une autre horloge
	 * @param c l'horloge � lib�rer
	 * @throws NonConnectedException si l'horloge n'est pas synchronis�e sur celle-ci
	 */
	public void unslave(Clock c) throws NonConnectedException
	{
		Clock temp;
		if(connected.contains(c))
		{
			temp = (Clock)connected.get(connected.indexOf(c));
			connected.remove(c);
			temp.enslaved = false;
		}
		else throw new NonConnectedException();
	}
	//question
	//redefinition
	@Override
	public boolean equals(Object o)
	{
		if(o == this)
			return true;
		if(this.getClass().equals(o.getClass()))
		{
			Clock c = (Clock)o;
			if(c.connected.equals(this.connected))
				if(c.enslaved == this.enslaved)
					if(c.paused == this.paused)
						if(c.ticksize == this.ticksize)
							return true;
		}
		return false;
	}
	@Override
	public void work() {//normalement c'est bon
		if(!enslaved)//si l'horloge n'est pas g�r�e par l'exterieur
		{
			while(!enslaved)// tant qu'elle n'est pas g�r�e de l'exterieur
			{
				if(paused)//on verifie toute les seconde si l'horloge est en pause 
					synchronized(this){try {Thread.sleep(1000);} catch (InterruptedException e) {e.printStackTrace();}}
				else //on effectue un tick tout les ticksize ms
				{
					if((lasttick + ticksize) <= System.currentTimeMillis())
					{
						lasttick = System.currentTimeMillis();
						tick();
					}
				}
			}
		}
		else tick();
	}
	//private
	
	private void setEnslaved(boolean b)//cette horloge est synchronis�e avec les ticks d'une autre horloge
	{enslaved = b;}
}